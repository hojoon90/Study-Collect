# 애노테이션 직접 만들기

위에서 @Qualifier로 작성한 mainDiscountPolicy 를 애노테이션으로 만들자.

```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}

```

만들어준 애노테이션은 다음과 같이 적용 가능
```java
@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy {
    ...
}

@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy rateDiscountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = rateDiscountPolicy;
    }

    ...
}
```

이런식으로 사용 시 @Qualifier 사용 없이 명시적으로 애노테이션 사용이 가능함.