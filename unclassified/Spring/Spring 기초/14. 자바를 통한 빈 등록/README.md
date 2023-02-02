#자바를 통한 빈 등록

스프링에서는 컴포넌트 스캔을 이용해 빈 등록을 할 수 있지만, 자바 설정코드를 이용해 직접 빈을 등록할 수도 있다.
이번엔 직접 등록하는 방법에 대해 알아본다.

먼저 컴포넌트로 스캔되는 어노테이션들을 모두 삭제해준다.
```java
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired  //Spring 컨테이너에서 MemberRepository를 가져와 주입해준다 DI.
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
...
```
```java
public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;
...
```
이 상태에서 스프링을 실행하면 컴포넌트 스캔이 되지 않아 에러가 발생한다. 빈을 다시 자바로 등록해보자.
```java
package hello.hellospring;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }
    
    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}

```
MemberService와 MemberRepository를 Bean으로 등록한 모습이다. MemberService의 경우 
생성할 때 MemberRepository를 필요로 하므로 Bean으로 등록 된 memberRepository()를 넣어준다.
설정 파일에 MemberService와 MemberRepository를 Bean으로 등록 하면 스프링이 실행 될 때
이 두개의 클래스를 스프링 컨테이너에 등록하여 관리하게 된다.

이 두가지 방식에는 서로 장단점이 있다. 일단, 정형화된 컨트롤러, 서비스, 레포지토리의 경우 컴포넌트 스캔을
이용하여 처리한다. 정형화되지 않은 상황인 경우는 Java 코드를 이용하여 처리하게 된다.

현재 프로젝트의 경우 Java코드를 이용하는 방식을 사용하는데, 이유는 아직 DB저장소가 선정되지 않은 상태이기 때문이다. 
나중에 DB가 정해지면 MemoryMemberRepository를 걷어내야 하는데, 그때 다른 코드를 수정하지 않고
설정코드의 return 클래스만을 변경하여 처리해주면 쉽게 변경이 가능하다.

DI 방식에는 3가지가 있는데 생성자 주입, 필드 주입, setter 주입이 있다. 요즘 권장하는 스타일은
생성자를 통해 주입되는 방식이다. 