# MVC 프레임워크 - V4 좀 더 편리한 컨트롤러

개발자가 좀 더 편리하게 컨트롤러를 사용할 수 있도록 수정.
* 현재 단계에선 컨트롤러에서 ModelView 객체를 생성하여 리턴해준다.
* 이를 좀 더 개선하여 컨트롤러는 단순히 논리 이름만 넘기도록 수정

![flowV4.png](images%2FflowV4.png)

* 그림에서 3번과 4번 부분이 수정됨.
* 컨트롤러에서는 파라미터로 받은 model에 데이터를 넣어주고, 단순히 viewName만 반환

### 인터페이스 생성
컨트롤러 인터페이스 생성. 
* 해당 인터페이스는 paramMap과 model을 받으며 ViewName(String) 리턴
```java
public interface ControllerV4 {

    /**
     *
     * @param paramMap
     * @param model
     * @return viewName
     */
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
```

### 컨트롤러 생성
각 컨트롤러들을 생성해줌. 기존 V3와 다르게 model 파라미터를 추가로 받으며, 해당 객체에 데이터를 넣어줌.
```java
public class MemberFormControllerV4 implements ControllerV4 {

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        return "new-form";
    }
}

public class MemberListControllerV4 implements ControllerV4 {
    ...
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        ...
        model.put("members", members);
        return "members";
    }
}

public class MemberSaveControllerV4 implements ControllerV4 {
    ...
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        ...
        
        model.put("member", member);
        return "save-result";
    }
}
```
각 컨트롤러들은 ModelView 객체를 별도로 생성할 필요가 없으며 단순히 논리이름만 리턴해줌.

### Front Controller 변경
model Map을 하나 생성하여 process 파라미터로 넘겨준다.
```java
@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {
    ...
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ...
        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>();

        String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName);
        view.render(model, request, response);
    }

    ...
}
```
* model이 process에 파라미터로 넘어가면서 각 컨트롤러에서 모델 값을 세팅해줌
* render시 model을 넘겨줌으로 써 데이터를 렌더링할 수 있도록 해줌.

프레임워크단에서 번거로운 작업들을 처리해주어야 개발자가 좀 더 쉽게 프레임워크를 사용할 수 있다.