# HTTP 요청 - @ModelAttribute

요청 파라미터로 넘어온 데이터를 객체로 만들어지도록 자동화해주는 애노테이션.

먼저 요청 파라미터를 받아 바인딩 할 객체 생성
```java
@Data
public class HelloData {
    private String username;
    private int age;
}
```

## ModelAttribute V1

```java
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData){
//    public String modelAttributeV1(@RequestParam String username, @RequestParam int age){
//        HelloData helloData = new HelloData();
//        helloData.setUsername(username);
//        helloData.setAge(age);
//
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
//        log.info("helloData = {}", helloData);

        return "ok";
    }
```
기존에 @RequestParam을 이용하던 것 대신 파라미터에 객체를 넣고 @ModelAttribute 애노테이션 사용.

@ModelAttribute가 있으면 다음과 같이 동작
* HelloData 객체 생성
* 요청 파라미터 이름으로 객체 프로퍼티를 찾음. 
* 프로퍼티 존재 시 setter를 호출하여 값을 세팅.

### 프로퍼티
객체에 만약 getUsername(), setUsername() 메소드가 있으면 해당 객체에 username이라는 프로퍼티가 있음.

## @ModelAttribute V2
@ModelAttribute는 생략이 가능함.
```java
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData){
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
```
스프링에서는 @RequstParam 혹은 @ModelAttribute를 생략 할 경우 다음 규칙을 따름.
* String, int, long 등의 단순 타입일 경우 @RequestParam 적용
* 그 외는 @ModelAttribute 적용