# 자바스크립트 인라인

자바스크립트에서 타임리프를 편리하게 사용할 수 있게 해주는 기능이다.

```html
<!-- 자바스크립트 인라인 사용 전 --> 
<script>
    var username = [[${user.username}]];
    var age = [[${user.age}]];
    
    //자바스크립트 내추럴 템플릿
    var username2 = /*[[${user.username}]]*/ "test username";
    
    //객체
    var user = [[${user}]];
</script>

<!-- 자바스크립트 인라인 사용 후 --> 
<script th:inline="javascript">
    var username = [[${user.username}]];
    var age = [[${user.age}]];
    
    //자바스크립트 내추럴 템플릿
    var username2 = /*[[${user.username}]]*/ "test username";
    
    //객체
    var user = [[${user}]];
</script>

<!-- 사용 전 결과 -->
<script>
    var username = userA;
    var age = 10;
    
    //자바스크립트 내추럴 템플릿
    var username2 = /*userA*/ "test username";
    
    //객체
    var user = BasicController.User(username=userA, age=10);
</script>

<!-- 사용 후 결과 -->
<script>
    var username = "userA";
    var age = 10;
    
    //자바스크립트 내추럴 템플릿
    var username2 = "userA";
    
    //객체
    var user = {"username":"userA","age":10};
</script>

```
인라인을 사용하지 않은 경우
* 데이터를 자동으로 변환해주지 않는다 (userA 참고. 변수로 인식함.)
* 인라인을 사용하지 않은 경우 내추럴 템플릿 기능이 동작하지 않음. 원래 방식 대로 해석.
  * 내추럴 템플릿 -> HTML 파일을 직접 열어도 타임리프가 동작하는 기능. 주석을 사용하여 기능을 사용할 수 있음.
* 객체가 toString 방식으로 노출되었다.

인라인을 사용한 경우
* 데이터를 적절한 반환값으로 변환하여 노출 시켜준다.
* 내추럴 템플릿 기능이 동작하여 userA 라는 값을 반환해준다.
* JSON 형태로 객체를 변환해준다.

## 인라인 each
자바스크립트에서 인라인 each는 다음과 같이 사용 가능.
```html
<script th:inline="javascript">
    [# th:each"user, stat : ${users}"]
    var user[[${stat.count}]] = [[${user}]];
    [/]
</script>
```
문법은 기존 each와 거의 유사하며, 표현식이 조금 다름.

결과는 아래와 같다.
```html
<script>
    var user1 = {"username":"userA","age":10};
    var user2 = {"username":"userB","age":20};
    var user3 = {"username":"userC","age":30};
</script>
```
