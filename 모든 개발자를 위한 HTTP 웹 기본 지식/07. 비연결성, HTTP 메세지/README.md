# 비연결성, HTTP 메세지

## 비연결성
* 서버는 각 클라이언트가 요청을 보내면 연결 후 응답만 보내고 바로 응답을 끊음
* 이렇게 하면 서버는 연결을 유지할 필요가 없음
* 최소한의 자원을 사용.
* HTTP는 기본적으로 연결을 유지하지 않는 모델
* 초단위 이하의 빠른 응답속도
* 1시간에 수천명이 사용해도 실제 동시 처리하는 요청건은 수십개 이하.

### 단점
* 다른 페이지 이동 시 3 way handshake 시간 추가
* 요청 시 HTML 뿐만이 아닌 css, js 등도 모두 다운로드 받음.
* 현재는 HTTP 지속 연결로 문제 해결
  * 모든 요청을 받을때 까지 대기.
  * 이렇기에 속도가 조금 더 빠름
* HTTP/2 HTTP/3 에서 더욱 최적화중.

### 서버 개발자가 힘들어하는 업무
**대용량 트래픽**이 가장 힘들다. 동시에 몰리기에 서버가 터질 수 있음.\
그렇기에 더더옥 stateless로 개발해야 한다.

## HTTP 메세지
* 요청 메세지와 응답 메세지의 구조가 약간 다름.
* 기본 구조는 아래와 같다.
> start-line (시작 라인)\
> --------------------\
> header (헤더)\
> --------------------\
> empty line 공백 라인 (CRLF)\
> --------------------\
> message body 메세지 바디
    
* 요청 메세지
> GET /search?q=hello&hl=ko HTTP/1.1 (시작 라인)\
> Host: www.google.com (헤더)\
> (공백 라인)\
> (메세지 바디는 옵션.)

* 응답 메세지
> HTTP/1.1 200 OK (시작 라인)\
> Content-Type: text/html;charset=UTF-8 (헤더)\
> Content-Length: 3423 (헤더)\
>(공백 라인)\
> html 값 (메세지 바디)

### 시작 라인
* request-line 과 status-line 으로 구분

**요청 메세지**
* 요청 메세지는 보통 request-line
* request-line = Http-method SP(공백) request-target SP HTTP-version CRLF(엔터)
  * HTTP 메서드
    * GET, POST, PUT, DELETE
    * 서버가 수행할 동작 지정
    * GET: 리소스 조회, POST: 요청 내역 처리
  * 요청 대상(request-target)
    * absolute-path[?query] (절대경로[?쿼리])
  * 요청 메세지 (HTTP-version)

**응답 메세지**
* 응답은 status-line
* status-line = HTTP-version SP status-code SP reason-phrase CRLF
  * HTTP 버전
  * HTTP 상태 코드: 요청 성공, 실패를 나타냄
    * 200: 성공
    * 400: 클라이언트 요청 오류
    * 500: 서버 내부 오류
  * 이유 문구: 사람이 이해할 수 있는 짧은 설명 글

### HTTP 헤더
header-field = field-name ":" OWS field-value OWS (OWS: 띄어쓰기 허용)
> Host: www.google.com

> Content-Type: text/html;charset=UTF-8 (헤더)\
> Content-Length: 3423 (헤더)

* HTTP 전송에 필요한 모든 부가정보 포함
* 표준 헤더가 너무 많다.
* 임의 헤더 추가도 가능

### HTTP 메세지 바디
* 실제 전송 데이터
* byte로 표현할 수 있는 모든 데이터

