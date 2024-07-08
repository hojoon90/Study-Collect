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
public class User{
    private Long userId;
    private String accountId;
    private String name;
    private String email;
    
    public User(){
        
    }
    
    public Long getUserId(){
        return userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }
    
    public String getEmail(){
        return email;
    }
}
```
유저 정보를 제공해주는 API를 이용해 유저 객체를 만들어 보려 한다. 제공되는 JSON 은 아래와 같다.

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

https://interconnection.tistory.com/137  
https://www.baeldung.com/jackson-object-mapper-tutorial  