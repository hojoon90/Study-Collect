# HTTP 요청 데이터

### 개요
클라이언트에서 서버로 HTTP 요청 메세지를 보내는 방법은 주로 3가지 방법이 있다.

* GET - 쿼리 파라미터
  * /url?name=kim&age=20
  * url에 쿼리파라미터에 데이터를 전달.
  * 검색, 필터, 페이징 등에서 많이 사용
* POST - HTML Form
  * html form 에서 작성한 데이터를 전달.
  * Content-Type: application/x-www-form-urlencoded
  * 메세지 바디에 쿼리파라미터 형식으로 데이터 전달.
  * 회원가입, 주문, HTML Form 에서 사용
* HTTP 메세지 바디에 데이터 직접 담아 요청
  * HTTP API 에서 주로 사용. 
  * JSON, XML, TEXT 방식 사용
    * 보통 JSON을 많이 사용
  * POST, PUT, PATCH 메소드로 호출

### GET - 쿼리 파라미터

서버에 다음 데이터들을 전달한다고 가정.
* username=kim
* age=20

위 데이터들을 쿼리 파라미터로만 전달. URL은 아래와 같이 작성 됨.
> http://localhost:8080/request-param?username=kim&age=20

서버에서는 HttpServletRequest 로 쉽게 파라미터 파싱이 가능.

```java
@WebServlet(name = "RequestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("RequestParamServlet.service");
        System.out.println("[전체 파라미터 조회] - start");
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> System.out.println(paramName + "="+ request.getParameter(paramName)));
        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();

        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");

        System.out.println("username = " + username);
        System.out.println("age = " + age);
        System.out.println();

        System.out.println("[이름이 같은 복수 파라미터 조회]");
        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("name = " + name);
        }

        response.getWriter().write("OK");
    }
}
```

쿼리 파라미터의 키를 전체 조회하는 메소드는 getParameterNames로 조회 가능. \
해당 키의 값을 찾기 위해선 getParameter로 해당 키의 값을 찾는다.
```java
request.getParameterNames().asIterator()
        .forEachRemaining(paramName -> System.out.println(paramName + "="+ request.getParameter(paramName)));
```

단일 파라미터 조회는 getParameter 메소드에 이름을 넣어 조회.
```java
String username = request.getParameter("username");
String age = request.getParameter("age");
```

이름이 같은 복수 파라미터는 getParameterValues 를 이용.
```java
String[] usernames = request.getParameterValues("username");
for (String name : usernames) {
    System.out.println("name = " + name);
}
```

보통 복수 파라미터를 쓰는 경우는 거의 없지만, 만약 사용하게 된다면 위와 같이 데이터를 가져와야 함.\
이렇게 중복일 때 getParameter를 이용해 값을 가져오면 맨 첫번째 파라미터 값을 조회해 온다.


### POST - HTML Form

POST 메소드로 HTML Form 방식의 데이터를 보낼 때는 아래와 같은 특징이 있음.
* Content-Type 은 x-www-form-urlencoded 를 사용
* 메세지 바디에 쿼리파라미터 형식으로 데이터를 전달.

아래와 같이 html 작성 후 테스트 진행
```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<form action="/request-param" method="post">
  username: <input type="text" name="username" /> 
  age: <input type="text" name="age" /> 
  <button type="submit">전송</button>
</form>
</body>
</html>
```
페이지 접근 후 호출 시 정상 동작 하는것을 볼 수 있음.

x-www-form-urlencoded 타입은 바디의 데이터가 GET 의 쿼리 파라미터와 동일한 것을 볼 수 있음.\
클라이언트 입장에서는 GET 과 POST의 보내는 방식이 다르지만 서버 입장에서는 두 방식의 데이터가 쿼리 파라미터로 동일 하므로
같은 메소드로 처리가 가능.

해당 테스트를 더 편하게 하기 위해선 Postman을 이용하는것도 편리함.

### API 메세지 바디 - 단순 텍스트

HTTP Message Body에 데이터를 직접 넣어서 요청하는 방식. 
* JSON형식이 제일 많이 쓰임.
* 그 외에 XML, TEXT 방식도 사용
* 사용 메소드는 POST, PUT, PATCH

단순 텍스트를 읽기 위한 서블릿 생성
```java
@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println("messageBody = " + messageBody);

        response.getWriter().write("OK");
    }
}
```
* 요청에 대한 값을 읽기 위해서 InputStream 메소드 사용
* InputStream은 byte 코드로 반환하기 때문에 우리가 읽을 수 있도록 Charset 인코딩을 설정해준다. 여기선 UTF-8로 세팅
* Postman을 통해 바디에 메세지를 넣어 요청 하면 정상적으로 메세지가 넘어오는 것을 확인.

```text
messageBody = hello!
```

### API 메세지 바디 - JSON

Message Body에 JSON으로 요청하는 방식
* Content-Type: application/json 으로 변경
* 메세지 바디엔 아까와 다르게 JSON 데이터를 넣어준다.
  * {"username": "kim", "age": 20}

JSON 데이터를 파싱 할 객체를 하나 만듦
```java
@Getter
@Setter
public class HelloData {
    private String username;
    private int age;
}
```
그 후 JSON 으로 처리할 서블릿을 만들어줌

```java
@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println("messageBody = " + messageBody);
    }
}

```
JSON으로 넣어 호출 시 정상적으로 메세지 바디가 출력되는것을 볼 수 있음.
```text
messageBody = {"username": "kim", "age": 20}
```
JSON을 담아줄 객체를 아까 만들었으므로 객체를 읽을 수 있는 ObjectMapper 를 추가해줌.
```java
@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();
  
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          ...
  
      HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
  
      System.out.println("helloData.username = " + helloData.getUsername());
      System.out.println("helloData.age = " + helloData.getAge());
  
      response.getWriter().write("OK");
    }
}
```
호출 해보면 다음과 같이 출력되는 것을 볼 수 있음.
```Text
helloData.username = kim
helloData.age = 20
```
JSON을 읽어와 객체로 변경해주려면 별도 라이브러리가 필요하다 (Jackson, GSON).\
스프링 MVC의 경우 Jackson 라이브러리를 기본적으로 제공해주므로 해당 라이브러리를 이용해 ObjectMapper를 사용한다.

