# 요청 매핑 핸들러 어댑터 구조

HTTP 메세지 컨버터는 @RequestMapping 핸들러에서 처리할 떄 같이 처리 된다.(RequestMappingHandlerAdapter)

## ArgumentResolver
* 애노테이션 기반 컨트롤러는 다양한 파라미터를 받음.
* 이런 파라미터들을 처리해주는 것이 ArgumentResolver.
* RequestMappingHandlerAdaptor 동작 시 ArgumentResolver 호출.
* ArgumentResolver 가 호출 되면서 컨트롤러가 필요로 하는 파라미터 값들을 생성.
* 생성된 파라미터를 갖고 핸들러 어댑터가 컨트롤러를 호출한다.

ArguementResolver 안에는 supportsParameter() 메소드가 존재. 이 메소드를 통해 파라미터 지원 여부를 확인할 수 있다.\
실제 지원이 가능하면 resolveArgument() 메소드가 처리.

## ReturnValueHandler
* ArgumentResolver와 비슷.
* 응답값을 변환하여 처리.
* ModelAndView, @ResponseBody, HttpEntity 등을 처리

## HTTP 메세지 컨버터
HTTP 메세지 컨버터는 ArgumentResolver, ReturnValueHandler 가 사용한다.
* ArgumentResolver의 경우
  * 파라미터 생성 시 받아온 파라미터들을 변경해주어야 함.
  * 이 때 HTTP 메세지 컨버터를 사용하여 필요한 객체를 생성
  * @RequestBody, HttpEntity ArgumentReslover들이 호출
* ReturnValueHandler의 경우
  * 만들어진 응답을 변경해주어야 함.
  * @ResponseBody, HttpEntity 를 처리하는 ReturnValueHandler가 메세지 컨버터 호출.

* @RequestBody @ResponseBody 
  * RequestResponseBodyMethodProcessor (ArgumentResolver)를 사용.
* HttpEntity 
  * HttpEntityMethodProcessor (ArgumentResolver)를 사용.