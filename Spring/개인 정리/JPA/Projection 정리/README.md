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
    * 해당 클래스 안에 직접 생성자를 만들면 Q클래스로 만들어주고, new 생성을 통해 객체를 만든다??