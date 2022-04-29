# Template Method 패턴

### Templte Method 패턴이란
쉽게 이야기해 템플릿의 기능을 가진 패턴이다. (여기서 템플릿이란, 어떤 모양이 새겨져 있는 플라스틱 판을 의미한다.)\
상위 클래스에서 템플릿들에 해당하는 메소드가 정의되어 있고, 그 메소드의 정의 안에는 추상 메소드가 선언되어 있다.\
그렇기 때문에 템플릿 메소드의 안을 보면 추상메소드가 어떻게 사용되고 있는지는 알 수 있지만, 구체적인 구현과 처리는 어떻게 되는지 알 수 없다.\
아래 예제 코드를 보자
```java
public abstract class AbstractDisplay{
    public abstract void open();
    public abstract void print();
    public abstract void close();
    public final void display(){
        open();
        for (int i = 0; i < 5; i++){
            print();
        }
        close();
    }
}
```
맨 마지막에 있는 display()를 주목해보자. 메소드 안에 추상메소드인 open(), print(), close()가 사용되고 있다.\
하지만 저 추상메소드들이 어떻게 사용되고 있는진 알지만, 구체적으로 어떻게 구현되어 있는지는 알지 못한다.\
즉 display()는 템플릿 메소드라고 할 수 있다.

### 그렇다면 추상 메소드들의 구현은 어디에?
추상 메소드들은 어디서 구현이 되는걸까? 바로 하위 클래스에서 메소드 구현이 일어난다.\
만약 서로 다른 하위 클래스에서 서로 다른 구현이 일어난다면 서로 다른 처리가 이루어지게 될 것이다.\
하지만 하위클래스들에서 어떤 구현이 일어나더라도 큰 틀, 즉 템플릿 메소드에서 정의한 흐름은 그대로 따라가게 될 것이다.\
아래 두개의 클래스를 살펴보자.
```java
public class CharDisplay extends AbstractDisplay {
    private char ch;

    public CharDisplay(char ch) {
        this.ch = ch;
    }

    @Override
    public void open() {
        System.out.print("<<");
    }

    @Override
    public void print() {
        System.out.print(ch);
    }

    @Override
    public void close() {
        System.out.println(">>");
    }
}
```
```java
public class StringDisplay extends AbstractDisplay {

    private String string;
    private int length;
    public StringDisplay(String string) {
        this.string = string;
        this.length = string.getBytes().length;
    }

    @Override
    public void open() {
        printLine();
    }

    @Override
    public void print() {
        System.out.println("|"+string+"|");
    }

    @Override
    public void close() {
        printLine();
    }

    private void printLine(){
        System.out.print("+");
        for(int i = 0; i<length; i++){
            System.out.print("-");
        }
        System.out.println("+");
    }
}
```
위에 작성한 클래스는 모두 AbstractDisplay 클래스를 상속받는 클래스들이다.\
각각의 클래스 안에서 구현이 모두 다른 것을 볼 수 있다. 이제 이 두개의 클래스를 실행하는 Main클래스를 만들어 보겠다.\

### 큰 흐름은 템플릿 메소드의 흐름대로
아래는 Main 클래스이다.
```java
public class Main {
    public static void main(String[] args){
        AbstractDisplay d1 = new CharDisplay('H');
        AbstractDisplay d2 = new StringDisplay("Hello, World");

        d1.display();
        d2.display();
    }
}
```
먼저 d1.display는 다음 흐름대로 출력이 된다.
> 1. CharDisplay클래스의 open()이 실행된다. '<<' 가 출력된다.
> 2. print()가 for문이 실행되면서 5번 호출된다. 호출 될 때마다 생성자에서 세팅된 글자인 'H'가 5번 출력된다.
> 3. close()가 실행된다. '>>'가 출력된다.
> 4. 출력 결과는 다음과 같다.\
> \<\<HHHHH\>\> 

다음 d2.display를 살펴보자.
> 1. StringDisplay클래스의 open()이 실행된다. printLine()이 실행되고 '+------------+'가 출력된다.
> 2. print()가 for문이 실행되면서 5번 호출된다. 호출 될 때마다 생성자에서 세팅된 글자 양옆에 |이 붙은 '|Hello, World|'가 5번 출력된다.
> 3. close()가 실행된다. printLine()이 실행되고 '+------------+'가 출력된다.
> 4. 출력 결과는 다음과 같다\
> +------------+\
> |Hello, World|\
> |Hello, World|\
> |Hello, World|\
> |Hello, World|\
> |Hello, World|\
> +------------+

위의 d1 과 d2의 1,2,3번을 유심히 보자. 두 메소드 모두 open() -> print()*5 -> close() 순서로 흐름이 진행 된 것을 볼 수 있다.\
위에서 설명했듯이 각 클래스의 구현은 다르지만 모두 템플릿 메소드에서 정의한 흐름대로 흐름이 이어지고 있다.\
구현은 하위클래스에서 하되 흐름은 템플릿 메소드에서 정의한다. 이것이 템플릿 메소드의 가장 큰 특징이다.

### 어떤점이 좋은가?
템플릿 메소드의 가장 좋은 점은 하위 클래스에서 일일히 로직을 구현할 필요가 없다는 것이다. 템플릿 메소드에서 공통화된 로직이 구현되어 있고,\
하위 클래스에서는 추상 메소드들에 대해서만 구현을 해주면 되기 때문이다. 그렇기 때문에 개발 후 로직에 버그가 생기더라도\
상위 클래스의 템플릿 메소드에서만 수정해주면 되기 때문에 유지보수에서도 조금 더 쉬워지게 된다.\

