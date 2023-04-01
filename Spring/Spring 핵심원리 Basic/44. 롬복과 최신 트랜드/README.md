# 롬복과 최신 트랜드

지금까지 만들었던 코드들을 좀 더 간간하게 만들어보자.

롬복 설정 추가
```groovy

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}


dependencies {

    //lombok 라이브러리 추가 시작
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    testCompileOnly 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'
    //lombok 라이브러리 추가 끝
}
```

롬복은 Getter, Setter, RequireArgsConstructor 등 실제 개발자들이 만들어야 할 코드를 줄여주는 어노테이션이다.

롬복 예시
```java
package hello.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("asdf");

        System.out.println("helloLombok = " + helloLombok);
    }


}
```
위에서 필드들의 Getter, Setter, ToString 등을 자동으로 만들어준다.

orderServiceImpl을 롬복을 통해 코드를 줄여보자.
```java
@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

//    @Autowired
//    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }
    ...
}
```
위의 생성자를 주석 처리함. @RequiredArgsConstructor를 통해 final이 붙은 필드들을 찾아 생성자를 자동으로 만들기 때문에 필요 없어짐.
