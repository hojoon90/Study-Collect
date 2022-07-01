# Bridge 패턴

### Bridge 패턴이란
Bridge 패턴은 단어 뜻대로 두 개 사이를 연결해주는 패턴이다. Bridge패턴이 다리역할을 하는 두 곳은 **기능의 클래스 계층**과 **구현의 클래스 계층**이다.\
일단 기능의 클래스 계층과 구현의 클래스 계층이 어떤것인지 부터 알아보자.

### 기능의 클래스 계층과 구현의 클래스 계층

우리가 새로운 기능을 추가하는 경우를 생각해보자. 기존에 Temp라는 클래스가 존재하는데 여기에 새로운 기능을 추가하고 싶을 때 우리는 Temp의 하위 클래스로 TempAdd 라는 클래스를 만든다.\
간단한 표로 보면 아래와 같다.
* Temp
    * TempAdd

이 TempAdd는 기능을 추가하기 위해 만든 계층이다. Temp클래스를 상속받게 되는 것이다.
>상위클래스는 기본적인 기능을 가지고 있다.\
>하위 클래스에서 새로운 기능을 추가한다.

새로운 기능을 추가하고 싶을 경우 클래스 계층안에서 자신이 원하는 기능과 가장 유사한 클래스를 찾아서 하위 클래스를 만든 후, 목적한 기능을 추가한 새로운 클래스를 만드는 것이 **기능의 클래스 계층**이다. \
간단하게 코드로 보면 아래와 같다.
```java
public class Temp {  // 상위클래스
    public void showName(){
        System.out.println("이름을 나타냅니다.");
    }

    public void showPhoneNum(){
        System.out.println("전화번호를 나타냅니다.");
    }
}
```
```java
public class TempAdd extends Temp{  // 하위클래스
    public void showAdress(){
        System.out.println("주소를 나타냅니다.");
    }

    public void showAllData(){
        showName();
        showPhoneNum();
        showAdress();
    }
}
```
\
이번엔 구현의 클래스계층에 대해 알아보자. 구현의 클래스는 우리가 앞에서부터 많이 봐왔던 추상클래스를 떠올리면 쉽다.\
추상 클래스가 일련의 메소드들을 추상메소드로 선언하면 이 추상 클래스를 상속받는 클래스들은 추상 메소드들을 구현해야 한다.\
추상 클래스는 상위 클래스로서 하위 클래스에서 구현해야 할 메소드들에 대해 명세를 하는 역할을 하며, 하위 클래스는 그것을 구현하는 역할을 한다.\
역시 표로 보면 다음과 같다.
* AbstractClass
    * ExtendClass

간단하게 정리하면
> 상위 클래스는 추상메소드에 의해 인터페이스를 규정한다.\
> 하위 클래스는 상위 클래스에서 정의된 인터페이스(추상 메소드)를 구현한다.

이러한 클래스 계층을 **구현의 클래스 계층** 이라고 한다. 위의 기능의 클래스 계층 처럼 클래스 안의 클래스 형태가 아닌 추상 클래스를 기준으로
명세된 추상 메소드들을 해당 클래스를 상속 받는 각 클래스에서 구현해주는 것이다. 아래와 같은 모양이다.
* AbstractClass
    * ExtendClass
    * AnotherExtendClass

### 기능의 클래스 계층과 구현의 클래스 계층을 연결하는 Bridge

위에서 이야기한 이 두개의 클래스 계층이 하나의 계층구조안에 같이 있다고 생각해보자. 이렇게 되면 두개의 계층이 서로 혼잡하게 섞여있게 될 것이다.\
이는 기능 추가 혹은 구현할 때 어떤 계층에 만들어야 할지 모르게 된다. 그래서 이를 막기 위해 기능의 클래스 계층과 구현의 클래스를 서로 분리해준다.\
이 분리된 계층들을 서로 연결하는 것이 바로 Bridge 패턴이다. 아래 예제 코드를 보자.

### 기능의 클래스 계층 코드
```java
public class Display {
    private DisplayImpl impl;
    public Display(DisplayImpl impl){
        this.impl = impl;
    }
    public void open(){
        impl.rawOpen();
    }
    public void print(){
        impl.rawPrint();
    }
    public void close(){
        impl.rawClose();
    }
    public final void display(){
        open();
        print();
        close();
    }
}
```
위 클래스는 기능의 클래스 계층의 상위 클래스인 Display 클래스이다. 여기서 주목할 점은 impl 필드이다. Display 생성 시 DisplayImpl을
인자로 받아 필드에 세팅해준다. 아래에 나오겠지만 DisplayImpl은 추상클래스로서, 구현의 클래스 계층의 상위 클래스이다.\
메소드들은 간단하게 설명하면 open은 출력 전처리, print는 출력, close는 출력 후처리이다. 모두 DisplayImpl 의 메소드들을 호출하는 것을
볼 수 있다. 실제 싱행되는 것은 DisplayImpl를 상속받아 구현하는 클래스의 메소드일 것이다.

```java
public class CountDisplay extends Display{
    public CountDisplay(DisplayImpl impl){
        super(impl);
    }
    public void multiDisplay(int times){
        open();
        for (int i = 0; i < times; i++){
            print();
        }
        close();
    }
}
```
Display의 하위 클래스인 CountDisplay 이다. Display에 구현된 기능에 출력횟수를 정하는 기능인 multiDisplay 메소드가 추가되어 있다.
여기까지가 기능의 클래스 계층이다.

### 구현의 클래스 계층
```java
public abstract class DisplayImpl {
    public abstract void rawOpen();
    public abstract void rawPrint();
    public abstract void rawClose();
}
```
DisplayImpl 은 구현의 클래스 계층 중 상위 클래스에 해당한다. 이 클래스는 기능의 클래스 계층과 구현의 클래스 계층을 연결해주는 Bridge 역할을 해준다.\
추상클래스로 이루어져 있으며, 해당 클래스를 상속받는 하위 클래스에서 추상 메소드들을 구현해주어야 한다.

```java
public class StringDisplayImpl extends DisplayImpl {

    private String string;
    private int width;

    public StringDisplayImpl(String string){
        this.string = string;
        this.width = string.getBytes().length;
    }

    @Override
    public void rawOpen(){
        printLine();
    }

    @Override
    public void rawPrint(){
        System.out.println("|" + string + "|");
    }

    @Override
    public void rawClose(){
        printLine();
    }

    private void printLine(){
        System.out.print("+");
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }
}
```
StringDisplayImpl 은 DisplayImpl 을 상속 받는 하위 클래스로서 DisplayImpl 안에 있는 추상 메소드들이 구현된 것을 확인할 수 있다.\
이 두 개의 메소드가 바로 구현의 클래스 계층에 해당한다.

### 실행 코드
다음은 위에서 만든 클래스들을 조합하여 실행 할 Main 클래스를 작성해준다.
```java
public class Main {
    public static void main(String[] args){
        
        Display d1 = new Display(new StringDisplayImpl("Hello, Korea!"));
        Display d2 = new CountDisplay(new StringDisplayImpl("Hello, World!"));
        
        CountDisplay d3 = new CountDisplay(new StringDisplayImpl("Hello, Universe!"));
        
        d1.display();
        d2.display();
        d3.display();
        
        d3.multiDisplay(5);
    }
}
```

변수 d1 은 Display, 변수 d2, d3는 CountDisplay 인스턴스를 생성하고 있다. 3개 변수 모두 인스턴스를 생성할 때 DisplayImpl의 구현체인
StringDisplayImpl 을 생성하는 것을 볼 수 있다. d1, d2, d3는 모두 display 메소드의 호출이 가능하며, d3 의 경우 Display의 기능뿐만 아니라
추가된 기능인 multiDisplay 메소드도 호출이 가능해진다.\
위 클래스의 출력결과는 아래와 같다.
> +-------------+\
> |Hello, Korea!|\
> +-------------+\
> +-------------+\
> |Hello, World!|\
> +-------------+\
> +----------------+\
> |Hello, Universe!|\
> +----------------+\
> +----------------+\
> |Hello, Universe!|\
> |Hello, Universe!|\
> |Hello, Universe!|\
> |Hello, Universe!|\
> |Hello, Universe!|\
> +----------------+\

### Bridge 패턴의 고려 사항들

Bridge 패턴의 가장 큰 특징은 '기능의 클래스 계층'과 '구현의 클래스 계층'을 분리한 것이다. 이렇게 분리하게 되면 각 계층 별로 독립적인 확장이 가능하다.\
만약 기능을 추가하고 싶다면 기능의 클래스 계층에 클래스를 추가하면 된다. 이때 구현의 클래스 계층엔 수정할 사항이 전혀 없게 된다. 또한 새로 추가한 기능은
'모든 구현'에서 이용이 가능하다. \
위의 Main 코드 중 d2와 d3를 보자. d3의 경우 기능이 추가된 클래스인데 인스턴스 생성 시 구현 클래스인 StringDisplayImpl 을 생성하는 것을 볼 수 있다.\
지금은 구현 클래스가 StringDisplayImpl밖에 없지만, 구현 클래스가 몇개 더라도 StringDisplayImpl 위치에 다른 구현클래스가 오더라도 기능을 사용하는 데는
아무런 문제가 되지 않는다. 즉, '모든 구현'에서 이용이 가능해지는 것이다.\
\
위에서는 상속과 위임이 동시에 쓰였다. 상속은 클래스 확장을 위해서는 편리한 방법이지만 클래스간의 연결이 강하게 이루어진다.\
Display와 CountDisplay를 보면 알기 쉬운데, CountDisplay는 Display 클래스의 하위 클래스이다. 그리고 이렇게 연결 된 클래스는 매우 견고하게
연결되어 클래스간의 관계 수정이 쉽지가 않게 된다.\
만약 소스 코드 변경 없이 클래스간의 관계를 바꾸고 싶을 때는 상속이 아닌 **위임**을 사용해야 한다. 다시 Display 클래스를 보자.\
Display 클래스 생성 시 impl 필드에 DisplayImpl 클래스를 구현하는 구현 인스턴스가 저장되어 있는데, 아래 메소드들에서 다음과 같이 호출을 한다.
* open 실행 시 impl.rawOepn()을 호출한다.
* print 실행 시 impl.rawPrint()을 호출한다.
* close 실행 시 impl.rawClose()을 호출한다.

이렇게 직접 처리가 아닌 impl안에 있는 메소드들을 호출하게끔 '떠넘기기'를 사용하고 있는데, 이걸 바로 위임이라고 한다. 위에서 보면 알겠지만 
실제 실행은 생성자로 만들어지는 **DisplayImpl의 구현체 클래스안에 있는** rawOepn(), rawPrint(), rawClose()이 실행 되는 것이다.\
실제 다른 메소드를 호출하고 싶다면 DisplayImpl의 구현체 클래스 부분을 변경해주면 되는 것이다. 만약 StrindDisplayImpl 가 아닌 다른 
구현체 클래스의 출력 결과로 변경하고 싶을 경우, Main 클래스 안의 StrindDisplayImpl 를 사용하고 싶은 클래스로 변경만 해주면 된다.