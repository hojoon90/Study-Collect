# 스프링 빈 설정 메타정보 - BeanDefinition

스프링이 이렇게 다양한 설정정보를 지원하는 것은 BeanDefinition 때문이다.

BeanDefinition은 역할과 구현을 잘 나눈 예시이다.
* 스프링 컨테이너는 BeanDefinition만 바라봄
* ApplicationCotext 생성 시 각 파일 안의 설정들을 다 읽음
  * Annotation 의 경우 AnnotatedBeanDefinitionReader 를 이용하여 AppConfig.class 안의 설정을 다 읽는다. (팩토리 메서드를 통해서 등록)
  * xml의 경우 XmlBeanDefinitionReader 를 이용하여 appConfig.xml 파일 안의 설정을 다 읽는다. (직접 등록)
* 읽은 후 BeanDefinition 을 만든다.


BeanDefinition 정보는 간단하게 다음과 같음
* BeanClassName: 생성할 빈의 클래스 명 (팩토리 사용시엔 값 없음) 
* factoryBeanName: 빈 팩토리 사용 이름 (직접 등록시에 값 없음. xml 등)
* factoryMethodName: 빈 팩토리 메서드 이름 (직접 등록시에 값 없음. xml 등)
* Scope: 싱글톤(기본 값)
* lazyInit: 컨테이너 생성 시 빈 생성이 아닌 사용 시 빈 생성을 할지 여부값
* InitMethodName: 빈 생성 후 의존관계를 적용한 뒤에 호출되는 초기화 메서드 명 
* DestroyMethodName: 빈의 생명주기가 끝나서 제거하기 직전에 호출되는 메서드 명 
* Constructor arguments, Properties: 의존관계 주입에서 사용.

테스트는 다음과 같이 해볼 수 있다
```java
package hello.core.beandefinition;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class BeanDefinitionTest {

//    AnnotationConfigApplicationContext ac  = new AnnotationConfigApplicationContext(AppConfig.class);
    GenericXmlApplicationContext ac  = new GenericXmlApplicationContext("appConfig.xml");

    @Test
    @DisplayName("빈 설정 메타정보 확인")
    void findApplicationBean(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION){
                System.out.println("beanDefinitionName = " + beanDefinitionName + " beanDefinition = " + beanDefinition);
            }
        }
    }

}

```

출력값은 다음과 같음
```shell
beanDefinitionName = appConfig beanDefinition = Generic bean: class [hello.core.AppConfig$$SpringCGLIB$$0]; scope=singleton; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodNames=null; destroyMethodNames=null
beanDefinitionName = memberService beanDefinition = Root bean: class [null]; scope=; abstract=false; lazyInit=null; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=appConfig; factoryMethodName=memberService; initMethodNames=null; destroyMethodNames=[(inferred)]; defined in hello.core.AppConfig
beanDefinitionName = memberRepository beanDefinition = Root bean: class [null]; scope=; abstract=false; lazyInit=null; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=appConfig; factoryMethodName=memberRepository; initMethodNames=null; destroyMethodNames=[(inferred)]; defined in hello.core.AppConfig
beanDefinitionName = orderService beanDefinition = Root bean: class [null]; scope=; abstract=false; lazyInit=null; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=appConfig; factoryMethodName=orderService; initMethodNames=null; destroyMethodNames=[(inferred)]; defined in hello.core.AppConfig
beanDefinitionName = discountPolicy beanDefinition = Root bean: class [null]; scope=; abstract=false; lazyInit=null; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=appConfig; factoryMethodName=discountPolicy; initMethodNames=null; destroyMethodNames=[(inferred)]; defined in hello.core.AppConfig
```