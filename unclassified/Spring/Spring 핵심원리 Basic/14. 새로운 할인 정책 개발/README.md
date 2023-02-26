# 새로운 할인 정책 개발

새로운 할인 정책을 개발하며 OCP와 DIP가 깨지게 되는데, 이를 해결하는 방법에 대해 공부해본다.

이번에 적용하는 정책은 정액 할인이 아닌 정률 할인을 적용하기로 함. 클래스를 만들자.
```java
public class RateDiscountPolicy implements DiscountPolicy{

    private int discountPercent = 10;

    //오버라이드 된 메소드에 10퍼센트 할인 로직 적용
    ...
}

```

기존에 할인에 대한 인터페이스를 만들었으므로, 해당 인터페이스를 정률로 구현하는 클래스를 하나더 만듦.\
여기서는 할인율을 일단 10프로로 고정해준다.

테스트를 위한 테스트 코드를 만듦(단축키 cmd+shift+t)

```java
class RateDiscountPolicyTest {

    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.")
    void vip_o(){
        //given. 멤버 생성
        ...
        //when. 할인금액 받아오기
        ...
        //then. 금액 비교
        ...
    }

    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다.")
    void vip_x(){
        //given. 멤버 생성
        ...
        //when. 할인금액 받아오기
        ...
        //then. 금액 비교
        ...
    }
    
}

```
테스트 후 결과를 확인한다.