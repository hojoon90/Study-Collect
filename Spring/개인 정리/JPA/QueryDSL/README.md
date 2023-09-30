# QueryDSL

## QueryDSL 이란
* 정적 타입을 이용하여 SQL 같은 쿼리를 생성할수 있도록 해주는 프레임워크.  
* 문자열, XML에 쿼리 작성 대신 QueryDSL의 플루언트 API(메소드 체이닝)를 이용해 쿼리 생성

## 사용 이유
* JPA 기준으로 보았을 때, JPQL 과 Criteria 쿼리를 모두 대체할 수 있음.  
* Criteria 쿼리의 장점(자바 코드로 쿼리를 작성)과 JPQL과 유사한 형태로 쿼리 작성이 가능함.  
* 복잡한 동적 쿼리 역시 쉽게 작성이 가능해짐.

## 사용 방법


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