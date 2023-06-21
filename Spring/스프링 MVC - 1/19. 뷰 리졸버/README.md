# 뷰 리졸버

스프링에서 뷰 처리 부분인 뷰 리졸버에 대해 알아본다.

OldController에 View를 사용할 수 있도록 ModelAndView 를 리턴
```java
@Component("/springmvc/old-controller")
public class OldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldCOntroller.handleRequest");
        return new ModelAndView("new-form");
    }
}
```
새로운 ModelAndView 객체를 리턴하면서 논리 이름으로 'new-form'을 추가.\
서버 구동 후 호출 시 동작은 하지만 페이지는 에러페이지가 나타남. 논리 이름만 넘겨주기 때문에 해당 논리 이름에 대한 전체 경로가 없어서 에러 발생.\
application.properties에 다음과 같이 추가후 재실행 하면 정상 동작함.
```properties
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp

```

기존 v5 구조 처럼 ModelAndView 객체를 리턴하면 뷰 리졸버에서 뷰 화면을 리턴.\
뷰 리졸버 역시 스프링에서 자동으로 몇가지를 등록해서 사용함.
* BeanNameViewResolver
* ViewResolverComposite 
* InternalResourceViewResolver 
* ContentNegotiatingViewResolver

#### 핸들러 어댑터에서 논리 이름 획득
* HandlerAdapter를 통해 논리 이름 (new-form)을 획득한다.

#### "new-form" 으로 뷰 리졸버 조회
* "new-form"이라는 이름으로 뷰 리졸버들을 위 순서대로 호출한다.
* 여기선 InternalResourceViewResolver가 호출 됨.

#### InternalResourceViewResolver 실행
* InternalResourceViewResolver 는 InternalResourceView 클래스를 반환함.
* InternalResourceView 클래스 안에는 아래와 같이 forward()를 해주는 메소드가 존재함.
* 이를 통해 JSP를 실행한다.
```java
...
		else {
			// Note: The forwarded resource is supposed to determine the content type itself.
			if (logger.isDebugEnabled()) {
				logger.debug("Forwarding to [" + getUrl() + "]");
			}
			rd.forward(request, response);
		}
...
```