# 변수 SpringEL

SpringEL 을 통해서 데이터를 가져와 사용이 가능 함.  
테스트를 위해 아래 객체를 만들어준다.
```java
@Data
static class User{
    private String username;
    private int age;

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }
}

@GetMapping("/variable")
public String variable(Model model) {
    User userA = new User("userA", 10);
    User userB = new User("userB", 20);

    List<User> list = new ArrayList<>();
    list.add(userA);
    list.add(userB);

    Map<String, User> map = new HashMap<>();
    map.put("userA", userA);
    map.put("userB", userB);

    model.addAttribute("user", userA);
    model.addAttribute("users", list);
    model.addAttribute("userMap", map);

    return "basic/variable";
}
```
* 생성한 User 객체를 각각 Object, list, map 형식으로 모델에 담는다.
* 아래 html 에서 담긴 데이터들을 불러온다.

```html
<h1>SpringEL 표현식</h1>
<ul>Object
    <li>${user.username} = <span th:text="${user.username}"></span></li>
    <li>${user['username']} = <span th:text="${user['username']}"></span></li>
    <li>${user.getUsername()} = <span th:text="${user.getUsername()}"></span></li>
</ul>
<ul>List
    <li>${users[0].username} = <span th:text="${users[0].username}"></span></li>
    <li>${users[0]['username']} = <span th:text="${users[0]['username']}"></span></li>
    <li>${users[0].getUsername()} = <span th:text="${users[0].getUsername()}"></span></li>
</ul>
<ul>Map
    <li>${userMap['userA'].username} = <span th:text="${userMap['userA'].username}"></span></li>
    <li>${userMap['userA']['username']} = <span th:text="${userMap['userA']['username']}"></span></li>
    <li>${userMap['userA'].getUsername()} = <span th:text="${userMap['userA'].getUsername()}"></span></li>
</ul>
```
* 데이터를 가져오는 방식은 총 3가지다.
    * .필드명 으로 가져오기
    * []에 '필드명'을 넣어서 가져오기
    * get 메서드를 사용하여 가져오기.
* 리스트나 맵형식도 가져오는 방식은 대동소이하다.

가져온 데이터를 변수에 넣어 사용하는 법은 아래와 같다.
```html
<h1>지역 변수 - (th:with)</h1>
<div th:with="first=${users[0]}">
<!--  지역 변수는 선언한 태그 안에서만 사용 가능.  -->
    <p>처음 사람의 이름은 <span th:text="${first.username}"></span></p>
</div>
```
with 속성을 이용하여 '변수=데이터' 로 세팅해주면 해당 태그 안에서는 변수를 통해 데이터 접근이 가능하다.
