# 서블릿

유저 등록에 대한 처리를 할 때 웹 애플리케이션을 직접 처리할 경우.
* 서버 TCP/IP 연결 대기, 소켓 연결
* HTTP 메소드 방식, URL 확인
* Content-Type 확인
* HTTP 메세지 바디 내용 파싱
* 저장 프로세스 실행
* 비즈니스 로직 실행
  * DB 저장 요청
* HTTP 응답 메세지 생성
* TCP/IP에 응답 전달, 소켓 종료

와 같은 단계들을 거침. 그러나 우리가 제일 의미 있는건 비즈니스 로직.\
그렇기에 서블릿을 사용하면 비즈니스 로직을 제외한 나머지는 개발자가 신경쓰지 않도록 해줌.


```java
@WebServlet(name = "helloServlet", urlPattern="/hello")
public class helloServlet extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response){
        //서비스 로직
    }
}

```
동작 방식
* UrlPattern 의 URL이 호출 되면 서블릿 코드 실행
* HttpServletRequest, HttpServletResponse
  * 각각 요청과 응답을 편하게 처리해주는 클래스.
* 개발자는 HTTP를 편리하게 사용.

HTTP 요청과 응답의 흐름.

* HTTP 요청시 
  * WAS는 Request, Response 객체를 새로 만들어 서블릿 객체 호출
    * 요청 때마다 새롭게 생성 됨.
  * 개발자는 Request 객체에서 요청값을 편리하게 사용
  * 또한 Response 객체에 응답값을 편리하게 담음.
  * WAS는 Response 객체에 담긴 응답값을 HTTP 응답값으로 생성.

### 서블릿 컨테이너
* 서블릿을 지원하는 WAS
* 서블릿 객체의 생성, 초기화, 호출, 종료의 생명주기 관리
* 싱글톤 관리
  * 최초 로딩 시점에서 객체를 만들고 재활용
  * 그래서 모든 고객은 같은 객체 인스턴스에 접근
  * 그렇기에 공유 변수 사용 조심!
  * 서블릿 컨테이너 종료시 종료
* JSP도 서블릿으로 변환하여 사용 가능
* 동시 요청을 위한 멀티쓰레드 지원