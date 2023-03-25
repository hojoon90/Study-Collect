# 필터 

컴포넌트 필터 기능에 대해 알아본다.

먼저 어노테이션들을 하나씩 만들어준다.

```java
//필터에 포함할(스캔시킬) 어노테이션
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {
}

//필터에 포함시키지 않을(스캔시키지 않을) 어노테이션
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {
}
```

그 후 위에서 만들어준 어노테이션들을 적용한 클래스를 만든다.

```java
@MyIncludeComponent
public class BeanA {
}

@MyExcludeComponent
public class BeanB {
}
```

그리고 아래와 같이 테스트를 만든다.

```java
public class ComponentFilterAppConfigTest {

    @Test
    void filterScan(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isNotNull();

        assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("beanB", BeanB.class));

    }

    @Configuration
    @ComponentScan(
            includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )
    static class ComponentFilterAppConfig{

    }

}
```
* 먼저 Config를 만든다.
* 스캔 시킬 어노테이션과 스캔하지 않을 어노테이션을 추가.
* filerScan에서 테스트 진행.

위와 같이 테스트 시 "beanA"는 스캔하고 beanB는 스캔하지 않는 것을 확인할 수 있음.

FilterType은 5가지 옵션이 있음.
* ANNOTATION: 기본 값. 어노테이션을 인식
* ASSIGNABLE_TYPE: 지정한 타입과 자식타입을 인식해서 동작
* ASPECTJ: AspectJ 패턴 사용
* REGEX: 정규 분포식 사용
* CUSTOM: TypeFilter라는 인터페이스를 구현하여 처

includeFilters는 사용할 일이 없으며 excludeFilters 도 사용을 많이 하진 않음. 스프링 기본 설정으로 사용하는 것을 권장.