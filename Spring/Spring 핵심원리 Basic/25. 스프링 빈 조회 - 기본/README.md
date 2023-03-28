# 스프링 빈 조회 - 기본

스프링 빈 조회 방법엔 여러가지가 있다.

**빈 이름으로 조회**
```java
@Test
@DisplayName("빈 이름으로 조회")
void findBeanByName(){
    MemberService memberService = ac.getBean("memberService", MemberService.class);
    assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
}
```
빈의 이름을 직접 입력하여 조회하는 것. 테스트 시 정상 동작 확인

**이름 없이 타입으로 조회**
```java
@Test
@DisplayName("이름 없이 타입으로만 조회")
void findBeanByType(){
    MemberService memberService = ac.getBean(MemberService.class);
    assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
}
```
이름을 넣지 않고 타입만 변수로 보내 확인하는 방법


**구체 타입으로 조회**
```java
@Test
@DisplayName("구체 타입으로 조회")
void findBeanByName2(){
    MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);
    assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
}
```
구현체 타입을 넣어 조회하는 방법. 잘 사용하지 않음.

**실패 케이스**
```java
@Test
@DisplayName("빈 이름으로 조회 X")
void findByNameX(){
//    ac.getBean("xxxx", MemberService.class);
    assertThrows(NoSuchBeanDefinitionException.class,
            () -> ac.getBean("xxxxxx", MemberService.class));
}
```
실패 시 NoSuchBeanDefinitionException 을 내뱉는데, 해당 익셉션이 잘 넘어오는지 테스트.