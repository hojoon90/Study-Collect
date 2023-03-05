# 컨테이너에 등록 된 모든 빈 조회

컨테이너에 등록된 빈 들은 다음과 같이 확인 가능하다.

```java
public class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name = " + beanDefinitionName + " object = " + bean);
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            //Role ROLE_APPLICATION: 직접 등록한 App Bean
            //Role ROLE_INFRASTRUCTURE: 스프링 내부에서 사용하는 Bean
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + " object = " + bean);
            }
        }
    }
}
```

* 모든 빈 출력하기는 실행 시 현재 스프링에 등록된 빈 모두를 확인할 수 있음.
* getBeanDefinitionNames: 등록된 모든 빈들의 이름을 가져옴
* getBean: 빈이름으로 빈 객체를 가져옴.


* 애플리케이션 빈 출력하기는 Role에 따라서 출력하는 빈을 나눌 수 있다.
  * getRole을 통해 내가 출력할 빈을 선택 가능
  * Role에 대한 설명은 위 주석으로 갈음