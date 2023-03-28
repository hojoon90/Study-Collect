# 스프링 빈과 의존관계

이제 실제 화면을 통해 회원가입이 이루어지도록 만들어본다. 회원 가입을 하기 위해선 API에서
회원 컨트롤러가 회원 서비스를 통해 회원 가입을 하고 회원 조회를 하는 등의 행동이 이루어져야 한다.
이런 것을 회원 컨트롤러가 회원 서비스를 '의존' 한다고 표현할 수 있는데, 이 작업을 Spring 스럽게 만들어 본다.
먼저 회원 컨트롤러를 만들어준다.

```java
package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller //스프링이 실행되고 스프링 컨테이너가 생성될 때, 이 객체를 생성하여 스프링 컨테이너에 넣어둔다.
public class MemberController {

    private final MemberService memberService;  //new 를 사용하여 생성해도 되지만, 스프링 컨테이너에 등록하여 사용하는게 더 이익이 많다.

    @Autowired  //Spring 컨테이너에서 MemberService를 가져와 주입해준다. DI.
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}

```
@Controller는 스프링에서 해당 클래스를 컨트롤러로 관리하도록 해주는 어노테이션이다. 해당 어노테이션이 붙어있으면
스프링 컨테이너가 생성 될 때, 해당 객체를 생성하여 스프링 컨테이너에 넣어 관리하게 된다. 그리고 MemberService를 선언해주고
생성자로 MemberService를 받아오도록 만들어주었다. 생성자에 @Autowired를 걸어주면 스프링 컨테이너에서 MemberService를 가져와
주입해준다. 

하지만 위와 같은 코드까지 진행하면 memberService 변수에 빨간 줄이 발생하는 것을 볼 수 있다. 이는 MemberService가 
Spring에서 관리하는 클래스가 아니라 순수 자바 클래스이기 때문에 스프링에서 해당 객체를 가져올 수 없어서 발생하는 줄이다.
그렇기 때문에 MemberService 에 @Service 어노테이션을 달아준다. 
```java
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired  //Spring 컨테이너에서 MemberRepository를 가져와 주입해준다 DI.
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
...
```
이렇게 되면 MemberService 역시 Spring에서 관리하게 된다.
MemberRepository 역시 @Repository 어노테이션을 달아준다.
```java
@Repository
public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;
...
```
이렇게 해주면 Controller부터 Repository까지 서로 연결된 것을 확인할 수 있게 된다.
> MemberController -> MemberService -> MemberRepository

스프링에 Bean을 등록하는 방법은 두가지가 있다.
* 컴포넌트 스캔과 자동 의존관계 설정
* 자바 코드로 직접 스프링 빈 등

위에서 사용한 방법은 모두 컴포넌트 스캔이다. 스프링 시작시 컴포넌트 스캔의 시작은 Application 클래스의
@SpringBootApplication이다. 해당 어노테이션이 걸려있는 메인 메소드가 실행되면 메인 메소드가 있는 패키지의
하위 패키지들을 모두 스캔하면서 컴포넌트들을 등록하게 된다. 해당 패키지 바깥에 있는 패키지는 컴포넌트 스캔의 대상이 되지 않는다.

스프링은 컨테이너에 빈을 등록할 때 모두 Singleton으로 등록한다. 그렇기 때문에 같은 빈일 경우 모두 같은 인스턴스이다.
설정으로 해당 옵션을 변경할 순 있지만, 대부분은 Singleton으로 처리한다.