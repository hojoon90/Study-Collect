# 싱글톤 패턴

클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 패턴.

아래와 같이 코드 작성
```java
package hello.core.singleton;

public class SingletonService {

    private static final SingletonService instance = new SingletonService();
    
    public static SingletonService getInstance(){
        return instance;
    }
    
    private SingletonService(){

    }

    public void logic(){
        System.out.println("싱글톤 객체 로직 호출");
    }
}

```
* static 영역에 객체 하나만 올리기 위해 static final 추가
* 해당 객체를 가져오기 위해 public으로 getInstance 메소드를 열고, 미리 생성된 객체를 가져온다.
* 싱글톤이기 때문에 객체 생성을 막기 위해 private으로 생성자 추가 (new 로 생성하지 못하게 막음.)

실제 사용 코드는 다음과 같음
```java
@Test
@DisplayName("싱글톤 패턴 적용한 객체 사용")
void singletonServiceTest(){
    SingletonService singletonService1 = SingletonService.getInstance();
    SingletonService singletonService2 = SingletonService.getInstance();

    System.out.println("singletonService1 = " + singletonService1);
    System.out.println("singletonService2 = " + singletonService2);

    assertThat(singletonService1).isSameAs(singletonService2);
    //same: == 비교
    //equal: equals 메소드 비교

}
```
테스트를 진행해보면, 두개 모두 같은 참조값을 가져오는 것을 확인할 수 있음.
```shell
singletonService1 = hello.core.singleton.SingletonService@769a1df5
singletonService2 = hello.core.singleton.SingletonService@769a1df5
```
싱글톤 패턴을 사용하면 요청이 들어올 때마다 객체를 생성할 필요 없이, 미리 만들어진 객체를 공유하여 사용 가능.

하지만 단점들도 존재
* 싱글톤 패턴 구현을 위한 별도 코드 추가
* 클라이언트가 구현객체에 의존 (DIP 위반)
* 위와 동일한 이유로 OCP도 위반 가능성 많음
* 테스트 힘듦
* 자식 클래스 생성 힘듦(private 사용으로 인해)
* 내부속성 변경 혹은 초기화 어려움.