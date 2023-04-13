# 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 Provider로 문제 해결

위에서 싱글톤 빈 안에 프로토타입 빈이 있으면 새로 생성이 되지 않았음. 이를 해결하는 방법에 대해 알아본다.

가장 간단하게 해결하는 방법은 아래와 같이 빈을 계속 찾아오는 방법임
```java
@Scope("singleton")
static class ClientBean {
    @Autowired
    private ApplicationContext ac;
    
    public int logic() {
        PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
        prototypeBean.addCount();
        int count = prototypeBean.getCount();
        return count;
    }

}
```
* 이렇게 의존 관계를 직접 찾아서 사용하는 방식을 의존관계 조회(Dependency Lookup, DL)라고 함.
* 위와 같은 경우 ApplicationContext를 주입 받아서 사용하는데, 이럴 경우 스프링에 의존적이고 단위 테스트가 어려움.
* 그렇기에 아래와 같이 사용.

```java
@Scope("singleton")
static class ClientBean {
    @Autowired
    private ObjectProvider<PrototypeBean> prototypeBeanProvider;

    public int logic() {
        PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
        prototypeBean.addCount();
        int count = prototypeBean.getCount();
        return count;
    }

}
```

출력 결과
```text
23:50:11.761 [Test worker] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@44be0077
23:50:11.768 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalConfigurationAnnotationProcessor'
23:50:11.777 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerProcessor'
23:50:11.778 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerFactory'
23:50:11.779 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalAutowiredAnnotationProcessor'
23:50:11.780 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalCommonAnnotationProcessor'
23:50:11.783 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'singletonWithPrototypeTest1.ClientBean'
PrototypeBean.init hello.core.scope.SingletonWithPrototypeTest1$PrototypeBean@b70da4c
PrototypeBean.init hello.core.scope.SingletonWithPrototypeTest1$PrototypeBean@51cd7ffc
```
* 해당 코드 실행 시 prototypeBeanProvider.getObject() 로 새로운 객체를 받아오는 것을 알 수 있음.
* ObjectProvider 의 getObject() 호출 시 스프링 컨테이너를 통해 해당 빈을 찾아서 반환.
* 기능이 단순하여 단위테스트를 만들거나 mock 코드를 만들기 좋음.
* 다만 스프링에 의존적임.(ObjectProvider 자체가 스프링에 종속되어있음.)

스프링에 의존적으로 사용하고 싶지 않으면 자바 표준인 JSR-330 Provider를 사용하면 됨.

먼저 라이브러리 추가
```groovy
dependencies{
    implementation 'javax.inject:javax.inject:1'
}
//스프링부트 3.X 버전부턴 아래와 같이 추가.
dependencies{
    implementation 'jakarta.inject:jakarta.inject-api:2.0.1' //추가
}
```

그 후 코드를 아래와 같이 변경
```java
import javax.inject.Provider;
//3.x 용
//import jakarta.inject.Provider;

@Scope("singleton")
static class ClientBean {
    @Autowired
    private Provider<PrototypeBean> prototypeBeanProvider;

    public int logic() {
        PrototypeBean prototypeBean = prototypeBeanProvider.get();
        prototypeBean.addCount();
        int count = prototypeBean.getCount();
        return count;
    }

}
```
출력 결과
```text
23:58:20.259 [Test worker] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@72ef8d15
23:58:20.265 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalConfigurationAnnotationProcessor'
23:58:20.274 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerProcessor'
23:58:20.275 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerFactory'
23:58:20.276 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalAutowiredAnnotationProcessor'
23:58:20.276 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalCommonAnnotationProcessor'
23:58:20.279 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'singletonWithPrototypeTest1.ClientBean'
PrototypeBean.init hello.core.scope.SingletonWithPrototypeTest1$PrototypeBean@73dce0e6
PrototypeBean.init hello.core.scope.SingletonWithPrototypeTest1$PrototypeBean@62ea3440
```
* 실행하면 위의 코드와 똑같이 provider.get() 을 통해 객체를 새로 생성하는 것을 볼 수 있음. 
* provider 의 get() 호출 시 스프링 컨테이너를 통해 해당 빈을 찾아서 반환.
* 기능 단순 및 테스트 용이도 위와 동일.
* 자바 표준이기에 스프링에 의존적이지 않아 다른 컨테이너에서 사용 가능
* 별도 라이브러리 추가 필요.

실무에서는 사실 위 두가지 방식은 거의 사용하지 않음.(당장 나조차도 처음보는 기능임.) \
두 방법은 모두 기능 자체는 동일하므로 상황에 맞게 취사 선택 필요. 큰 이슈가 없으면 스프링 기능으로 사용하는것을 추천.