# request 스코프 예제 만들기

동시에 여러 HTTP 요청에 대해 구분하기 위한 로그를 request 스코프를 이용해 개발해보자.

원하는 출력은 다음과 같다.
```text
[d06b992f...] request scope bean create
[d06b992f...][http://localhost:8080/log-demo] controller test
[d06b992f...][http://localhost:8080/log-demo] service id = testId
[d06b992f...] request scope bean close
```

웹 스코프는 웹에서만 동작하기 때문에 먼저 스프링 웹 라이브러리를 추가해준다. 
```groovy
dependencies {
    //web 라이브러리 추가
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```

빌드 후 다음과 같이 코드 작성
```java
@Component
@Scope(value = "request")
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" +"[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init(){
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] Request scope bean create: " + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] Request scope bean close: " + this);
    }

}
```
* @Scope(value = "request")를 사용하여 request 스코프로 지정. 요청이 들어올때 생성되며 응답으로 나갈때 까지 유지됨.
* 요청을 하는 사용자마다 uuid를 생성하여 구분해주고, URL 정보 또한 넘겨주어 어떤 URL로 접근했는지를 확인.
* 그렇기에 PostConstruct 부분에서 uuid를 생성해주고 메세지로 표시해준다.

이제 실제 코드가 동작할 컨트롤러와 서비스를 만든다.
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
* 컨트롤러에서 RequestURL을 받아와서 myLogger에 세팅
* 이렇게 해두면 요청마다 구분하여 URL을 확인할 수 있음.
```java
@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final MyLogger myLogger;

    public void logic(String id) {
        myLogger.log("service id = "+ id);
    }
}
```
* 서비스는 간단하게 서비스 아이디의 로그만 남기도록 적용하였음.
* request scope 사용 없이 서비스에서 파라미터를 받아 처리도 가능하지만, 그럴 경우 파라미터가 많아져서 지저분해짐.
* 웹과 관련된 부분은 컨트롤러에서만 처리하고 서비스는 웹기술에 종속되지 않고 순수하게 유지하는것이 좋음.
* request scope를 사용하는 MyLogger 덕분에 코드와 계층을 깔끔하게 유지 가능

그 후 실행해보면 다음과 같이 에러가 발생한다.
```text
Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2023-04-15T23:02:43.608+09:00 ERROR 15585 --- [           main] o.s.boot.SpringApplication               : Application run failed

...
Caused by: org.springframework.beans.factory.support.ScopeNotActiveException: Error creating bean with name 'myLogger': Scope 'request' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:374) ~[spring-beans-6.0.4.jar:6.0.4]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.0.4.jar:6.0.4]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.0.4.jar:6.0.4]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1405) ~[spring-beans-6.0.4.jar:6.0.4]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1325) ~[spring-beans-6.0.4.jar:6.0.4]
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:885) ~[spring-beans-6.0.4.jar:6.0.4]
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:789) ~[spring-beans-6.0.4.jar:6.0.4]
	... 33 common frames omitted
Caused by: java.lang.IllegalStateException: No thread-bound request found: Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread? If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet: In this case, use RequestContextListener or RequestContextFilter to expose the current request.
	at org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes(RequestContextHolder.java:131) ~[spring-web-6.0.4.jar:6.0.4]
	at org.springframework.web.context.request.AbstractRequestAttributesScope.get(AbstractRequestAttributesScope.java:42) ~[spring-web-6.0.4.jar:6.0.4]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:362) ~[spring-beans-6.0.4.jar:6.0.4]
	... 39 common frames omitted
```
에러를 자세히 보면 'Error creating bean with name 'myLogger': Scope 'request' is not active for the current thread' 를 볼 수 있음.
request scope가 생성되지 않아서 발생하는 것임. request scope는 실제 사용자의 요청이 들어올 때 생성되어 응답이 나가면 종료되는 생명주기를 갖는 빈인데 
스프링을 실행 시키는 단계에서는 http 요청이 들어오지 않음. 그렇기에 request scope가 활성화되지 않아서 발생하는 에러임. \
이를 해결하는 방법은 전에 배웠던 provider로 해결 가능. 

