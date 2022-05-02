# Adaptor 패턴

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
        Print p = new PrintBanner("hello");
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
다음은 위임을 사용한 어뎁터 패턴이다. 여기서의 위임은 어떤 메소드의 실제 처리를 다른 인스턴스의 메소드에 맡기는 것을 의미한다.\
예제는 1번의 상속과 동일하지만, Print가 인터페이스가 아닌 클래스라고 가정한다.\
이렇게 되면 PrintBanner 클래스의 수정이 필요한데, JAVA의 경우 2개의 클래스를 한꺼번에 상속할 수 없기 때문이다.\
우리는 Print클래스를 이용하여 우리가 원하는 기능을 수행할 것이기 때문에 Print와 PrintBanner클래스를 아래와 같이 수정해준다.
```java
public abstract class Print {
    public abstract void printWeak();
    public abstract void printStrong();
}

public class PrintBanner extends Print {
    private Banner banner;
    public PrintBanner(String string){
        this.banner = new Banner(string);
    }
    public void printWeak(){
        banner.showWithParen();
    }
    public void printStrong(){
        banner.showWithAster();
    }
}
```
PrintBanner는 Banner클래스를 갖는 banner 변수가 있으며, 생성자를 통해서 생성된다. 그리고 printWeak()와 printStrong()은 각각\
banner변수를 통해 showWithParen()과 showWithAster() 를 호출한다. 즉, banner변수에게 기능을 **위임**하고 있는 것이다.

### 어떨때 사용하는가
어뎁터 패턴을 사용하는 경우는 앞에서 이야기한 것 처럼 기존에 존재하는 기능을 다시 사용하시 위해서이다.\
좀 더 자세하게 이야기하면 이미 존재하고 있는 클래스가 실제로 많이 사용하고, 그 과정을 거치면서 충분한 테스트가 이루어져 안정화 되어있는 클래스라면, 위험성을 조금이나마 더 줄이기 위해 그 클래스를 사용할 것이다.\
장점들을 3가지 정도로 정리해보자면,
1. 어텝터 패턴을 이용하여 기존에 있는 메소드를 사용하여 개발하므로 빠른 개발진행이 가능하다.
2. 버그가 생기더라도 어뎁터 패턴을 위해 사용하는 메소드는 안정화 되어있는 메소드이므로, 그 부분을 제외한 나머지 부분만 디버깅해보면 된다.
3. 유지보수 업무에서 어뎁터 패턴을 사용하여 구버전메소드를 사용하여 버전업을 진행할 수 있으므로 호환성 이슈에서 조금이나마 편해질 수 있다.
