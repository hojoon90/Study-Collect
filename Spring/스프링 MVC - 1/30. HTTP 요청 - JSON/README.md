# HTTP 요청 - JSON

JSON 형식은 HTTP API 에서 주로 사용한다.

## requestBodyJsonV1

```java
@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBocy = {}", inputStream);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }
}
```
* HttpServletRequest 를 이용하여 메세지 바디 데이터를 읽어와 문자로 변환.
* 문자로 변환된 messageBody를 ObjectMapper를 이용하여 객체 변환하여 처리.

## requestBodyJsonV2
```java
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBocy = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }
```
* @RequestBody를 이용하여 메세지 바디를 꺼내옴
* 꺼내온 메세지 바디를 ObjectMapper로 변환

## requestBodyJsonV3
```java
    /**
     * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter 가 동작.
     * @param data
     * @return
     */
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData data) {
        /*
            스프링은 @RequestParam, @ModelAttribute 생략 시 다음과 같이 동작
                - 단순 타입 = @RequestParam
                - 그 외 = @ModelAttribute
            즉, @RequestBody 생략 시 @ModelAttribute 가 적용됨. -> Http 메세지 바디가 실행되지 않음.
         */
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }

```
* @RequestBody 와 변환할 객체를 바로 파리미터에 넣어서 변환
* 단순하게 data.getUsername(), data.getAge()를 통해 데이터를 바로 꺼내올 수 있음.
* @RequestBody 생략 시 @ModelAttribute로 처리되므로 Http 메세지 바디가 아닌 요청 파라미터를 처리하도록 실행 됨.

## requestBodyJsonV4
```java
    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
        HelloData helloData = httpEntity.getBody();
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
```
* HttpEntity를 이용하여 처리할 수도 있음.

## requestBodyJsonV5
```java
    /**
     * 메세지 바디 정보 직접 변환 (view 조회 x)
     * @param data
     * @return
     */
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return data;
    }
```
* 반환값에 객체를 넣어 메세지 바디를 바로 반환함.