# HTTP API 설계 예시

### HTTP API - 컬렉션
* POST 기반 등록


* 회원 목록 /members -> GET
* 회원 등록 /members -> POST
* 회원 조회 /members/{id} -> GET
* 회원 수정 /members/{id} -> PATCH, PUT, POST
* 회원 삭제 /members/{id} -> DELETE


* 클라이언트는 등록될 리소스의 URI를 모른다.
  * POST /members 로 클라이언트는 회원등록 API만 호출
* 서버가 새로 등록된 리소스의 URI를 생성해줌.
  * HTTP/1.1 201 Created\
    Location: /members/100
* 이와 같은 방식을 컬렉션이라고 함.
* 컬렉션
  * 서버가 관리하는 리소스 디렉토리
  * 서버가 리소스의 URI를 생성하고 관리한다.
  * /members가 컬렉션임.
  * 대부분은 이 방식을 많이 쓴다.

### HTTP API - 스토어
* PUT 기반 등록


* 파일 목록 /files -> GET
* 파일 조회 /files/{filename} -> GET
* 파일 등록 /files/{filename} -> PUT
* 파일 삭제 /files/{filename} -> DELETE
* 파일 대량 등록 /files -> POST


* 클라이언트가 리소스 URI를 알고있어야 함.
  * PUT /files/{filename} -> 파일 등록.
* 클라이언트가 직접 리소스의 URI를 지정
* 이와 같은 방식을 스토어라고 함.
* 스토어(Store)
  * 클라이언트가 관리하는 리소스 저장소
  * 클라이언트가 리소스의 URI를 알고 관리해야 함.
  * 여기서 스토어는 /files

### HTML FORM 사용
* GET, POST 만 지원
* AJAX 같은 기술을 사용하여 나머지 방식에 대해 처리 가능.
* 하지면 여기서는 순수한 HTML 방식만을 생각한다.
* 즉, GET과 POST만 사용해야하는 제약이 있음.


* 회원 목록 /members -> GET
* 회원 등록 폼 /members/new -> GET
* 회원 등록 /members/new, /members -> POST
* 회원 조회 /members/{id} -> GET
* 회원 수정 폼 /members/{id}/edit -> GET
* 회원 수정 /members/{id}/edit, /members/{id} -> POST
* 회원 삭제 /members/{id}/delete -> POST


* HTML FORM은 GET, POST 만 지원
* 컨트롤 URI
  * GET, POST만 지원하므로 제약이 있음.
  * 제약 해결을 위해 동사로 된 리소스 사용
  * POST의 /new, /edit, /delete가 컨트롤 URI
  * HTTP 메서드로 해결하기 애매한 경우에만 사용 (HTTP API 포함)

