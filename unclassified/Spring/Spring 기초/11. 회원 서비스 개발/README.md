# 회원 서비스 개발

회원 서비스 개발을 진행한다. 회원 서비스와 리포지토리를 이용하여 비즈니스 로직을 만든다.

일단 전체적인 코드는 다음과 같다.
```java
package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    /**
     * 회원 가입
     */
    public Long join(Member member){
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    // commend + option + m -> 로직을 메소드로 분리.
    // control + t 로 단축 명령? 확인.
    private void validateDuplicateMember(Member member) {   
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     * 단일 회원 조회
     */
    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }

}

```
join 메소드의 로직에서는 요구사항에 있던 것과 같이 중복 회원을 검증하는 메소드를 거친 후 회원 가입이 진행된다.
memberRepository에서 findByName을 이용해 회원 정보를 조회하는데, findByName은 Optional return이므로
ifPresent 메소드 사용이 가능하다. ifPresent는 간단히 설명하면 값이 있으면 코드를 실행하는 메소드이다.

위에서는 중복 체크를 하는 로직에 사용되었으므로, 이름으로 사용자 조회 시 값이 존재하면 IllegalStateException이 throw 되도록
처리되었다. 별도의 Exception이 발생하지 않으면 회원 가입이 진행되고 ID를 리턴해준다.

나머지 메소드들 역시 전체 회원 조회와 단일 회원 조회 메소드를 구현하였다. 

여기서 repository의 경우 단순 저장소에 데이터를 저장하는 네이밍을 정한다. (findAll, findById...)
반면 service의 네이밍은 좀 더 비즈니스적인 네이밍을 따른다.(join, findMembers...)