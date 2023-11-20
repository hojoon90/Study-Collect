# 템플릿 조각

웹 페이지 개발 시 공통영역에 대해 처리할 때 사용한다. 이 조각들을 이용하여 반복되는 코드들을 줄일 수 있다.

```html
<body>
<footer th:fragment="copy">
    푸터 자리
</footer>

<footer th:fragment="copyParam (param1, param2)">
    <p>파라미터 자리</p>
    <p th:text="${param1}"></p>
    <p th:text="${param2}"></p>
</footer>
</body>
```
fragment 는 다른 곳에 포함 될 코드조각이라고 생각한다.
```html
<body>
<h1>부분 포함</h1>
<h2>부분 포함 insert</h2>
<div th:insert="~{template/fragment/footer :: copy}"></div>

<h2>부분 포함 replace</h2>
<div th:replace="~{template/fragment/footer :: copy}"></div>

<h2>부분 포함 단순 표현식</h2>
<div th:replace="template/fragment/footer :: copy"></div>

<h1>파라미터 사용</h1>
<div th:replace="~{template/fragment/footer :: copyParam ('데이터1', '데이터2')}"></div>

</body>


<!--결과-->
<body>
<h1>부분 포함</h1>
<h2>부분 포함 insert</h2>
<div>
<footer>
    푸터 자리
</footer>
</div>

<h2>부분 포함 replace</h2>
<footer>
    푸터 자리
</footer>

<h2>부분 포함 단순 표현식</h2>
<footer>
    푸터 자리
</footer>

<h1>파라미터 사용</h1>
<footer>
    <p>파라미터 자리</p>
    <p>데이터 1</p>
    <p>데이터 2</p>
</footer>

</body>
```
사용 방법은 아래와 같다.
* insert: 말그대로 현재 태그에 값을 삽입한다. 현재의 div 태그에 copy라는 fragment가 들아간다.
* replace: 현재 태그값을 대체한다. div 태그는 빠지고 footer로 변경된다.
* 단순 표현식: {} 생략이 가능하다.
* 파라미터 사용: 파라미터를 이용해 동적으로 사용할 수 있다.

