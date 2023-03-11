# 웹 애플리케이션과 싱글톤

* 스프링은 기업용 온라인 서비스 기술을 지원하기 위해 탄생
* 대부분 스프링 앱은 웹앱임.
* 웹앱은 보통 여러고객이 동시에 요청한다.
* 우리가 기존에 스프링 없이 만든 앱의 경우, 이렇게 여러 고객에게 요청이 들어올때마다 해당 요청에 대한 클래스를 계속 새로 생성함
  * memberService의 경우, 고객이 요청할 때마다 새롭게 만들어냄.

```java
package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer(){
        AppConfig appConfig = new AppConfig();
        //1. 조회: 호출할 때 마다 객체 생성
        MemberService memberService1 = appConfig.memberService();
        MemberService memberService2 = appConfig.memberService();

        // 참조 값이 다름을 확인.
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        //memberService1 != memberService2
        assertThat(memberService1).isNotSameAs(memberService2);

    }
}
```
* 실제 테스트를 진행해보면 객체값이 다른것을 확인할 수 있음
```shell
memberService1 = hello.core.member.MemberServiceImpl@79e4c792
memberService2 = hello.core.member.MemberServiceImpl@5f049ea1
```
* 이렇게 요청때마다 객체가 계속 생성되면 메모리 낭비가 심해지게 됨.
* 이를 방지하기 위해선 객체가 1개만 생성 된 후, 그 객체를 공유할 수 있도록 해주면 됨.
