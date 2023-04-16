# 스코프와 Provider

로그가 나올 수 있도록 다음과 같이 수정
```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {
    private final LogDemoService logDemoService;
//    private final MyLogger myLogger;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        MyLogger myLogger = myLoggerProvider.getObject();   //추가
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");

        return "OK";
    }
}
```
서비스도 아래와 같이 변경해준다.
```java
@Service
@RequiredArgsConstructor
public class LogDemoService {

//    private final MyLogger myLogger;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    public void logic(String id) {
        MyLogger myLogger = myLoggerProvider.getObject();   //추가
        myLogger.log("service id = " + id);
    }
}
```
* MyLogger를 ObjectProvider로 감싸주어 의존주입을 받을 수 있도록 해주었다.
    * ObjectProvider는 getObject를 호출하는 시점까지 빈 생성을 해주지 않는다.(지연시킨다.)
* 실제 getObject 를 호출하는 시점에 http request에 대한 처리가 진행중이므로 MyLogger 빈이 생성된다.
* 그럼 getObject가 컨트롤러와 서비스에서 각각 호출 되는데 빈이 달라지는게 아닌가?
  * 같은 HTTP 호출이기 때문에 같은 스프링 빈이 반환된다.
  * 요청으로 빈이 하나 생성되면 요청이 끝나기전까지 그 빈을 공유한다!

로그는 아래와 같이 남는다
```text
[e33b445b-cd9e-4114-98a0-71464dbb75c9] Request scope bean create: hello.core.common.MyLogger@657c1716
[e33b445b-cd9e-4114-98a0-71464dbb75c9][http://localhost:8080/log-demo] controller test
[e33b445b-cd9e-4114-98a0-71464dbb75c9][http://localhost:8080/log-demo] service id = testId
[e33b445b-cd9e-4114-98a0-71464dbb75c9] Request scope bean close: hello.core.common.MyLogger@657c1716
```
UUID 도 동일하고, 서비스에서 getObject를 호출하였더라도 객체 주소가 동일한 것을 확인 가능.