# 회원 서비스 테스트

회원 서비스의 테스트를 위한 테스트 파일을 만들어 준다. (테스트 파일 만드는 단축키: cmd+shift+t)
```java
package hello.hellospring.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    @Test
    void join() {
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}
```
그러면 test 안에 hello.hellospring.service 패키지에 파일이 생성되는 것을 볼 수 있다.

먼저 회원가입 테스트 코드를 만든다
```java
@Test
void 회원가입() {   //테스트는 메소드 이름을 한글로 써도 무방하다
    //테스트 할때 기본적인 패턴
    //given
    Member member = new Member();
    member.setName("hello");

    //when
    Long saveId = memberService.join(member);

    //then
    Member findMember = memberService.findOne(saveId).get();
    assertThat(member.getName()).isEqualTo(findMember.getName());
}
```
위 코드는 단순히 회원가입을 시키고 그 회원이 실제 가입이 되었는지 확인하는 테스트이다. 아주 단순한 테스트인데, 
테스트는 이런 단순한 정상 테스트뿐만 아니라 예외에 대한 테스트도 필요하다. 아래 중복 가입에 대한 테스트도 작성해준다.
```java
    @Test
    void 중복_회원_예외(){
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
 /*
        try {
            memberService.join(member2);
            fail(); //위 메소드에서 Exception 이 throw 되지 않으면 실패
        } catch (IllegalStateException e){
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
 */

        //then
    }
```
위 코드는 회원의 중복가입에 대한 예외 테스트이다. 예외에 대한 처리는 try-catch문을 사용해도 상관없지만, 테스트에서는
이보다 더 편리한 메소드를 제공해준다. 바로 'assertThrows'이다. assertThrows는 Exception에 대한 테스트 결과를 알려주는 메소드이다.
앞 인자는 기대되는 Exception 클래스를, 뒤 인자는 람다식을 이용해 기대되는 Excpetion이 발생하는 메소드를 실행시킨다.
위 테스트를 실행하면 바로 테스트를 성공하는 것을 볼 수 있다.

그리고 원할한 테스트를 위해 메모리를 클리어시켜주는 테스트 코드도 같이 넣어준다.
```java
...
class MemberServiceTest {

    MemberService memberService = new MemberService();
    MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }

...
```

여기서 하나 애매한 부분이 있는데, MemberService와  MemberServiceTest 클래스내에 memberRepository가
각각 new로 생성된 것을 볼 수 있다. 물론 repository안에 있는 Map은 static이기 때문에 문제는 없지만, static이 사라지면
바로 문제가 생기게 된다. 또한, new로 선언했기 때문에 같은 repository가 아닌 다른 repository 객체라는 느낌을 지울수 없다.
이를 방지하기 위해 repository를 생성자로 주입할 수 있도록 해준다.

먼저 MemberService에 생성자를 만들어 memberRepository를 받아오도록 바꿔준다.
```java
...
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
...
```

테스트 코드 역시 수정을 해주는데, @BeforeEach를 사용해준다.
```java
...
class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }


    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }
...
```
@BeforeEach는 테스트가 실행되기전에 실행되는 메소드라고 보면 된다.
여기서 코드를 보면 테스트전에 MemoryMemberRepository를 만들고, 해당 repository를 service가 생성될 때 생성자 값으로 넣어준다. 
이렇게 되면 테스트쪽 서비스와 실제 서비스 모두 같은 Repository 환경에서 테스트를 할 수 있게 된다.

생성자를 통해서 외부에서 생성된 클래스를 받아와 사용하는 것을 DI(Dependency Injection)이라고 한다.