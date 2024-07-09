# ObjectMapper

Json 데이터를 객체에 매핑할 때 많이 사용하는 객체가 바로 Jackson ObjectMapper(이하 ObjectMapper)다.  
ObjectMapper 를 사용하는 가장 큰 이유는 JSON 데이터를 객체로 손쉽게 매핑해주기 때문인데, JSON을 객체에 매핑하거나 객체를 JSON으로 변환할 때
일련의 과정을 거쳐 데이터 변환이 이루어 진다. 이 글에서는 사용 방법과 동작 원리에 대해 간단히 정리 해보려 한다.

## ObjectMapper 의존성 추가
의존성은 아래와 같은 방법으로 추가할 수 있다.

### Maven
pom.xml 안에 아래와 같이 추가.
```xml
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>${jackson.version}</version>
  </dependency>
```
### Gradle
build.gradle 안에 아래와 같이 추가.
```groovy
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.12.3'
```

## 사용법
간단한 예시로 확인해보자.
```java
@Getter
@NoArgsConstructor
public class User{
    private Long userId;
    private String accountId;
    private String name;
    private String email;
}
```
유저 정보를 제공해주는 API를 이용해 유저 객체를 만들어 보려 한다. 객체는 Getter와 빈 생성자만 만들도록 해주었다.  
제공되는 JSON 은 아래와 같다.
```json
{
  "userId": 21,
  "accountId": "sirlewis123",
  "userName": "Lewis Hamilton",
  "email": "sirlewis123@test.com",
  "phone": "010-0123-4567",
  "gender": "M"
}

```
위와 같이 API 를 통해 응답값이 오면 대개 String 형태로 반환이 된다. 반환된 응답값은 다음과 같이 객체로 변환해주면 된다.  
여기서는 API를 통해 json 데이터를 가져왔다고 가정하고 response값을 스태틱으로 만들었다.
```java
String response ="{\"userId\": 21,\"accountId\": \"sirlewis123\", \"userName\": \"Lewis Hamilton\", \"email\": \"sirlewis123@test.com\", \"phone\": \"010-0123-4567\", \"gender\": \"M\"}";
ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 객체에 없는 변수 무시

//Json String 데이터를 객체로 변환
User user; 
try{
    //readValue는 JsonProcessingException, JsonMappingException 예외를 던지므로, 해당 예외의 최상위 예외인 IOException으로 잡아준다.
    user = objectMapper.readValue(content, User.class);
} catch (IOException ie){
    log.error("IOException", ie);
}
```
이렇게 하면 JSON String 으로 받아온 데이터를 객체로 손쉽게 변환할 수 있다.  
반대의 경우도 가능하다.
```java
@Setter
public static class Address{
    private int zipCode;
    private String address;
    private String addressDetail;
}

public static void main(String[] args){
    ObjectMapper objectMapper = new ObjectMapper();

    //Json String 데이터를 객체로 변환
    Address address = new Address();
    address.setZipCode(12345);
    address.setAddress("서울특별시 중구 세종대로 110");
    address.setAddressDetail("서울시청");
    
    try{
        //readValue는 JsonProcessingException, JsonMappingException 예외를 던지므로, 해당 예외의 최상위 예외인 IOException으로 잡아준다.
        String jsonData = objectMapper.writeValueAsString(address);
    } catch (IOException ie){
        log.error("IOException", ie);
    }
}

```
이렇게 하면 Address 객체는 다음과 같은 String 형태의 JSON 데이터가 된다.
```json
{
  "zipCode": 12345,
  "address": "서울특별시 중구 세종대로 110",
  "addressDetail": "서울 시청"
} 
```
위와 같이 JSON -> 객체로 변환하는 것을 역직렬화(Deserialize), 객체 -> JSON 으로 변환시키는 것을 직렬화(Serialize)라고 한다.

objectMapper 객체를 생성할 때 설정을 하나 넣어주었는데, 역직렬화 시 JSON에는 존재하지만 객체에 없는 경우 해당 필드를 무시하는 옵션을 같이 추가해주었다.  
이렇게 해주지 않으면 역직렬화 과정에서 ObjectMapper가 존재하지 않는 값을 처리하려다가 예외를 발생시키게 된다.  
여기서 한가지 신기한 점은 역직렬화 시 객체에 setter나 변수를 받는 생성자가 존재하지 않는데, 객체에 알아서 매핑되어 생성된다는 것이다.  
ObjectMapper 가 동작하는 원리는 아래와 같다.

## 동작 원리



https://interconnection.tistory.com/137  
https://www.baeldung.com/jackson-object-mapper-tutorial  