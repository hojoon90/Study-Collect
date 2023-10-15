# 02. 텍스트 - text,utext

기본적인 텍스트 출력하는 기능.  
타임리프는 기본적으로 HTML 태스의 속성에 기능을 정의하여 동작함.

기본 출력 방법은 아래와 같음.
```java
@GetMapping("text-basic")
public String textBasic(Model model){
    model.addAttribute("data", "Hello Spring!");
    return "basic/text-basic";
}
```
* model 에 data 로 "Hello Spring!" 을 담아준 후 html 경로를 리턴.
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<h1>컨텐츠에 데이터 출력하기</h1>
<ul>
    <li>th:text 사용 <span th:text="${data}"></span></li>
    <li>컨텐츠 안에 직접 출력하기 = [[${data}]]</li>
</ul>

</body>
</html>
```
* html 태그에 thymeleaf 를 사용하기 위한 설정 추가
* th:text 를 이용하여 모델에 담아두었던 데이터를 출력.
* 또는 [[]] 를 이용하여 데이터를 컨텐츠 안에서 직접 출력.

**text vs unescaped**
만약 데이터에 태그가 달려있을 경우는 unescaped 처리를 해주어야 함.
```java
@GetMapping("text-unescaped")
public String textUnescaped(Model model){
    model.addAttribute("data", "Hello <b>Spring!</b>");
    return "basic/text-unescaped";
}
```

```html
<h1>text vs unescaped</h1>
<ul>
    <li>th:text = <span th:text="${data}"></span></li>
    <li>th:utext = <span th:utext="${data}"></span></li>
</ul>

<h1><span th:inline="none">[[...]] vs [(...)]</span></h1>
<ul>
    <li><span th:inline="none">[[...]] = </span>[[${data}]]</li>
    <li><span th:inline="none">[(...)] = </span>[(${data})]</li>
</ul>
```

출력 결과는 아래와 같다.
```
text vs unescaped
  th:text = Hello <b>Spring!</b>
  th:utext = Hello Spring!
[[...]] vs [(...)]
  [[...]] = Hello <b>Spring!</b>
  [(...)] = Hello Spring!
```