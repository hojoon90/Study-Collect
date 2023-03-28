# 싱글톤 방식의 주의점

싱글톤 방식으로 사용할때 주의할 점
* 공유되는 필드는 사용하지 않는다.
* 무상태로 설계한다.
  * 특정 클라이언트가 값을 변경할 수 있으면 안된다.
  * 읽기만 가능해야함
  * 지역변수, 파라미터, ThreadLocal등을 사용

**스프링 빈의 필드에 공유값을 설정하면 장애발생 위험성이 올라간다**

문제가 될수 있는 코드
```java
package hello.core.singleton;

public class StatefulService {
    private int price; //상태를 유지하는 필드

    public void order(String name, int price){
        System.out.println("name = " + name + " price = " + price);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

```
```java
package hello.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //ThreadA: A사용자 10000원 주문
        statefulService1.order("userA", 10000);
        //ThreadB: B사용자 20000원 주문
        statefulService2.order("userB", 20000);

        //ThreadA: A사용자 주문 금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        assertThat(statefulService1.getPrice()).isEqualTo(20000);

    }

    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }

}
```
* 위의 코드의 경우 StatefulService 인스턴스가 공유되는 경우(참조값이 같음).
* A 사용자가 10000원을 주문했는데 바로 뒤에 B 사용자가 20000원을 주문 한 경우.
* 그후 A 사용자가 주문 금액을 조회.
* 이렇게 되면 주문 금액이 20000원으로 변경되게 됨.
* StatefulService 의 price 필드가 공유되고 있기 때문!!
* 이런식의 필드 사용은 싱글톤 패턴에서 굉장히 위험함.
* 스프링은 무상태(stateless)로 설계해야함.