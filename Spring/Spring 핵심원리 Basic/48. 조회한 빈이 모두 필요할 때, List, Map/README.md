# 조회한 빈이 모두 필요할 때, List, Map

조회한 빈이 모두 필요할 경우 Map과 List로 받아서 사용이 가능하다.

코드로 바로 확인.

```java
static class DiscountService{
    private final Map<String, DiscountPolicy> policyMap;
    private final List<DiscountPolicy> policies;

    @Autowired
    public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
        this.policyMap = policyMap;
        this.policies = policies;
        System.out.println("policyMap = " + policyMap);
        System.out.println("policies = " + policies);
    }

    public int discount(Member member, int price, String discountCode) {
        DiscountPolicy discountPolicy = policyMap.get(discountCode);
        return discountPolicy.discount(member, price);
    }
}
```
* 스프링 빈 등록 시 policyMap에 빈 이름을 Key로, 객체를 Value로 하여 Map에 주입 받음
* 리스트는 배열에 DiscountPolicy를 상속 받는 FixDiscountPolicy, RateDiscountPolicy 두 객체가 각각 들어감.

```java
public class AllBeanTest {
    @Test
    void findAllBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(1000);

        int rateDiscountPrice = discountService.discount(member, 20000, "rateDiscountPolicy");
        assertThat(rateDiscountPrice).isEqualTo(2000);
    }
}
```
* AnnotationConfigApplicationContext를 통해 AutoAppConfig, DiscountService를 빈 등록해줌.]
* 테스트 Member 객체 생성
* DiscountService에 있는 discount 메소드 호출
* discount 메소드는 뒤에 받는 빈 이름에 따라 알맞은 DiscountPolicy 객체를 반환해줌.
* 가져온 객체에 맞게 가격 할인이 되었는지 확인.