# 회원웹기능 조회

이제 회원 기능 중 조회에 대한 기능을 개발하도록 한다.

```java
@GetMapping("/members")
public String list(Model model){
    List<Member> memberList = memberService.findMembers();
    model.addAttribute("members", memberList);
    return "members/memberList";

}
```
회원을 조회할 수 있는 컨트롤러를 만들어준다. Model 객체를 받아와 조회한 회원 정보들의 리스트를 model에 담아
members/memberList.html로 넘겨주도록 처리한다.
```html
<!--members/memberList.html-->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<body>

  <div class="container">
    <div>
      <table>
        <thead>
        <tr>
          <th>#</th>
          <th>이름</th>
        </tr>
        </thead>
        <tbody>
        <tr th:each="member : ${members}">
          <td th:text="${member.id}"></td>
          <td th:text="${member.name}"></td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>

</body>
</html>
```
가입된 회원을 조회하는 리스트이다. 아까 컨트롤러에서 members라는 키로 회원 리스트 전체를 담아서 html로 전달한다.
아래 tr태그에 th:each(thymeleaf 문법. 반복문) 반복문을 돌면서 members에 있는 값을 처리한다.
위에td에 member.id와 member.name을 세팅해준다. 값들은 getId, getName을 통해 가져온다.

이렇게 만든 후 member를 2명 정도 추가한 후 목록을 눌렀을 때, 등록된 회원들이 나타나는 것을 볼 수 있다.

<img src="images/memberList.png" width="80%" height="80%"/>

해당 회원 정보들은 현재 메모리에 존재하기 때문에, 서버를 내리면 회원데이터는 모두 사라지게 된다.