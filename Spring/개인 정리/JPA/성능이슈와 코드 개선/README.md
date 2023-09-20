# 성능 이슈와 코드 개선

## 이슈 사항
현재 개발 후 오픈을 앞두고 있는 앱의 API 속도가 빠르게 나오지 않는 이슈가 생겼다.   
API 호출 시 최대 5초까지 응답 시간이 걸리는데,
정작 응답으로 오는 데이터는 그렇게 많은 양의 데이터는 아니었다.   
게다가 이 API 는 앱의 메인 화면에서 호출하는 API 이기 때문에 속도가 빠르게 나와줘야해서 빠르게 병목 구간을 찾아 내서 코드 개선을 진행하였다.

## 이슈의 원인
먼저 로그 레벨을 디버깅으로 돌리고, 메소드 구간 별 소요 시간을 체크해 보았다.  
문제가 된 부분은 모두 쿼리에서 데이터를 가져오는 부분이었다.  
이번 프로젝트를 진행하면서 처음으로 JPA 를 사용해 보았는데, JPA에 대해 확실하게 알지 못하고 적용하다 보니, 조회와 관련된 성능 이슈들이 발생한 것이다.  

먼저 첫번째 문제가 된 쿼리는 아래 쿼리이다.
```java
@Query("select new com.domain.primary.store.model.dto.EventStoreDto(e, s) "
        + "from Event e inner join Store s on e.storeId = s.id "
        + "where s.deleted = false "
        + "and e.period.endDate >= :selectDate ")
List<EventStoreDto> findWithStoreBySelectDate(@Param("selectDate") LocalDate selectDate);
```
JPQL 을 통해서 이벤트와 스토어가 조인된 데이터를 조회해오는 것인데, 조회 방식에 문제가 있었다. 해당 쿼리는 아래와 같이 동작했다.
```sql
select event.id, store.id from event inner join store on event.store_id = store.id;

select * from event where event.id = ?
select * from store where event.id = ?
select * from event where event.id = ?
select * from store where event.id = ?
select * from event where event.id = ?
select * from store where event.id = ?
select * from event where event.id = ?
select * from store where event.id = ?
select * from event where event.id = ?
select * from store where event.id = ?
......
```
이벤트 아이디와 스토어 아이디를 먼저 전체 조회 한 다음 해당 아이디들을 조회하는 쿼리를 하나씩 호출하고 있었다.(...)  
당연한 이야기이지만 이렇게 조회되면 성능에 영향이 안 갈 수 없다.  
원인은 JPQL에 생성자로 객체를 바로 넣으면서 생기는 문제 같은데, 일단 급한대로 QueryDSL을 이용해 쿼리 한번으로 조회할 수 있도록 변경해두었다.
해당 원인은 빠른 시일 내에 자세하게 찾아 볼 예정이다.  


* 출시하는 앱의 메인(이벤트) API의 응답속도가 현저하게 느림 (최대 5초)
* 앱의 메인화면에서 호출 하는 API 이므로 속도가 빠르게 나와줘야 함.
* 코드 분석 진행
  * 쿼리 관련
    * 쿼리 조회 시 리스트 데이터를 가져올 때 조회 할 데이터의 ID를 먼저 조회 한 후 하나씩 쿼리 조회를 진행
    * 예를 들어 이벤트가 300개 존재할 시, 이벤트 아이디를 먼저 조회 한 후 이벤트 조회 쿼리가 300번 수행 되는 방식
    * 확인 결과 JPQL에서 DTO 생성자를 통해 데이터를 세팅해줄 때 이러한 문제 발생.
  * JPA 로직 관련
    * QueryDSL 에서 Projection.constuctor 메소드 이슈
    * 쿼리 조회는 10 ms 안쪽으로 조회되나, 생성자 생성 시 500ms 소요
    * Projection.constuctor 대신 Tuple 을 통해서 데이터를 세팅해주도록 변경
    * 소요시간 30~50 ms 으로 감소.
    * 해당 문제는 좀 더 조사가 필요함.

위 내용들에 대해 정리 필요