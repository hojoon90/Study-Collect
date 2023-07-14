# HTTP 요청 - 단순 텍스트

Http 메세지 바디에 데이터를 직접 담아서 요청하는 경우는 다음과 같다.
* HTTP API 에서 주로 사용함 - JSON, XML, TEXT
* 주로 JSON을 많이 사용
* POST, PUT, PATCH 메서드 사용

바디에 데이터가 직접 넘어오는 경우는 앞서 배운 방식들과 다르게 처리해야 함.

## Request Body String V1
```java
@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        response.getWriter().write("ok");
    }
}
```
기본적으로 메세지 바디는 InputStream을 통해서 읽어올 수 있음. 

## Request Body String V2
```java
    @PostMapping("request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody = {}", messageBody);
        responseWriter.write("ok");
    }
```
파라미터에 바로 InputStream을 받아서 처리할 수도 있음. 스프링 MVC는 다음 파라미터를 지원 함
* InputStream(Reader): 조회
* OutputStream(Writer): 출력

## Request Body String V3
```java
    @PostMapping("request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
        String messageBody = httpEntity.getBody();
        log.info("messageBody = {}", messageBody);

        return new HttpEntity<>("ok");
    }
```
HttpEntity를 사용하여 바디값을 읽어올 수 있음
* HttpEntity: header, body 정보를 편하게 조회.
* 요청 파라미터 조회 기능과 관련이 없다.
* 응답에도 사용 가능하며, 사용 시엔 view 조회를 하지 않는다.
* ResponseEntity, RequestEntity 도 같은 기능 제공.
  * 이 중 ResponseEntity는 HTTP 상태코드 설정 가능.

## Request Body String V4
```java
    @ResponseBody
    @PostMapping("request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) throws IOException {
        log.info("messageBody = {}", messageBody);

        return "ok";
    }
```
@RequestBody 를 이용하면 간단하게 메세지 바디값을 읽어올 수 있음. 헤더 정보 필요시엔 HttpEntity 혹은 @RequestHeader 사용.

## 정리
* 요청 파라미터 조회 시: @RequestParam, @ModelAttribute 사용
* HTTP 메세지 바디 조회 시: @RequestBody 사용

@ResponseBody는 응답 결과를 바디에 넣어서 전달한다. 이 경우에도 view 는 사용하지 않음. 
