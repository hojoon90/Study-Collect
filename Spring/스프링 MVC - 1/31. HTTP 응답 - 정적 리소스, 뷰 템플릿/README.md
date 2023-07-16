# HTTP 응답 - 정적 리소스, 뷰 템플릿

스프링에서 응답 데이터를 제공하는 방식은 세가지이다.

* 정적 리소스
  * 정적 HTML, css, js 를 제공할 때 정적 리소스 사용
* 뷰 템플릿
  * 동적 HTML 제공 시 뷰 템플릿 사용
* HTTP 메세지 사용
  * HTTP API 제공 시 데이터를 전달해야 하므로 JSON등의 형식으로 메세지 바디에 데이터 전달

## 정적 리소스
파일을 변경 없이 그대로 서비스 하는 것. 스프링은 정적 리소스를 제공할 수 있는 경로가 별도로 존재.
* /static
* /public
* /resources
* /META-INF/resources

/src/main/resources 는 리소스를 보관하는 곳이며 클래스 패스의 시작점.\
/src/main/resources/static 이곳에 리소스를 넣어두면 스프링 부트가 정적 리소스로 데이터 제공

* 파일 등록 경로: /src/main/resources/static/basic/hello.html
* URL 호출 경로: http://localhost:8080/basic/hello.html

## 뷰 템플릿

뷰 템플릿을 거쳐 HTML이 생성되고, 뷰가 응답을 만들어서 전달.\
기본 뷰 템플릿 경로는 아래와 같음
* src/main/resources/templates

뷰 템플릿을 생성해줌.
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<p th:text="${data}">empty</p>
</body>
</html>
```
뷰 템플릿을 호출 할 컨트롤러 생성
```java
@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1(){
        ModelAndView mv = new ModelAndView("response/hello")
                .addObject("data", "hello");

        return mv;
    }

    /**
     * ResponseBody 애노테이션이 없으면 'response/hello'로 뷰 리졸버가 실행되어 뷰를 찾고 렌더링.
     * @param model
     * @return
     */
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model){
        model.addAttribute("data", "hello");
        return "response/hello";
    }

    /**
     * void 를 반환하는 경우
     *  - @Controller를 사용
     *  - HTTP 메세지 바디 처리 파라미터가 없으면
     *  요청 URL을 참고하여 논리뷰 이름으로 사용.
     *  명시성이 많이 떨어져서 권장하지 않음.
     * @param model
     */
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model){
        model.addAttribute("data", "hello");
    }
}
```
### response-view-v1
* ModelAndView에 경로와 보여줄 데이터를 입력한 후 리턴 해주면 해당 경로의 html에 데이터를 보여줌.

### response-view-v2
* 리턴값이 String일 때 @ResponseBody가 없으면 "response/hello"로 뷰 리졸버가 실행되어 뷰를 찾고 렌더링.

### /response/hello
* void 리턴 시 
  * @Controller를 사용하고
  * 메세지 바디를 처리하는 객체가 없으면
* URL을 참고하여 논리뷰 이름으로 사용한다.
