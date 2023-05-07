# 협상(콘텐츠 네고시에이션)
클라이언트가 원하는 표현으로 요청하는 것.

* Accept: 클라이언트가 선호하는 미디어 타입
* Accept-Charset: 클라이언트가 선호하는 문자 인코딩
* Accept-Encoding: 클라이언트가 선호하는 압축 인코딩
* Accept-Language: 클라이언트가 선호하는 자연 언어

만일 다중 자연언어를 지원하는 서버에 클라이언트가 원하는 언어를 보내면 서버가 해당하는 언어를 제공해주는 방식.\
그런데 독일어를 기본 제공해주며 영어는 부가적으로 제공해주는 서버에 요청을 보내면? 이를 보완하기 위해 우선순위가 있음.

### 협상과 우선순위
Quality Values(q)

#### 0~1, 클수록 **높은 우선순위**를 갖는다.
* 생략하면 1
* 다음과 같이 나타냄. ko-KR은 1이므로 생략 됨.
  * Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7

#### 구체적인 것이 우선
* 다음과 같이 나타내며 우선순위는 아래와 같음.
* Accept: text/*, text/plain, text/plain;format=flowed, */*
  * text/plain;format=flowed
  * text/plain
  * text/*
  * */*

#### 구체적인 것을 기준으로 미디어 타입을 맞춤
* 이렇게까지 딥하게는 들어가지 않지만 정리하면 우선순위는 다음과 같음
  * text/html;level1 -> 1
  * text/html -> 0.7
  * text/plain -> 0.3
  * image/jpeg -> 0.5
  * text/html;level=2 -> 0.4
  * text/html;level=3 -> 0.7