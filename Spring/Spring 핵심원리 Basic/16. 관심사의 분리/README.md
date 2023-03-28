# 관심사의 분리

* 애플리케이션을 공연이라고 생각해보자. 인터페이스는 각각 배역이라고 생각한다.
* 공연에서 각각의 배역은 배우들이 정하는게 아니다. 아마 공연을 기획한 사람이 정하겠지?
* 그런데 남자주인공을 하는 배역이 '다른 사람은 부담스러우니 내가 편한 사람을 여자주인공 배역으로 데려올게!' 라고 생각해보자.
* 이렇게 되면 남자주인공은 자신의 역할인 '남자주인공'뿐만이 아닌 여자주인공을 '데려오는' 역할도 수행하게 된다. 다양한 책임이 생기는 것이다.
* 배우는 '대본을 외우고 배역을 소화하는 역할'만 해야 한다. 어떤 여주인공이 와도 똑같이 공연을 해야 한다.
* 공연 구성, 담당 배우 섭외, 배우 지정하는 책임은 그 것만 담당하는 '공연 기획자'가 해야 한다.
* 즉, 공연 기획자와 배우의 책임을 확실히 분리해야 한다.
* 앱도 마찬가지. 인터페이스는 본인의 역할만 수행해야 하며, 이 인터페이스들을 할당해 줄 별도의 '역할'이 필요하다.

이 역할을 수행하는 클래스를 만들자. 바로 AppConfig다.

```java
public class AppConfig {

    // MemberService 안에 직접 생성하던 MemoryMemberRepository를 AppConfig에서 생성한다.
    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }

}
```

위에서는 각 서비스를 생성할 때 필요한 구현체를 생성자를 통해 new 로 생성해주었다. 이렇게 되면 구현체들은 아래와 같은 모양이 된다.

```java

public class MemberServiceImpl implements MemberService {

//    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    ...
}

public class OrderServiceImpl implements OrderService {

//    private final MemberRepository memberRepository = new MemoryMemberRepository();
//    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    ...
}

```
각 구현체들은 더이상 new를 통해 구현체를 생성하지 않음. 대신 생성자를 통해 구현체들을 받아오고 있음.\
이는 곧 클래스들이 구현체에 의존하는 것이 아닌 인터페이스에만 의존하게 됨.

정리하면 다음과 같음.
* AppConfig를 통해 구현 객체들을 생성함.
  * MemberServiceImpl
  * MemoryMemberRepository
  * OrderServiceImpl
  * FixDiscountPolicy
* AppConfig가 생성한 객체의 참조를 생성자를 통해 넣어준다. 아래 클래스를 보면 좀더 이해 가능
  * MemberServiceImpl -> MemoryMemberRepository
  * OrderServiceImpl -> MemoryMemberRepository, FixDiscountPolicy
* 생성자를 통한 구현체 주입으로 인해 각 Service 구현체들은 인터페이스에만 의존하게 된다 (DIP 지킴.)
* 이를 통해 각 서비스 구현체는 의존관계에 대해 고민하지 않는다. 
  * 인터페이스에 정의된 기능만을 수행하게 된다. Service 들은 더이상 구현체가 어떤 것이 들어오든 상관하지 않는 것이다.
  * 모든 의존 관계는 AppConfig에서만 처리하게 된다.
  * 그렇기에 구현체들은 의존관계 고민 없이 실행에만 집중하게 된다.

**즉 관심사가 분리된 것**이다.

클래스 다이어그램

<img src="images/class.png" width="80%" height="80%"/>

그림을 보면 AppConfig를 통해 관심사가 분리된 것을 볼 수 있다.

인스턴스 다이어그램

<img src="images/instance.png" width="80%" height="80%"/>

* AppConfig는 MemoryMemberRepository를 생성하면서 그 생성값의 참조값을 MemberServiceImpl에 넘겨줌
* MemberServiceImpl입장에서 보면 의존관계가 외부에서 주입되는것 처럼 보이기에 이를 DI(Dependency Injection)라고 함.

AppConfig의 코드들이 DI를 나타내는 코드이다.

실제 코드로 사용해보면 아래와 같다.

```java
public class MemberApp {

    public static void main(String[] args) {
        //기존 로직
//        MemberService memberService = new MemberServiceImpl();
        //변경 로직
        AppConfig appConfig = new AppConfig();
        MemberService memberService = appConfig.memberService();
    
        ...
    }
}

public class OrderApp {
    
    public static void main(String[] args) {
        
        // 기존 로직
//        MemberService memberService = new MemberServiceImpl();
//        OrderService orderService = new OrderServiceImpl();
        
        // 변경 로직
        AppConfig appConfig = new AppConfig();
        MemberService memberService = appConfig.memberService();
        OrderService orderService = appConfig.orderService();
        
        ...
    }
}
```

AppConfig 생성 후 AppConfig를 통해 서비스들을 가져오고 있다. 실제 AppConfig를 통해 리턴되는 서비스들은 객체 주입이 모두 완료된 객체들이다.
해당 코드로 테스트 진행 시 정상적으로 테스트 통과 되는 것을 볼 수 있다. 