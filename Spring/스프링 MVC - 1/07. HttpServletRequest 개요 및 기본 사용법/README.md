# HttpServletRequest 개요 및 기본 사용법

HttpServletRequest는 개발자가 HTTP 요정 메세지를 편하게 사용할 수 있도록 해주는 객체.\
HTTP 요청 메세지를 파싱하여 HttpServletRequest 객체에 담아서 전달.

다음과 같은 HTTP 요청 메세지를 편하게 조회 가능
> POST /save HTTP/1.1 \
> Host: localhost:8080 \
> Content-Type: application/x-www-form-urlencoded\
>  \
> username=kim&age=30

* START LINE(HTTP 요청 메세지의 처음 라인)
  * HTTP 메소드
  * URL
  * 쿼리 스트링
  * 스키마, 프로토콜

등이 있음.

* 헤더
  * 헤더 조회
* 바디
  * form 파라미터 형식 조회 
  * message body 데이터 직접 조회

#### 임시 저장소 기능
해당 HTTP 요청이 시작부터 끝날때 까지 유지하는 임시 저장소 기능
* 저장: setAttribute
* 조회: getAttribute

#### 세션 관리 기능
로그인 등의 세션을 관리하는 기능임
* getSession