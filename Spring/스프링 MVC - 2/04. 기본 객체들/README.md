# 기본 객체들

Thymeleaf 에서 제공하는 기본 객체들은 아래와 같다.
* ${#request}
* ${#response}
* ${#session}
* ${#servletContext}
* ${#locale}

각 객체들을 확인할 수 있는 컨트롤러 생성
```java
@GetMapping("/basic-objects")
public String basicObjects(Model model, HttpServletRequest request, 
        HttpServletResponse response, HttpSession session){
    session.setAttribute("sessionData", "Hello Session");
    model.addAttribute("request", request);
    model.addAttribute("response", response);
    model.addAttribute("servletContext", request.getServletContext());
    return "basic/basic-objects";
}

@Component("helloBean")
static class HelloBean {
    public String hello(String data){
        return "Hello" + data;
    }
}
```
* model에 각각 session데이터, request, response 등을 받아와 넣어준다.
* 넣어준 값을 basic-objects html로 보낸다.
* 아래는 스프링 Bean 테스트를 위해 Bean을 하나 생성함.

```html
<h1>식 기본 객체 (Expression Basic Objects)</h1> <ul>
    <li>request = <span th:text="${request}"></span></li>
    <li>response = <span th:text="${response}"></span></li>
    <li>session = <span th:text="${session}"></span></li>
    <li>servletContext = <span th:text="${servletContext}"></span></li>
    <li>locale = <span th:text="${#locale}"></span></li>
</ul>
```
* locale을 제외한 나머지 값은 #을 붙이지 않고 호출한다.
  * 스프링 3.0 부터는 ${#request}, ${#response}, ${#session}, ${#servletContext} 을 지원하지 않는다.
* 서버를 띄운 후 호출 하면 아래와 같이 객체주소값을 볼 수 있다.
```text
request = org.apache.catalina.connector.RequestFacade@63aafd2d
response = org.apache.catalina.connector.ResponseFacade@4ef0d63
session = org.thymeleaf.context.WebEngineContext$SessionAttributeMap@6660fdeb
servletContext = org.apache.catalina.core.ApplicationContextFacade@4cf0e6b4
locale = en_US
```

편의 객체들은 thymeleaf 를 좀더 편리하게 사용할 수 있게 해주는 객체들이다.
* param
* session
* bean

편의 객체 사용은 아래와 같이 작성해준다.
```html
<ul>
    <li>Request Parameter = <span th:text="${param.paramData}"></span></li>
    <li>session = <span th:text="${session.sessionData}"></span></li>
    <li>spring bean = <span th:text="${@helloBean.hello('Spring!')}"></span></li>
</ul>
```
* param을 통해 쿼리파라미터에 바로 접근이 가능하다. (ex -> param.파라미터_이름)
* bean의 경우 '@빈이름'을 통해 접근할 수 있고, .을 통해 메소드에 접근하여 사용할 수 있다.