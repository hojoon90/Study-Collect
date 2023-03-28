# 회원 도메인 실행과 테스트

실제 개발한 회원 도메인에 대한 테스트 진행

* MemberApp이라는 클래스를 만들어 실제 테스트를 진행해봄
```java
public class MemberApp {

    public static void main(String[] args) {
        //MemberService 생성
        ...
        //회원 정보 입력
        ...
        //회원 가입 진행
        ...
        //회원 조회
        ...
        //System.out.println 으로 입력값과 조회값 비교
        ...
    }

}

```
* 이렇게 확인하는것도 좋지만, 테스트 코드를 작성하여 테스트 진행
* src/test/java 디렉토리 안에 테스트 파일 생성 후 테스트
```java
public class MemberServiceTest {

    //MemberService 생성

    @Test
    void join(){
        //given (회원 정보 생성)
        ...
        
        //when (회원 가입, 회원 조회 후 변수에 담음)
        ...
        
        //then (회원정보가 같은지 조회)
        ...
    }
}

```
테스트 정상 동작 확인.

고민해보아야 할 것.
* 코드의 설계상 문제점은?
* 저장소 변경 시 OCP(개방-폐쇄 원칙) 원칙을 잘 준수할 수 있는가?
* DIP(의존관계 역전 원칙)를 잘 지키고 있는가?
* 현재 코드는 의존관계가 인터페이스뿐만이 아닌 구현까지 모두 의존하고 있음.
