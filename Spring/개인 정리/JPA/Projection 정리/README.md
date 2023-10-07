# Projection 정리

Projection은 QueryDSL을 통해 데이터를 조회해올 때, 엔티티와 다른 타입을 반환해야할 때 사용한다.  
Projection 사용법은 아래 4가지 방법이 존재함.

* Projection.bean
    * setter 기반.
    * setter가 안열려있으면 사용할 수 없다.
* Projection.fields
    * field에 값을 직접 주입.
    * type 이 다르면 매칭되지 않고, 런타임 시점에 에러 확인 가능.
* Projection.constructor
    * 해당 클래스와 클래스 안에 있는 필드값들을 넘겨주면 매핑하여 해당 객체로 반환?
* @QueryProjection
    * 해당 클래스 안에 직접 생성자를 만들면 QType 으로 만들어 주고, new 생성을 통해 객체를 만든다

이중 제일 추천하는 방법은 @QueryProjection 이다.  

## @QueryProjection

@QueryProjection은 생성자를 통해 DTO로 데이터를 조회해오는 방식이다. 정확히는 QType의 DTO를 만들어서 사용하는 것이다.  
이 방식의 좋은점은 new QDTO 로 클래스를 생성하여 사용하기 떄문에 컴파일 시점에서 에러를 잡을 수 있다는 것이다.  

하지만 DTO는 @QueryProjection 애노테이션이 달리는 순간 Querydsl에 의존성을 가지게 되기 때문에 구조적으로는 조금 고민해 볼 필요가 있다.  
사용법은 우선 DTO 클래스의 생성자에 @QueryProjection 애노테이션을 달아준다.
```java
public class TestDTO {
    private String name;
    private int age;
    
    @QueryProjection
    public TestDTO(String name, int age){
        this.name = name;
        this.age = age;
    }
}
```
