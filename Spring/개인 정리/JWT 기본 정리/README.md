# JWT 기본 정리

## JWT(JSON Web Token) 간략 정리
* JSON 을 이용하여 토큰 자체에 사용자 인증 정보를 저장하는 웹 토큰방식
* 서명을 이용하여 인증을 확인함.
* 인증 권한을 부여하거나, 정보 교환 시에 많이 사용함.

### 장점
* Secret Key 가 탈취되지 않는 한 데이터 위변조 방지 가능
* 인증 정보를 모두 갖고 있으므로 별도 저장소가 필요 없음.
* 모바일에서 사용 가능
* 확장성 우수

### 단점
* 토큰 탈취 시 대처하기가 쉽지 않음
  * 토큰 만료전까지는 자유롭게 사용 가능
  * 강제 로그아웃, 접근 제한등의 처리를 할 수 없음(별도 저장을 하지 않으므로)
* payload 가 많을 경우 성능 영향
* payload 는 암호화 되지 않으므로 자체 암호화 혹은 중요 정보는 담지 않도록 처리

### 구성 요소
Header, Payload, Signature 세가지 부분으로 분리되며, 보통 키는 아래와 같은 형태를 띔
> xxxxx.yyyyyyyy.zzzz

'.'으로 구분 되며, 각각 앞에서 부터 Header, Payload, Signature 이다. 

* Header: Signature(서명) 생성에 사용된 알고리즘, 타입이 들어감
* Payload: 실제 사용하는 정보가 있음. JWT 에서 정의한 값도 있으며, 사용자 임의로 커스텀 가능
  * https://datatracker.ietf.org/doc/html/rfc7519#section-4.1
* Signature: 토큰 유효성을 검증하기 위한 문자열. Header+Payload+SecretKey 로 값을 생성함.

### 인증 타입
> Authorization: &#60;type&#62; &#60;credentials&#62;

인증 헤더 부분에서 Type 에 해당하는 부분이 인증 타입임.

* Basic: 기본적인 인증방식. 사용자의 아이디, PW를 Base64로 인코딩한 값을 인증정보로 사용.
* Bearer: OAuth2.0 프레임 워크에서 사용하는 토큰 인증방식. (JWT에서 사용)
* 이 외에 Digest, HOBA, Mutual, AWS4-HMAC-SHA256 등이 있음.

## Refresh Token

