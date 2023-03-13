# 싱글톤 컨테이너

싱글톤 컨테이너는 싱글톤 패턴의 문제점을 해결한 방식임. 여기서 지금까지 다룬 빈이 모두 싱글톤으로 관리되는 빈.

* 스프링에서 스프링 컨테이너는 싱글톤 컨테이너 역할을 함.
* 싱글톤 객체를 생성하고 관리하는 기능 -> 싱글톤 레지스트리
* 이 덕분에 스프링의 단점을 해결하며 객체를 싱글톤으로 유지 가능.
  * 싱글톤 패턴을 위한 별도 코드 필요 없음
  * DIP, OCP, 테스트등에서 자유로움

```java
@Test
@DisplayName("스프링 컨테이너와 싱글톤")
void springContainer(){
//  AppConfig appConfig = new AppConfig();

    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    MemberService memberService1 = ac.getBean("memberService", MemberService.class);
    MemberService memberService2 = ac.getBean("memberService", MemberService.class);

    System.out.println("memberService1 = " + memberService1);
    System.out.println("memberService2 = " + memberService2);

    assertThat(memberService1).isSameAs(memberService2);
}
```
실제 테스트 코드를 호출하면 같은 참조값을 가져오는 것을 확인할 수 있다.
```shell
memberService1 = hello.core.member.MemberServiceImpl@3276732
memberService2 = hello.core.member.MemberServiceImpl@3276732
```

이는 Bean으로 등록된 객체가 컨테이너에서 관리되면서 미리 만들어진 객체를 반환해주기 때문이다. 이렇게 될 경우 객체 생성 없이 만들어진 객체의 공유가 가능하다.