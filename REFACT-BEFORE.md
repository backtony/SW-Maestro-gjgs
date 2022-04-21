
# 🚀 리팩토링 & 성능 개선 <a name = "refactoring"></a>

## 의미있는 이름과 함수
코드를 다시 되돌아보았을 때, 당시에는 이해할 수 있을 정도의 이름으로 지었다고 생각했으나 명확하게 와닿지 않는 네이밍들이 있었습니다.  
따라서 주석이 필요 없을 정도로 명확하게 변수명과 함수명을 수정하였고, 함수의 경우 예외를 던진다면 마지막에 OrThrow를 붙여주었습니다.  
함수에 대해서는 Clean Code에서 5줄 이내를 권장하고 있었습니다. 코드를 되돌아본 결과 생각보다 함수가 긴 것들이 존재했고 충분히 줄일 수 있는 수준의 내용들이었기에 할 수 있는 한에서 5줄 내외를 지키도록 수정했습니다.

<br>


## Bulk Query
<div align="center">
 <img src="/images/refac-bulk.PNG" alt="refac-bulk">
</div>

코드상 여러 곳에서 이런 문제가 발생했지만 회원가입시 선호하는 카테고리를 입력하는 부분을 예시로 작성하겠습니다.  
회원가입 시 선호하는 카테고리를 선택하게 됩니다.

### 문제점
코드상 cascade를 이용해 따로 save하지 않아도 member를 save하면 같이 member_category가 save되도록 설계했습니다.  
또한, 고아 객체 orphanRemoval를 사용하여 삭제 또한 따로 delete 쿼리를 보내지 않아도 동작하도록 설계했습니다.  
개발자 입장에서는 위와 같은 설계로 코드를 작성하기가 매우 수월했고, 당연히 한번에 한방 쿼리가 나갈 것으로 예상했습니다.  
하지만 쿼리로그를 찍어본 결과 save와 delete 모두 한방 쿼리가 아니라 여러번의 쿼리가 나가는 것을 확인했습니다.

### 해결책
<div align="center">
 <img src="/images/refac-bulk-solution.PNG" alt="refac-bulk-solution">
</div>

결론부터 말씀드리면, cascade를 제거했고 다음과 같이 수정했습니다.
+ Insert의 경우 : JdbcTemplate.batchUpdate() 사용
+ delete의 경우 : queryDsl의 in 쿼리 사용

<br>

#### Insert 해결책
해결책은 2가지가 존재했습니다.
1. Table Id strategy를 SEQUENCE로 변경하고 Batch 작업
2. JdbcTemplate.batchUpdate() 사용

MySQL의 Table Id 전략은 대부분이 IDENTITY 전략을 사용하기도 하고, 저희는 이미 Id 전략을 IDENTITY 전략으로 사용하고 있었기에 Id전략을 변경하기에는 무리가 있었습니다.
또한, Jdbc를 사용하는 것이 성능상 더 뛰어나다는 결과를 확인했습니다.
<div align="center">
 <img src="/images/refac-bulk-performance.PNG" alt="refac-bulk-performance">
</div>

[출처](https://homoefficio.github.io/2020/01/25/Spring-Data%EC%97%90%EC%84%9C-Batch-Insert-%EC%B5%9C%EC%A0%81%ED%99%94/)

<br>

#### Delete 해결책
이미 프로젝트에서 queryDsl를 사용하고 있어 이를 이용하는 것이 가장 간단했기 때문에 queryDsl의 delete in 쿼리를 사용하여 해결했습니다.

<br>

## JPA
JPA에 대해서는 서로 어느 정도 이해하고 있어, 적절한 fetch join을 사용하여 코딩했었기에 N+1 문제는 발생하지 않았습니다.  
하지만 연관관계에 대해서 문제가 있었습니다.  
가장 좋은 연관관계 설계는 단방향을 기초로 하되 필요하면 양방향 설계를 하는 것입니다.  
JPA 프로그래밍의 저자, 김영한 선생님의 의견을 빌리자면 다음과 같습니다.
>양방향으로 하면 복잡도가 높아지는 단점이 있지만 성능상 이점을 얻을 수 있습니다.  
>정말 성능이 너무 중요해서 쿼리 하나를 줄이는게 꼭 필요한 상황이라면 복잡해지더라도 최적화를 해야합니다.  
>반면에 쿼리가 하나 더 나가더라도 시스템 자원이 충분해서 성능에 영향을 미치는 것이 미미하다면 코드 복잡도를 낮게 유지하는 것이 더 중요합니다.

<div align="center">
 <img src="/images/refac-mapped.PNG" alt="refac-mapped">
</div>

기존 코드에는 왼쪽과 같이 무분별한 양방향 관계가 존재했고, 리팩토링 과정에서 불필요한 양방향 관계를 모두 끊어내고 정리했습니다.

<br>

## QueryDsl 성능 개선
### exist 메서드 개선
<div align="center">
 <img src="/images/refac-querydsl-exist.PNG" alt="refac-querydsl-exist">
</div>

기본적으로 JPA에서 제공하는 exists는 조건에 해당하는 row 1개만 찾으면 바로 쿼리를 종료하기 때문에 전체를 찾아보지 않아 성능상 문제가 없습니다.
하지만 조금이라도 복잡하게 되면 메소드명으로만 쿼리를 표현하기 어렵기 때문에 보통 @Query를 사용합니다.  
여기서 문제가 발생합니다. JPQL의 경우 select의 exists를 지원하지 않습니다.(where문의 exists는 지원) 따라서 count쿼리를 사용해야 하는데 이는 총 몇건인지 확인을 위해 전체를 봐야하기 때문에 성능이 나쁠 수 밖에 없습니다.  
이를 개선하기 위해서 Querydsl의 selectOne과 fetchFirst(= limit 1)을 사용해서 직접 exists 쿼리를 구현했습니다.

<br>

### Cross Join 회피
<div align="center">
 <img src="/images/refac-querydsl-cross.PNG" alt="refac-querydsl-cross">
</div>

queryDsl은 용빼는 재주가 있는 것이 아니고 그저 편리하게 query를 날려주는 도구일 뿐인데 너무 안일하게 코드를 작성한 것이 문제였습니다.  
where 문에서 체이닝으로 타고 들어가기 때문에 cross join이 발생하게 되는데 이 부분은 join을 통해 cross join이 발생하지 않도록 수정했습니다.

<br>

### 조회컬럼 최소화하기
<div align="center">
 <img src="/images/refac-querydsl-minimize.PNG" alt="refac-querydsl-minimize">
</div>

엔티티에 수정이 필요한 경우라면, Entity를 꺼내야겠지만 이외의 경우라면 굳이 Entity를 꺼낼 필요가 없습니다.  
FK에 들어갈 Id가 필요한 경우라면 위와 같이 Id만을 가져와서 해당 엔티티를 새로 만들어 연관관계를 맞춰줄 수 있습니다.  
실제로 DB에서는 FK인 Id값만을 요구하기 때문입니다.

<br>

<div align="center">
 <img src="/images/refac-querydsl-minimize-dto.PNG" alt="refac-querydsl-minimize-dto">
</div>

필요한 데이터가 명확하게 한정적이라면, 위와 같이 Member의 reward 총액 데이터값만 필요하다면 Member 엔티티를 꺼내서 찾는 것이 아니라, dto를 이용하여 필요한 데이터만 가져오도록 수정했습니다.

<br>

## AOP
```java
@AfterReturning(value = "@annotation(CheckLeader)", returning = "team")
public void checkTeamLeader(Team team) {
    team.checkNotLeader(team.getLeader(), Member.from(getLeaderUsername()));
}
```
리워드 적립, 권한 체크 등 횡단 분리가 가능한 로직들은 AOP로 분리했습니다.  
<br>


## 테스트 코드
### 네이밍
테스트 함수의 이름을 카멜 케이스를 사용했습니다.  
하지만 스네이크 케이스가 조금 더 가독성이 좋다고 판단하여 테스트 함수명을 스네이크 케이스로 수정했습니다.

<br>

### 상속
<div align="center">
 <img src="/images/refac-test-extends.PNG" alt="refac-test-extends">
</div>

테스트에 필요한 중복적인 코드는 상속을 통해 여러 번 작성하지 않아도 되도록 했습니다.

<br>

### Test Container
<div align="center">
 <img src="/images/refac-testContainer.PNG" alt="refac-testContainer">
</div>

테스트에서는 H2 DB를 사용하지만, 실제 운영 DB는 MySQL를 사용하고 있기에 서로 문법이 100% 호환되지 않습니다.  
H2를 사용할 경우, Bulk Insert 부분에서 쿼리가 정확히 나가는지 확인할 수 없었습니다.  
Test 전체에서 MySQL Test Container을 띄워서 사용하기에는 수행 시간이 너무 오래걸리기에 호환되지 않는 문법에 한해서만 Test Container를 적용하여 테스트를 진행했습니다.
