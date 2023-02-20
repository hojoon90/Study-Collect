# 회원 도메인 설계

정책이 현재 없는 상태에서 회원 도메인을 설계.
* 요구사항
  * 회원 가입, 조회 가능.
  * 일반과 VIP 두 가지 등급의 회원.
  * 회원 데이터는 자체 DB or 외부 시스템과 연동가능. (미확정)

회원 도메인의 협력관계는 다음과 같음.

<img src="images/domain.png" width="80%" height="80%"/>

클래스 다이어그램은 아래와 같음.

memberService 는 역할, MemberServiceImpl은 구현체가 된다.\
마찬가지로 MemberRepository가 역할, 나머지 두개 클래스가 구현체.

<img src="images/class_diagram.png" width="80%" height="80%"/>

회원 객체 다이어그램은 아래와 같다.
> 클라이언트  -> 회원 서비스 -> 메모리 회원 저장소