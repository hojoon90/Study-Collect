# HTTP 요청 - 기본, 헤더 조회

스프링에서는 다양한 파라미터를 지원함.

HTTP 헤더를 조회 하는 방법은 아래 코드와 같다.
```java
@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale,
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCookie", required = false) String cookie
                          ) {
        log.info("request = {}", request);
        log.info("response = {)", response);
        log.info("httpMethod = {)", httpMethod);
        log.info("locale = {)", locale);
        log.info("headerMap = {)", headerMap);
        log.info("host = {)", host);
        log.info("cookie = {)", cookie);

        return "ok";
    }
}
```

* HttpServletRequest
  * Http 요청에 대한 내용 조회
* HttpServletResponse
  * Http 응답에 대한 내용을 담아서 전달할 때 사용
* HttpMethod
  * Http 메서드 조회
* locale
  * Locale 정보 조회
* @RequestHeader MultiValueMap<String, String> headerMap
  * 모든 Http Header 를 MultiValueMap 형식으로 조회. 
  * MultiValueMap은 하나의 key 에 여러 value 처리 가능
* @CookieValue(value = "myCookie", required = false) String cookie
  * 특정 쿠키 조회
  * value: 쿠키의 key
  * required: 필수 여부