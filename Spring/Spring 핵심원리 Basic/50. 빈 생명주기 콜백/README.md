# 빈 생명주기 콜백 **

** 어떤 이야기인지는 알겠지만 예시랑 연결이 잘 되지 않음... 다시 공부 필요

* APP 실행시점에서 APP은 기본적으로 DB 와 커넥션 풀을 이용하여 연결을 미리 맺어듬.
* 그리고 앱 종료 시 연결을 모두 끊어줌. 즉 객체의 초기화와 종료 작업이 필요함
* 스프링을 통해 초기화, 종료 작업이 어떻게 이루어지는지 확인해보자.

단순 문자만 출력하는 네트워크 클래스를 통해 예시로 확인.
```java
package hello.core.lifecycle;

public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
        connect();
        call("초기화 연결 메세지");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void connect(){
        System.out.println("connect = " + url);
    }

    public void call(String message){
        System.out.println("call = " + url + ", message = " + message);
    }

    public void disconnect() {
        System.out.println("close = " + url);
    }
}
```

다음 테스트 코드 작성
```java
package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        client.disconnect();
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig {

        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello.spring.dev");
            return networkClient;
        }
    }
}
```
테스트를 실행하면 다음과 같은 결과가 나옴.

```text
생성자 호출, url = null
connect = null
call = null, message = 초기화 연결 메세지
close = http://hello.spring.dev
```

* 메세지를 보면 처음 생성 시 URL이 null로 표기되어 연결되는 것을 볼 수 있음.
  * 생성 시 URL을 세팅해주지 않았기 때문.
* 그 후 URL이 세팅 되고 나서 빈으로 등록 된다.
* 그렇기에 client.disconnect()를 호출하면 URL이 리턴되는것을 확인할 수 있음.

스프링은 **객체생성 -> 의존관계 주입** 의 라이프사이클을 가짐. 그렇기에 빈을 사용시엔 이 라이프사이클이 끝나야 필요 데이터 사용이 가능해짐.

* 스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해 초기화 시점을 알려주는 기능 제공
* 스프링은 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 줌.

스프링 빈의 이벤트 라이프사이클은 아래와 같다.
* 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 사용 -> 소멸 전 콜백 -> 스프링 종료

