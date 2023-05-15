#회원 도메인과 레포지토리 만들기

회원에 대한 도메인을 만들어준다.
```java
package hello.hellospring.domain;

public class Member {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```

그리고 회원 레포지토리를 만들어준다.

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long Id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
```
레포지토리는 추후 DB가 정해지면 해당 DB에 맞는 클래스로 바꿔줄 수 있게 interface로 구현한다.\
그리고 아직 DB가 정해지지 않았으므로 Memory에 올릴 구현체를 만들어준다.

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public class MemoryMemberRepository implements MemberRepository{

    @Override
    public Member save(Member member) {
        return null;
    }

    @Override
    public Optional<Member> findById(Long Id) {
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        return null;
    }
}

```

구현체 코드는 아래와 같이 작성해준다.
```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);   //시스템이 정해주는 ID. name 은 사용자가 지정하는 값.
        store.put(member.getId(), member);  //회원 가입이 되면 시퀀스 값이 올라가면서 메모리에 id를 key로 하여 member를 저장
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) { //요즘은 Null처리를 Optional로 처리해준다.
        return Optional.ofNullable(store.get(id)); //메모리에 해당 아이디가 없으면 Optional로 처리
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()  //Stream은 Optional로 리턴이 가능하기 때문에 스트림으로 바로 리턴 가능.
                .filter(member -> member.getName().equals(name))    //member이름이 같은 값을 필터링함.
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); //전체 values를 리턴
    }
}
```

이렇게 만든 코드를 테스트 코드를 작성하여 실제로 동작하는지 확인해준다.