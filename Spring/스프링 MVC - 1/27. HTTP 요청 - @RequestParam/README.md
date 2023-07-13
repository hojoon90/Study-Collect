# HTTP 요청 - @RequestParam

스프링이 제공하는 @RequestParam 을 통해 요청 파라미터를 편리하게 사용 가능

## RequestParam V2

```java
    /**
     * @RequestParam으로 파라미터 처리
     * @param memberName
     * @param memberAge
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge){
        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }
```

기본적인 @RequestParam 사용법. 파라미터의 key를 괄호안에 넣어준다.\
@RequestBody를 사용하면 View 조회 없이 바로 리턴값을 Http 메세지 바디에 전달한다.

## RequestParam V3
변수명과 파라미터의 Key 이름이 동일하다면 괄호 작성(name)을 생략해도 된다.
```java
    /**
     * 변수명과 파라미터명이 같으면 name값 생략 가능
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }
```
파라미터에 username, age로 전달 시 변수명과 동일하다면 정상적으로 값이 들어온다.

## RequestParam V4
파라미터가 단순 타입인 경우 @RequestParam 생략 가능
```java
    /**
     * 단순 타입의 경우 @RequestParam 생략 가능
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

```

## 파라미터 필수 여부
@RequestParam 에 필수 여부값을 세팅해줄 수 있음. 필수값이 true일 경우 호출 시 해당 파라미터 key가 없으면 400 에러
```java
    /**
     * required 값으로 필수로 받을 파라미터 정의 가능
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age){

        log.info("username={}, age={}", username, age);
        return "ok";
    }
```
파라미터 이름만 있을 경우 빈값으로 값이 넘어오게 된다.

## 기본 값 적용
파라미터가 안넘어 왔거나, 값이 안넘어왔을 경우 기본값을 세팅해줄 수 있음.
```java
 /**
     * 파라미터의 값이 없을 경우 defaultValue를 통해 기본값 세팅 가능
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age){

        log.info("username={}, age={}", username, age);
        return "ok";
    }

```
defaultValue를 통해 값이 없을 때 기본값으로 넣어줄 내용을 세팅해줌.

## 파라미터 Map으로 조회
파라미터를 Map으로 받아서 처리 가능
```java
    /**
     * @RequestParam Map, MultiValueMap
     * MultiValueMap 은 동일한 파라미터 값이 2개 이상 들어올 경우 사용. (하지만 거의 사용하지 않음)
     * @param paramMap
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap){

        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }
```