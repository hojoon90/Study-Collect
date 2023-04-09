# 빈 등록 초기화, 소멸 메서드

다음은 메서드를 지정하는 방식

```java
package hello.core.lifecycle;

public class NetworkClient {

    ...

    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}

```
이름을 위와 같이 변경

```java
@Configuration
static class LifeCycleConfig {

    @Bean(initMethod = "init", destroyMethod = "close")
    public NetworkClient networkClient() {
        NetworkClient networkClient = new NetworkClient();
        networkClient.setUrl("http://hello.spring.dev");
        return networkClient;
    }
}
```
* config의 빈에 위와 같이 initMethod 에 init을, destroyMethod 에 close를 사용.
* 인터페이스 등록 처럼 동일하게 동작하는 것을 확인 가능

```text
생성자 호출, url = null
NetworkClient.init
connect = http://hello.spring.dev
call = http://hello.spring.dev, message = 초기화 연결 메세지
23:34:48.358 [Test worker] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@cd1e646, started on Sun Apr 09 23:34:48 KST 2023
NetworkClient.close
close = http://hello.spring.dev
```

특징
* 메서드 이름 지정 가능
* 스프링 빈이 스프링 코드에 의존하지 않음.
* 외부 라이브러리에도 초기화, 종료 메서드 적용 가능.


destroyMethod 의 경우 기본값이 "(inferred)"로 되어있음. 이 기본값은 close, shutdown이라는 이름의 메서드를 자동 호출해줌.\
그렇기에 destroyMethod를 별도로 작성해주지 않아도 close 혹은 shutdown 메서드만 있으면 자동으로 스프링 빈을 소멸 시킴.