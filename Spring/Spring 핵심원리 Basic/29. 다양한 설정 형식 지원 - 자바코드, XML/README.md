# 다양한 설정 형식 지원 - 자바코드, XML

* 스프링 설정은 Java 뿐만이 아닌 XML로도 설정이 가능하다. 
* 하지만 현 시점에서는 거의 사용하지 않는 방식
* XML로 설정하는 방법도 있다 정도만 확인하고 넘어가면 됨.

XML로 빈 설정을 위해선 GenericXmlApplicationContext 를 사용하면 됨.

테스트 코드로 다음과 같은 코드를 작성
```java
public class XmlAppContextTest {

    @Test
    void xmlAppConetxt(){
        ApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberService.class);
    }
}
```
appConfig.xml설정을 읽어와 빈을 가져오는 방식. 

설정은 resources에 넣어준다.
```xml
<!-- resources/appConfig.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="memberService" class="hello.core.member.MemberServiceImpl">
        <constructor-arg name="memberRepository" ref="memberRepository"/>
    </bean>

    <bean id="memberRepository" class="hello.core.member.MemoryMemberRepository"></bean>

    <bean id="orderService" class="hello.core.order.OrderServiceImpl">
        <constructor-arg name="memberRepository" ref="memberRepository"/>
        <constructor-arg name="discountPolicy" ref="discountPolicy"/>
    </bean>

    <bean id="discountPolicy" class="hello.core.discount.RateDiscountPolicy"></bean>
</beans>
```
beans 태그 안에 bean들을 만들고 constructor-arg 태그에 필요한 빈들을 넣어준다. 실제 Java Config와 크게 다른 부분은 없음.

이상태에서 테스트 시 정상 동작 확인
```shell
21:30:36.634 [Test worker] DEBUG org.springframework.beans.factory.xml.XmlBeanDefinitionReader - Loaded 4 bean definitions from class path resource [appConfig.xml]
21:30:36.636 [Test worker] DEBUG org.springframework.context.support.GenericXmlApplicationContext - Refreshing org.springframework.context.support.GenericXmlApplicationContext@14f232c4
21:30:36.649 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memberService'
21:30:36.654 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memberRepository'
21:30:36.657 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'orderService'
21:30:36.658 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'discountPolicy'
```
자세한 내용은 스프링 공식 레퍼런스 문서 확인