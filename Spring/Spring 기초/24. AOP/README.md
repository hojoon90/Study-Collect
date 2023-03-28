# AOP

AOP가 필요한 상황?
* 모든 메소드의 호출 시간 측정?

먼저 우리가 만든 메소드의 시간을 측정하고 싶다고 해보자.
```java
...
public Long join(Member member) {
    long start = System.currentTimeMillis();
    try{
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }finally {
        long finish = System.currentTimeMillis();
        long timeMs = finish-start;
        System.out.println("join = "+timeMs+"ms");
    }
}
...
```
위의 메소드는 회원 가입에 대한 로직이다. 회원 가입의 메소드 소요시간을 알고 싶어 다음과 같이
시간을 측정하는 로직을 넣어주었다. finally 로직 안에 메소드 종료시간과 소요시간을 출력하도록 로직을
만들어 주었다. 해당 상황이 회원가입만 측정하는 경우에는 상관이 없다. 하지만 우리가 1000개의 메소드를
만들었는데 1000개 메소드 모두 시간 측정이 필요하다면? 문제가 될 것이다.

여기서 문제점은 다음과 같다
* 위 메소드들에서 시간 측정은 핵심 로직이 아니다.
* 시간 측정은 모든 메소드에서 실행되어야 할 공통 사항이다.
* 하지만 메소드별 시간 측정은 공통적으로 만들 수 있는 부분이 아니다.
* 시간 측정 로직이 들어가면서 기존 로직에 대한 유지보수가 힘들어진다.
* 시간 측정 로직 변경 시 모두 다 수정해주어야 한다.

생각보다 문제점이 많아지게 된다. 이런 문제점을 해결할 수 있는 것이 AOP이다.

