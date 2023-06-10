# MVC 프레임워크 V1 - 프론트 컨트롤러
* 먼저 프론트 컨트롤러 역할부터 구현
* 동작 순서는 아래와 같음.
  1. 프론트 컨트롤러가 요청을 받음
  2. 해당 요청에 맞는 컨트롤러 호출
  3. 해당 컨트롤러에서 JSP 반환

![flow.png](images%2Fflow.png)

### 컨트롤러 생성
각 기능들의 컨트롤러를 만들어줌. 그 전에 서블릿과 비슷한 컨트롤러 인터페이스를 만듦
```java
public interface ControllerV1 {
    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
```
이렇게 만들면 프론트 컨트롤러에서는 이 인터페이스만 호출하여 어떤 컨트롤러든 일관성 있게 로직 수행을 시킬 수 있다.\
각 기능의 컨트롤러를 만들어줌. 로직은 서블릿과 동일하게 만들되 위 인터페이스를 구현한 메소드 안에 로직을 넣어준다.
```java
public class MemberFormControllerV1 implements ControllerV1 {
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}

public class MemberListControllerV1 implements ControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ...
    }
}

public class MemberSaveControllerV1 implements ControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ...
    }
}
```

다음 프론트 컨트롤러를 만들어줌. 프론트 컨트롤러는 서블릿으로 만들어준다.
```java
@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {

    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        ControllerV1 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        controller.process(request, response);
    }
}
```
* urlPatterns 에서 /front-controller/v1/ 뒤에 어떤 값이 들어오더라도 프론트 컨트롤러를 거치게 만듦
* 프론트 컨트롤러 생성 시 각 URI 에 맞는 컨트롤러를 생성하여 Map에 매핑해 주었음.
* 매핑에 없는 URL로 들어왔을 경우 404 리턴
* controller.process를 통해 각 컨트롤러 안에 구현한 로직이 수행 되도록 처리 되었음.

그 후 서버를 로컬에서 구동하여 테스트 하면 기존 서블릿으로 구현한 코드와 동일하게 동작하는 것을 확인할 수 있음.
