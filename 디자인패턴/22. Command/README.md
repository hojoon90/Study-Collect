# Command 패턴

### Command 패턴이란

클래스가 일을 실행할 때, 자신의 클래스 혹은 다른 클래스의 메소드를 호출한다. 메소드를 호출한 결과는 오브젝트에 남지만, 일의 이력은 남지 않는다. 이럴때 명령을 표현하는
클래스가 있으면 편하다. 실행하고 싶은 메소드를 명령을 나타내는 클래스의 인스턴스로 하나의 '물건'처럼 표현할 수 있기 때문이다. 이력을 관리하고 싶으면 그 인스턴스의 집합을
관리하면 되며, 명령의 집합을 저장해두면 같은 명령을 재실행 할 수도 있으며 복수의 명령을 모아 새로운 명령으로 재이용할 수 있다. 이런 방식을 디자인 패턴에서는
**Command 패턴**이라고 한다. Command 는 Event라고도 부른다. 마우스를 클릭하거나 키를 누르는 등의 이벤트가 발생할 때, 그 사건을 발생 순서에 따른 행렬로 나열한다. 그리고 나열한 이벤트를 순서대로
실행한다. GUI에서 이러한 이벤트가 자주 등장한다. 

### 예제코드
Command 패턴을 사용하여 만들 프로그램은 간단한 그림을 그리는 프로그램이다. 사용자가 마우스를 드래그할 때 마다 그림이 그려지는 프로그램이다.
```java
public interface Command {
    public abstract void execute();
}
```
Command 인터페이스는 '명령'을 표현하기 위한 인터페이스이고, execute 메소드만을 가진다. execute 메소드를 호출할 때 일어나는 일은 이 Command 인터페이스를
구현한 클래스가 결정한다.

```java
public class MacroCommand implements Command {
    private Stack commands = new Stack();
    public void execute(){
        Iterator it = commands.iterator();
        while (it.hasNext()){
            ((Command)it.next()).execute();
        }
    }
    public void append(Command cmd){
        if (cmd != this){
            commands.push(cmd);
        }
    }
    public void undo(){
        if(!commands.empty()){
            commands.pop();
        }
    }
    public void clear(){
        commands.clear();
    }
}
```
MacroCommand 클래스는 명령들을 모은 클래스이며, Command 인터페이스를 구현하고 있다. commands 필드는 java.util.Stack 형인데, 복수의 Command를 모아두기 위한 것이다.
여기서는 List 사용도 좋긴하지만, undo 메소드를 위해 Stack을 사용한다. MacroCommand 클래스는 Command 인터페이스를 구현하고 있어서 execute 메소드가 정의되어 있다.
execute 메소드에서는 commands에 쌓여있는 명령어들을 순서대로 처리하는 역할을 한다. append 메소드는 명령어들을 추가하는 메소드이다. 명령어를 추가 할 때 조건이 있는데,
MacroCommand 자신을 추가하지 않도록 조건이 걸려 있다. 만약 자기자신이 추가된다면 프로그램은 무한반복에 빠지게 될 것이다. undo 메소드는 commands의 마지막 명령어를 삭제하는
메소드이다. clear는 모든 명령어들을 삭제한다.

```java
public class DrawCommand implements Command {
    protected Drawable drawable;
    private Point position;
    public DrawCommand(Drawable drawable, Point position){
        this.drawable = drawable;
        this.position = position;
    }
    public void execute(){
        drawable.draw(position.x, position.y);
    }
}
```