# 📝 목차

- [리팩토링 & 성능 개선](#refactoring)
- [프로젝트 종료 이후 혼자서 진행한 리팩토링](#update)


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


<br>

# ✍️ 프로젝트 종료 이후 혼자서 진행한 리팩토링 <a name = "update"></a>
여기부터 작성하는 내용은 [Backtony(최준성)](https://github.com/backtony)가 프로젝트가 끝난 이후 혼자서 시도하는 내용입니다.  
프로젝트가 끝난 후 공부한 내용을 적용하기 위해 시작합니다.  
오랜기간 동안 팀원이 같이 진행해온 프로젝트이다 보니 혼자 진행하기에는 양이 너무 방대하여 제가 작성한 코드에 대해서만 적용해보려고 합니다.


<br>

## 테스트 코드 최적화
### 공통 로직 추상화 및 도메인 특화 언어(DSL)
클린코드 책에서 읽기 쉬운 코드를 강조하는 내용이 많았고 이를 적용하도록 노력했습니다.  
서로 밀집한 코드 행은 세로로 가까이 배치시키고 코드를 읽기 쉽도록 명확한 이름의 함수로 로직들을 추상화시켰습니다.  
공통된 부분은 추상화하여 중복을 제거하고 상속받도록 수정했습니다.  
테스트 코드를 독자가 읽기 쉽도록 도메인 특화 언어(DSL)를 추가했습니다.  
위의 모든 내용을 코드로 보이기는 어려우니 간단하게 DSL 부분만 코드로 보이겠습니다.
```java
public class MemberDummy {

  public static Member createTestMember() {
    Member member = Member.builder()
            .username("testUser")
            .authority(Authority.ROLE_USER)
            .nickname("가나다")
            .name("testName")
            .phone("01000000000")
            .imageFileUrl("imageUrl")
            .profileText("profileText")
            .directorText("directorText")
            .age(25)
            .sex(Sex.M)
            .zone(ZoneDummy.createZone())
            .totalReward(3000)
            .build();

    member.setMemberCategories(Arrays.asList(CategoryDummy.createCategory()));
    return member;
  }
  
  ... 생략
}
```
매번 테스트를 하다 보면 Member를 생성해서 테스트 환경을 구성해야 하는 일이 많습니다.  
테스트 보조 클래스를 만들어 정적 메서드를 정의하고 테스트 환경을 구성할 때 사용하여 중복 코드도 제거하고 독자가 읽기 쉽도록 구성했습니다.  
이외에도 테스트에서만 사용하는 가독성 좋은 함수들을 만들어 사용했습니다.

<br>

### Test Container 제거
H2 DB로는 Batch Insert 쿼리가 한 개만 나가는지 확인할 수 없어서 해당 테스트만 MySQL로 확인하기 위해 Test Container를 도입했었습니다.  
하지만 실제로 쿼리가 한 개만 나가는것을 확인하는 작업은 로그로 일일이 찾아야 한다는 점에서 효율적이지 않다고 생각되었습니다.  
또한, 컨테이너 뜨는 시간이 너무 오래걸리기 때문에 클린코드에서 제시하는 F.I.R.S.T에서 Fast에 어느정도 위반하는 것 같다고 느껴서 결과적으로 제거하게 되었습니다.  
<br>

### Application Context 재활용 (3분 단축)
현재 테스트 코드가 약 670개 정도 존재합니다.  
테스트를 돌렸을 때 인텔리제이에 찍히는 시간은 약 50초였으며, 실제로 걸리는 시간은 3분 중후반에 걸쳐서 진행되었습니다.  
이는 매번 테스트마다 Application Context를 새로 만들어 사용하기 때문에 생기는 문제였습니다.  
ControllerTest 추상 클래스를 만들어 컨트롤러 테스트에 필요한 공통 부분을 넣어두고 컨트롤러 WebMvcTest는 이를 상속받도록 하여 Application Context를 재활용하도록 수정했습니다.  
JpaTest도 마찬가지로 추상 클래스를 만들어 상속받도록 하여 Application Context를 재활용하도록 수정했습니다.  
__결과적으로 인텔리제이에 찍히는 테스트 시간은 30초 -> 20초, 실제로 걸리는 시간은 3분 50초 -> 50초 정도로 전에 비해 약 3분이 단축되었습니다.__  
<br>

결과적으로 3분이 단축되었지만 설계상으로는 고민이 아직 남아있습니다.
```java
@WebMvcTest({
        BulletinController.class,
        SearchValidator.class,
        MemberCouponController.class,              
        ... 생략
})
public abstract class ControllerTest {

    @MockBean
    protected DirectorEditService directorEditService;

    @MockBean
    protected MatchingService matchingService;

    ... 생략
}
```
Application Context를 새로 띄우지 않고 재활용하기 위해 한곳에 몰아넣고 사용하다 보니 새로운 테스트를 추가할 때마다 해당 추상 클래스를 수정해야 했습니다. 즉, OCP 원칙을 위반하고 있습니다.  
구글링 해본 결과 Controller 테스트의 경우 Spring을 사용하지 않고 Standalone으로 테스트하면 해결할 수 있다고 하여 시도해봤지만 validator에서 문제가 생겼습니다.  
직접 만들어 사용하는 Validator는 Standalone으로 띄울 때 등록하면 되지만 Spring에서 제공하는 Validation은 테스트할 수 없게 되었습니다.(@NotBlank, @NotNull 같은 검증 애노테이션)  
이렇게 되면 같은 맥락의 Validation 로직인데 테스트를 또 분리해서 관리해야 하는 번거로움이 생기게 됩니다. 이 부분은 조금 더 고민해야할 부분인 것 같습니다.

<br>

## 예외 코드 구조 변경
```java
new MemberException(MemberErrorCodes.MEMBER_NOT_FOUND)
```
기존 예외처리 방식은 Member 관련 예외라면 모두 MemberException으로 처리하고 Enum으로 ErrorCodes를 만들어 위와 같이 사용했습니다.  
클린코드 책에서는 이것을 의존성 자석 문제(무분별한 import)라고 정의하고 OCP를 위반하는 코드라고 설명하고 있었는데, 딱 제가 이렇게 사용하고 있었습니다.  
따라서 전체적은 예외처리 구조를 변경했습니다.(제가 작성한 코드만 수정하려고 했으나 이 문제는 너무 방대하게 걸쳐져 있어서 전체를 수정하는 작업을 진행했습니다.)  
Member관련 예외 처리 코드만 예시로 작성하겠습니다.
```java
@Getter
public abstract class ApplicationException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;
    private BindingResult errors;

    protected ApplicationException(String errorCode, HttpStatus httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    protected ApplicationException(String errorCode, HttpStatus httpStatus, String message, BindingResult errors) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errors = errors;
    }
}
--------------------------------------------------------------------------------------------------------------
public abstract class MemberException extends ApplicationException {
    protected MemberException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }

    protected MemberException(String errorCode, HttpStatus httpStatus, String message, BindingResult errors) {
        super(errorCode, httpStatus, message, errors);
    }
}
--------------------------------------------------------------------------------------------------------------
@Getter
public class MemberNotFoundException extends MemberException{

    public static final String MESSAGE = "탈퇴했거나 존재하지 않는 회원입니다.";
    public static final String CODE = "MEMBER-401";

    public MemberNotFoundException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
```
RuntimeException을 상속받아 비즈니스 로직 예외로 사용할 ApplicationException 추상 클래스를 만들었습니다.  
이를 각각의 영역별로 상속받습니다.(MemberException, NotificationException ....)  
그리고 해당 영역안에 예외는 이를 상속받아 명확한 이름을 갖는 예외로 만듭니다.(MemberNotFoundException, MemberNotAdminException....)  
최종적인 예외 처리는 ControllerAdvice를 통해 처리합니다.  
결과적으로 예외를 사용하는 곳마다 Enum의 패키지가 무분별하게 import되는 의존성 자석 문제를 해결하였으며, 새로운 예외가 생길 때마다 ErrorCodes에 추가하는 문제(OCP 위반)를 해결하였습니다.  
<br>



## SecurityUtils 통합
```java
// 기존 코드
public class DirectorLectureServiceImpl implements DirectorLectureService {
    
    ... 생략
    
    private String getCurrentUsername() {
        ...
    }
}
```
기존에는 현재 유저의 정보를 가져오는 기능이 필요한 곳 마다 private 메서드로 만들어서 사용하고 있었습니다.  
즉, 여러 곳에서 중복되는 코드로 사용하고 있었습니다.

```java
@Component
@RequiredArgsConstructor
public class SecurityUtil {

  public Member getCurrentUserOrThrow(){
    ...
  }

  public Optional<String> getCurrentUsername() {
    ...
  }

  public Optional<Authority> getAuthority(){
    ...
  }
}
```
이를 SecurityUtils 클래스의 기능으로 만들고 현재 유저의 정보를 가져오는 기능은 모두 이 클래스를 사용하도록 하여 중복 코드를 제거했습니다.

<br>

## 페이징 쿼리 성능 개선 - NoOffset
```java
@Repository
@RequiredArgsConstructor
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Slice<NotificationDto> findNotificationDtoListPaginationNoOffsetByUsername(String username, Long lastNotificationId, Pageable pageable) {

        List<NotificationDto> content = query
                .select(new QNotificationDto(
                        notification.id.as("notificationId"),
                        notification.title,
                        notification.message,
                        notification.checked,
                        notification.notificationType,
                        notification.uuid,
                        notification.TeamId,
                        notification.createdDate
                ))
                .from(notification)
                .join(notification.member, member)
                .where(ltNotificationId(lastNotificationId),
                        notification.member.username.eq(username))
                .orderBy(notification.id.desc())
                .limit(pageable.getPageSize()+1)
                .fetch();

        return RepositorySliceHelper.toSlice(content,pageable);
    }

    private BooleanExpression ltNotificationId(Long lastNotificationId) {
        if (lastNotificationId == null){
            return null;
        }
        return notification.id.lt(lastNotificationId);
    }
}
```
기존에는 Slice 기반 페이징 쿼리에서 Offset을 사용하고 있었습니다.  
데이터가 많아질수록 페이징 쿼리에 Offset을 설정하게 되면 쿼리 성능이 나빠지는 것을 확인하고 Offset을 사용하지 않는 방식으로 쿼리를 수정하여 성능을 개선했습니다.  
공지사항과 같이 페이지 번호가 필요한 경우에는 NoOffset으로 개선이 불가능하여 커버링 인덱스 방식을 고려했으나, 애초에 페이지 수도 적고 불필요한 인덱싱 작업이 필요하여 과도한 리팩토링이라 생각하여 이 부분은 진행하지 않았습니다.  
Querydsl 최적화 관련 내용은 따로 정리하여 [포스팅](https://backtony.github.io/jpa/2021-10-04-jpa-querydsl-6/) 했습니다.

<br>

## 경계 조건 캡슐화
if문의 조건으로 사용되는 코드가 복잡한 경우 함수로 만들어 가독성 좋게 수정하였습니다.
```java
public class MatchingServiceImpl implements MatchingService {
    
  @Override
  @CheckIsAlreadyMatching
  public void matching(MatchingRequest matchingRequest) {
    ...

    // 기존 코드
    if (matchingMemberList.size() != matchingRequest.getPreferMemberCount() - 1) {
        ...
    }
    
    // 수정한 코드
    if (canMatch(matchingRequest, matchingMemberList)) {
        ...
    }
  }
}
```

<Br>

## 일반 유저와 디렉터의 편집 로직 분리
일반 유저의 편집 로직과 디렉터의 편집 로직을 하나의 컨트롤러와 서비스에서 관리하고 있었는데 이는 해당 코드를 변경해야 하는 이유가 2가지가 된다고 생각이 들었습니다.(SRP 위반)  
따라서 둘의 로직을 분리했습니다.

<br>

## 정적 팩터리 메서드
이 부분은 이펙티브 자바를 읽고 적용한 내용입니다.  
기존 코드에서도 이미 of와 의미있는 이름을 갖는 정적 팩터리 메서드를 사용하고 있었으나 책을 읽으면서 규약중에 from이 있다는 사실을 처음 알았습니다.  
따라서 from과 of를 구분해서 수정했습니다.

<br>

## PATCH, PUT 구분
수정 요청에 대부분이 무분별하게 @PutMapping 으로 구성되어있었는데 이를 PATCH와 PUT을 구분하도록 수정하였습니다.

<br>

## gson 제거
gson을 제거하고 내장되어있는 jackson 사용으로 변경했습니다.

<br>

## 기타
기술적으로 지식이 부족해서 아쉬웠던 부분을 공부하고 정리했습니다.
+ [로그인](https://github.com/backtony/spring-study/tree/master/spring-security-oauth2-jwt-redis)
+ [Spring batch](https://backtony.github.io/tag-spring-batch/)
+ [Spring data elasticsearch](https://github.com/backtony/spring-study/tree/master/spring-data-elasticsearch)