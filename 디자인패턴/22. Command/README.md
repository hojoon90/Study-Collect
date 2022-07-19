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
DrawCommand 클래스는 '점 그리기 명령'을 표현한 클래스이며 Command 인터페이스를 구현하고 있다. drawable과 position 두개의 변수를 갖고 있으며, drawable은 그리기 대상을 지정하고
position 필드는 그리기를 실행하는 위치를 나타낸다. Point 클래스는 x, y좌표를 가진 이차원 평면의 위치를 나타낸다. 생성자에서는 Drawble의 인스턴스와 Point 인스턴스를 
받아서 변수에 대입해준다. 이를 통해 '이 위치에 점을 그려라'하는 명령을 만들고 있는 부분이다. execute 메소드에서는 drawable의 draw를 호출하고 있다. 이것이 명령을 실행하는 부분이다.

```java
public interface Drawable {
    public abstract void draw(int x, int y);
}
```
Drawable 인터페이스는 그림그리기 대상을 표현하는 것이다. draw는 그림을 그리는 메소드이며 좌표값을 인자로 받는다.

```java
public class DrawCanvas extends Canvas implements Drawable{
    private Color color = Color.red;
    private int radius = 6;
    private MacroCommand history;
    public DrawCanvas(int width, int height, MacroCommand history){
        setSize(width, height);
        setBackground(Color.white);
        this.history = history;
    }
    public void paint(Graphics g){
        history.execute();
    }
    public void draw(int x, int y){
        Graphics g = getGraphics();
        g.setColor(color);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}
```
DrawCanvas 클래스는 Drawable 인터페이스를 구현하는 클래스이고 java.awt.Canvas 클래스의 하위 클래스이다. 그림 그리기 명령의 집합은 history 변수에 저장된다.
생성자에서는 폭, 높이, 내용(history)을 받아서 DrawCanvas의 인스턴스를 초기화한다. setSize와 setBackground는 java.awt.Canvas의 메소드이며, 각각 크기와 배경색을 지정한다.
paint 메소드는 DrawCanvas를 다시 그려야할 때 호출되는 메소드이다. history.execute메소드가 실행되면 history에 기록되어 있는 명령어들이 재실행된다.
draw 메소드는 Drawable 인터페이스가 구현된 것이며 setColor로 색상이, fillOval로 원을 표시한다.

```java
public class Main extends JFrame implements ActionListener,
MouseMotionListener, WindowListener{
    private MacroCommand history = new MacroCommand();
    private DrawCanvas canvas = new DrawCanvas(400, 400, history);
    private JButton clearButton = new JButton("clear");
    
    public Main (String title){
        super(title);
        
        this.addWindowListener(this);
        canvas.addMouseMotionListener(this);
        clearButton.addActionListener(this);
        
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        buttonBox.add(clearButton);
        Box mainBox = new Box(BoxLayout.Y_AXIS);
        mainBox.add(buttonBox);
        mainBox.add(canvas);
        getContentPane().add(mainBox);
        
        pack();
        show();
    }
    
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == clearButton){
            history.clear();
            canvas.repaint();
        }
    }
    public void mouseMoved(MouseEvent e){
    }
    public void mouseDragged(MouseEvent e){
        Command cmd = new DrawCommand(canvas, e.getPoint());
        history.append(cmd);
        cmd.execute();
    }
    
    public void windowClosing(WindowEvent e){
        System.exit(0);
    }
    public void windowActivated(WindowEvent e){}
    public void windowClosed(WindowEvent e){}
    public void windowDeactivated(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    public void windowIconified(WindowEvent e){}
    public void windowOpened(WindowEvent e){}
    
    public static void main(String[] args){
        new Main("Command Pattern Sample");
    }
}
```
Main 클래스는 예제프로그램을 동작시키기 위한 클래스이다. history 변수는 그림그리기의 이력을 저장하는데, 나중에 DrawCanvas인스턴스에 전달하는것과 같다.
canvas 필드는 그림을 그리는 영역이며, 초기값으로 400x400 값을 갖고 있다. clearButton 은그림을 그린점을 지우는 제거버튼이며, JButton은 javax.swing 패키지의
클래스로 버튼을 표현한 것이다.

생성자에서는 이벤트 리스너를 설정하여 그림을 그리는 컴포넌트를 레이아웃 하고 있다. buttonBox에 clearButton을 올리는데, 이때 버튼박스를 생성할 때 가로로 나열하기 위해
BoxLayout.X_AXIS 를 넘겨준다. 그 후 mainBox를 생성해준 후 그 위에 buttonBox와 canvas를 올려놓는다. 그리고 마지막으로 JFrame에 mainBox를 올려놓는다.
actionPerformed 메소드는 ActionListener 인터페이스를 구현하기 위한 것이며, clear 버튼이 눌렸을 때 이력을 지우고 다시 그림을 그리도록 실행한다.

mouseMoved 메소드와 mouseDragged 메소드는 MouseMotionListener 인터페이스를 구현하기 위한 것이다. mouseMoved는 별다른 처리가 없으며, mouseDragged는
마우스를 드래그했을 때 그 위치에 점을 그리라는 명령을 만들고 있다. 해당 명령은 history 에 추가되며, cmd.execute를 통해 바로 실행된다. 

window들로 시작하는 메소드들은 WindowListener 인터페이스를 구현하기 위해 작성된 것이며 여기서는 종료처리에 대해서만 구현되어있다.

### 패턴 사용시 고려할 점
명령에 어느정도의 정보를 갖게 할 지는 목적에 따라 달라진다. 예제에 있는 DrawCommand 클래스는 그림을 그리는 점의 위치정보만 갖고 있다. 여기에 만약 이벤트가 발생한
시간까지 갖고 있다면 사용자의 마우스 동작의 완급까지도 재현이 가능해질 수 있다. 예제 프로그램에서는 history 변수에 그림그리기의 이력을 저장하고 있다. 이것을 만약 파일로
남겨둔다면 이력 저장 또한 가능해진다.

