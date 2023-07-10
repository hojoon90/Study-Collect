# HTTP 메세지 컨버터

HTTP 메세지 컨버터는 API 처럼 JSON 등의 데이터를 메세지 바디에 직접 읽거나 쓰는 경우에 사용한다.

동작 원리는 간단하게 아래와 같음.
* @ResponseBody 애노테이션 사용.
* viewResolver 대신 HttpMessageConverter 가 동작함.
* 문자는 StringHttpMessageConverter, 객체는 MappingJackson2HttpMessageConverter 로 처리.

스프링 MVC는 아래 조건일 경우 HttpMessageConverter를 적용함.
* HTTP 요청: @RequestBody, HttpEntity(RequestEntity)
* HTTP 응답: @ResponseBody, HttpEntity(ResponseEntity)

## HttpMessageConverter 인터페이스
HttpMessageConverter는 요청, 응답 모두에 사용된다. 4가지 메소드가 선언되어있음.
* canRead(), canWrite(): 메소드를 읽고 쓸 수 있는 지 판단하는 메소드.
* read(), write(): 실제 읽고 쓰는 메소드

요청 데이터를 읽는 동작은 다음과 같다.
1. @RequestBody, HttpEntity를 사용하는 컨트롤러에 HTTP 요청이 옴.
2. 메세지 읽기 처리 여부 확인을 위해 canRead() 호출
3. 클래스 타입, Http 요청의 'Content-type' 미디어 타입 지원 확인.
4. 조건 만족 시 read() 메소드를 호출하여 객체 생성 및 반환.

응답 데이터 동작은 다음과 같다.
1. 컨트롤러에서 @ResponseBody, HttpEntity 로 값 반환.
2. 메세지 쓰기 처리 여부 확인을 위해 canWrite() 호출.
3. 클래스 타입, Http 요청의 'Accept' 미디어 타입 처리 여부 확인.
4. 조건 만족 시 write() 메소드를 호출하여 응답 메세지 바디 데이터 생성.

스프링 부트는 다양한 메세지 컨버터를 제공.\
클래스 타입과 미디어 타입 두개를 체크하여 사용여부 결정. 조건이 만족하지 않으면 다음으로 넘어감.
1. ByteArrayHttpMessageConverter
2. StringHttpMessageConverter
3. MappingJackson2HttpMessageConverter
4. ...

위 순서대로 조건에 맞는 클래스를 찾아서 실행.

### ByteArrayHttpMessageConverter
바이트 배열 (byte[]) 데이터 처리.
* 클래스 타입: byte[]
* 미디어 타입: \*/*

### StringHttpMessageConverter 
String 문자로 데이터 처리. 
* 클래스 타입: String 
* 미디어 타입: \*/*

### MappingJackson2HttpMessageConverter
application/json 관련 데이터 처리. 객체 형태도 처리함.
* 클래스 타입: 객체 또는 HashMap
* 미디어 타입: application/json 

