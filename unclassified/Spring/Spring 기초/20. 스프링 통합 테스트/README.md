# 스프링 통합 테스트

이번 시간엔 스프링 통합 테스트를 진행한다. 기존엔 스프링 연동 없이 테스트를 진행했는데, 실제 우리는
스프링을 사용하여 개발을 진행하므로 같이 연동하여 테스트 진행하도록 한다.

먼저 테스트 클래스를 만들어준다. 아래는 전체 테스트 코드이다.

```java
package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest //스프링 연동 테스트는 이 어노테이션을 붙여준다.
@Transactional  //테스트 후 insert, update, delete 됐던 데이터들을 rollback 해준다.
public class MemberServiceIntegrationTest {

    //@Autowired로 Spring Container에서 객체를 가져온다.
    //또한 Test는 별도의 설계없이 그냥 테스트용이므로 편한 방법으로 Injection하면 된다.
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;  //인터페이스를 가져온다.

    //beforeEach, afterEach가 삭제되었다.

    @Test
    void 회원가입() {   //테스트는 메소드 이름을 한글로 써도 무방하다
        //테스트 할때 기본적인 패턴
        //given
        Member member = new Member();
        member.setName("hello");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 중복_회원_예외(){
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        //then
    }

}

```
여기서 기존 테스트와 몇군데 변경된 것을 확인할 수 있는데, 눈여겨 봐야 할 부분은 어노테이션 부분들이다. 
먼저 @SpringBootTest는 스프링과 연동하여 테스트를 진행할 수 있도록 해주는 코드이다. 그 아래 @Transactional 어노테이션은 
테스트 후 롤백을 하는 역할을 한다.
테스트는 지속적으로 수행되어야 하는데, 우리가 만든 회원가입과 같은 테스트는 실제 DB에 값이 들어가면 더이상 테스트 진행이
불가능 해진다.(중복회원의 가입이 불가능 하므로.) 그런 현상을 방지하기 위해 위와 같이 @Transactional 
어노테이션을 통해 실제 넣었던 데이터들을 rollback 해주는 것이다.

아래 @Autowired는 Spring Container에서 객체를 가져오기 위해 추가해준다. 이렇게하면 Spring이
실제 실행될 때 컨테이너에서 해당 객체를 가져올 수 있게 된다. 마지막으로 beforeEach, afterEach가 삭제되었다.

이 상태로 실제 테스트를 돌려보면 정상적으로 테스트 케이스들이 동작 하는 것을 확인할 수 있다. 그렇다면 우리가
기존에 만든 테스트들은 필요하지 않는것일까? 아마 아닐 것이다. 현재 만든 스프링 통합 테스트와 기존에 만들었던
테스트들을 서로 비교해보자. 기존에 만든 테스트들은 실행할 때 실행시간이 굉장히 빠르게 끝나는 것을 볼 수 있다.
반면, Spring 통합 테스트의 경우 Spring 구동 시간까지 같이 합쳐보면 테스트가 좀 오래 걸리는 것을 볼 수 있다.

물론 통합 테스트가 오래걸린다고 나쁜 테스트라고 할 수는 없다. 하지만 단위별로 쪼갠 테스트들을 잘 만들어두면, 
개발 범위가 많아 졌을때 테스트로 허비하는 시간이 굉장히 단축될 수 있다. 이러한 점들을 잘 고려하여 테스트케이스를
작성하면 좋다.