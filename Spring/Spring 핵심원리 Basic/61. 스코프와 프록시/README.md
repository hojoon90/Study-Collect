# 스코프와 프록시

코드를 좀더 줄여보자.

아래와 같이 MyLogger를 수정
```java
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    ...
}
```
그 후 Provider를 달아주었던 변수들을 모두 원래대로 돌려준다.
```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {
    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");

        return "OK";
    }
}
```
```java
@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final MyLogger myLogger;

    public void logic(String id) {
        myLogger.log("service id = " + id);
    }
}
```
그 후 서버를 띄우면 정상 동작하고, 호출도 아래처럼 정상적으로 되는것을 확인할 수 있음.
```text
[72614a2e-8f15-4585-b98c-e953c80ef7ea] Request scope bean create: hello.core.common.MyLogger@319b028e
[72614a2e-8f15-4585-b98c-e953c80ef7ea][http://localhost:8080/log-demo] controller test
[72614a2e-8f15-4585-b98c-e953c80ef7ea][http://localhost:8080/log-demo] service id = testId
[72614a2e-8f15-4585-b98c-e953c80ef7ea] Request scope bean close: hello.core.common.MyLogger@319b028e
```

이렇게 동작하는 이유는 프록시 덕분이다.
* 위에 추가한 'proxyMode = ScopedProxyMode.TARGET_CLASS' 덕분에 MyLogger의 가짜 프록시 클래스가 만들어짐.
* 이 가짜 프록시 클래스 덕분에 HTTP 요청에 상관없이 미리 의존주입이 가능해짐.

실제로 찍어보면 다음과 같이 나옴.
```text
myLogger = class hello.core.common.MyLogger$$SpringCGLIB$$0
```
* 스프링 컨테이너에서 CGLIB이라는 바이트코드 조작 라이브러리를 통해 MyLogger를 상속받는 가짜 클래스를 만듬.
* 이 가짜 객체는 스프링 컨테이너에 진짜 객체 대신에 등록되어 있음.
* ac.getBean("myLogger", MyLogger.class)를 조회해도 위의 가짜 객체가 반환됨. 의존관계도 가짜가 등록되어있음.
* 이 가짜 프록시 객체는 진짜 객체를 찾는 법을 알고 있기 때문에 요청이 들어올 때 진짜 객체의 메서드를 호출.
* 가짜 프록시 객체는 원본을 상속받아 만들어진 것이므로 클라이언트 입장에서는 이 객체가 원본인지 아닌지 알 필요가 없음.

프록시 특징
* 프록시를 사용하면서 클라이언트는 싱글톤 빈을 사용하듯이 request scope 사용 가능.
* provider든 프록시든 중요한건 진짜 객체를 필요시점까지 지연처리 해준다는 것임.
* 애노테이션 변경 하나만으로 원본 객체를 프록시로 대체 가능. -> 다형성, DI 컨테이너의 강점.
* 다만 싱글톤을 사용하는것 같기 때문에 주의하면서 사용이 필요하며, 꼭 필요한 곳에서 최소한으로만 사용한다.