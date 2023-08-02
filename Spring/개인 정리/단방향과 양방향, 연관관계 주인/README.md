# 단방향과 양방향, 연관관계의 주인

## 단방향과 양방향?
기본적인 쿼리 사용시엔 고민하지 않을 내용이다. JPA 를 사용하게 되면 객체 중심 모델링으로 되므로 객체 간 관계에 대해 고민해야 한다.

**테이블과 객체의 차이**
* 테이블은 외래키(FK)를 사용하여 양방향 쿼리가 가능. 조인을 통해 쿼리 조회가 가능하다.
* 객체의 경우 참조용 필드를 가진 객체만 연관된 객체를 조회할 수 있게 된다.

두 객체 사이에 하나의 참조용 필드만 있다면 단방향, 두 객체 모두 서로의 참조용 필드가 있다면 양방향 관계이다. 이벤트와 상점 관계로 보면 다음과 같다.

(그림 추후 삽입)

단방향 혹은 양방향 관계를 걸어주기 위해선 @ManyToOne, @OneToMany 어노테이션을 엔티티에 걸어준다. 
### @ManyToOne
* 다대일(n:1)의 관계. 
* 하나의 상점엔 여러개의 이벤트를 걸 수 있다. 
* 이벤트가 N, 상점이 1의 관계가 된다.
* 이벤트가 N이기 때문에 외래키를 관리한다. (DB를 생각해보자.)

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
* 하지만 Store엔 별다른 어노테이션을 걸지 않음.

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