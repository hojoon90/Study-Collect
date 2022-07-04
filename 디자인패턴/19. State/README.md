# State 패턴

### State 패턴이란
State 는 '상태' 라는 의미를 갖는 단어이다. 여기에서 알아볼 State 패턴은 '상태' 를 클래스로 표현하는 패턴이다. 보통 우리가 클래스로 표현하는 것은 대부분 사물을\
클래스로 만드는 경우가 많다. 그렇기 때문에 상태를 클래스로 만드는 경우는 약간 생소할 수 있다. State 패턴을 이용하면 클래스를 교체해서 상태의 변화를 표현할 수 \
있고, 새로운 상태를 추가해야 할 때 무엇을 프로그램하면 좋을지 분명해진다.\
예제 코드로 살펴보자.

### 예제 코드
여기서는 시간마다 경비상태가 변하는 금고경비 시스템을 프로그래밍 해 본다. 여기서는 제한사항들이 있어 프로그램상 1초를 1시간으로, 그리고 경비센터 호출 대신\
화면에 표시하는 것으로 대체한다. 조건은 아래와 같다.

> 금고는 경비센터와 접속되어 있음\
> 금고에는 비상벨과 일반 통화용 전화가 접속 되어있음\
> 금고에 시계가 있어 현재 시간을 감시\
> **주간은 9:00 ~ 16:59, 야간은 17:00 ~ 23:59 과 00:00 ~ 08:59**
> 금고는 주간만 사용 가능\
> 주간에 금고 사용시 경비센터에 기록이 남음\
> 야간에 금고 사용시 경비센터에 비상사태로 통보\
> 비상벨은 언제나 사용 가능\
> 비상벨 사용 시 경비센터에 비상벨 통보가 됨\
> 일반 통화용 전화는 언제나 사용 가능(야간은 녹음만 가능)\
> 주간에 전화 사용 시 경비센터 호출\
> 야간에 전화 사용 시 경비센터 자동응답기가 호출

위 조건들에 유의하면서 예제 코드를 살펴보자.

```java
public interface State {
    public abstract void doClock(Context context, int hour);
    public abstract void doUse(Context context);
    public abstract void doAlarm(Context context);
    public abstract void doPhone(Context context);
}
```
State 인터페이스는 금고의 상태를 나타내는 인터페이스이다. 각 메소드들은 추상 메소드들로 이루어져 있는데 모두 상태들에 대해 나타내고 있다. 여기서 잠깐 로직이\
어떻게 이루어질지 생각해보자. 보통 우리는 위의 조건들을 보고 로직을 생각할 때 대부분 if else 문을 떠올리게 된다. 예를 들어 금고 사용에 대한 조건에 대해 보자면\
주간에는 금고 사용 시 경비센터에 기록이 남게 되며, 야간에 사용 시 경비센터에 비상사태로 통보를 하게 된다. if else 문으로 주간 야간에 따라 처리를 다르게\
할 수 있다. 하지만 State 패턴에서는 위와 같이 분기절을 만들어 코드를 만들지 않는다. 다시 위의 추상 메소드 중 doUse 메소드를 잘 보자. 이 메소드는 금고를 \
사용할 때의 처리를 담당하는 메소드이다. State에 대한 인터페이스가 있으니 아래에서 설명하겠지만 해당 인터페이스를 구현하는 주간 상태 클래스와 야간 상태 \
클래스를 만들면 한 메소드로 각각 상황에 맞는 처리가 가능해지게 된다. 즉 State 인터페이스는 상태의존 메소드의 집합이라고 볼 수 있다.

```java
public class DayState implements State {
    private static DayState singleton = new DayState();
    private DayState(){
        
    }
    public static State getInstance(){
        return singleton;
    }
    public void doClock (Context context, int hour){
        if(hour < 9 || 17 <= hour){
            context.changeState(NightState.getInstance());
        }
    }
    public void doUse(Context context){
        context.recordLog("금고사용 (주간)");
    }
    public void doAlarm(Context context){
        context.callSecurityCenter("비상벨 (주간)");
    }
    public void doPhone(Context context){
        context.callSerucityCenter("일반통화 (주간)");
    }
    public String toString(){
        return "[주간]";
    }
}
```
DayState 클래스는 주간 상태를 나타내는 클래스이다. State 인터페이스를 구현하고 있으며 State 인터페이스에 선언 된 메소드들을 구현한다. 상태가\
변화할 때 마다 인스턴스를 계속 생성하면 메모리와 시간이 낭비되기 때문에 클래스는 한 개씩만 인스턴스를 만들어 준다. 그렇기 때문에 상단에 singleton이 사용된\
것을 볼 수 있다.\
doClock 메소드는 시간을 설정하는 메소드이다. 변수의 hour가 야간 시간대이면 야간 상태로 인스턴스를 바꾸어 준다. context 에서 해당 로직을 실행하는데, 
NightState 역시 싱글턴으로 인스턴스를 얻어오게 되어있다는 것을 볼 수 있다.\
doUse, doAlarm, doPhone 모두 context 안의 메소드들을 호출하는 역할만 하고 있다. 해당 메소드들 안에 if 문이 없다는 것에 주목하자. 이 클래스에서 \
사용되는 메소드들은 모두 주간상태일 때의 처리라는 것이기 때문에 별도의 if문이 존재하지 않는다.\

```java
public class NightState implements State {
    private static NightState singleton = new NightState();
    private NightState(){
        
    }
    public static State getInstance(){
        return singleton;
    }
    public void doClock (Context context, int hour){
        if(9 <= hour && hour <= 17){
            context.changeState(DayState.getInstance());
        }
    }
    public void doUse(Context context){
        context.recordLog("비상: 야간금고 사용!");
    }
    public void doAlarm(Context context){
        context.callSecurityCenter("비상벨 (야간)");
    }
    public void doPhone(Context context){
        context.callSerucityCenter("야간통화 녹음");
    }
    public String toString(){
        return "[야간]";
    }
}
```
NightState 클래스는 야간의 상태를 나타내는 클래스이다. 위의 주간 상태 클래스와 크게 다르지 않으므로 바로 넘어간다.

```java
public interface Context{
    public abstract void setClock(int hour);
    public abstract void changeState(State state);
    public abstract void callSecurityCenter(String msg);
    public abstract void recordLog(String msg);
}
```
Context 인터페이스는 상태를 관리하거나 경비센터를 호출하는 역할을 한다. 실제 역할은 아래 SafeFrame 클래스에서 다룬다.

```java
import java.awt.*;

public class SafeFrame extends Frame implements ActionListener, Context{
    private TextField textClock = new TextField(60);
    private TextArea textScreen = new TextArea(10, 60);
    private Button buttonUse = new Button("금고 사용.");
    private Button buttonAlarm = new Button("비상벨");
    private Button buttonPhone = new Button("일반 통화");
    private Button buttonExit = new Button("종료");
    
    private State state = DayState.getInstance();
    
    public SafeFrame(String title){
        super(title);
        setBackground(Color.lightGray);
        setLayout(new BorderLayout());
        add(textClock, BorderLayout.NORTH);
        textClock.setEditable(false);
        add(textScreen, BorderLayout.CENTER);
        textScreen.setEditable(false);
        
        Panel panel = new Panel();
        panel.add(buttonUse);
        panel.add(buttonAlarm);
        panel.add(buttonPhone);
        panel.add(buttonExit);
        
        add(panel, BorderLayout.SOUTH);
        
        pack();
        show();
        
        buttonUse.addActionListener(this);
        buttonAlarm.addActionListener(this);
        buttonPhone.addActionListener(this);
        buttonExit.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e){
        System.out.println(e.toString());
        if(e.getSource() == buttonUse){
            state.doUse(this);
        }else if(e.getSource() == buttonAlarm){
            state.doAlarm(this);
        }else if(e.getSource() == buttonPhone){
            state.doPhone(this);
        }else if(e.getSource() == buttonExit){
            System.exit(0);
        }else{
            System.out.println("?");
        }
    }
    public void setClock(int hour){
        String clockString = "현재 시간은";
        if(hour < 10){
            clockString += "0" + hour + ":00";
        }else{
            clockString += hour + ":00";
        }
        System.out.println(clockString);
        textClock.setText(clockString);
        state.doClock(this, hour);
    }
    public void changeState(State state){
        System.out.println(this.state + "에서" + state +"로 상태가 변경되었습니다.");
        this.state = state;
    }
    public void callSecurityCenter(String msg){
        textScreen.append("call! " + msg + "\n");
    }
    public void recordLog(String msg){
        textScreen.append("record ..." + msg + "\n");
    }
}
```
SafeFrame 클래스는 경비센터 모습을 GUI로 간이적으로 보여주는 클래스이다. Context 인터페이스를 구현하고 있다.\
이 클래스 안에 있는 변수들은 화면상에 표시되는 입력 영역, 표시 영역 밑 버튼등의 부품이다. 그러나 state 변수는 금고의 현재 상태를 나타내주는 필드이다.
처음에는 주간으로 세팅되어 있다.\
생성자에서는 크게 네가지의 작업을 진행한다. 작업 내용들은 다음과 같다.
> GUI 배경색상 설정 \
> 레이아웃 구성 \
> 변수들에 대해 세팅 \
> 리스너 설정

리스너는 각 버튼에서 addActionListener 메소드를 호출하여 Listener를 설정한다. 이 때 addActionListener 메소드 호출 시 넘겨주는 변수에 
'버튼을 클릭했을 때 호출되는 인스턴스'를 넘겨준다. 이 때 넘겨주는 변수의 인스턴스는 ActionListener 인터페이스를 구현해주어야 한다.\
여기에서는 this, 즉 SafeFrame 클래스를 변수로 넘겨주고 있다. SafeFrame은 ActionListener 인터페이스를 구현하고 있으므로 바로 인스턴스로 넘겨줄 수 있다.\
actionPerfomed 메소드는 버튼이 눌러졌을 때 호출되는 메소드이다. 이 메소드 안에서 어떤 버튼이 눌러졌는지 조사하여 그에 해당하는 처리를 진행한다. 여기서 버튼의 형태에 따라
분기문이 이루어 지는데, 분기문 안에는 doUse 혹은 doAlarm 등의 메소드를 바로 호출하는 것을 볼 수 있다. 현재 상태가 무엇인지 따로 **확인하지 않고** 바로 메소드를 호출 하는데,
이는 State 패턴의 특징이다. \
setClock 메소드는 현재 시각을 지정한 시각으로 설정해주는 메소드이다. changeState 메소드는 DayState 와 NightState 내에서 실행되며 상태 전환이 일어날 때 호출되는 메소드이다.
callSecurityCenter 는 경비센터 호출, recordLog 는 경비센터의 기록을 표현한다.

```java
public class Main {
    public static void main(String[] args){
        SafeFrame frame = new SafeFrame("State Sample");
        while(true){
            for (int hour = 0; hour < 24; hour++){
                frame.setClock(hour);
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException ie){}
            }
        }
    }
}
```
Main 클래스에서는 SafeFrame 인스턴스를 생성하여 시간설정을 해준다. 여기선 1초마다 시간이 바뀌지만 프로그램 내에선 1초가 1시간이라고 가정한다.

### 패턴 사용시 고려 사항
프로그램 알고리즘 중 '분할정복'이라는 것이 있다. 말 그대로 세세하게 쪼개서 문제를 풀어나가는 방식이다. State 패턴도 각각의 상태들을 클래스로 만들어 분할한 것이다.
위와 같은 상태가 2가지인 경우에는 별로 필요성이 없을 수 있지만 상태가 많은 경우는 State 패턴이 유용해진다. 예를들어 위에서 State 패턴을 사용하지 않으면 각 메소드에 대해
상태에 대한 분기가 필요해지게 된다. 만약 상태가 많아지게 된다면 그에 대한 분기도 더욱 늘어날 것이다. 또한 비슷한 분기가 생길때마다 메소드들을 일일히 작성해주어야 한다.
State 패턴을 사용하면 위와 같은 불편한 점들을 한번에 해결할 수 있다.\
\
