# HttpServletResponse

HttpServletResponse는 Http 응답 메세지를 생성하는데 사용됨. 다음과 같은 값들을 생성할 때 사용.
* HTTP 응답 코드 지정
* 헤더 생성
* 바디 생성

HttpServletResponse는 편의 기능도 제공해줌
* Content-Type
* Cookie
* Redirect

### 기본 HttpServletResponse 사용
HTTP Servlet을 사용하기 위한 클래스를 생성해줌
```java
@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //[status-line]
        response.setStatus(HttpServletResponse.SC_OK);
        
        //[response-headers]
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("my-header", "hello");

        //[message-body]
        PrintWriter writer = response.getWriter();
        writer.println("OK");

    }

}
```
* 상태코드는 setStatus를 사용하여 세팅. (값을 직접 넣어도 되지만 상수로 만들어놓은 값을 써주자.)
* Header 에 Content-Type, Cache-Control, Pragma, 그리고 커스텀한 my-header를 넣어줌.
* Body는 PrintWriter 객체를 이용하여 응답값을 만들어줌.

호출 시 정상적으로 OK 응답이 오며, 개발자 모드로 확인 시 위에 세팅한 값들이 모두 나오는것을 볼 수 있음.

HttpServletResponse에서 제공하는 편의기능으로 좀 더 편하게 세팅도 가능.
* Content-Type
```java
private void content(HttpServletResponse response) {
//Content-Type: text/plain;charset=utf-8

//    response.setHeader("Content-Type", "text/plain;charset=utf-8");
    response.setContentType("text/plain");
    response.setCharacterEncoding("utf-8");
//    response.setContentLength(2); //생략 시 자동 생성
}
```
Content-Type과 CharacterEncoding은 직접 제공하는 setter 메소드로 바로 세팅이 가능함.

* Cookie
```java
private void cookie(HttpServletResponse response) {
    //Set-Cookie: myCookie=good; maxAge=600

//    response.setHeader("Set-Cookie", "myCookie=good; maxAge=600");
    Cookie cookie = new Cookie("myCookie", "good");
    cookie.setMaxAge(600);
    response.addCookie(cookie);
}
```
쿠키 역시 addCookie 메서드를 이용해 바로 세팅해줄 수 있음. 대신 Cookie 객체를 생성하여 해당 객체를 넣어주어야 함.

* Redirect
```java
private void redirect(HttpServletResponse response) throws IOException{
    //Status Code 302
    //Location: /basic/hello-form.html

//    response.setStatus(HttpServletResponse.SC_FOUND);
//    response.setHeader("Location", "/basic/hello-form.html");
    response.sendRedirect("/basic/hello-form.html");
}
```
Redirect 역시 sendRedirect를 이용해서 리다이렉트 시킬 수 있다. 다만 해당 메소드는 응답 코드가 302로 반환되게 된다.

### Html로 반환
Html은 Content-Type 을 "text/html" 로 세팅해주어야 한다.

```java
@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
public class ResponseHtmlServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Content-Type: text/html;charset=utf-8
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<body>");
        writer.println("  <div>안녕</div>");
        writer.println("</body>");
        writer.println("</html>");

    }
}
```
PrintWriter 객체를 이용해 직접 html 코드를 만들어서 반환. 호출 후 소스보기를 클릭해주면 html형태인 것을 볼 수 있음.

### JSON 반환
JSON 반환 역시 우선 Content-Type 을 "application/json"으로 세팅해준다.

```java
@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Content-Type: application/json
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        HelloData helloData = new HelloData();
        helloData.setUsername("kim");
        helloData.setAge(20);

        //{"username":"kim", "age":20}
        String result = objectMapper.writeValueAsString(helloData);
        response.getWriter().write(result);
    }
}
```
미리 만들어둔 HelloData 객체를 세팅한 후 해당 객체를 JSON으로 반환해준다. \
JSON 반환 시에 ObjectMapper의 writeValueAsString 메서드를 이용하여 String을 반환 하면 호출 시 JSON 으로 나오는 것을 볼 수 있다.\
(JSON도 결국은 문자열임을 확인하자.)