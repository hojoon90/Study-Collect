# 주문과 할인 도메인 실행과 테스트

주문과 할인 도메인 역시 이전 회원 도메인 테스트와 크게 다르지 않다.

* OrderApp을 통해 테스트 진행
```java
public class OrderApp {

    public static void main(String[] args) {
        //MemberService와 OrderService 생성
        ...
        //회원 정보 입력
        ...
        //회원 가입 진행
        ...
        //주문 정보 생성
        ...
        //생성된 주문정보에 대한 toString 출력
        ...
    }

}

```
* 동일하게 테스트 코드를 작성하여 출력
```java
public class OrderServiceTest {

    //MemberService 생성
    //OrderService 생성

    @Test
    void createOrder(){
        //given (회원 정보 생성, 회원 가입)
        ...
        
        //when (주문 정보 생성)
        ...
        
        //then (할인 금액이 정상적으로 할인 됐는지 확인)
        ...
    }
}

```
테스트 정상 동작 확인.
