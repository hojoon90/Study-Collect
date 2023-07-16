# HTTP 응답 - HTTP API, 메세지 바디

API 제공 시엔 메세지 바디에 데이터를 담아서 전달한다.

## responseBodyV1
```java
@Slf4j
@RestController //@ResponseBody, @Controller를 모두 포함.
public class ResponseBodyController {

    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }
}
```
* HttpServletResponse에 "ok"를 메세지 바디에 직접 써서 전달.

## responseBodyV2
```java
    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2(HttpServletResponse response) throws IOException {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
```
* ResponseEntity 로 응답값 전달. HTTP 메서드 상태 전달 가능

## responseBodyV3
```java
    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3(){
        return "ok";
    }
```
* @ResponseBody 사용 시 HTTP 메세지 컨버터를 이용하여 바로 HTTP 메세지 직접 입력.

## responseBodyJsonV1
```java
    @GetMapping("response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }
```
* ResponseEntity 반환. HTTP 컨버터를 통해 JSON으로 데이터 변환 후 리턴.

## responseBodyJsonV2
```java
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("response-body-json-v2")
    public HelloData responseBodyJsonV2() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);

        return helloData;
    }
```
* @ResponseBody 사용 시 HTTP 응답 코드값 사용이 까다로워 짐.
* @ResponseStatus 를 통해 응답 코드값 제공이 가능.
* 하자만 동적 변경 혹은 커스텀이 안되므로 동적 변경, 커스텀 시엔 ResponseEntity 사용

## @RestController
@RestController 사용 시 해당 컨트롤러들에 @ResponseBody가 적용. view 처리가 동작하지 않는다.\
@RestController 안에 @ResponseBody가 사용됨.

