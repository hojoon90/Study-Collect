# Decorator 패턴

### Decorator 패턴이란
Decorator 패턴은 베이스가 되는 오브젝트가 있고, 이 오브젝트를 알맞는 용도대로 꾸미는 패턴을 Decorator 패턴이라고 한다. 간단한 예시로 케이크를 생각해보자.\
케이크의 베이스는 스펀지 케이크다. 이 스펀지 케이크에 생크림을 바르면 생크림 케이크가 되며, 초코무스를 바르면 초코 케이크가 된다. 이렇게 목적에 맞게 오브젝트에\
장식이 되는 기능들을 하나씩 붙여나가면 Decorator 패턴이 된다. 예제코드로 알아보자.

### 예제코드
여기서 만들 예제 코드는 문자열 주변에 장식을 만들어 표시하는 것이다. Main 클래스를 제외한 총 5가지의 클래스를 만들게 된다.

```java
public abstract class Display {
    public abstract int getColumns();
    public abstract int getRows();
    public abstract String getRowText(int row);
    public final void show(){
        for (int i = 0; i < getRows(); i++){
            System.out.println(getRowText(i));
        }
    }
}
```
Display 클래스는 추상클래스로서 복수행으로 구성되는 문자열을 출력한다. getColumns 와 getRows 메소드는 각 가로와 세로의 문자수를 얻기 위한 메소드이다.\
이 두개는 추상 메소드로서, 하위 클래스에서 구체화된다. getRowText 메소드는 지정한 행의 문자를 표시하는 메소드이며 이 역시 하위 클래스에서 구현된다.\
show 메소드는 모든 행을 표시하는 메소드이다. getRows 메소드로 행 수를 구한 후 getRowText를 for 문을 이용하여 모든 행을 출력한다.

```java
public class StringDisplay extends Display {
    private String string;
    public StringDisplay(String string){
        this.string = string;
    }
    public int getColumns(){
        return string.getBytes().length;
    }
    public int getRows(){
        return 1;
    }
    public String getRowText(int row){
        if (row == 0){
            return string;
        }else{
            return null;
        }
    }
}
```
StringDisplay 클래스는 Display를 상속받는 하위 클래스이며, 1행의 문자열을 표시하는 클래스이다. string 변수는 표시할 문자열을 저장한다. 이 클래스에서는\
string 변수 내용 하나만을 표시하기 때문에 getColumns 는 string 의 바이트 길이 만큼을 반환하며, getRows 는 1을 반환한다. 그리고 getRowText는 row가\
0번째일 때만 string 을 리턴한다. 이 StringDisplay 클래스가 바로 베이스가 되는 클래스이다.

### 장식을 나타내는 클래스들
여기서 작성하는 Border 클래스와 SideBorder 클래스는 각각 장식을 나타내는 추상 클래스와 그 하위 클래스이다. 
```java
public abstract class Border extends Display {
    protected Display display;
    protected Border(Display display){
        this.display = display;
    }
}
```
장식을 나타내는 추상클래스인 Border 클래스이다. 잘 보면 Display 클래스를 상속 받고 있는 것을 볼 수 있다. 이는 Border 클래스를 상속받는 하위 클래스에서\
Display 에 있는 내용물과 동일한 메소드를 갖기 위해 Display를 상속받고 있다. Border가 Display와 같은 메소드를 갖고 있다는 것은 인터페이스적으로 두개를\
동일시 할 수 있다는 의미이다.\
\
Border 클래스 안에 display 변수가 있는데, 이게 바로 위에서 작성했었던 베이스가 되는 내용물이다. 하지만 display 변수에는 꼭 StringDisplay가 올거란\
보장은 할 수 없다. Border 클래스를 다시 봐보자. Display 클래스를 상속 받고 있다. 이는 곧 display 변수에 Border의 하위 클래스가 올 수도 있다는 것을\
의미 한다. Border클래스도 어쨌든 Display 클래스를 상속받기 때문이다.

```java
public class SideBorder extends Border {
    private char borderChar;
    public SideBorder(Display display, char ch){
        super(display);
        this.borderChar = ch;
    }
    public int getColumns(){
        return 1 + display.getColumns() + 1;
    }
    public int getRows(){
        return display.getRows();
    }
    public String getRowText(int row){
        return borderChar + display.getRowText(row) + borderChar;
    }
}
```
SideBorder 클래스는 Border 클래스를 상속 받는 하위 클래스이다. 이름에서 알 수 있듯이 글자의 사이드 양쪽에 정해진 문자로 문자열 장식을 하는 클래스이다.\
예를 들어 borderChar 필드의 값이 '|'라고 한다면 
> | 문자열 |

과 같이 문자열 양 옆에 지정된 문자가 붙어 노출된다. borderChar 변수는 생성자에서 ch 변수를 받아 지정된다. Display 클래스에 있던 getColumns와 getRows\
getRowText 메소드가 여기에서 구현된 것을 볼 수 있다. Display를 상속 받았기 때문이다. 