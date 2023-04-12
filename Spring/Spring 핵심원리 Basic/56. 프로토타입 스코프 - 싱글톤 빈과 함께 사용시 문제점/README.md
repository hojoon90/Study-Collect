# 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 문제점

* 프로토타입 스코프는 컨테이너에서 요청할 때 마다 새로운 인스턴스 객체를 반환해줌.
* 하지만 싱글톤 빈과 함께 사용시엔 문제가 발생하게 됨.

### 프로토타입 빈 요청
* 프로토타입의 빈이 하나 있다고 가정
* 이 빈은 count 필드가 있으며 초기값은 0
* 이 빈의 addCount()를 호출하면 카운트가 1 올라감.
* 서로 다른 클라이언트가 이 빈을 호출할 때마다 각각 다른 인스턴스 객체를 받으므로 카운트는 서로 각기 올라간다.

아래 코드와 같음.
```java
class Test1{
    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;
    
        public void addCount() {
            count++;
        }
    
        public int getCount() {
            return count;
        }
    
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }
    
        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
```
* 테스트에서 prototypeBean1과 prototypeBean2는 같은 빈을 호출하지만 서로 각각의 인스턴스를 받게 됨.
* 그렇기에 테스트 시 둘다 카운트는 1이 나오게 된다.

하지만 싱글톤과 같이 사용하면 위와 같이 동작하지 않게 된다.

### 싱글톤 타입 빈에 프로토타입 빈 요청

* 싱글톤 타입 빈에서 프로토타입 빈의 의존관계 주입을 한다고 가정해보자.
* 아래 ClientBean은 싱글톤이면서 PrototypeBean을 의존관계 주입을 받는다.
* ClientBean이 싱글톤이기에 컨테이너 생성 시점에 같이 생성 되며 의존관계 주입도 발생.
* 이 때 PrototypeBean을 호출하여 의존관계 주입을 받고 내부 필드(prototypeBean)에 보관.

```java
@Scope("singleton")
static class ClientBean {
    private final PrototypeBean prototypeBean;  //생성시점에 주입

    public ClientBean(PrototypeBean prototypeBean) {
        this.prototypeBean = prototypeBean;
    }

    public int logic() {
        prototypeBean.addCount();
        int count = prototypeBean.getCount();
        return count;
    }

}
```
* 이 ClientBean의 logic()을 클라이언트중 한명이 호출하면 count 1을 반환함.
* 그 후 다른 클라이언트가 logic()을 호출하면?
* count값은 1이 아닌 2가 반환됨.
* 아래 테스트에서 확인 가능

```java
class Test2{
    @Test
    void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);
    }
}
```

* 이렇게 되는 이유는 호출과 관련이 있음.
* ClientBean은 일단 싱글톤 빈이기에 한번 객체가 생기면 스프링 빈 소멸 전까지 계속 유지 됨.
* 그리고 프로토타입 스코프의 빈은 컨테이너에서 요청할 때 새로운 객체를 반환함.
* ClientBean이 컨테이너 생성 시점에서 의존관계 주입으로 PrototypeBean을 호출하여 새로운 객체를 반환받고, 그 받은 객체를 계속 들고 있는 것임.(싱글톤이기에)
* 그렇기에 ClientBean안에 있는 prototypeBean은 객체가 계속 생성되지 않고 싱글톤처럼 동작하게 된다.

하지만 프로토타입을 사용하는경우는 이런 경우를 원하는 것은 아님. 사용할 때마다 새롭게 생성받아 사용하기를 원하는것임. 이를 해결하려면 어떻게 해야하는가.