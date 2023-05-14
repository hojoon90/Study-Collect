# 캐시 무효화

Cache-Control 중에 확실한 캐시 무효화 응답이 있음.
* Cache-Control: no-cache, no-store, must-revalidate
* Pragma: no-cache
  * HTTP 1.0 하위 호환

* no-cache, no-store은 앞에서 설명
* Cache-Control: must-revalidate
  * 캐시 만료 후 최초 조회 시 원서버에 검증해야함.
  * 원서버 접근 실패 시 반드시 오류 출력 - 504 (Gateway Timeout)
  * must-revalidate는 캐시 유효 시간이라면 캐시 사용
  
### no-cache vs must-revalidate

간단하게 이야기하면 캐시를 보여줄것이냐 그냥 오류를 보여줄것이냐 차이.

* no-cache의 경우 캐시 만료 후 프록시를 거쳐 원서버에 접근할 때, 순단이 발생하면 캐시를 제공할 수도 있다.
  * 캐시 데이터를 제공해주어도 무방한 경우에 사용.
* must-revalidate의 경우 원서버에 순단이 발생하면 무조건 오류를 내 주어야함.
  * 보통 중요하게 갱신이 되어야 할 데이터의 경우 이 경우를 사용한다.