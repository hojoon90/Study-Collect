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

