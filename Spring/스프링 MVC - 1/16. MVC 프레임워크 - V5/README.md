# MVC 프레임워크 - V5 유연한 컨트롤러

형태가 각각 다른 컨트롤러를 사용할 수 있도록 프레임워크를 수정.

### 어댑터 패턴
* 지금까지 개발한 프론트 컨트롤러는 하나의 컨트롤러 인터페이스만 사용 가능했음.
* 그러나 어댑터 패턴을 이용해서 서로 다른 컨트롤러를 사용할 수 있음.
* 어댑터 패턴을 이용해 여러 컨트롤러를 사용할 수 있도록 코드를 변경

![flowV5.png](images%2FflowV5.png)

* 핸들러 어댑터: 중간에 어댑터 역할을 해줌. 이 어댑터 덕분에 여러 컨트롤러를 호출 가능
* 핸들러: 컨트롤러를 핸들러로 이름 변경. 이제 컨트롤러 뿐만이 아닌 어떤 것이라도 어댑터만 있으면 처리가 가능하기 떄문.

### Handler Adapter interface 추가
어댑터 역할을 할 수 있는 인터페이스를 추가한다.
```java
public interface MyHandlerAdapter {

    boolean supports(Object handler);

    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;
}
```
* supports: 넘어온 핸들러가 처리되는 핸들러인지 판단
* handler: 호출된 경로에 맞는 실제 컨트롤러를 호출하여 ModelView를 반환해주는 메소드

그리고 위의 어댑터를 구현하는 구현 클래스를 만들어준다.
```java
public class ControllerV3HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV3);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV3 controller = (ControllerV3) handler;

        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);

        return mv;
    }
    
}
```
* 각 메소드는 위의 설명과 동일하게 동작하는것을 확인 가능.
* handler 의 경우 ControllerV3 로 다운캐스팅 되었는데, 넘어오는 handler 는 ControllerV3밖에 없기 때문에 상관없음
  * 어짜피 supports를 먼저 호출하여 해당 값이 true일 때만 handle 메소드를 호출하니 괜찮다.

### FrontController 생성
프론트 컨트롤러를 생성해준다.
```java
@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();    // 핸들러(컨트롤러) 매핑 메소드
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
    }

    private void initHandlerMappingMap() {
        ...
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Object handler = getHandler(request);
        
        ...
        
        MyHandlerAdapter adapter = getHandlerAdapter(handler);
        ModelView mv = adapter.handle(request, response, handler);

        ...
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Object handler = handlerMappingMap.get(requestURI);
        return handler;
    }
    ...
}
```
* 기존에 생성자에서 URL만 매핑하였었는데 추가적으로 생성자에 어댑터도 추가하도록 해주었음.
* initHandlerAdapters는 handlerAdapters 리스트 안에 추가해줄 어댑터를 넣어준다.
* 넘어온 요청(request)을 통해 handler를 찾아냄.
* 그 후 getHandlerAdapter를 통해 알맞은 어댑터를 찾음. (없으면 Exception 처리)
* 받아온 어댑터의 handle 메소드를 통해 컨트롤러를 직접 호출
  * 호출 후 ModelView를 리턴받음
* 나머지 로직은 그대로 실행하여 렌더링 진행.

서버 실행 후 호출 시 정상 동작 하는것을 확인할 수 있음.\
이를 통해 좀 더 유연하게 컨트롤러를 받아 호출할 수 있게 되었음.