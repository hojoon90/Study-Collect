# @Autowired 필드 명, @Qualifier, @Primary

빈이 2개 이상일 경우 다음 세가지 방법으로 해결 가능

### @Autowired

* 타입 매칭 시도 시 여러빈이 있으면 필드 이름, 파라미터 이름으로 빈 이름을 추가 매칭

다음과 같은 경우임
```java
@Autowired
private DiscountPolicy discountPolicy; // discountPolicy라는 빈 이름으로 등록됨.

@Autowired
private DiscountPolicy rateDiscountPolicy; // rateDiscountPolicy라는 빈 이름으로 등록됨.
```

### @Qualifier
추가 구분자를 추가해주는 어노테이션. 빈 이름을 변경하진 않는다.

```java
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {
    ...
}

@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy{
    ...
}
```

사용시엔 생성자 혹은 수정자 주입때 @Qualifier 어노테이션을 달아준다.

```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy rateDiscountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = rateDiscountPolicy;
}
```

이렇게 되면 mainDiscountPolicy인 RateDiscountPolicy가 빈으로 등록된다.

### @Primary
우선순위를 지정. @Autowired 로 조회된 빈이 2개 이상일 경우 @Primary 어노테이션이 걸린 빈이 우선순위로 사용.

```java
@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy {
    ...
}

@Component
public class FixDiscountPolicy implements DiscountPolicy{
    ...
}
```

이렇게 사용할 경우 @Qualifier 처럼 별도의 세팅없이도 @Primary 가 달려있는 빈이 자동 등록된다.

```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

@Primary와 @Qualifier 어노테이션의 우선순위는 어떻게 될까? 
* 우선순위는 자세한 내용이 있는 어노테이션이 우선순위를 갖는다.
* @Qualifier의 경우 이름까지 상세 지정을 해주기 때문에 우선순위를 갖게 된다.