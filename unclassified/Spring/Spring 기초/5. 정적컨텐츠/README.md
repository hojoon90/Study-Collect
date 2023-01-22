# 정적컨텐츠

웹 개발엔 3가지 방법이 있음
* 정적 컨텐츠 - 파일을 웹 브라우저에 그대로 보여줌
* MVC와 템플릿 엔진 - HTML을 서버쪽에서 동적으로 처리하여 보여주는 방식. 정적 컨텐츠와는 달리
서버에서 변형이 들어감.
* API - JSON 데이터 구조 포멧으로 클라이언트에 데이터를 전달해줌.

### 정적컨텐츠
https://docs.spring.io/spring-boot/docs/current/reference/html/web.html#web.servlet.spring-mvc.static-content

스프링은 static 디렉토리에 있는 정적 컨텐츠를 제공해준다. static 디렉터리에 아래 파일을 만들어준다.
```html
<!--hello-static.html-->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>hello static</title>
</head>
<body>
정적 컨텐츠.
</body>
</html>
```
그 후 localhost:8080/hello-static.html을 호출 하면 아래와 같이 페이지가 나타나게 된다.
<img src="images/hello-static.png" width="80%" height="80%"/>

동작 원리는 다음과 같다.
1. 클라이언트가 localhost:8080/hello-static.html 을 호출
2. Spring은 먼저 컨트롤러에서 위 엔드포인트가 매핑되어있는지 확인.
3. 컨트롤러에 매핑된게 없으면 resources:static/hello-static.html을 찾음
4. 해당 파일이 있으면 그 파일 페이지를 리턴헤준다.