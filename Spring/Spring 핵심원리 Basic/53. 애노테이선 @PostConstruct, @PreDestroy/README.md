# 애노테이선 @PostConstruct, @PreDestroy

애노테이션으로 변경해보자. 스프링에서 권장하는 방식

```java
package hello.core.lifecycle;

public class NetworkClient {

    ...

    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}

```

기존에 Bean에서 설정한 값은 삭제.

```java
@Configuration
static class LifeCycleConfig {

    @Bean
    public NetworkClient networkClient() {
        NetworkClient networkClient = new NetworkClient();
        networkClient.setUrl("http://hello.spring.dev");
        return networkClient;
    }
}
```
호출 하면 정상 동작하는것을 확인 가능

```text
생성자 호출, url = null
NetworkClient.init
connect = http://hello.spring.dev
call = http://hello.spring.dev, message = 초기화 연결 메세지
23:46:52.323 [Test worker] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@cd1e646, started on Sun Apr 09 23:46:52 KST 2023
NetworkClient.close
close = http://hello.spring.dev
```

특징
* 최신 스프링에서 가장 권장하는 방식.
* 패키지가 javax.annotation.PostContruct 임. 즉 스프링이 아닌 자바 표준이기 때문에 다른 컨테이너에서도 사용 가능.
* 컴포넌트 스캔과 어울림.
* 외부라이브러리에 적용을 못함. 외부라이브러리 적용은 앞에서 배운 방식으로 사용.