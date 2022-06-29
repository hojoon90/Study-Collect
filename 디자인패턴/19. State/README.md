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
        context.callSecurity
    }
}
```