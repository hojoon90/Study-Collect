# 특별한 정보, 인증

## 특별한 정보
* Host: 요청한 호스트 정보(도메인)
* Location: 페이지 리다이렉션
* Allow: 허용 가능한 HTTP 메서드
* Retry-After: 유저 에이전트가 다음 요청을 하기까지 기다려야 하는 시간

#### Host
* 요청에서 사용하며 필수이다.
* 하나의 서버가 여러 도메인을 처리해야 할 경우.(하나의 IP에 여러 도메인이 있는 경우.)

#### Location
* 브라우저가 3xx 응답값에 Location 헤더가 있으면 Location으로 자동 이동
* 201 Created: Location은 요청에 의해 생성된 URI
* 3xx: Location은 요청을 해당 위치로 자동 리다이렉션 하기 위한 리소스

#### Allow
* 405 (Method Not Allowed) 에서 응답에 포함해야 함.
* Allow: GET, HEAD, PUT

#### Retry-After
* 503(Service Unavaliable): 서비스가 언제까지 불능인지 알려줄 수 있음.
* 날짜 표기 및 초단위 표기 가능.

## 인증
* Authorization: 클라이언트의 인증정보를 서버에 전달
* WWW-Authenticate: 리소스 접근시 필요한 인증방법 정의

#### Authorization
* 클라이언트 인증 정보를 서버에 전달.
* 인증 정보는 여러가지가 있음. (OAuth, JWT, 등등...)

#### WWW-Authenticate
* 리소스 접근시 필요한 인증방법 정의
* 401 Unauthorized 응답과 함께 사용