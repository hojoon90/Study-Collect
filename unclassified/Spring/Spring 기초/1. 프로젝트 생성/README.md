# 프로젝트 생성
Spring 기초 프로젝트 생성

프로젝트의 경우 각 회사마다 사용하는 버전들이 모두 다르고, 현재 유튜브에 공개된 강의 역시 버전이 낮음.\
똑같진 않지만 최대한 비슷한 버전을 사용하도록 함.

### 기초 프로젝트 생성
start.spring.io 에 접속.\
위 사이트는 스프링부트 기반으로 스프링 프로젝트를 만들어주는 사이트.\
접속하면 프로젝트, 언어, 스프링부트 버전, 메타정보등을 작성하는 화면이 나타남.\
다음과 같은 버전으로 세팅을 진행

1. Project - Gradle Groovy (라이브러리를 땡겨오고 라이프사이클까지 관리해주는 관리 프로그램. 요즘은 대부분 Gradle 사용.)
2. Language - Java
3. Spring Boot - 2.7.7 (SNAPSHOT은 현재 개발중인 버전. 안정적이지 않다.)
4. Project Metadata
   1. Group - hello (보통 기업명을 작성 ex. com.corp등)
   2. Artifact - hello-spring (빌드되었을 때 나오는 결과물)
   3. Name - hello-spring
   4. Description - Demo project for Spring Boot
   5. Package name - hello.hello-spring
5. Packaging - jar (jar 파일로 패키징.)
6. Java - 17

Dependencies 세팅
Dependencies는 어떤 라이브러리를 떙겨서 쓸것이냐 세팅하는 화면

- Spring Web (웹프로젝트 개발에 필요)
- Thymeleaf (html을 만들어주는 템플릿 엔진. 추후 별도 설명)

Generate를 눌러 다운로드 후 프로젝트 오픈\
오픈시엔 build.gradle 을 열어주면 알아서 바로 빌드까지 진행을 해준다..!

```groovy
plugins {   
	id 'java'
	id 'org.springframework.boot' version '2.7.7'
	id 'io.spring.dependency-management' version '1.0.15.RELEASE'
}

group = 'hello' // 그룹
version = '0.0.1-SNAPSHOT'  // 프로젝트 버전
sourceCompatibility = '17'  //자바 버전

repositories {
	mavenCentral()  //mavenCentral에서 아래 라이브러리 다운로드.
}

dependencies {  // 라이브러리 모음
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
	useJUnitPlatform()
}
```

.gitignore는 소스를 제외한 불필요한 파일을 Spring 프로젝트 생성시 자동적으로 생성해줌.

src안에 java 디렉토리내에 기본적인 패키지와 클래스가 생성되어있음.\
클래스는 SpringApplication을 실행하는 main메소드가 있음.\
실행 시 8080포트로 웹서버가 실행됨. (localhost:8080)\

설정에서 'Build, Execution, Deployment > Build Tools > Gradle'에서 'Build and run using'  과 'Run tests using' 을 모두 IntelliJ IDEA로 변경해줌.\
그럼 웹앱 실행 시 Gradle 을 안거치고 바로 인텔리제이에서 실행함.\
Gradle을 거치면 종종 실행시 느려짐. (그래서 맨날 프로젝트 실행이....)