# URI와 웹 브라우저 요청 흐름

### URI(Uniform Resource Identifier)

URI, URL, URN 의 차이
* URI는 로케이더(Locator), 이름(Name) 또는 둘다 추가로 분류 가능.
* 즉 URL과 URN 모두를 포함한다고 볼 수 있다.
* URI의 뜻
  * Uniform: 리소스를 식별하는 통일된 방식
  * Resource: 자원. URI로 식별할 수 있는 모든 것.
  * Identifier: 식별정보
* URL: 리소스가 있는 '위치'를 지정
* URN: 리소스에 '이름'을 부여
  * 하지만 URN은 거의 사용하지 않음(보편화 되어있지 않다.)

### URL

문법은 다음과 같음
> scheme://[userinfo@]host[:port][/path][?query][#fragment]

**scheme**
* 주로 프로토콜에 사용
  * 프로토콜은 어떤 방식으로 접근할 것인가에 대한 약속 규칙
* http는 80, https는 443 포트 사용. 이 프로토콜 사용시엔 포트 생략 가능

**userinfo**

사용자 인증시 사용하지만 거의 사용하지 않음.

**host**
* 호스트명
* 도메인 또는 IP를 넣는 곳.

**Port**

생략 가능. 접속 포트를 입력한다.

**path**

리소스의 경로. 계층적 구조로 되어있다.\
ex) /home/file1.jpg

**query**

* key=value 형태
* ?로 시작하며 &로 쿼리 추가 가능.
* 보톨 쿼리파람으로 불림. 웹 서버에 제공하는 파라미터. 문자형태이다.

**fragment**

* SPA 와 같은 사이트에서 페이지 이동시 사용된다.
* #page 형태

### 웹 브라우저 요청 흐름

웹 브라우저는 다음과 같은 단계로 호출됨.
1. 웹 브라우저에 우리가 호출할 URL을 던져준다.
2. 웹 브라우저는 DNS와 포트정보를 찾아냄.
3. 그 후 HTTP 요청 메세지를 생성
   1. HTTP 요청 메세지는 아래와 같이 생김
   > GET /search?q=hello&hl=ko HTTP/1.1\
   > Host: www.google.com
4. SOCKET 라이브러리를 통해 메세지를 전달.
   1. 이 때 위에서 배웠던 TCP/IP 연결을 진행한 후 메세지를 전달\
      ![click_setting](./images/packet.png)\
   위 이미지에서 '전송 데이터'가 바로 HTTP 요청 메세지임.
5. 웹 브라우저가 요청 패킷을 서버로 전달.
6. 패킷을 받은 서버는 TCP/IP 패킷은 버리고 HTTP 메세지를 가져와 분석
7. 요청한 값에 맞는 응답 메세지를 만듦
   1. 대략 아래와 같이 생김
   > HTTP/1.1 200 OK\
   > Content-Type: text/html;charset=UTF-8\
   > Content-Length: 3423
   > 
   > html 값
8. 그리고 위의 전달 단계처럼 서버에서도 똑같이 웹 브라우저에 응답값을 보내줌
9. 응답값을 받은 웹 브라우저는 html을 렌더링하여 응답 데이터를 보여준다.