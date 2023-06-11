# MVC 프레임워크 V2 - View 분리
* 코드에서 View 처리 시 forward 로직이 중복적으로 들어감.
* 해당 코드를 분리하여 좀 더 코드를 간결하게 변경
* 순서는 다음과 같음.
  1. 사용자 호출
  2. 컨트롤러에서 매핑정보를 통해 컨트롤러 호출
  3. 컨트롤러에서 MyView 객체 반환
  4. 프론트 컨트롤러에서 MyView 객체안의 render 메소드 호출
  5. JSP 응답

![flowV2.png](images%2FflowV2.png)

### MyView 객체 생성
뷰 화면을 렌더링해 줄 MyView 객체를 만들어줌.

```java
public class MyView {
    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```
* 생성 시 viewPath를 받아서 생성.
* 프론트 컨트롤러에서 render 호출 시 forward 진행.

### 각 컨트롤러 수정
각 컨트롤러를 MyView 객체를 반환 하도록 수정
```java
public class MemberFormControllerV2 implements ControllerV2 {
    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return new MyView("/WEB-INF/views/new-form.jsp");
    }
}

public class MemberListControllerV2 implements ControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ...
        return new MyView("/WEB-INF/views/members.jsp");
    }버
}

public class MemberSaveControllerV2 implements ControllerV2 {
    
    private MemberRepository memberRepository = MemberRepository.getInstance();
    
    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ...
        return new MyView("/WEB-INF/views/save-result.jsp");
    }
}
```
* 각 컨트롤러들이 모두 MyView 객체를 생성하여 반환하고 있음.
* 컨트롤러마다 각각 실행되어아 햘 JSP의 경로를 반환해준다.

### 프론트 컨트롤러 수정
프론트 컨트롤러에선 마지막에 반환받은 MyView 객체의 render 함수를 호출하여준다.
```java
@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {
    
    ...

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ...
        
        MyView view = controller.process(request, response);
        view.render(request, response);
    }
}
```
* getRequestDispatcher 와 forward 가 render 안에서 실행 됨.
* 각 컨트롤러에서 포워드 하던 로직이 프론트 컨트롤러에서 호출
* 각 컨트롤러는 단순하게 자신이 호출되어야 하는 JSP의 경로만 반환해줌.