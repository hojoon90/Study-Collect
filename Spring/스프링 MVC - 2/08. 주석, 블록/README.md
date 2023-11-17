# 주석 / 블록

## 주석

타임리프 주석은 html 주석과는 별개로 동작한다.

```html
<h1> HTML 주석</h1>
<!-- <span th:text="${data}">html data</span> -->

<h1> 타임리프 파서 주석 </h1> 
<!--/* [[${data}]] */-->
<!--/*-->
  <span th:text="${data}">html data</span>
<!--*/-->

<h1> 타임리프 프로토타입 주석</h1>
<!--/*/
<span th:text="${data}">html data</span> 
/*/-->
```
랜더링 되었을 때, html 주석의 경우 타임리프는 별도 처리를 하지 않고 그대로 주석상태로 놔둔다.  
타임리프 파서 주석의 경우 랜더링 될 때 주석안에 있는 태그나 데이터는 모두 무시하고 랜더링한다.  
타임리프 프로토타입 주석의 경우 html 상에서는 보이지만, 타임리프로 랜더링되면 해당 태그는 삭제된다.

## 블록

타임리프 내 유일한 자체 태그이다. 

```html
<th:block th:each="user : ${users}">
    <div>
        사용자 이름1<spanth:text="${user.username}"></span>
        사용자 나이1<spanth:text="${user.age}"></span> 
    </div>
    <div>
        요약 <span th:text="${user.username} + ' / ' + ${user.age}"></span>
    </div>
</th:block>
```
보통 블록단위별로 반복이나 조건을 걸고 싶을 때, block 태그를 이용하면 된다.  
위와 같은 경우는 블록안에 있는 태그들이 반복되면서 html 에 그려지게 된다.
