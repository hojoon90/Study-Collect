# AppConfig 리팩터링

AppConfig를 보면 역할에 따른 구현이 명확하게 보이지 않음.

```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }

}
```

위의 코드에서 확인해볼 내용은 다음과 같다.

* 역할들이 한눈에 보이지 않음
  * 서비스는 명확하게 보이지만, 레포지토리와 할인정책은 그렇지 않음
* 중복코드가 있음

코드를 아래와 같이 수정해주면 좀 더 역할이 명확해진다.

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
        return new FixDiscountPolicy();
    }

}
```

* 중복되는 코드가 줄어들었음
* 역할들이 모두 명확하게 나타남.
* 추후 코드 수정이 좀 더 편해짐.