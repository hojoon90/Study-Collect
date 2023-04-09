# 인터페이스 InitializingBean, DisposableBean

인터페이스를 통한 초기화와 소멸전 콜백

다음과 같이 코드를 추가해준다.
```java

public class NetworkClient implements InitializingBean, DisposableBean {

    ...
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메세지");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }
}
```

그 후 테스트 코드를 돌리면 다음과 같이 출력된다.
```text
생성자 호출, url = null
NetworkClient.afterPropertiesSet
connect = http://hello.spring.dev
call = http://hello.spring.dev, message = 초기화 연결 메세지
23:23:47.274 [Test worker] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@cd1e646, started on Sun Apr 09 23:23:47 KST 2023
NetworkClient.destroy
close = http://hello.spring.dev
```

* 생성자 호출 후 스프링 의존주입까지 완료되어 빈 사용준비.
* 준비가 되면 InitializingBean 인터페이스의 afterPropertiesSet(초기화 메서드) 을 호출
* connect() 메소드가 호출되며 세팅한 URL 이 출력됨.
* 그 후 스프링 컨테이너가 내려가면서 종료 전에 DisposableBean의 destroy(소멸 메서드) 호출
* 그러면 빈이 소멸 됨.


단점
* 두 인터페이스는 스프링 전용 인터페이스임
* 초기화, 소멸 메소드의 이름 변경 불가
* 외부라이브러리엔 적용 불가

위 방법들은 초창기에 사용하였으며 지금은 거의 사용하지 않음 (2003년 12월에 나옴......)