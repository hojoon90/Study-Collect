# 핸들러 매핑과 핸들러 어댑터

스프링 프레임워크를 이해하기 위해선 핸들러 매핑과 핸들러 어댑터가 중요함.

옛날 방식으로 매핑과 어댑터를 처리하는 방법에 대해 확인.

### Controller 인터페이스를 통한 흐름 확인

과거에는 @Controller가 아닌 Controller 인터페이스를 통해 컨트롤러 구현.
```java
public interface Controller {
      ModelAndView handleRequest(HttpServletRequest request, 
                                 HttpServletResponse response) throws Exception;
}
```

컨트롤러를 구현하는 구현체 생성
```java
@Component("/springmvc/old-controller")
public class OldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldCOntroller.handleRequest");
        return null;
    }
}
```
코드를 만들어준 후 서버를 실행하여 위의 빈 네임으로 접근하면 해당 클래스가 호출됨.\
위에 클래스가 호출되기 위해선 두가지 조건이 필요함
* 핸들러 매핑에서 컨트롤러를 찾을수 있어야 함.
  * 여기선 스프링 빈의 이름으로 컨트롤러를 찾음
* 핸들러 매핑으로 찾은 컨트롤러를 실행할 어댑터가 필요함.
  * Controller 인터페이스를 실행할 수 있는 핸들러 어댑터를 찾아서 실행.

HandlerMapping의 호출 순서는 DispatcherServlet.initHandlerMappings 메소드에서 확인가능.\
가져오는 순서는 다음과 같다.
1. requestMappingHandlerMapping
2. beanNameHandlerMapping
3. routerFunctionMapping
4. resourceHandlerMapping

마찬가지로 HandlerAdapter 순서는 initHandlerAdapter에서 확인 가능.
1. requestMappingHandlerAdapter 
2. handlerFunctionAdapter 
3. httpRequestHandlerAdapter 
4. simpleControllerHandlerAdapter 

#### 핸들러 매핑으로 핸들러 조회
* OldController의 경우 RequestMapping이 없음.
* 다음 순서인 beanNameHandlerMapping을 통해 빈 이름으로 핸들러를 찾아냄.

#### 핸들러 어댑터 조회
* HandlerAdapter의 supports 를 순서대로 호출
* simpleControllerHandlerAdapter가 Controller를 지원하므로 해당 어댑터를 사용함.

#### 핸들러 어댑터 실행
* DispatcherServlet에서 simpleControllerHandlerAdapter를 실행하면서 핸들러 정보를 넘겨줌
* 어댑터에서는 핸들러인 OldController를 내부에서 실행하고, 그 결과값으로 ModelAndView 객체를 리턴

사용된 매핑과 어댑터
* HandlerMapping -> beanNameHandlerMapping
* HandlerAdapter -> simpleControllerHandlerAdapter

### HttpRequestHadler를 통한 흐름 확인

컨트롤러가 아닌 다른 핸들러로 한번 더 테스트 해보자.

```java
public interface HttpRequestHandler {
	void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

}
```

위에와 똑같이 구현
```java
@Component("/springmvc/request-handler")
public class MyHttpRequestHandler implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MyHttpRequestHandler.handleRequest");
    }
}
```

#### 핸들러 매핑으로 핸들러 조회
* MyHttpRequestHandler 역시 RequestMapping이 없음.
* 다음 순서인 beanNameHandlerMapping 로 핸들러를 찾음.

#### 핸들러 어댑터 조회
* HandlerAdapter의 supports 를 순서대로 호출
* httpRequestHandlerAdapter 가 HttpRequestHandler 를 지원하므로 해당 어댑터를 사용함.

#### 핸들러 어댑터 실행
* DispatcherServlet에서 httpRequestHandlerAdapter 를 실행하여 핸들러 정보를 넘겨줌
* 어댑터에서는 핸들러인 MyHttpRequestHandler 를 내부에서 실행. handlerRequest는 void 이므로 내부에서 바로 실행됨.

사용된 매핑과 어댑터
* HandlerMapping -> beanNameHandlerMapping
* HandlerAdapter -> httpRequestHandlerAdapter