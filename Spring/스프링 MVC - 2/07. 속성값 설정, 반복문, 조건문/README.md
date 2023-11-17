# 속성값 설정 / 반복문 / 조건부 평가

## 속성 값 설정

타임리프는 th:* 를 이용하여 속성값을 설정할 수 있다. 기존 속성이 있으면 th가 적용된 속성으로 변경된다.

```html
<!--소스 상-->
<input type="text" name="mock" th:name="userA" />
<!--실제 html 속성-->
<input type="text" name="userA" /> 
```
name="mock"이 name="userA" 로 변경됨.

속성 추가는 다음 3가지 방법이 있다.
* th:attrappend : 속성 값의 뒤에 값을 추가한다. 
* th:attrprepend : 속성 값의 앞에 값을 추가한다. 
* th:classappend : class 속성에 자연스럽게 추가한다.

속성 중 checked 는 html 에서는 무조건 체크박스에 체크표시가 되는 속성이다. (true/false 값에 상관하지 않음.)  
타임리프는 th:checked를 통해 값이 false일때 checked 속성을 삭제하고, true면 노출 시켜준다.

## 반복문

타임리프에서 반복문은 th:each 를 사용. 자바에서 Iteratable, Enumeration을 구현한 모든 객체에 사용 가능.  
Map.Entry 도 반복문을 사용 가능하다.

```html
<tr th:each="user : ${users}">
    <td th:text="${user.username}">username</td>
    <td th:text="${user.age}">0</td>
</tr>
<tr th:each="user, userStat : ${users}">
    <td th:text="${userStat.count}">username</td>
    <td th:text="${user.username}">username</td>
    <td th:text="${user.age}">0</td>
</tr>
```
* 오른쪽의 ${users} 의 값을 user로 가져와서 사용한다.  
* 아래 반복문은 반복 상태를 확인할 수 있게 사용하는 문법
* 두번째 파라미터(userStat)의 경우 생략 가능. 생략 시엔 지정한 변수명(user) + Stat 으로 사용 가능.

반복 상태 유지 기능은 아래와 같음.
* index : 0부터 시작하는 값 
* count : 1부터 시작하는 값 
* size : 전체 사이즈 
* even , odd : 홀수, 짝수 여부( boolean ) 
* first , last :처음, 마지막 여부( boolean ) 
* current : 현재 객체

## 조건부 평가

타임리프 조건식은 다음과 같다.
* if
* unless(if의 반대)
* switch

```html
<tr th:each="user, userStat : ${users}">
    <td th:text="${userStat.count}">1</td>
    <td th:text="${user.username}">username</td>
    <td>
        <span th:text="${user.age}">0</span>
        <span th:text="'미성년자'" th:if="${user.age lt 20}"></span> 
        <span th:text="'미성년자'" th:unless="${user.age ge 20}"></span>
    </td> 
</tr>
```

타임리프에서는 조건에 맞지 않을 경우 렌더링하지 않음. (태그가 아예 없어진다.)  
lt 와 ge 는 각각 less than(<), great equal(>=) 을 나타냄.

```html
<tr th:each="user, userStat : ${users}">
    <td th:text="${userStat.count}">1</td>
    <td th:text="${user.username}">username</td>
    <td th:switch="${user.age}">
        <span th:case="10">10살</span> 
        <span th:case="20">20살</span> 
        <span th:case="*">기타</span>
    </td> 
</tr>
```
switch 문 사용법은 크게 다르지 않으며, * 일 경우 디폴트값으로 처리