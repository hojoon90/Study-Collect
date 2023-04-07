# 자동 수동의 올바른 실무 운영 기준

* 스프링 빈은 기본적으로 편리한 자동기능을 사용해주는게 좋다.
* 스프링은 계층에 맞는 애노테이션(@Repository, @Service, @Controller)을 자동으로 스캔할 수 있도록 지원함
* 자동으로 빈을 등록해도 OCP,DIP를 지킬 수 있음.

### 수동 빈 사용 경우

수동빈은 업무 로직 보단 기술 지원 로직에서 사용하는게 더 적합.

* 업무 로직 빈
  * 컨트롤러, 서비스, 리포지토리
* 기술 지원 빈
  * DB 연결 공통로그 처리 등에 대한 업무로직을 지원하는 기술들

기술 지원 로직은 업무로직에 비해 수가 적으며 APP 전반에 영향을 미침. 그리고 기술 지원이기 때문에 로직이 잘 드러나지 않음.\
그렇기에 수동빈으로 등록해주어서 설정정보에 바로 나타나게 하는것이 유지보수에 좋음.

다만, 위에서 진행했던 DiscountPolicy의 경우 추상화에 따라 빈이 두가지로 나뉘는 것을 볼 수 있음.\
이럴 경우 수동빈으로 등록해주면 유지보수 할 때 DiscountPolicy 인터페이스를 상속 받는 구현체가 2개가 있다는 것을 쉽게 확인할 수 있다.

```java
@Configuration
public class DiscountPolicyConfig(){
    
    @Bean
    public DiscountPolicy rateDiscountPolicy(){
        return new RateDiscountPolicy();
    }
    
    @Bean
    public DiscountPolicy fixDiscountPolicy(){
        return new FixDiscountPolicy();
    }
    
}
```

다만 스프링과 스프링 부트가 자동으로 등록해주는 빈들은 모두 예외임. 이런 것들은 매뉴얼을 참고하여 잘 사용하는것이 더 중요하다.