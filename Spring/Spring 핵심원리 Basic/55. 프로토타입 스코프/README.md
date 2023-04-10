# 프로토타입 스코프

* 프로토타입 스코프를 스프링 컨테이너에서 조회 하면 항상 새로운 인스턴스를 생성하여 반환.
* 싱글턴의 경우 스프링컨테이너에서 빈을 관리하면서 클라이언트마다 동일한 객체를 반환
* 프로토타입의 호출 순서 
  1. 요청 시점에서 프로토타입 빈을 생성
  2. 필요 의존관계 주입 및 초기화
  3. 스프링 컨테이너에서 생성한 프로토타입 빈 반환. 반환 후 관리 X
  4. 이후 같은 요청이 들어오면 들어올 때마다 빈 생성

* 이후 빈 관리는 클라이언트에서 책임을 지고 하게 된다. 그래서 @PreDestroy 같은 종료 메서드가 호출되지 않음.

코드로 확인.

### 싱글톤 빈
```java
@Scope("singleton")
static class SingletonBean {
    @PostConstruct
    public void init() {
        System.out.println("SingletonBean.init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("SingletonBean.destroy");
    }
}
```
테스트 코드
```java
@Test
void singletonBeanFind() {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);

    SingletonBean bean1 = ac.getBean(SingletonBean.class);
    SingletonBean bean2 = ac.getBean(SingletonBean.class);
    System.out.println("bean1 = " + bean1);
    System.out.println("bean2 = " + bean2);
    assertThat(bean1).isSameAs(bean2);  //== 비교
    ac.close();
}
```

그럼 다음과 같이 출력된다.
```text
SingletonBean.init
bean1 = hello.core.scope.SIngletonTest$SingletonBean@555cf22
bean2 = hello.core.scope.SIngletonTest$SingletonBean@555cf22
22:57:27.617 [Test worker] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@93081b6, started on Mon Apr 10 22:57:27 KST 2023
SingletonBean.destroy
```
* 빈이 초기화 되고 2번 호출 했을 때 둘이 같은 객체인 것을 확인 가능.
* 그 후 스프링 컨테이너 종료시에도 destroy 되는것을 확인 가능.


### 프로토타입 빈
```java
@Scope("prototype")
static class PrototypeBean {
    @PostConstruct
    public void init() {
        System.out.println("PrototypeBean.init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("PrototypeBean.destroy");
    }

}
```
테스트 코드
```java
@Test
void prototypeBeanFind() {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
    System.out.println("find prototypeBean1");
    PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
    System.out.println("find prototypeBean2");
    PrototypeBean bean2 = ac.getBean(PrototypeBean.class);
    System.out.println("bean1 = " + bean1);
    System.out.println("bean2 = " + bean2);
    assertThat(bean1).isNotSameAs(bean2);

    ac.close();
}
```

테스트 결과는 아래와 같다.
```text
find prototypeBean1
PrototypeBean.init
find prototypeBean2
PrototypeBean.init
bean1 = hello.core.scope.PrototypeTest$PrototypeBean@555cf22
bean2 = hello.core.scope.PrototypeTest$PrototypeBean@6bb2d00b
22:59:32.808 [Test worker] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@93081b6, started on Mon Apr 10 22:59:32 KST 2023
```
* 빈을 2번 호출 할 때 마다 초기화가 되는것을 확인 가능
* 빈 객체 역시 서로 주소값이 다름.
* 그 후 close 시 종료메소드가 호출되지 않는것 확인 (destroy 호출 없음) - 의존관계 주입과 초기화까지만 관여하고 그 이후는 관리하지 않음.

특징
* 스프링 컨테이너에 요청할 때마다 새로 생성.
* 스프링 컨테이너는 의존관계 주입, 초기화까지만 관여
* 종료 메서드 호출 x
* 그렇기에 프로토타입 스코프는 호출하는 클라이언트에서 관리 필요.