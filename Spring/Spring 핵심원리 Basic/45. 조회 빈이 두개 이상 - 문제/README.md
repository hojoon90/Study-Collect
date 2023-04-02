# 조회 빈이 2개 이상 - 문제

우리가 지금까지 사용한 @Autowired는 타입으로 조회함.

만약 DiscountPolicy의 하위타입인 FixDiscountPolicy와 RateDiscountPolicy를 둘다 빈으로 등록할때는 어떻게 될까

```java
@Component
public class RateDiscountPolicy implements DiscountPolicy {
    ...
}

@Component
public class FixDiscountPolicy implements DiscountPolicy{
    ...
}
```

위와 같이 두개 등록 후 실행해보면 아래와 같이 에러가 발생한다.
```text

Error creating bean with name 'orderServiceImpl' defined in file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/order/OrderServiceImpl.class]: Unsatisfied dependency expressed through constructor parameter 1: No qualifying bean of type 'hello.core.discount.DiscountPolicy' available: expected single matching bean but found 2: fixDiscountPolicy,rateDiscountPolicy
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'orderServiceImpl' defined in file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/order/OrderServiceImpl.class]: Unsatisfied dependency expressed through constructor parameter 1: No qualifying bean of type 'hello.core.discount.DiscountPolicy' available: expected single matching bean but found 2: fixDiscountPolicy,rateDiscountPolicy
	at app//org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:798)

...
```
빈이 1개가 매칭되기를 기대했는데 2개가 매칭됐다는 예외문이 나온다.

이때 하위타입을 지정할 수도 있지만, 그럴경우 DIP를 위배하며 유연성이 떨어짐.\
스프링 빈을 수동으로 등록할 수도 있지만, 자동으로 등록하는 여러가지 방법들을 확인해본다.