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

## LocalDate 사용하기
자바 8부터 새롭게 추가된 LocalDateTime 객체를 사용하려면 아래와 같은 라이브러리가 필요
* thymeleaf-extras-java8time
  * 스프링 부트를 사용하는 경우엔 자동으로 추가된다.

날짜용 유틸리티를 사용하는 객체는 ```#temporals``` 이며, 아래와 같이 사용함
```html
yyyy-MM-dd HH:mm:ss = <span th:text="${#temporals.format(localDateTime, 'yyyy-MM-dd HH:mm:ss')}"></span>
```
그 외에 날짜 데이터들은 아래와 같이 가져올 수 있다.
```html
<h1>LocalDateTime - Utils</h1>
<ul>
    <li>${#temporals.day(localDateTime)} = <span th:text="${#temporals.day(localDateTime)}"></span></li>
    <li>${#temporals.month(localDateTime)} = <span th:text="${#temporals.month(localDateTime)}"></span></li>
    <li>${#temporals.monthName(localDateTime)} = <span th:text="${#temporals.monthName(localDateTime)}"></span></li>
    <li>${#temporals.monthNameShort(localDateTime)} = <span th:text="${#temporals.monthNameShort(localDateTime)}"></span></li>
    <li>${#temporals.year(localDateTime)} = <span th:text="${#temporals.year(localDateTime)}"></span></li>
    <li>${#temporals.dayOfWeek(localDateTime)} = <span th:text="${#temporals.dayOfWeek(localDateTime)}"></span></li>
    <li>${#temporals.dayOfWeekName(localDateTime)} = <span th:text="${#temporals.dayOfWeekName(localDateTime)}"></span></li>
    <li>${#temporals.dayOfWeekNameShort(localDateTime)} = <span th:text="${#temporals.dayOfWeekNameShort(localDateTime)}"></span></li>
    <li>${#temporals.hour(localDateTime)} = <span th:text="${#temporals.hour(localDateTime)}"></span></li>
    <li>${#temporals.minute(localDateTime)} = <span th:text="${#temporals.minute(localDateTime)}"></span></li>
    <li>${#temporals.second(localDateTime)} = <span th:text="${#temporals.second(localDateTime)}"></span></li>
    <li>${#temporals.nanosecond(localDateTime)} = <span th:text="${#temporals.nanosecond(localDateTime)}"></span></li>
</ul>
```