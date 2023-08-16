# 단방향과 양방향, 연관관계의 주인

## 단방향과 양방향?
기본적인 쿼리 사용시엔 고민하지 않을 내용이다. JPA 를 사용하게 되면 객체 중심 모델링으로 되므로 객체 간 관계에 대해 고민해야 한다.

**테이블과 객체의 차이**
* 테이블은 외래키(FK)를 사용하여 양방향 쿼리가 가능. 조인을 통해 쿼리 조회가 가능하다.
* 객체의 경우 참조용 필드를 가진 객체만 연관된 객체를 조회할 수 있게 된다.

연관관계 매핑을 이해하기 위해선 아래 단어들에 대해 정리할 필요가 있다.
* 방향: 양방향과 단방향이 있음. 한쪽만 참조용 필드만 있다면 단방향, 서로의 참조용 필드가 있다면 양방향. 이는 객체관계에서만 해당되며, 테이블 관계는 항상 양방향.
* 다중성: 1:1(일대일), 1:N(일대다), N:1(다대일), N:M(다대다) 이 있음. 여러 이벤트는 하나의 상점에 있으므로 이는 다대일이다.
* 연관관계 주인: 객체를 양방향으로 만들면 연관관계의 주인이 필요.

## 연관관계 차이
객체에서는 기본적으로 단방향 관계가 맺어짐.
```java
class Event {
    Store store;
}

class Store{
    
}
```
Event는 Store 를 알 수 있지만, Store는 Event를 알 수 없음.

테이블에서의 연관관계
```sql
select * 
from event e
left join store s on e.store_id = s.id

select *
from store s 
left join event e on s.id = e.store_id
```
store_id 외래 키 하나로 양방향 조인.

객체는 언제나 단방향 연관관계임.  
서로 다른 단방향 2개를 이용해 양방향처럼 걸어줄 수 있음.  
**객체는 언제나 단방향 관계만 맺을 수 있음.**
```java
class Event {
    Store store;
}

class Store{
    Event event;
}
```
* 객체
  * 참조로 연관관계를 맺음.
  * 참조를 사용하므로 단방향 연관관계.
  * 단방향 연관관계 2개로 양방향 연관관계를 만듦.
* 테이블
  * 외래키로 연관관계를 맺음.
  * 외래키를 사용하므로 양방향 연관관계.


## JPA 에서의 객체 관계 매핑

### @ManyToOne
* 다대일(n:1)의 관계.
* 이벤트가 N, 상점이 1의 관계.
* 이벤트가 N이기 때문에 외래키 관리. (DB를 생각해보자.)

단방향 처리.
```java
@Entity
public class Event{
    @Id
    private Long Id;
    
    private String eventName;
    
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}

@Entity
public class Store{
    @Id
    private Long Id;
    
    private String storeName;
    ...
}
```
* 단방향 처리이며, Event에 @ManyToOne으로 Store 객체를 참조하도록 함.
* Event 객체의 store 와 event 테이블의 store_id를 매핑 -> '연관관계 매핑'이라고 함.
* 하지만 Store엔 별다른 어노테이션을 걸지 않음.

### @OneToMany
* 일대다(1:N)의 관계.
* 양방향 처리 때 사용.

양방향 처리
```java
@Entity
public class Event{
    @Id
    private Long Id;
    
    private String eventName;
    
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}

@Entity
public class Store{
    @Id
    private Long Id;
    
    private String storeName;

    @OneToMany(mappedBy = "store") //연관 관계 주인 지정
    List<Event> events = new ArrayList<>();
    ...
}
```
* 양방향 처리시엔 1쪽에 @OneToMany 어노테이션을 걸어줌.
* 양방향이므로 연관관 계 주인을 mappedBy 로 지정.
* mappedBy 값은 대상 변수명을 따라 지정. 위에서는 Event 객체의 store 라는 이름의 변수이므로 store로 지정.

### 연관관계의 주인

### 연관관계 주의점