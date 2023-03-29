# 옵션 처리

주입할 스프링 빈이 없어도 동작해야할 때가 있음. 그럴땐 @Autowired 에 옵션을 걸어주면 된다.

옵션 처리 방법은 세가지가 있다.
1. @Autowired(required=false) 처리. 
   1. 자동 주입 대상이 없으면 메소드 자체를 호출하지 않음. 
2. org.springframework.lang.@Nullable 
   1. 자동 주입 대상이 없으면 null 처리
3. Optional<> 처리
   1. 자동 주입 대상이 없으면 Optional.empty 처리

테스트 코드로 확인
```java
public class AutowiredTest {

    @Test
    void AutowiredOption(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    static class TestBean {

        @Autowired(required = false)
        public void setNoBean1(Member noBean1) {
            System.out.println("noBean1 = " + noBean1);
        }

        @Autowired
        public void setNoBean2(@Nullable Member noBean2) {
            System.out.println("noBean2 = " + noBean2);
        }
        
        @Autowired
        public void setNoBean3(Optional<Member> noBean3) {
            System.out.println("noBean3 = " + noBean3);
        }

    }

}
```

위에서 부터 1, 2, 3번 케이스. 테스트 시 각각 출력되는 값을 확인한다. 여기서 Member는 스프링 빈이 아니므로 @Autowired로 자동주입을 해줄 수 없다.


출력 결과
```text
23:09:28.640 [Test worker] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@93081b6
23:09:28.647 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalConfigurationAnnotationProcessor'
23:09:28.659 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerProcessor'
23:09:28.660 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerFactory'
23:09:28.661 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalAutowiredAnnotationProcessor'
23:09:28.661 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'org.springframework.context.annotation.internalCommonAnnotationProcessor'
23:09:28.664 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'autowiredTest.TestBean'
noBean2 = null
noBean3 = Optional.empty
```

2번과 3번은 각각 null과 Optional.empty로 값이 세팅된 것을 확인할 수 있지만, 1번 케이스의 경우 아예 메소드 호출이 되지 않아 출력 자체가 되지 않았다.