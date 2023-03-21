# 컴포넌트 스캔과 의존관계 자동 주입 시작하기

* 위 예제들에서는 스프링 빈 등록 시 설정 정보에 직접 빈을 등록하였음.
* 하지만 등록할 빈이 수십 수백개가 될 경우 일일히 등록해주기에는 한계가 생김. (누락 등)
* 그렇기 때문에 스프링은 자동으로 빈을 등록해주는 컴포넌트 스캔 기능을 제공함.
* 그리고 의존관계를 자동으로 주입해주는 기능 역시 제공해줌.

먼저 컴포넌트 스캔을 사용하는 config 클래스를 만들어줌.
```java
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)//@Component 를 찾아서 빈으로 등록
public class AutoAppConfig {

}
```
@ComponentScan은 @Component 어노테이션을 찾아 빈으로 등록. 여기서 클래스의 내용엔 Bean이 존재하지 않음.
* 위의 excludeFilters 는 제외할 컴포넌트를 지정해줌.
* 기존에 사용한 Configuration 어노테이션들에 영향이 안가게 하기 위해 일단 제외

그리고 아래 클래스들에 @Component 추가.

```java

@Component
public class RateDiscountPolicy implements DiscountPolicy{
    ...
}

@Component
public class MemberServiceImpl implements MemberService{
    ...
}

@Component
public class MemoryMemberRepository implements MemberRepository{
    ...
}

@Component
public class OrderServiceImpl implements OrderService {
    ...
}

```
* 위 4개 클래스에 @Component 등록. 이렇게 하면 빈 등록이 끝난 것임.
* 이렇게 되면 스프링이 자동으로 빈으로 등록.
* 이럴 경우 MemberServiceImpl과 OrderServiceImpl의 경우 의존 관계 주입이 어려워짐
  * 기존 Config 클래스에서는 명시하여 의존관계설정이 가능했지만, 자동으로 빈을 등록할 경우 이게 어려워짐.
* 그렇기에 @Autowired를 추가해줌.

```java
    @Autowired //ac.getBean(MemberRepository.class)
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
```
* @Autowired를 이용하면 스프링에서 클래스 생성 시 의존관계를 자동적으로 주입해준다.

실제 테스트를 진행하여 로그를 확인하면 다음과 같이 나타남.
```text
23:39:42.044 [Test worker] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@cd1e646
23:39:42.051 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalConfigurationAnnotationProcessor'
23:39:42.090 [Test worker] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/discount/RateDiscountPolicy.class]
23:39:42.090 [Test worker] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/member/MemberServiceImpl.class]
23:39:42.091 [Test worker] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/member/MemoryMemberRepository.class]
23:39:42.091 [Test worker] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/order/OrderServiceImpl.class]
23:39:42.131 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerProcessor'
23:39:42.132 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerFactory'
23:39:42.133 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalAutowiredAnnotationProcessor'
23:39:42.134 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalCommonAnnotationProcessor'
23:39:42.140 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'autoAppConfig'
23:39:42.142 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'rateDiscountPolicy'
23:39:42.143 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memberServiceImpl'
23:39:42.151 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memoryMemberRepository'
23:39:42.153 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Autowiring by type from bean name 'memberServiceImpl' via constructor to bean named 'memoryMemberRepository'
23:39:42.153 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'orderServiceImpl'
23:39:42.154 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Autowiring by type from bean name 'orderServiceImpl' via constructor to bean named 'memoryMemberRepository'
23:39:42.154 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Autowiring by type from bean name 'orderServiceImpl' via constructor to bean named 'rateDiscountPolicy'
```

* ClassPathBeanDefinitionScanner
  * @ComponentScan이 동작하면서 @Component 어노테이션이 걸린 클래스들을 빈으로 등록
  * 아래 Creating 로그 확인 시 빈 등록이 된 것을 확인 가능.
  * 빈 등록 시엔 빈의 이름은 클래스 이름을 사용. 다만 맨 앞의 대문자만 소문자로 변경된 이름으로 등록 됨 (ex. MemberServiceImpl -> memberServiceImpl)
  * 이름을 직접 등록하고 싶을 시엔 @Component("등록하고싶은 이름") 으로 사용 가능.
* Autowired
  * 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입.
  * 이때 스프링은 타입이 같은 빈을 찾아서 주입한다. 
  * 위에서는 memoryMemberRepository가 MemberRepository를 상속 받으므로 자동으로 주입됨.
  * 생성자에 파라미터가 많아도 자동으로 주입.
