# @Configuration과 싱글톤

AppConfig 클래스를 보면 memberService와 orderService에서 각각 memberRepository를 호출 하는 것을 볼 수 있음.

```java
@Bean
public MemberService memberService() {
    return new MemberServiceImpl(memberRepository());
}

@Bean
public MemberRepository memberRepository() {
    return new MemoryMemberRepository();
}

@Bean
public OrderService orderService() {
    return new OrderServiceImpl(memberRepository(), discountPolicy());
}
```
* 만약 지금과 같은 상황이라면 memberRepository는 총 3번 생성되게 됨.
* 싱글톤이 깨지게 되는 구조.
* 정말 3번 호출하는지 테스트 진행.

memberService와 orderService 에 다음과 같은 코드를 추가
```java
public MemberRepository getMemberRepository(){
    return memberRepository;
}
```

그 후 아래와 같이 테스트 코드를 만들어줌
```java
package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);

        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println("memberRepository = " + memberRepository);
        System.out.println("memberRepository1 = " + memberRepository1);
        System.out.println("memberRepository2 = " + memberRepository2);

        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);

    }

}
```
실제 스프링 빈 생성 후 각 서비스의 memberRepository와 빈으로 등록된 memberRepository를 출력하면 모두 같은 값이 나오는것 확인 가능
```text
memberRepository = hello.core.member.MemoryMemberRepository@9bd0fa6
memberRepository1 = hello.core.member.MemoryMemberRepository@9bd0fa6
memberRepository2 = hello.core.member.MemoryMemberRepository@9bd0fa6
```
참조값이 모두 동일. 테스트도 통과되는 것을 볼 수 있음.

그렇다면 스프링 빈이 생성될 때 어떻게 생성되는지 확인.\
AppConfig에 출력들을 찍어줌

```java
@Bean
public MemberService memberService() {
    System.out.println("AppConfig.memberService");
    return new MemberServiceImpl(memberRepository());
}

@Bean
public MemberRepository memberRepository() {
    System.out.println("AppConfig.memberRepository");
    return new MemoryMemberRepository();
}

@Bean
public OrderService orderService() {
    System.out.println("AppConfig.orderService");
    return new OrderServiceImpl(memberRepository(), discountPolicy());
}
```

예상대로라면
1. AppConfig.memberService
2. AppConfig.memberRepository
3. AppConfig.memberRepository
4. AppConfig.orderService
5. AppConfig.memberRepository

순으로 출력되어야 함.

하지만 실제 출력은 다음과 같이 한번씩만 출력됨
```text
AppConfig.memberService
AppConfig.memberRepository
AppConfig.orderService
```

싱글톤 패턴이 꺠지지 않고 유지됨.