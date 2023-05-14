# 캐시와 조건부 요청 헤더

### 캐시 제어 헤더
* Cache-Control: 캐시 제어
* Pragma
* Expires

### Cache-Control
캐시 지시어 
* Cache-Control: max-age
  * 캐시 유효시간, 초 단위. 보통 길게 잡음
* Cache-Control: no-cache
  * 데이터는 캐시해도 되지만, 항상 원래 서버(Origin)에 검증한 후 사용
  * 중간 캐시 프록시 서버들에서 처리가 될 수도 있기 때문.
* Cache-Control: no-store
  * 데이터에 민감한 정보가 있어서 저장하면 안됨.
  * 메모리에서만 사용.

### Pragma
캐시 제어(하위호환)
Pragme: no-cache
HTTP 1.0 하위호환

### Expires
캐시 만료일 지정(하위호환)
* 캐시 만료일을 정확한 날짜로 지정
* HTTP 1.0 부터 사용
* 지금은 Cache-Control: max-age 권장
* Cache-Control: max-age 와 같이 사용 시 Expires 무시 

### 검증 헤더
* ETag
* Last-Modified

### 조건부 요청 헤더
* If-Match, If-None-Match - ETag
* If-Modified-Since, If-Unmodified-Since - Last-Modified