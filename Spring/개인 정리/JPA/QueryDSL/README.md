# QueryDSL

## QueryDSL 이란
* 정적 타입을 이용하여 SQL 같은 쿼리를 생성할수 있도록 해주는 프레임워크.  
* 문자열, XML에 쿼리 작성 대신 QueryDSL의 플루언트 API(메소드 체이닝)를 이용해 쿼리 생성

## 사용 이유
* JPA 기준으로 보았을 때, JPQL 과 Criteria 쿼리를 모두 대체할 수 있음.  
* Criteria 쿼리의 장점(자바 코드로 쿼리를 작성)과 JPQL과 유사한 형태로 쿼리 작성이 가능함.  
* 복잡한 동적 쿼리 역시 쉽게 작성이 가능해짐.
* Entity 클래스와 매핑되는 QClass를 사용하여 쿼리 실행.

### QClass
QueryDSL은 프로젝트 컴파일 시점에 Entity를 기반으로 QClass 를 생성한다.  
JPAAnnotationProcessor가 @Entity 가 붙어있는 클래스들을 찾아서 QClass로 생성해준다.  
Entity와 똑같은 형태의 static 클래스이며, QueryDSL에서 쿼리 작성 시 QClass 기반으로 쿼리를 실행한다.

## 사용 방법
먼저 build.gradle엔 아래 라이브러리들을 추가해준다.
```groovy
implementation 'com.querydsl:querydsl-jpa'
implementation 'com.querydsl:querydsl-apt'
```
* querydsl-jpa는 QueryDSL을 사용하기 위한 라이브러리이다.
  * QueryDSL 내부 클래스만 사용 가능. QClass는 별도로 생성하지 않음.
* querydsl-apt는 QClass를 생성을 위한 라이브러리.

QClass 생성 방법엔 두가지가 있음
```groovy
//1
plugins{
  id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
}

//2
annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jpa"
annotationProcessor 'jakarta.persistence:jakarta.persistence-api'
annotationProcessor 'jakarta.annotation:jakarta.annotation-api'
```
* 1번의 경우 2018년 이후 라이브러리 업데이트가 되고 있지 않음
* 지속적으로 업데이트 중인 2번 방법을 추천.

```groovy
def querydslDir = "$buildDir/generated/querydsl"

sourceSets {
    main.java.srcDir querydslDir
}

querydsl {
    jpa = true
    querydslSourcesDir = querydslDir
}

compileQuerydsl {
  options.annotationProcessorPath = configurations.querydsl
}

configurations {
  compileOnly {
    extendsFrom annotationProcessor
  }
  querydsl.extendsFrom compileClasspath
}
```
* sourceSets: gradle build 시에 QClass도 함께 build 하기 위해서 sourceSets 에 해당 위치 추가.
* 


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