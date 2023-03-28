# 스프링으로 전환하기

지금까지 순수 자바로 만들었던 코드들을 스프링으로 전환해보자.

먼저 Config 부분을 아래와 같이 수정해준다.
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // 앱에 대한 설정 정보
public class AppConfig {

    @Bean   // @Bean 작성 시 Spring Container에 등록 됨.
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

    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

}
```
여기서 추가된 것은 @Configuration 어노테이션과 @Bean 어노테이션이다.
* @Configuration: 앱에 대한 설정정보가 들어간다.
* @Bean: Spring Container에 등록되는 메소드.

그리고 각 앱 코드들을 다음과 같이 바꿔준다.
```java
public class MemberApp {

    public static void main(String[] args) {
        //기존 로직
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
       
        //변경 로직
        // AppConfig 안에 있는 설정정보를 갖고 Bean을 컨테이너에 모두 등록해준다.
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        //앞엔 Bean의 이름, 뒤엔 반환 타입을 넣어준다. 이름은 기본적으로 메소드 이름으로 등록
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
    
        ...
    }
}

public class OrderApp {
    
    public static void main(String[] args) {
        // 기존 로직
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();
        
        // 변경 로직
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);
        
        ...
    }
}
```

* ApplicationContext 는 기본적으로 Spring Container다. 
* 기존 로직은 AppConfig를 new로 생성하여 사용자가 직접 DI를 해줬지만, 이젠 Spring Container가 그 작업을 해준다.
* Spring Container 는 AppConfig 안에 있는 설정정보를 갖고 Bean을 컨테이너에 모두 등록.
* 이렇게 등록된 빈들은 Spring Bean이라고 부름.
* 기존에는 개발자가 직접 AppConfig를 생성하여 메소드를 가져왔지만, 이젠 Spring Container에서 Bean을 찾아와서 사용함.