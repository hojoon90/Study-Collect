# Abstract Factory 패턴

### Abstract Factory 패턴이란
먼저 **추상적**이라는 단어에 대해 정확하게 생각하고 가자. 추상적이라는 단어는 객체지향에서는 구체적인 구현은 생각하지 않고,\
**인터페이스만을 생각하는 상태**를 말한다. 즉, '어떻게 할 것이다~' 라는 내용만 정의하여 큰 밑그림을 그리는 것과 비슷하다고 볼 수 있다.\
이는 앞에서 보았던 Builder 패턴과 Template Method 패턴에서도 나타난다.\
Abstract Factory 패턴은 말 그대로 추상적으로 모든것을 만드는 패턴이라고 보면 된다.\
부품의 구체적인 구현은 신경쓰지 않고, 인터페이스만 신경써서 만든다. 그리고 이 인터페이스만을 사용하여 부품을 조립하고 제품을 만든다.\
Abstract Factory 패턴 역시 Template Method, Builder 와 같이 구체적인 구현은 하위 클래스들에서 이루어진다.\
예제 코드를 보면서 확인해보자.

### 예제 코드와 패키지 구성
이번 예제에서는 계층구조를 가진 링크페이지를 html페이지로 만드는 예제이다.\
이번 예제는 클래스들이 조금 많기 때문에 먼저 필요한 클래스들에 대해 한번 정리 후 코드 작성을 진행한다.

> * Main.java
> * factory
>   * Factory.java
>   * Item.java
>   * Link.java
>   * Tray.java
>   * Page.java
> * listFactory
>   * ListFactory.java
>   * ListLink.java
>   * ListTray.java
>   * ListPage.java

위의 factory 패키지들은 모두 추상메소드들로 이루어져 있고, 구체적인 구현들은 아래 listFactory의 패키지에서 진행된다.\
먼저 factory 패키지 부터 살펴보자.

### 추상 메소드들로 이루어진 factory 패키지

```java
package example.asf.factory;

public abstract class Item {
    protected String caption;
    public Item(String caption){
        this.caption = caption;
    }
    public abstract String makeHtml();
}
```

