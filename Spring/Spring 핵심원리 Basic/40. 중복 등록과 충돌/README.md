# 중복 등록과 충돌

빈이 중복 등록되는 경우는 다음과 같다.

* 자동 빈등록 vs 자동 빈등록
* 수동 빈등록 vs 자동 빈등록

자동 빈등록의 경우 스프링은 컴포넌트 스캔으로 빈을 등록함. 이때 이름이 같은 경우 스프링이 오류를 발생시킴.
 > ConflictingBeanDefinitionException
 
수동 빈등록과 자동 빈등록은 아래와 같이 테스트를 진행한다.
```java
@Component
public class MemoryMemberRepository implements MemberRepository{
    ...
}

@Configuration
@ComponentScan(
//        basePackages = "hello.core.member",
        basePackageClasses = AutoAppConfig.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)//@Component 를 찾아서 빈으로 등록
public class AutoAppConfig {

    @Bean(name = "memoryMemberRepository")
    MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
```

빈의 이름을 서로 똑같이 만들어줌. 차이점은 아래 Config에 작성한 빈은 수동 등록한 빈임. 이렇게 하고 실행하면 다음과 같이 정상 동작함
```text
22:37:04.198 [Test worker] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@cd1e646
22:37:04.205 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalConfigurationAnnotationProcessor'
22:37:04.248 [Test worker] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/discount/RateDiscountPolicy.class]
22:37:04.249 [Test worker] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/member/MemberServiceImpl.class]
22:37:04.249 [Test worker] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/member/MemoryMemberRepository.class]
22:37:04.249 [Test worker] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/order/OrderServiceImpl.class]
22:37:04.257 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Overriding bean definition for bean 'memoryMemberRepository' with a different definition: replacing [Generic bean: class [hello.core.member.MemoryMemberRepository]; scope=singleton; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodNames=null; destroyMethodNames=null; defined in file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/member/MemoryMemberRepository.class]] with [Root bean: class [null]; scope=; abstract=false; lazyInit=null; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=autoAppConfig; factoryMethodName=memberRepository; initMethodNames=null; destroyMethodNames=[(inferred)]; defined in hello.core.AutoAppConfig]
22:37:04.297 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerProcessor'
22:37:04.298 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerFactory'
22:37:04.299 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalAutowiredAnnotationProcessor'
22:37:04.299 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalCommonAnnotationProcessor'
22:37:04.303 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'autoAppConfig'
22:37:04.305 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'rateDiscountPolicy'
22:37:04.305 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memberServiceImpl'
22:37:04.311 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memoryMemberRepository'
22:37:04.312 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Autowiring by type from bean name 'memberServiceImpl' via constructor to bean named 'memoryMemberRepository'
22:37:04.313 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'orderServiceImpl'
22:37:04.313 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Autowiring by type from bean name 'orderServiceImpl' via constructor to bean named 'memoryMemberRepository'
22:37:04.313 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Autowiring by type from bean name 'orderServiceImpl' via constructor to bean named 'rateDiscountPolicy'
BUILD SUCCESSFUL in 934ms
4 actionable tasks: 3 executed, 1 up-to-date
10:37:04 PM: Execution finished ':test --tests "hello.core.scan.AutoAppConfigTest.basicScan"'.

```

중간에 보면 Overriding bean definition... 메세지 확인이 가능. 이렇게 빈을 수동으로 만든 경우 스프링은 수동 등록빈을 우선적으로 등록함.\
최근에는 스프링에서 수동 빈등록과 자동 빈등록의 충돌이 발생하면 오류가 발생 하도록 변경하였음. 스프링 부트에서 앱을 실행하면 아래와 같이 메세지를 남김.
```text
Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2023-03-27T22:41:34.800+09:00 ERROR 13115 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   : 

***************************
APPLICATION FAILED TO START
***************************

Description:

The bean 'memoryMemberRepository', defined in class path resource [hello/core/AutoAppConfig.class], could not be registered. A bean with that name has already been defined in file [/Users/choehojun/Documents/Workspace/hello-core/build/classes/java/main/hello/core/member/MemoryMemberRepository.class] and overriding is disabled.

Action:

Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true


Process finished with exit code 1
```
메세지 내용은 빈이 충동했다는 내용이며, spring.main.allow-bean-definition-overriding 값을 true로 변경하도록 권장하고 있다.\
properties 에서 위 옵션을 추가해주면 정상적으로 실행되는 것을 볼 수 있음.

