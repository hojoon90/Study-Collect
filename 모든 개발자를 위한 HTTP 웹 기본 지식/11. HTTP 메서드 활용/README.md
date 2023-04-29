# HTTP 메서드 활용

## 클라이언트에서 서버로의 데이터 전송
전송 방법엔 2가지가 이음.
* 쿼리파라미터를 통한 데이터 전송
  * GET
  * 주로 정렬 필터
* 메세지 바디를 통한 데이터 전송
  * POST,PUT,PATCH
  * 회원 가입, 상품 주문, 리소스 등록 등...

### 정적 데이터 조회
* 이미지, 정적 텍스트 문서
* 조회에 GET 사용
* 정적 데이터는 쿼리 파라미터 없이 단순 경로만으로 조회 가능.

### 동적 데이터 조회
* 주로 검색, 게시판 목록에서 정렬 필터(검색어)
* 조회 조건을 줄요주는 필터, 조회 결과를 정렬하는 정렬 조건에서 사용
* GET 사용
* 쿼리 파라미터를 사용하여 데이터 전달

### HTML Form 데이터 전송
* HTML Form submit 시 POST 전송
  * 회원 가입, 상품 주문 등
* HTML Form은 GET 전송도 가능
* Content-Type: application/x-www-form-urlencoded
  * form의 내용을 메세지 바디에 담아 전송(key-value 형식)
  * 전송 데이터 url encoding 처리
* Content-Type: multipart/form-data
  * 바이너리 데이터 전송에 사용(파일 업로드)
  * 다른 종류의 여러 파일과 폼 내용 함꼐 전송 가능
* GET, POST 만 지원함.

### HTTP API 데이터 전송
* 서버 to 서버
* 앱 클라이언트
  * 아이폰, 안드로이드
* 웹 클라이언트
  * Form 전송 대신 자바스크립트를 통한 통신(AJAX)
* POST, PUT, PATCH: 메세지 바디를 통해 데이터 전송
* GET: 조회, 쿼리 파라미터로 데이터 전달
* Content-Type: application/json을 주로 사용(거의 표준
  * TXT, XML, JSON 등 사용