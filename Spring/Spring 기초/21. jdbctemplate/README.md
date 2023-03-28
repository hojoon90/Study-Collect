# jdbctemplate

Spring Jdbc Template을 이용한 DB 연동법을 알아본다. JDBC Template 은 기존에서 사용했던
JDBC 코드에서 중복되는 부분(연결부분 혹은 prestatement등)의 코드들을 줄여주기 위해 사용한다.

먼저 JdbcTemplateMemberRepository클래스를 만들어준다.
```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        return null;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id).stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        return null;
    }

    public RowMapper<Member> memberRowMapper(){
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }

}

```

클래스를 만든 후 아이디로 멤버를 조회하는 코드까지 작성하였다. 먼저 JdbcTemplate 을 선언 해주고
JdbcTemplateMemberRepository를 Bean으로 등록할 때 datasource를 jdbcTemplate에
주입하여 사용할 수 있게 해준다. 실제 19장에서 했던 JDBC와 비교해보자. 멤버 아이디로 멤버를 조회하는 코드가 
단 한줄로 줄어든 것을 볼 수 있다.

나머지 코드들도 모두 작성해주자.
```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.*;

public class JdbcTemplateMemberRepository implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());

        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id).stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        return jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name).stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    public RowMapper<Member> memberRowMapper(){
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }

}

```
이제 템플릿을 통한 쿼리 작성을 완료했으니 다시 Config를 수정해보자.
```java
@Bean
public MemberRepository memberRepository(){
//    return new MemoryMemberRepository();
//    return new JdbcMemberRepository(dataSource);
    return new JdbcTemplateMemberRepository(dataSource);
}
```
그 후 테스트를 진행하여 데이터가 제대로 테스팅되는지 확인해보면 된다.