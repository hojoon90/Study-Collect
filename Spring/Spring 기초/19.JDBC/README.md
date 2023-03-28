# JDBC

이제 DB에 실제 JDBC를 이용하여 데이터를 넣어보자. (이번 내용은 편하게...)

먼저 build.gradle에 라이브러리를 추가해준다.
```groovy
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
runtimeOnly 'com.h2database:h2'
```

그리고 접속 정보를 추가해준다.
```properties
#resources/application.properties
spring.datasource.url=jdbc:h2:tcp://localhost/~/test
spring.datasource.driver-class-name=org.h2.Driver
```
이렇게 만들어주면 DB 연결 세팅이 완료된다. 요즘은 spring에서 바로바로 세팅이 가능하다.
그 후 DB와 연결에 필요한 repository 클래스를 만들어준다. 이때 MemberRepository를
implements 해준다.

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JdbcMemberRepository implements MemberRepository{

    private final DataSource dataSource;

    public JdbcMemberRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(name) values(?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;    //결과를 받음

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);    //RETURN_GENERATED_KEYS -> 아이디값 얻어오기

            pstmt.setString(1, member.getName());   //첫번째 ?에 name을 넣는다.

            pstmt.executeUpdate();  //DB에 실제 쿼리가 날아감.
            rs = pstmt.getGeneratedKeys();  //RETURN_GENERATED_KEYS 와 연계됨. 번호를 반환해준다.

            if(rs.next()) { //rs에 값이 있으면 번호를 가져와서 세팅한다.
                member.setId(rs.getLong(1));
            }else{
                throw new SQLException("id 조회 실패");
            }
            return member;
        }catch (Exception e){
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();  //  조회는 executeQuery

            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }else{
                return Optional.empty();
            }
        }catch (Exception e){
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }

    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);

            rs = pstmt.executeQuery();  //  조회는 executeQuery

            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }

            return Optional.empty();
        }catch (Exception e){
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();  //  조회는 executeQuery

            List<Member> members = new ArrayList<>();
            while (rs.next()){
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }

            return members;
        }catch (Exception e){
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 스프링 프레임워크를 통해 DB 접근할때는 DataSourceUtils를 통해서 dataSource connection을 해야한다.
    // 트랜잭션 유지를 위해..
    private Connection getConnection(){
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs){
        try{
            if(rs != null){
                rs.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            if(pstmt != null){
                pstmt.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            if(conn != null){
                close(conn);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //DB 연결을 끊을 땐 releaseConnection을 이용해 끊어준다.
    private  void close(Connection conn) throws SQLException{
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
```
설명은 주석에 작성하고 별다른 설명은 하지 않는다... 현재는 거의 사용하지 않는 방법이기 때문에
주석으로 대체한다.

그 후 이제 MemoryMemberRepository를 JDBC용으로 교체해보자. Config 클래스에 가서 다음과 같이 변경해준다.
```java
package hello.hellospring;

import hello.hellospring.repository.JdbcMemberRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private DataSource dataSource;  //추가
    
    //추가
    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
//        return new MemoryMemberRepository();
        return new JdbcMemberRepository(dataSource);    // 추가
    }
}
```
여기 코드에서 실제 수정한 부분은 SpringConfig와 JdbcMemberRepository가 전부이다.
스프링의 경우 '다형성 활용'을 굉장히 편리하게 해준다(Spring Container를 통해). 실제 위의 코드도
기존 코드의 수정 없이 config쪽의 코드만 일부 수정이 들어갔다(DI). 스프링은 이러한 작업을 굉장히
편하게 해준다. 이런 원칙을 바로 개방-폐쇄의 원칙(OCP)이라고 한다.
