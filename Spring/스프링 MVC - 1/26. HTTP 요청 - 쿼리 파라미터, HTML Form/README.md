# 26. HTTP 요청 - 쿼리 파라미터, HTML Form

클라이언트에서 서버로 데이터를 전달하는 방식은 3가지가 존재.

* GET - 쿼리 파라미터
* POST - HTML Form
* HTTP Response Body를 직접 담아 요청

그 중 GET - 쿼리 파라미터와 POST - HTML Form 방식에 대해 먼저 정리.

## GET - 쿼리 파라미터
* URL의 ? 뒤에 key=value 형식으로 전달. 두개 이상일 경우 &로 구분
  * /url?key1=value1&key2=value2
* 스프링에서 조회는 HttpServletRequest의 getParameter() 메소드로 조회 가능.

## POST - HTML Form
* GET 쿼리 파라미터 방법과 전송방식은 동일.
* 전송방식은 다음과 같음
  * > POST /url \
    content-type: application/x-www-form-urlencoded\
    \
    key1=value1&key2=value2

코드는 다음과 같다.
```java
@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParmaV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age={}", username, age);
        response.getWriter().write("ok");
    }
}
```
* 위의 컨트롤러는 GET, POST 어느 방식으로 호출해도 호출 가능.
* 파라미터의 경우 request.getParameter("키")를 넣어 조회.
* GET 호출 전체 URL은 아래와 같음.
  * http://localhost:8080/request-param-v1?username=a&age=10
* POST의 경우 아래 Form을 통해 호출
  * ```html
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
    </head>
    
    <body>
        <form action="/request-param-v1" method="post">
            username:   <input type="text" name="username" />
            age:        <input type="text" name="age" />
            <button type="submit">전송</button>
        </form>
    </body>
    </html>
    ```
  * http://localhost:8080/basic/hello-form.html 으로 접근하여 form에 입력 후 호출하면 정상 호출