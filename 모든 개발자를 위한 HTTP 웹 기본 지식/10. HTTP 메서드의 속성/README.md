# HTTP 메서드의 속성

* 안전 (Safe Method)
* 멱등 (Idempotent Method)
* 캐시 가능 (Cacheable Method)

### 안전
* 호출해도 리소스를 변경하지 않음.
  * 만약 로그가 계속 쌓여서 장애가 발생하면?
  * -> 안전은 해당 리소스에만 고려함.

### 멱등
* f(f(x)) = f(x)
* 몇번을 호출하든 결과는 동일해야 한다.
* 멱등 메서드
  * GET - 몇번을 조회하든 결과는 동일.
  * PUT - 결과를 대체함. 같은걸 계속 덮어쓰기에 결과는 동일
  * DELETE - 결과를 삭제함. 여러번 요청해도 삭제 결과는 동일
* POST는 멱등이 아님! 호출할때마다 결과가 바뀔수 있음.

언제 활용하는가.
* 자동 복구 메커니즘
* 만약 서버에서 제대로 처리가 되었는지 판단할 수 있는 근거가 됨. (결과는 어찌됐든 동일하기 때문에)
  * 그렇다면 중간에 리소스가 변경되면?
  * -> 멱등은 외부요인으로 중간에 리소스 바뀌는것까지 고려하지 않는다.

### 캐시 가능
* 응답 결과 리소스를 캐시하여 사용해도 되는가.
* GET, HEAD, POST, PATCH는 캐시 가능.
* 실제는 GET, HEAD 정도만 캐시 사용
  * 캐시 사용을 위해선 KEY가 중요함.
  * POST, PATCH는 내용까지 캐시 고려가 필요하므로 거의 사용하지 않음.