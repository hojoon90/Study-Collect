# 로깅 알아보기

시스템 운영 시 시스템 콘솔인 System.out.println() 대신 로그 라이브러리를 사용.\
여기선 최소한의 사용법만 확인.

## 로깅 라이브러리
스프링 부트 라이브러리 사용 시, 스프링 부트 로깅 라이브러리(spring-boot-starter-logging)가 함께 포함.\

* SLF4J
  * Logback, Log4J, Log4J2 등의 라이브러리를 통합하여 인터페이스로 제공.
* Logback
  * SLF4J 안에 포함된 로그 라이브러리. 실무에서 대부분 사용.

## 로그 선언
로그는 기본적으로 아래와 같이 선언
```java
private Logger log = LoggerFactory.getLogger(getClass());
private static final Logger log = LoggerFactory.getLogger(some.class);
//롬복 사용 시
@Slf4j
public class ... {
    
}
```

## 로그 호출
```java
log.info("some log");

//로그 사용 시엔 아래와 같이 사용해주는게 좋다. (로그 처리를 info 메소드에서 처리하도록)
log.info("log data = {}", log);
```
로그 호출 시 시스템 콘솔보다 보여주는 데이터 양이 많음. 그렇기 떄문에 실무에서도 로그를 사용하는걸 권장.\
로그 출력 시 아래와 같이 출력 됨.
```java
2023-07-09T23:40:07.010+09:00  INFO 16831 --- [nio-8080-exec-2] h.springmvc.basic.LogTestController      : info log=Spring
name = Spring
2023-07-09T23:40:07.010+09:00 DEBUG 16831 --- [nio-8080-exec-2] h.springmvc.basic.LogTestController      : debug log=Spring
2023-07-09T23:40:07.012+09:00  INFO 16831 --- [nio-8080-exec-2] h.springmvc.basic.LogTestController      : info log=Spring
2023-07-09T23:40:07.012+09:00  WARN 16831 --- [nio-8080-exec-2] h.springmvc.basic.LogTestController      : warn log=Spring
2023-07-09T23:40:07.012+09:00 ERROR 16831 --- [nio-8080-exec-2] h.springmvc.basic.LogTestController      : error log=Spring
```
시간, 로그레벨, 프로세스 ID, 쓰레드명, 클래스명, 로그메세지 순으로 로그가 노출 됨.\
## 로그레벨 설정
로그 레벨은 TRACE>DEBUG>INFO>WARN>ERROR 순. 뒤로 갈 수록 출력 내용이 적어진다.\
```properties
#application.properties
#전체 로그레벨 설정 (기본 info)
logging.level.root=info

#hello.springmvc 패키지와 및 하위 로그 레벨 설정 
logging.level.hello.springmvc=debug
```
전체 로그 레벨 및 특정 패키지와 그 하위에 로그 레벨 설정 가능.

## 장점
* 쓰레드 정보, 클래스 정보등의 부가정보를 볼 수 있음.
* 서버, 환경에 따라 로그레벨 지정 가능
* 로그에 대한 파일 자체를 별도로 저정할 수 있음. 서버 별 로그를 일별로 저장 가능
* 성능이 시스템 콘솔보다 좋음.



