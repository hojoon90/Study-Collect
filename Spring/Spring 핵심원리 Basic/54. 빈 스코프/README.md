# 빈 스코프

* 빈 스코프는 빈이 존재할수 있는 범위를 뜻함.
* 스프링은 기본적으로 싱글톤 스코프로 생성 .
* 그렇기에 스프링 빈이 스프링 컨테이너 시작시 생성되고 종료 때까지 유지되는 것.

스코프는 다음과 같이 지원.
* 싱글톤: 기본 스코프. 스프링 컨테이너 시작부터 종료까지 유지된다.
* 프로토타입: 스프링 컨테이너에서 빈의 생성, 의존관계 주입까지만 관여 후 그 이후는 관리하지 않음.
* 웹 관련 스코프
  * request: 웹 요청이 들어오고 나갈 때 까지 유지.
  * session: 웹 세션이 생성되고 종료될 때 까지 유지.
  * application: 웹의 서블릿 컨텍스와 같은 범위로 유지.

빈 스코프 등록 방법

* 컴포넌트 스캔 자동 등록
```java
@Scope("prototype")
@Component
public class helloBean(){}
```

수동 등록
```java
@Scope("prototype")
@Bean
PrototypeBean HelloBean(){
    return new HelloBean();
}
```