# 속성값 설정

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
