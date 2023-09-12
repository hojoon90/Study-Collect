# 다양한 연관관계 매핑

연관관계 정리 전 아래 내용에 대한 정리 필요

* 단방향/양방향
  * 객체간 관계를 나타내는 것.
  * 객체는 참조용 필드를 갖는 객체만 연관 객체 조회 가능.
  * 한쪽만 참조하면 단방향, 양쪽 다 참조하면 양방향
* 연관관계 주인
  * 양방향 관계일 시 정의 필요 (서로가 참조하므로)
  * 연관관계 주인이 아닐 경우 'mappedBy'속성 사용 후 연관관계 주인의 필드 이름을 값으로 사용.

## 다대일 (N:1)
객체 양방향에서 연관관계 주인은 항상 다(N)쪽임.  

### 다대일 단방향
```java
@Entity
public class Member {
    @Id @GeneratedValue
    @Column (name = "MEMBER_ID")
    private Long id;
    
    private String username;
    
    @ManyToOne
    @JoinColumn (name = "TEAM_ID")
    private Team team; 
    
    //Getter, Setter ...
}

@Entity
public class Team {
    @Id @GeneratedValue 
    @Column (name = "TEAM ID") 
    private Long id;
    
    private String name; 
    
    //Getter, Setter ...

}
```
* 회원은 team 으로 팀 엔티티 참조 가능.
* 팀은 회원을 참조할 수 없음. 
* @JoinColumn (name = "TEAM_ID")로 외래키 매핑.
* 이로 인해 team 필드로 회원테이블(MEMBER)의 TEAM_ID 외래키 관리.

### 다대일 양방향
```java
@Entity
public class Member {
    
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void setTeam(Team team) { // 편의 메소드
        this.team = team;
        // 무한 루프에 빠지지 않도록 체크
        if (!team.getMembers().contains(this)) {
            team.getMembers().add(this);
        }
    }
}

@Entity
public class Team {
    
    @Id @GeneratedValue 
    @Column(name = "TEAM_ID ")
    private Long id;
    
    private String name;
    
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<Member>();
    
    public void addMember (Member member) {
        this.members.add(member);
        if (member.getTeam() != this){ //무한 루프에 빠지지 않도록 체크
            member.setTeam(this);
        }
    }
}
```
* 양방향은 외래키가 있는쪽이 연관관계 주인
  * 일대다, 다대일은 항상 다(N)에 외래키가 있음. 
  * 여기서는 Member 객체의 team 필드가 주인.
* 양방향은 서로를 항상 참조
  * 위의 addMember, setTeam등의 편의 메소드를 양쪽에 모두 만들어 참조되도록 한다.
  * 무한루프 주의. 위 코드의 경우 체크가 없으면 무한으로 팀에 멤버가 추가됨.

## 일대다 (1:N)
다대일 관계의 반대 방향. 엔티티가 한개이상일 수 있으므로 Collection, List, Set, Map 등을 사용.

### 일대다 단방향
하나가 여러 엔티티를 참조. (하나의 팀이 여러 회원을 참조)

```java
@Entity
public class Team {
    @Id @GeneratedValue 
    @Column(name = "TEAM_ID")
    private Long id; 
    
    private String name;
    
    @OneToMany
    @JoinColumn(name = "TEAM_ID") // MEMBER 테이블의 TEAM_ID (FK) 
    private List<Member> members = new ArrayList<Member>();
    
    //Getter, Setter ...
}

@Entity
public class Member {
    
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    
    private String username;
    
    //Getter, Setter ...
}
```
* 형태가 조금 특이한데, members가 TEAM_ID 외래키를 관리.
* 일대다 관계에서 외래키는 항상 다(N)쪽에 있지만, Member 객체는 참조할 수 있는 필드가 없음.
* 대신 Team 객체엔 members라는 참조 필드가 있으므로, 여기서 외래키를 관리. 

### 일대다 단방향의 단점
매핑한 객체가 관리하는 외래키가 다른테이블에 있으므로 별도의 업데이트 쿼리가 필요하다.  

```java
class Test{
    public void testSave(){
        Member member1 = new Member ("member1"); 
        Member member2 = new Member ("member2");
        
        Team team1 = new Team("team"); 
        team1.getMembers().add(member1); 
        team1.getMembers().add(member2);
        em.persist(member1); //INSERT-member1
        em.persist(member2); //INSERT-member2
        em.persist(team1); //INSERT-team1, UPDATE-member1. fk, //UPDATE-member2.fk
        
        transaction.commit();
    }
}
```
위 메소드의 실행결과는 아래와 같이 동작하게 됨.
```sql
insert into Member (MEMBER_ID, username) values (null, ?);
insert into Member (MEMBER_ID, username) values (null, ?);
insert into Team (TEAM_ID, name) values (null, ?);
update Member set TEAM_ID = ? where MEMBER_ID = ?;
update Member set TEAM_ID = ? where MEMBER_ID = ?;
```
* Member 객체는 Team 엔티티를 알지 못함.
  * 연관관계는 Team.members에서 관리하기 때문.
* Member 저장 시엔 TEAM_ID를 알지 못하기 때문에 insert 때 TEAM_ID 제외.
  * team 엔티티가 아직 DB에 저장되지 않아 TEAM_ID를 모름.
* team 엔티티를 저장할 때, team.members안에 있는 참조값을 확인하여 해당 member 객체에 TEAM_ID 업데이트.

### 일대다 양방향 관계
일대다 양방향 매핑은 존재하지 않음.  
일대다, 다대일 관계에서 연관관계 주인은 무조건 다(N)쪽에 있다. 즉, @ManyToOne 이 연관관계의 주인이 될 수 밖에 없다.  
아예 못 만드는건 아니고, 일대다 단방향 매핑 반대 엔티티에 다대일 단방향 매핑을 읽기전용으로 만들어주면 된다.  
하지만 이렇게 어렵게 쓰느니 다대일 양방향 매핑으로 쓰는게 좋다. 

## 일대일 (1:1)
일대일 관계는 양쪽이 서로 하나의 관계만 가지는 것. 사용자와 사물함과 같은 관계.  
(사용자는 하나의 사물함만 사용. 사물함도 한명의 사용자만 사용.)
* 일대일은 그 반대도 일대일임.
* 주테이블, 대상테이블 어느곳에서든 외래키를 가질 수 있음.
* 그렇기에 주테이블에 외래키를 두는법, 대상테이블에 외래키를 두는 법 두가지로 나뉨.

### 주 테이블에 외래키
* 주 객체가 대상 객체를 참조하는 것처럼 주 테이블에서 대상 테이블을 참조함.  
* 주 테이블만 확인해도 연관관계 파악이 쉽다.
* JPA에서도 주 테이블에 외래키가 있으면 편리

### 주테이블 단방향
주 테이블은 Member, 대상 테이블은 Locker 이다.
```java
@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;
}

@Entity
public class Locker {
    @Id @GeneratedValue
    @Column (name = "LOCKER_ID")
    private Long id;
    
    private String name;

}
```
* 일대일 관계에선 @OneToOne 어노테이션 사용
* 주 테이블에 Locker 객체 필드가 있고, @JoinColumn을 이용해 외래키가 LOCKER_ID 라는 것을 명시
* 즉, Member(주테이블)에서 Locker(대상테이블)를 관리하고 있음.
* 다대일 단방향과 거의 비슷

### 주테이블 양방향
```java
@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String username;
    
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;
}

@Entity
public class Locker {
    @Id
    @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;
    private String name;
    
    @OneToOne(mappedBy = "locker")
    private Member member;
}
```
* 양방향이기 때문에 연관관계 주인이 필요
* MEMBER 테이블이 외래키를 갖고 있으므로 Member.locker 가 연관관계 주인.
* Locker 에서는 member 필드에 mappedBy를 통해 연관관계 주인이 아니라고 설정.

### 대상 테이블에 외래키
* 대상 테이블에 외래키를 두는 방법. 전통적인 방법.
* 테이블 관계를 일대일에서 일대다로 변경할 때 구조 유지 가능.

### 대상 테이블 단방향
* 일대일 관계 중 대상 테이블에 외래키가 있는 단방향은 JPA 에서 지원하지 않음.
* 단방향 관계를 Locker에서 Member 방향으로 수정(Locker에서 @OneToOne 사용).
  * 이는 주 테이블을 Locker로 변경한다는 이야기와 동일하다. 현재 주 테이블은 Member이다.
* 또는 양방향 관계를 만든 후 연관관계 주인을 Locker 로 설정.

### 대상 테이블 양방향
```java
@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name ="MEMBER_ID")
    private Long id;
    
    private String username;
    
    @OneToOne(mappedBy = "member")
    private Locker locker;
}
@Entity
public class Locker {
    @Id @GeneratedValue
    @Column(name ="LOCKER_ID")
    private Long id;
    
    private String name;
    
    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
```
* 주 엔티티인 Member 대신에 대상 엔티티인 Locker 를 연관관계의 주인으로 만듦
* Member 엔티티엔 mappedBy가 설정 된 것을 볼 수 있다.

## 다대다 (N:N)
* 연관관계DB 에서는 테이블 2개로 다대다 관계 표현이 안된다.
* 이 둘을 연결해주는 연결 테이블이 있어야 한다.
  * 다대다를 일대다, 다대일 관계로 풀어낸다.
* 반면 객체는 객체 2개로 다대다를 만들 수 있다.
* 두 필드를 모두 컬렉션 객체를 사용해 만들면 된다.

### 단방향
다대다 단방향 관계인 회원과 상품 엔티티를 만들어보자.  
```java
@Entity
public class Member {
    @Id
    @Column(name = "MEMBER ID")
    private String id;
    
    private String username;
    
    @ManyToMany
    @JoinTable(
            name = "MEMBER_PRODUCT", 
            joincolumns = @JoinColumn(name = "MEMBER_ID"), 
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")
    )
    private List<Product> products = new ArrayList<Product>();
}

@Entity
public class Product {
    @Id
    @Column(name = "PRODUCT_ID")
    private String id;
 
    private String name;
}

```
* 여기서 MEMBER_PRODUCT는 MEMBER 테이블과 PRODUCT 테이블을 연결해주는 연결 테이블.
  * 즉 MEMBER -> MEMBER_PRODUCT -> PRODUCT 방향
* @ManyToMany 와 @JoinTable을 이용하여 연결테이블을 바로 매핑.
* 그렇기에 MEMBER_PRODUCT 엔티티가 필요 없음.
* @JoinTable 속성은 아래와 같음.
  * name: 연결 테이블을 지정.
  * joinColumns: 연결할 방향인 회원과 매핑할 조인 컬럼 정보 지정.
  * inverseJoinColumn: 반대 방향인 상품과 매핑할 조인 컬럼 정보 지정.
* MEMBER_PRODUCT 는 단순 연결 테이블일 뿐이므로 @ManyToMany 로 매핑할 경우 이 테이블은 신경쓰지 않아도 됨.
