# ObjectMapper

Json 데이터를 객체에 매핑할 때 ObjectMapper를 많이 사용한다. 기존에 ObjectMapper를 통해 객체를 매핑할때는
객체에 아래와 같은 애노테이션들을 추가해주었다.
```java
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sample{
    
    private String sample1;
    private String sample2;
    private String sample3;
    private String sample4;
    //...
    
}
```
위와 같이 작성해 줄 경우 API 에서 응답하는 Json 데이터를 정상적으로 매핑하게 된다.  
하지만 매핑이 어떻게 이루어지는지 정확하게 알고 사용하지 않고 있어서 이번 기회에 ObjectMapper가 어떤 방식으로 동작하는지 확인하려 한다.