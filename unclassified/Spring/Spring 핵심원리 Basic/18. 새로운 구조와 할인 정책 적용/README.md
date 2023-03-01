# 새로운 구조와 할인 정책 적용

* 구조 변경 후 정책 변경 혹은 레포지토리의 변경이 필요할 떄 AppConfig만 수정.
* 사용영역와 구성영역이 분리된 것이며, 우리는 구성영역의 코드만 변경.

```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    private MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    private DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

}
```

* discountPolicy 메소드에서 정액 할인 대신 정률 할인 구현체로만 변경.
* 할인 정책을 변경해도 AppConfig의 코드만 수정.
* 구조 변경 전에는 OrderServiceImpl을 직접 수정해주어야 했는데, 이제 그럴 필요가 없음.
* 그림으로 표현하면 아래와 같음

<img src="images/change.png" width="80%" height="80%"/>

* 정책 변경시에도 구성 영역만 영향이 갈 뿐, 사용 영역은 전혀 영향이 없음. (OCP 만족)
* 각 서비스도 인터페이스만 사용할 뿐, 구현체를 직접 의존하지 않는다 (DIP 만족)

