# 회원웹기능 등록

실제 회원 가입을 하는 기능을 만들어본다. 먼저 MemberController에 회원가입 페이지를 접근할 수 있는
컨트롤러를 만들어준다.

```java
    @GetMapping("/members/new")
    public String createForm(){
        return "members/createMemberForm";
    }
```
```html
<!--/members/createMemberForm.html-->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<body>

<div class="container">
  <form action="/members/new" method="post">
    <div class="form-group">
      <label for="name">이름</label>
      <input type="text" id="name" name="name" placeholder="이름을 입력하세요">
    </div>
    <button type="submit">등록</button>
  </form>
</div>

</body>
</html>
```
컨트롤러는 회원가입 폼으로 접근하기 위한 컨트롤러이다. 해당 컨트롤러를 호출하면 "members/createMemberForm"
으로 접근하도록 해준다. 아래 html 은 회원가입폼을 간단하게 작성한 html로 이름을 입력받도록 되어있다.

위에 form을 보면 action에 "/members/new", method에 "post"라고 적혀있다. 즉, 등록 버튼을 누르면
서버의 /members/new 라는 경로를 POST 형태로 호출하도록 되어있는 것이다. 그리고 아래 input 태그 안에 
"name" 옵션의 값이 "name"으로 되어있는 것을 볼 수 있다. 서버에 등록이 일어날 때 저 name이 키가 되어
사용자가 입력한 값이 들어오게 된다. 서버에서는 name안에 있는 필드값으로 값을 받아올 수 있다.

이제 화면에서 입력한 이름을 받아올 수 있는 도메인을 만들어준다.
```html
package hello.hellospring.controller;

public class MemberForm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```

해당 도메인은 이름을 get/set 하는 기능을 갖는 도메인이다. 위의 html에서 name으로 값을 넘겨주면
이 도메인의 set메소드를 통해 사용자가 입력한 이름이 세팅된다. 이제 실제 DB에 저장할 수 있는 컨트롤러를
만들어준다. MemberController안에 만들어준다.

```java
    @PostMapping("/members/new")
    public String create(MemberForm form){
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }
```
Post는 form과 같은 데이터를 받아올 때 사용된다. 위에서 변수를 MemberForm 형태를 받아오게 되어있다.
아까 html에서 세팅한 name 데이터가 넘어오게 되면, 이 컨트롤러 안에서 form.getName으로 
사용자의 이름을 받아올 수 있게 된다. 그리고 나서 받아온 이름을 Member객체 안에 넣어준 후, 
회원가입 메소드를 태워 실제 회원가입 로직을 탈 수 있도록 해준다. 