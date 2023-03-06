# 스프링 빈 조회 - 동일한 타입이 둘 이상

동일한 타입이 둘 이상인 경우 빈 이름 지정

**타입 조회 시 같은타입이 2개이상이면 중복 오류**
```java
@Test
@DisplayName("타입으로 조회 시 같은 타입이 둘 이상일 경우 중복오류 발생.")
void findBeanByTypeDuplicate(){
    assertThrows(NoUniqueBeanDefinitionException.class,
            () -> ac.getBean(MemberRepository.class));
}
```
같은 타입으로 Bean이 2개 이상일 경우 NoUniqueBeanDefinitionException 을 뱉는다.
그렇기에 타입으로 조회 시 같은 타입이 둘 이상일 경우 빈 이름을 지정함.

**같은 타입이 둘 이상일 경우 빈 이름을 지정**
```java
@Test
@DisplayName("타입으로 조회 시 같은 타입이 둘 이상일 경우 빈 이름 지정.")
void findBeanByName(){
    MemberRepository memberRepository = ac.getBean("memberRepository1",MemberRepository.class);
    assertThat(memberRepository).isInstanceOf(MemberRepository.class);
}
```
이렇게 하면 memberRepository1 만 가져오게 됨.

그렇다면 같은 타입의 빈을 모두 가져오는건 어떻게 해야하나

**특정 타입을 모두 조회**
```java
@Test
@DisplayName("특정 타입을 모두 조회하기")
void findAllBeanByType(){
    Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
    for (String key : beansOfType.keySet()) {
        System.out.println("key = " + key + " value = " + beansOfType.get(key));
    }
    System.out.println("beansOfType = " + beansOfType);
    assertThat(beansOfType.size()).isEqualTo(2);
}
```
getBeansOfType을 이용해 같은 타입을 모두 조회.