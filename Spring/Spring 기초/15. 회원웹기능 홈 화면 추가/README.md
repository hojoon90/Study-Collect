# 회원웹기능 홈 화면 추가

이제 실제로 웹 MVC를 통해 회원 관리를 할 수 있도록 만들어본다. 

먼저 홈 화면부터 만들기 위해 홈 컨트롤러를 추가해준다.
```java
package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "home";
    }

}

```

```html
<!--home.html-->
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
  <div>
    <h1>Hello Spring</h1>
    <p>회원 기능</p>
    <p>
      <a href="/members/new">회원 가입</a>
      <a href="/members">회원 목록</a>
    </p>
  </div>
</div>

</body>
</html>
```
홈 컨트롤러에서 "/" 로 접근하면 home.html로 접근할 수 있도록 컨트롤러를 작성해주었다. 여기서 
컨트롤러 매핑을 / 로 해주었는데, 기존에 매핑이 되어있지 않았을 경우 resources/static/index.html
을 호출하지만, 위와 같이 매핑을 해주면 매핑해준 html을 호출하도록 해준다.

html 파일은 각각 회원가입과 회원 목록을 조회할 수 있는 화면이 있으며, 해당 URL을 호출하면 현재는 
기능 구현이 되어있지 않아 404 Error가 나는 것을 확인할 수 있다.
