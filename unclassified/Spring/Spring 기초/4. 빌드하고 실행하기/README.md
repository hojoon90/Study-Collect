# 빌드하고 실행하기

빌드는 디렉토리 안에 gradlew 파일을 실행시켜주면 된다.
```shell
choehojun@choehojuns-MacBook-Air hello-spring % pwd
/Users/choehojun/Documents/Workspace/hello-spring
choehojun@choehojuns-MacBook-Air hello-spring % ./gradlew build

Welcome to Gradle 7.6!

Here are the highlights of this release:
 - Added support for Java 19.
 - Introduced `--rerun` flag for individual task rerun.
 - Improved dependency block for test suites to be strongly typed.
 - Added a pluggable system for Java toolchains provisioning.

For more details see https://docs.gradle.org/7.6/release-notes.html

Starting a Gradle Daemon (subsequent builds will be faster)

BUILD SUCCESSFUL in 9s
7 actionable tasks: 7 executed
choehojun@choehojuns-MacBook-Air hello-spring %
```
빌드하고 난 후 build/libs 디렉토리에 들어가면 다음과 같이 jar파일이 존재하는것을 볼수 있다.
```shell
choehojun@choehojuns-MacBook-Air hello-spring % cd build/libs
choehojun@choehojuns-MacBook-Air libs % ls
hello-spring-0.0.1-SNAPSHOT-plain.jar   hello-spring-0.0.1-SNAPSHOT.jar
choehojun@choehojuns-MacBook-Air libs % 
```
배포시에는 hello-spring-0.0.1-SNAPSHOT.jar 파일을 서버에 넣고 jar로 실행시키면 된다.

```shell
choehojun@choehojuns-MacBook-Air libs % java -jar hello-spring-0.0.1-SNAPSHOT.jar 

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.7)

2023-01-22 21:31:53.633  INFO 5788 --- [           main] h.hellospring.HelloSpringApplication     : Starting HelloSpringApplication using Java 17.0.1 on choehojuns-MacBook-Air.local with PID 5788 (/Users/choehojun/Documents/Workspace/hello-spring/build/libs/hello-spring-0.0.1-SNAPSHOT.jar started by choehojun in /Users/choehojun/Documents/Workspace/hello-spring/build/libs)
2023-01-22 21:31:53.634  INFO 5788 --- [           main] h.hellospring.HelloSpringApplication     : No active profile set, falling back to 1 default profile: "default"
2023-01-22 21:31:54.056  INFO 5788 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2023-01-22 21:31:54.062  INFO 5788 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2023-01-22 21:31:54.062  INFO 5788 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.70]
2023-01-22 21:31:54.105  INFO 5788 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2023-01-22 21:31:54.105  INFO 5788 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 444 ms
2023-01-22 21:31:54.204  INFO 5788 --- [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page: class path resource [static/index.html]
2023-01-22 21:31:54.279  INFO 5788 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2023-01-22 21:31:54.284  INFO 5788 --- [           main] h.hellospring.HelloSpringApplication     : Started HelloSpringApplication in 0.827 seconds (JVM running for 1.029)
```
build가 제대로 안될 시엔 build 명령어 실행 전에 clean 명령어를 추가해주면 build 디렉토리를 지우고 다시 생성함.



