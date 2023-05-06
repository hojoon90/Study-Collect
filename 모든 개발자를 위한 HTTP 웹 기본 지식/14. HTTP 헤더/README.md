# HTTP 헤더

## 개요
* header-field = field-name ":" OWS field-value OWS (OWS: 띄어쓰기 허용)
* field-name 은 대소문자 구분 없음
* HTTP 전송에 필요한 모든 부가정보가 들어감.
* 필요시 임의 헤더 추가 가능

### Entity 헤더
* 헤더 중 entity 헤더는 Content-Type 및 Content-Length 등의 데이터 정보가 있었다.
* 이 정보는 entity-body의 데이터를 해석할 수 있는 정보다. 물론 관계없는 헤더값도 같이 존재
* 이후 스펙 변경이 일어나면서 Entity -> Representation(표현)으로 변경되었음.
* 표현은 표현 메타데이터 + 표현 데이터임.
* 바뀐 스펙은 메세지 본문을 통해 표현 데이터 전달.
* 표현은 요청이나 응답에서 전달할 실제 데이터이다.
  * 표현헤더는 표현 메타데이터와 페이로드 메세지를 구분해야 하지만, 여기선 생략함.

## 표현
* Content-Type: 표현 데이터의 형식 (html, json 등등..)
* Content-Encoding: 표현 데이터의 압축 방식
* Content-Language: 표현 데이터의 자연 언어 (한국어, 영어 등등..)
* Content-Length: 표현 데이터의 길이
* 표현 헤더는 전송, 응답 둘다에 사용된다.

#### Content-Type
* 미디어 타입, 문자 인코딩

#### Content-Encoding
* 표현 데이터를 압축하기 위해 사용.
* 데이터 전달하는 곳에서 압축 후 인코딩 헤더 추가
* 읽는 쪽에서 헤더에 있는 정보로 압축 해제

#### Content-Language
* 표현 데이터의 자연 언어
  * ko
  * en
  * en-US
* ex) 애플 사이트에서 언어에 따라 홈페이지가 다르게 나오는 경우

#### Content-Length
* 표현 데이터의 길이
* 바이트 단위
* Transfer-Encoding(전송 코딩)을 사용하면 Content-Length를 사용하면 안됨.