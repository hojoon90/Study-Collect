# hasRole과 hasAuthority

스프링에서 로그인한 유저의 권한을 설정할 때 많이 사용하는 것이 hasRole과 hasAuthority이다.  
이 두 메서드는 서로 비슷한듯 하지만 다르기 때문에 모르고 무작정 사용하다간 엄청난 삽질을 할 수 있다.  
두 메서드의 차이를 알아보자

## hasRole
* 사용자가 가진 '역할'에 따라 접근을 제어하기 위해 사용.
* 가장 큰 특징은 접두사로 'ROLE_'이 붙음.
  * 만약 Role 이 ADMIN이면 역할확인 시 'ROLE_ADMIN'으로 들어온 역할에 대해서 접근을 허용함.
* 보통 아래와 같은 코드로 사용.
```java
http.requestMatchers(
        AntPathRequestMatcher.antMatcher("/api/admin/**")
).hasRole("ADMIN") // ROLE_ADMIN 역할을 가진 사용자만 위 URL에 접근할 수 있다.

//또는 아래와 같이 메서드에 애노테이션으로 사용
@PreAuthorize("hasRole('ADMIN')")
public void admin() {...}

```

## hasAuthority
* 사용자의 '권한'에 따라 접근을 제어하기 위해 사용.
* 별도의 접두사가 붙지는 않는다.
* 특정 사용자 몇명에게 삭제권한, 혹은 회원관리 권한 등과 같은 기능에 대한 접근이라고 볼 수 있음.
```java
http.requestMatchers(
        AntPathRequestMatcher.antMatcher("/api/delete/**")
).hasAuthority("REMOVER") // REMOVER 권한을 가진 사용자만 위 URL에 접근할 수 있다.

//또는 아래와 같이 메서드에 애노테이션으로 사용
@PreAuthorize("hasAuthority('REMOVER')")
public void deleteUser() {...}
```