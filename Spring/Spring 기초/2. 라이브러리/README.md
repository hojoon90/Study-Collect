# 라이브러리

프로젝트에서 라이브러리를 열면 꽤 많은 라이브러리들이 보임.

gradle, maven 같은 빌드 tool들은 의존관계를 모두 관리해주는데, 예를 들면 spring-boot-starter-web 라이브러리의 경우
해당 라이브러리에서 사용하는 다른 라이브러리들을 모두 같이 가져온다. 우리는 spring-boot-starter-web 하나만 필요하지만, spring-boot-starter-web가
의존성을 갖는 라이브러리도 같이 가져오는 것임.

gradle 툴 클릭 시 Dependencies 에서 사용하는 라이브러리들에 대해 확인해볼 수 있음. 요즘은 대부분의 라이브러리들을 스프링에서 자체적으로 들고 있어서
개발을 좀더 쉽게 진행할 수 있다.

### 그 외
#### 로그
spring-boot-starter-logging안에 여러가지 라이브러리들을 볼 수 있음. 요즘은 logback + slf4j 조합으로 로그를 많이 사용함. 
(예전 log4j는 이제 별도로 사용하지 않는듯..)

#### 테스트
테스트는 대부분 JUnit4를 많이 사용했지만 요즘은 JUnit 5로 많이 넘어가고 있음. 라이브러기가 많긴하지만 핵심은 JUnit5를 이용해 테스트를 진행함.

### 핵심 라이브러리
* spring-boot-starter-web
  * spring-boot-starter-tomcat (톰캣 서버)
  * spring-webmvc (스프링 웹 mvc)
* spring-boot-starter-thymeleaf (타임리프 템플릿 엔진)
* spring-boot-starter(공통. 타임리프와 스타터웹에 모두 들어가 있음. 스프링 부투 + 스프링 코어 + 로깅)
  * spring-boot
    * spring-core
  * spring-boot-starter-logging
    * logback, slf4j
### 테스트 라이브러리
* spring-boot-starter-test
  * JUnit: 테스트 프레임워크. 버전 5
  * mockito: 목 라이브러리
  * assertj: 테스트 코드를 편하게 작성하게 도와주는 라이브러리
  * spring-test: 스프링 통합 테스트 지원