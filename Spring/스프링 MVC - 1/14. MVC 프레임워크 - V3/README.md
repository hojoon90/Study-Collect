# MVC 프레임워크 - V3 Model 추가
* V3에서는 Model에 대해 추가한다.
* 컨트롤러에서 서블릿의 종속성을 제거함.
  * 컨트롤러 안에서 request, response를 받아서 처리하고 있음.
  * 컨트롤러는 request, response 전체가 아닌 파라미터만 필요함.
  * 파라미터는 Map을 통해 컨트롤러에 전달하고, 컨트롤러는 서블릿에서 종속되지 않도록 처리.
* 뷰 이름 중복 제거함.
  * 코드 상에서 '/WEB-INF/views...' 경로가 중복으로 들어감
  * 해당 코드를 분리해 컨트롤러는 논리 이름만 반환
  * 프론트 컨트롤러에서 실제 물리 위치를 찾아서 처리

### Model객체 생성
Model과 View 역할을 같이 할 Model 객체를 만든다.
```java
@Data
public class ModelView {
    private String viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelView(String viewName) {
        this.viewName = viewName;
    }
}
```
* model 변수가 실제 Model을 담아오는 역할을 함.
* viewName은 논리 이름을 받아온다.

### 인터페이스 생성
다형성 처리할 수 있는 인터페이스를 생성.
```java
public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);

}
```

### 컨트롤러 생성
각 컨트롤러들을 생성해줌.
```java
public class MemberFormControllerV3 implements ControllerV3 {
    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form");
    }
}

public class MemberListControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        ...
        ModelView mv = new ModelView("members");
        mv.getModel().put("members", members);
        ...
        return mv;
    }
}

public class MemberSaveControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));
        
        ...
        
        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);

        ...
        
        return mv;
    }
}
```
* 리턴값이 ModelView로 변경
* 기존 process 에서 받아오던 HttpServletRequest, HttpServletResponse 객체와 Exception 제거
  * 이렇게 변경하면서 서블릿의 종속성이 제거되었다. 컨트롤러는 단순하게 모델객체만 넘겨주면 된다.
* 기존 request.setArrtibute()를 통해 담던 로직이 'mv.getModel().put()' 으로 변경됨.
* 경로 역시 전체 경로를 넣어주던 방식에서 논리이름을 넣도록 변경.
  * new ModelView("save-result")

### Front Controller 변경
프론트 컨트롤러에서 추가적으로 JSP 경로 처리와 파라미터 처리 로직이 추가 되었다.
```java
@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    ...
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ...
        
        //paramMap. request로 들어온 파라미터들을 Map으로 변환. 세세한 로직은 메소드로 빼주는게 좋다.
        Map<String, String> paramMap = createParamMap(request);
        //컨트롤러 실행.
        ModelView mv = controller.process(paramMap);

        String viewName = mv.getViewName(); // 논리이름.
        MyView view = viewResolver(viewName); // 물리이름으로 변경

        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
```
* createParamMap 메소드를 통해 Request에서 들어온 파라미터들을 맵으로 변경해준다.
* 실제 내부 컨트롤러를 호출하여 ModelView 객체를 받아옴.
* ModelView 객체에서 받아온 논리이름을 물리이름으로 변경해줌.
  * viewResolver()
* MyView에서 렌더링 작업을 한다.
  * 이때 ModelView에서 리턴받은 모델을 처리한다.

### MyView 수정
ModelView 객체에서 넘어온 모델을 처리하기 위한 메소드를 추가해줌.
```java
public class MyView {
    
    ...

    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        modelToRequestAttribute(model, request);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void modelToRequestAttribute(Map<String, Object> model, HttpServletRequest request) {
        model.forEach((key, value) -> request.setAttribute(key, value));
    }
}
```
* 모델에 있는 데이터를 forEach로 모두 가져와 request.setAttribute를 해준다.
  * 기존 컨트롤러에서 처리하던 로직들이 MyView 안에서 처리된다.
* 나머지 로직은 동일.

### KeyPoint
* 기존 컨트롤러에서 처리되던 서블릿 코드가 MyView 쪽으로 분리되어 처리 됨. 이로 인해 서블릿 종속성이 사라짐.
  * 코드가 공통화 되었음.
  * 각 컨트롤러는 단순하게 ModelView 객체안의 model 변수에 처리할 데이터(model 정보)만 넣어주면 됨.
  * 프론트 컨트롤러에서는 파라미터만 별도로 뽑아와서 생성 컨트롤러에 넘겨줌. 
  * 컨트롤러들은 넘어온 Map객체에서 단순하게 파라미터만 뽑아오면 된다.
* 경로 역시 프론트 컨트롤러에서 처리함.
  * 구현된 컨트롤러들은 단순히 논리이름만 반환해주면 됨.