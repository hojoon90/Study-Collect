# Spring Data JPA

스프링 데이터 JPA는 리포지토리 없이 인터페이스만으로 개발 완료가 가능하다. 또한 기본 CRUD 역시 
모두다 제공해준다. 한마디로 개발 생산성을 맣이 증가시켜주는 것이다.

우선 인터페이스를 하나 만들어준다.
```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    @Override
    Optional<Member> findByName(String name);

}

```
JpaRepository를 먼저 받아준다. 첫번째는 클래스, 두번째는 아이디의 타입을 넣어준다. 여기서는 <Member, Long>이 된다.
그리고 MemberRepository도 같이 extends 해준다.

그리고 아래 findByName 메소드를 만들어준다. 코드는 이게 전부다(!). 메소드 작성 후 설정 파일을 바꿔준다.
```java
package hello.hellospring;

import hello.hellospring.repository.JpaMemberRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository);
    }

//    @Bean
//    public MemberRepository memberRepository(){
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
//        return new JpaMemberRepository(em);
//    }
}

```
Bean으로 올리던 MemberRepository를 주입받아서 사용하게 변경해주었다. 스프링이 실행될 때 Spring Data JPA가
SpringDataJpaMemberRepository 에 붙어있던 JpaRepository 를 보고 해당 인터페이스를 내부적으로 처리하여 Bean으로 자동 등록해준다. 
그렇게 되면 SpringConfig 에서 MemberRepository를 주입받아 사용할 수 있게 된다.
(위의 extends에 MemberRepository 도 주입되어있다.)

그렇다면 기본적인 save, findById, findAll 메소드는 왜 작성을 하지 않은 걸까? 그 이유는 바로
JpaRepository 안에 공통화 되어있는 메소드들이 모두 존재하기 때문이다. 해당 인터페이스를 확인해보면
기존에 Repository를 만들 때 사용했던 메소드들이 존재하는 것을 확인할 수 있다.

```java
...
@NoRepositoryBean
public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll()
	 */
	@Override
	List<T> findAll();

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
	 */
	@Override
	List<T> findAll(Sort sort);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll(java.lang.Iterable)
	 */
	@Override
	List<T> findAllById(Iterable<ID> ids);

...
```
그렇기 때문에 공통적으로 필요한 메소드들은 굳이 작성해줄 필요가 없어지는 것이다.
(위 JpaRepository 뿐만 아니라 JpaRepository가 상속 받는 PagingAndSortingRepository, 
그리고 PagingAndSortingRepository이 상속 받는 CRUDRepository에 모든 공통 메소드들이 존재한다.)
그렇다면 Name은 왜 별도로 만들어 준 것일까. 위와 같이 name을 통한 조회는 공통적인 비즈니스가 아니기 때문에
공통 메소드로 제공해줄 수 없다. 그래서 Spring Data JPA에서는 메소드의 이름을 작성해주면 해당 이름으로
JPQL을 만들어준다. 즉 아래와 같다.
```java
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    //JPQL select m from Member m where m.name = ?
    @Override
    Optional<Member> findByName(String name);

}
```
위의 메소드 이름에 맞게 JPQL을 만들어주기 때문에 메서드 이름만으로 조회가 가능해진다.

복잡한 동적쿼리의 경우 대부분 QueryDSL을 사용하여 쿼리를 작성해주면 된다. 그래서 실무에서도 
JPA, Spring Data JPA, QueryDSL을 모두 조합하여 사용한다. 하지만 이 마저도 사용이 힘들 경우
JPA는 동적 쿼리도 허용을 해준다. 그렇기 때문에 MyBatis등 과도 조합하여 사용할 수 있다.