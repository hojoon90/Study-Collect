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
* 인라인을 사용하지 않은 경우 