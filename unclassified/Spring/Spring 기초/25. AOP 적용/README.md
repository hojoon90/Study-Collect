# AOP 적용

* AOP: Aspect Oriented Programming
* 공통 관심 사항과 핵심 관심 사항을 나누는 것
* AOP를 적용하면 시간 측정 로직을 하나로 모은 후 각 메소드에 적용되도록 지정

전체 코드

```java
package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))") // 메소드에 타게팅해준다. 여기서는 패키지 하위에 전체적용.
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        
        System.out.println("START: " + joinPoint.toString());
        try{
            return joinPoint.proceed(); //다음 메서드로 진행.
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
```
* @Around: 메소드 혹은 패키지 별로 적용할 수 있도록 지정해주는 것.
* @Aspect: AOP 사용을 위한 어노테이션
* @Component: 빈 등록을 위해 적용.

실제 등록 후 메소드 실행 시 전체 타임이 콘솔에 출력됨.
```shell
START: execution(String hello.hellospring.controller.MemberController.list(Model))
START: execution(List hello.hellospring.service.MemberService.findMembers())
START: execution(List org.springframework.data.jpa.repository.JpaRepository.findAll())
Hibernate: select member0_.id as id1_0_, member0_.name as name2_0_ from member member0_
END: execution(List org.springframework.data.jpa.repository.JpaRepository.findAll()) 89ms
END: execution(List hello.hellospring.service.MemberService.findMembers()) 95ms
END: execution(String hello.hellospring.controller.MemberController.list(Model)) 111ms
```

AOP 동작 방식
1. 기존: 컨트롤러로 진입 시 실제 memberService를 호출
2. AOP 적용 후: 컨트롤러 진입 시 스프링 컨테이너에서 proxy 적용된 memberService 먼저 호출 -> 그 후 joinPoint.proceed() 후 실제 memberService 호출

AOP 자체가 적용될 수 있는건 DI기반이기 때문. 
* DI는 객체만 넘어와서 처리되기만 하면 되기 때문에 객체가 Proxy로 만든 가짜여도 상관이 없다.
* proxy를 통한 메소드가 실제 사용할 곳에 주입 되어 사용 가능