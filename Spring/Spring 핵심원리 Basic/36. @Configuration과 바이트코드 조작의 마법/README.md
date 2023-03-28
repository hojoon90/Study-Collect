# @Configuration과 바이트코드 조작의 마법

* 스프링 컨테이너는 싱글톤 레지스트리.
* 그렇기에 빈이 싱글톤이 되도록 보장해주어야 함.
* 위의 AppConfig 코드만 놓고 보면 memberRepository는 3번 호출되어야함.
* 그래서 싱글톤을 보장해주기 위해 @Configuration 어노테이션이 있음.

```java
@Test
void configurationDeep(){
    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    AppConfig bean = ac.getBean(AppConfig.class);

    System.out.println("bean.getClass() = " + bean.getClass());
}

```
위 코드는 AppConfig 조회 메소드임. 실행 시 다음과 같이 출력

```text
bean.getClass() = class hello.core.AppConfig$$SpringCGLIB$$0
```
클래스 이름이 다른것을 확인할 수 있음. 만약 순수한 클래스라면 아래와 같이 출력됨.
```text
bean.getClass() = class hello.core.AppConfig
```

* 클래스 이름이 다른 이유
  * 스프링이 CGLIB이라는 바이트코드 라이브러리로 AppConfig클래스를 새로 만듦
  * 이때 새로 만들어진 클래스는 AppConfig를 상속 받음.
  * 빈에 등록된 클래스는 라이브러리를 통해 만들어진 클래스.

아마 GCLIB을 통해 만들어진 클래스에서 빈으로 등록된 메소드들은 다음과 같이 동작할 것임. (memberRepository 기준)
1. 스프링 컨테이너에 memoryMemberRepository가 등록되었는지 확인
   1. 등록 되어있으면 등록된 memoryMemberRepository 반환
   2. 없으면 기존 로직 호출로 memoryMemberRepository 생성 후 스프링 컨테이너에 등록. 그 후 반환

즉 @Bean이 붙은 메소드마다 스프링 빈이 등록되어있으면 등록된 빈 반환. 없으면 생성하여 컨테이너에 등록 후 반환.

@Configuration이 없을 경우 아래와 같이 동작

```java
//@Configuration  // 앱에 대한 설정 정보
public class AppConfig {
    ...
}
```
```text
bean.getClass() = class hello.core.AppConfig
```
순수 자바 클래스로 출력되는것을 확인할 수 있음.

하지만 순수 자바로 되었기 때문에 빈 등록은 되지만, 스프링 컨테이너의 관리를 받지 못하게 됨.\
즉 싱글톤이 보장되지 않게 됨.

이전에 만든 테스트 확인.
```java
@Test   //테스트 실행
void configurationTest(){
    ...
}
```
```text
23:53:09.137 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memberService'
AppConfig.memberService
AppConfig.memberRepository
23:53:09.141 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memberRepository'
AppConfig.memberRepository
23:53:09.141 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'orderService'
AppConfig.orderService
AppConfig.memberRepository
23:53:09.142 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'discountPolicy'
memberRepository = hello.core.member.MemoryMemberRepository@1c6804cd
memberRepository1 = hello.core.member.MemoryMemberRepository@655f7ea
memberRepository2 = hello.core.member.MemoryMemberRepository@549949be
```
주소값도 전부 다를 뿐더러 호출이 여러번 되는것을 확인할 수 있음.\
이렇게 사용할 경우 각 클래스에서 new 를 통해 클래스를 생성하는 코드와 다를게 없어짐.

결론
* @Configuration 어노테이션으로 인해 싱글톤이 보장됨.
* @Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않음.