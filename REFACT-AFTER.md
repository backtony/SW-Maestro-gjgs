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