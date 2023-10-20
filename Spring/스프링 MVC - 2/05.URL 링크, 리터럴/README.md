# URL 링크, 리터럴

## URL 링크
URL 링크를 만드는 법은 생각보다 간단하다.  
먼저 아래 테스트를 위한 컨트롤러 생성
```java
@GetMapping("link")
public String link(Model model){
    model.addAttribute("param1", "data1");
    model.addAttribute("param2", "data2");

    return "basic/link";
}
```
그리고 리턴 경로와 같은 위치에 html 생성  
```html
<h1>URL 링크</h1>
<ul>
    <li><a th:href="@{/hello}">basic url</a></li>
    <li><a th:href="@{/hello(param1=${param1}, param2=${param2})}">hello query param</a></li>
    <li><a th:href="@{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}">path variable</a></li>
    <li><a th:href="@{/hello/{param1}(param1=${param1}, param2=${param2})}">path variable + query parameter</a></li>
</ul>
```
* 첫번쨰는 기본적인 URL 링크 만드는 방법. href 앞에 th: 을 붙여준다.
* 두번째는 쿼리파라미터로 데이터를 보낼 때 사용. url 뒤에 괄호를 만들고, 그 안에 파라미터 키와 값을 입력
* PathVariable 로 파라미터 url 세팅
* PathVariable과 쿼리 파라미터를 동시에 사용

## 리터럴
리터럴은 우리가 값으로 사용하는 것들을 말한다. 아래와 같다.  
* String s = "Hello";
* int a = 10 * 20;

여기서 Hello 는 문자 리터럴, 10, 20은 숫자 리터럴이다. 타임리프는 4개의 리터럴이 존재 함.
* 문자: 'hello'
* 숫자: 10
* 불린: true, false
* null: null

타임리프에서 문자열 사용 시 주의할 점은 띄어쓰기가 존재할 경우 '' 로 감싸 주어야 함.  
띄어쓰기가 없을땐 생략해도 무방하다.
```java
@GetMapping("literal")
public String literal(Model model){
    model.addAttribute("data", "Spring!");
    return "basic/literal";
}
```
테스트 할 html 생성
```html
<h1>리터럴</h1>
<ul>
    <!--주의! 다음 주석을 풀면 예외가 발생함-->
    <!--    <li>"hello world!" = <span th:text="hello world!"></span></li>-->
    <li>'hello' + ' world!' = <span th:text="'hello' + ' world!'"></span></li>
    <li>'hello world!' = <span th:text="'hello world!'"></span></li>
    <li>'hello ' + ${data} = <span th:text="'hello ' + ${data}"></span></li>
    <li>리터럴 대체 |hello ${data}| = <span th:text="|hello ${data}|"></span></li>
</ul>
```
* 첫번쨰는 + 를 통해 문자를 조합하여 사용 가능
* 두번째는 띄어쓰기가 존재하므로 ''로 문자를 묶음
* 세번쨰는 SpringEL 을 통해 문자를 치환
* 마지막 리터럴 대체를 통해 문자를 사용하면 템플릿을 사용하는것 처럼 편하다.