# Prototype 패턴

### Prototype 패턴이란
인스턴스 생성 시 보통은 new '클래스 이름()' 과 같이 클래스의 이름을 지정하여 인스턴스를 생성한다.\
하지만 클래스 이름을 지정하지 않고 인스턴스를 생성할 경우도 있다. 클래스로부터 인스턴스를 생성하는 것이 아닌,\
인스턴스로부터 별도의 인스턴스를 만드는 패턴을 우리는 'Prototype 패턴'이라고 이야기 한다.\
이렇게 사용하기 위해선 보통 '모형' 혹은 '틀'이 되는 인스턴스를 만들어 둔 후, 이 틀을 복사하여 새로운 인스턴스를 생성하는 방법이다.\
이 틀을 우리는 보통 framework 라고 한다. 아래 예제 코드로 한번 살펴보자.

### framework
```java
public interface Product extends Cloneable{
    public abstract void use(String s);
    public abstract Product createClone();
}
```
```java
public class Manager{
    private HashMap showcase = new HashMap();
    public void register(String name, Product proto){
        showcase.put(name, proto);
    }
    public Product create(String protoname){
        Product p = (Product) showcase.get(protoname);
        return p.createClone();
    }
}
```
위의 두 개의 코드는 framework를 하는 클래스와 인터페이스 이다.\
먼저 Product 인터페이스는 실제 구현할 클래스들의 인터페이스로, Cloneable 인터페이스를 상속하고 있다. Cloneable 인터페이스를 구현하고있는 클래스의 인스턴스는
clone 메소드를 사용하여 인스턴스를 복제할 수 있다. \
use 는 하위 클래스에서 구현할 메소드로서 무엇을 할지는 하위에서 구체적으로 구현될 것이다.\
\
Manager 클래스는 Product 인스턴스를 이용하여 복제를 실행한다(create() 부분). 여기서 register 는 Product 를 등록하는 메소드로\
등록시에 Product를 proto라는 변수에 담아 인자로 받는데 이 proto는 실제 클래스가 어떤건지는 모르지만, Product 인터페이스를 구현한 클래스라는 것은 알 수 있다.\
\
위 두개의 클래스와 인터페이스는 실제 구현한 클래스들의 이름은 전혀 나오지 않는다. 즉 실제 구현된 클래스들과는 **독립적인 관계**라는 것이며,\
수정 역시 독립적으로 이루어질 수 있다는 것을 의미한다. Manager클래스를 잘 보면 Product 만 사용될 뿐, 실제 구현된 클래스 이름들은 존재하지 않는다.\
Product 인터페이스가 Manager와 실제 구현된 클래스들의 다리 역할을 하고 있는 것이다.

### 구현체 부분
```java
public class MessageBox implements Product {
    private char decochar;
    public MessageBox(char decochar){
        this.decochar = decochar;
    }
    public void use(String s){
        int length = s.getBytes().length;
        for (int i = 0; i < length + 4; i++) {
            System.out.println(decochar);
        }
        System.out.println(" ");
        System.out.println(decochar + " " + s + " " + decochar);
        for (int i = 0; i < length + 4; i++) {
            System.out.println(decochar);
        }
        System.out.println(" ");
    }
    public Product createClone(){
        Product p = null;
        try {
            p = (Product)clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return p;
    }
}
```
```java
public class UnderlinePen implements Product {
    private char ulchar;
    public UnderlinePen(char ulchar){
        this.ulchar = ulchar;
    }
    public void use(String s){
        int length = s.getBytes().length;
        System.out.println("\"" + s + "\"");
        System.out.println(" ");
        for (int i = 0; i < length; i++) {
            System.out.println(ulchar);
        }
        System.out.println(" ");
    }
    public Product createClone(){
        Product p = null;
        try {
            p = (Product)clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return p;
    }
}
```
위의 두개 클래스들은 Product를 실제 구현한 구현체 클래스들이다.\
MessageBox 클래스의 decochar 변수는 문자열을 감싸는 문자로, use()를 사용하면 인자로 받아온 문자열에다가 클래스 생성 시 세팅한 문자가 문자열 주변을 둘러싸게 된다.\
createClone()은 자기자신을 복제하는 메소드이다. 메소드 안에 clone()이 있는데, clone()으로 복사가 가능한 것은 Cloneable 인터페이스를 구현하고 있는 클래스 뿐이다.\
여기서는 Product 인터페이스가 Cloneable을 상속받고 있으므로, clone()앞에 Product 인터페이스를 캐스팅 해주었다.\
또한 clone()은 자신의 클래스에서만 호출이 가능하기 때문에 다른 클래스에서 복제 요청을 하는 경우는 중간다리 역할을 하는 메소드를 하나 만들어서 그 곳에서 호출 해 주어야 한다.\
UnderlinePen 클래스는 MessageBox 클래스와 거의 동일하며, 단지 문자열 아래에 세팅된 문자가 그려진다는 것만 다르다.\
\
그럼 실제 Main클래스를 통해 코드를 실행해보자.
```java
public class Main{
    public static void main(String[] args){
        Manager manager = new Manager();
        UnderlinePen upen = new UnderlinePen('-');
        MessageBox mbox = new MessageBox('*');
        MessageBox sbox = new MessageBox('/');
        manager.register("strong message", upen);
        manager.register("warning box", mbox);
        manager.register("slash box", sbox);
        
        Product p1 = manager.create("strong message");
        p1.use("Hello World");
        Product p2 = manager.create("warning box");
        p2.use("Hello World");
        Product p3 = manager.create("slash box");
        p3.use("Hello World");
    }
}
```
먼저 Manager 클래스의 인스턴스를 생성해주고, 구현체 클래스들을 등록해준다. \
그 후 manager.create 메소드를 이용해 복제된 인스턴스를 Product 인터페이스의 변수들에 넣고 use 메소드를 호출하여 실제 결과를 출력해준다.\
출력 결과는 다음과 같다.
> "Hello World" \
> ----------- \
> *************** \
> &#42; Hello World * \
> *************** \
> /////////////// \
> / Hello World / \
> ///////////////

### 왜 Prototype이 필요할까?
Prototype이 필요한 경우는 다음과 같다.
1. 종류가 너무 많아서 클래스로 정리할 수 없는 경우 \
    위 예제 프로그램에는 3개의 모형이 등장하였다. 모형이 많이 없을 경우는 상관없지만, 많은 종류의 모형이 생겨날 경우 모든 모형을 각각의 클래스로\
    만들게 되면 클래스가 너무 많아지게 된다. 이렇게 되면 소스 코드의 관리가 힘들어지게 된다.
2. framework와 생성하는 인스턴스를 분리하고 싶은 경우 \
    위의 코드에서는 framework 에서 인스턴스의 복사를 실행하는 부분을 설정하고 있다. Manager의 create()에서 등록된 문자열을 불러와서 인스턴스를 생성하고 있다.\
    이로 인해 직접적인 클래스 이름을 넣지 않고 인스턴스 생성을 하므로 framework 와 구현클래스간의 분리가 가능하게 된다.
3. 클래스로부터 인스턴스 생성이 어려운 경우 \
    사용자가 조작해서 만든 도형을 나타내는 인스턴스 같은 것을 만들 때 인스턴스를 복사해서 만드는 방법이 간단

2번 내용에서 조금 더 추가해보자면, 클래스이름을 쓰는게 나쁘진 않지만 클래스 이름이 쓰여져 있게 된다면 부품처럼 코드를 재활용할 수 있는 폭이 많이 줄어들게 된다.\
서로 긴밀하게 결합해야 하는 소스 안에 클래스 이름이 있는 것은 문제가 없지만, 부품처럼 사용하기 위한 소스에서는 독립성을 위해 클래스 이름을 쓰는것은 지양해야 한다.