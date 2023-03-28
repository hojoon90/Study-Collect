# 다양한 의존관계 주입방법

의존관계 주입은 다음 4가지 방법이 있음
* 생성자 주입
* 수정자 주입(Setter)
* 필드 주입
* 일반 메서드 주입

### 생성자 주입
* 생성자를 통해 의존관계를 주입 받음
* 특징
  * 생성자 호출 시점에 1번만 호출되는것이 보장된다.
  * 불변, 필수 의존관계에서 사용
  * **생성자가 1개만 있을 시엔 @Autowired 생략 가능**
```java
@Component
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired  //생성자 1개일 시엔 생략 가능
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    ...
}
```

### 수정자 주입
* setter를 이용하여 의존관계를 주입받는 방법
* 특징
  * 선택, 변경 가능성이 있는 의존관계에서 사용
  * 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용
  * 사용하는 케이스는 어쩌다 한번. 많이 쓰이진 않음.

```java

@Component
public class OrderServiceImpl implements OrderService {

  private MemberRepository memberRepository;
  private DiscountPolicy discountPolicy;

  @Autowired
  public void setDiscountPolicy(DiscountPolicy discountPolicy) {
    this.discountPolicy = discountPolicy;
  }

  @Autowired
  public void setMemberRepository(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

    ...
}
```

### 필드 주입
* 필드에 바로 주입하는 방법
* 특징
  * 코드자체는 간결해지지만 외부에서 사용할 수 없으므로 테스트가 불가능. 예전에 많이 쓰던 방법.
  * 필드인젝션으로 테스트 하려면 setter를 이용해서 값을 세팅해주어야함. 아니면 NPE 발생.
  * 근데 그렇게 쓸거면 위의 수정자 주입을 쓰는게 더 나음.
  * DI 프레임 워크가 없으면 아무것도 못함.
```java

@Component
public class OrderServiceImpl implements OrderService {

  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private DiscountPolicy discountPolicy;
  
    ...
}
```

### 일반 메서드 주입
* 일반 메서드를 통해 주입 가능(?)
* 특징
  * 한번에 여러 필드 주입을 받을 수 있음.
  * 사용할 일 거의 없음..

```java
@Component
public class OrderServiceImpl implements OrderService {

    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;

    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    ...
}
```

* 참고
  * **모든 의존관계 자동주입을 해당 클래스가 스프링에서 관리하는 스프링 빈이어야 한다!**