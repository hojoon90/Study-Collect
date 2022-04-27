#Adaptor 패턴

### Adaptor 패턴이란?
Adaptor 패턴(이하 '어뎁터 패턴'으로 통일)은 간단히 표현하면 이미 제공되고 있는 것을 그대로 사용할 수 없을 때 필요한 형태로 바꿔주는 패턴이다.\
우리가 흔히 "돼지코"라고 부르는 어뎁터를 생각하면 편하다.\
어뎁터 패턴은 다른말로는 Wrapper 패턴이라고 불리기도 한다.\
무언가를 포장해서 다른 용도로 사용할 수 있게 해준다고 하여 이런 이름으로도 불린다.\
어뎁터 패턴은 아래와 같이 두가지 종류가 있다.

* 상속을 사용한 어뎁터 패턴
* 위임을 사용한 어뎁터 패턴

코드를 통해 두가지 종류에 대해 살펴보자.

### 상속을 사용한 어뎁터 패턴
상속을 사용한 어뎁터 패턴은 말 그대로 **기존에 있는 클래스를 상속하여 새로운 클래스를 구현**하는 방법이다.
예제 코드를 보자.
```java
public class Banner {
    private String text;
    public Banner(String text) {
        this.text = text;
    }

    public void showWithParen(){
        System.out.println("("+this.text+")");
    }
    public void showWithAster(){
        System.out.println("*"+this.text+"*");
    }
}
```
Banner 클래스는 기존에 제공되고 있던 클래스로, 단어 앞뒤에 괄호를 붙여주는 showWithParen()과 앞뒤에 별표를 붙여주는 showWithAster()를 갖고 있다.\
이 클래스를 상속하여 새로운 클래스를 만들어보자.
아래는 새롭게 만들 클래스의 인터페이스이다.
```java
public interface Print {
    public abstract void printWeak();
    public abstract void printStrong();
}
```
Print 인터페이스에는 단어 앞뒤에 괄호를 붙여 글씨를 강조하지 않게 해줄 printWeak()와 단어 앞뒤에 별표를 붙여 글씨를 강조시켜줄 printStrong()이 있다.\
우리가 진행 할 것은 위의 Banner클래스를 **상속**하여 Print 인터페이스를 **구현**할 구현체를 만들 것이다.
실제로 구현된 코드는 아래와 같다.
```java
public class PrintBanner extends Banner implements Print {
    public PrintBanner(String string){
        super(string);
    }
    public void printWeak(){
        showWithParen();
    }
    public void printStrong(){
        showWithAster();
    }
}
```
클래스를 실행 할 Main 클래스도 만들어준다.
```java
public class Main{
    public static void main(String[] args){
        Print p = new PrintBanner('Hello');
        p.printWeak();
        p.printStrong();
    }
}
```
클래스를 실행하면 다음과 같이 출력된다.
```shell
(Hello)
*Hello*
```
여기서 중요한건 main 메소드에서 사용한 건 **Print 인터페이스**라는 것이다. 우리가 사용한것은 단지 어뎁터 역할을 하는 PrintBanner클래스의\
printWeak()와 printStrong()밖에 없다. 기존에 존재하는 Banner 클래스는 건드리지도 않았으며,\
PrintBanner라는 어뎁터를 이용하여 우리가 원하는 기능을 사용하게 되었다.\
또한 Main에서는 PrintBanner의 메소드만 호출할 뿐, 안의 구현은 어떻게 되고 있는지 모르므로, Main의 수정 없이 PrintBanner의 구현변경도 가능하다.

### 위임을 사용한 어뎁터 패턴