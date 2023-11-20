# 템플릿 레이아웃

코드 조각을 레이아웃에 넘겨서 사용하는 방식이다. 아래 코드를 보자

```html
<!--template/layout/base.html-->
<html xmlns:th="http://www.thymeleaf.org">
<head th:fragment="common_header(title,links)"> 
    <title th:replace="${title}">레이아웃 타이틀</title>
    <!-- 공통 -->
    <link rel="stylesheet" type="text/css" media="all" th:href="@{/css/awesomeapp.css}">
    <link rel="shortcut icon" th:href="@{/images/favicon.ico}">
    <script type="text/javascript" th:src="@{/sh/scripts/codebase.js}"></script>
    <!-- 추가 -->
    <th:block th:replace="${links}" />
</head>

<!--template/layout/layoutMain.html-->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="template/layout/base :: common_header(~{::title},~{::link})">
    <title>메인 타이틀</title>
    <link rel="stylesheet" th:href="@{/css/bootstrap.min.css}">
    <link rel="stylesheet" th:href="@{/themes/smoothness/jquery-ui.css}">
</head>
<body>
메인 컨텐츠
</body>
</html>
```
* 아래 layoutMain html 에서 head 태그를 template/layout/base의 코드조각으로 사용하려 한다.
* 이때 파라미터로 title 태그와 link 태그를 조각쪽으로 넘긴다.
* 조각에서 넘어온 태그들을 파라미터 받는곳에서 교체해준다.
* 교체되어 만들어진 최종 html로 head 태그가 교체된다.
* 아래와 같은 결과로 나온다.

```html
<!--template/layout/layoutMain.html-->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head> 
    <title>메인 타이틀</title>
    <!-- 공통 -->
    <link rel="stylesheet" type="text/css" media="all" href="/css/awesomeapp.css">
    <link rel="shortcut icon" href="/images/favicon.ico">
    <script type="text/javascript" src="/sh/scripts/codebase.js"></script>
    
    <!-- 추가 -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/themes/smoothness/jquery-ui.css">
</head>
<body>
메인 컨텐츠
</body>
</html>
```

위와 같이 사용하는 방법은 코드 조각을 좀 더 적극적으로 사용하는 방식이다.   
베이스가 되는 html에 태그들을 넘겨서 동적으로 만들어진 html을 사용한다고 생각하면 쉽다.

위에서는 head 태그 한정으로 적용했지만, html 전체로도 적용할 수 있다.
```html
<!--template/layoutExtend/layoutFile.html-->
<!DOCTYPE html>
<html th:fragment="layout (title, content)" xmlns:th="http://www.thymeleaf.org">
<head>
    <title th:replace="${title}">레이아웃 타이틀</title> 
</head>
<body>
<h1>레이아웃 H1</h1>
<div th:replace="${content}">
    <p>레이아웃 컨텐츠</p>
</div>
<footer>
    레이아웃 푸터
</footer>
</body>
</html>


<!--template/layoutExtend/layoutExtendMain.html-->
<!DOCTYPE html>
<html th:replace="~{template/layoutExtend/layoutFile :: layout(~{::title}, ~{::section})}"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <title>메인 페이지 타이틀</title> 
</head>
<body>
<section>
    <p>메인 페이지 컨텐츠</p>
    <div>메인 페이지 포함 내용</div>
</section>
</body>
</html>
```
* html 태그를 layoutFile.html 로 바꾸려고 한다.
* 이 때, 타이틀과 section 태그를 넘겨준다.
* layoutFile.html 에서 파라미터로 받아온 코드 조각을 변경해준다.
* 변경된 코드를 다시 layoutExtendMain.html 로 넘긴다.
* 아래와 같은 결과로 나오게 된다.

```html
<!--template/layoutExtend/layoutExtendMain.html-->
<!DOCTYPE html>
<html>
<head>
    <title>메인 페이지 타이틀</title> 
</head>
<body>
<h1>레이아웃 H1</h1>
<section>
    <p>메인 페이지 컨텐츠</p>
    <div>메인 페이지 포함 내용</div>
</section>
<footer>
    레이아웃 푸터
</footer>
</body>
</html>
```

파라미터로 받는 부분이 변경된 것을 볼 수 있다.