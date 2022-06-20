# Observer 패턴

### Observer 패턴이란
'Observer'란 단어는 관찰자, 관찰하는 사람이라는 뜻을 가진 단어이다. Observer 패턴은 관찰 대상의 변화가 감지되면 관찰자에게 알려주는 패턴이다. 이 패턴은\
상태 변화에 따른 처리를 해야 할 때 효과적으로 사용할 수 있다.\
바로 예제로 넘어가보자.

### 예제 코드
이번 예제 프로그램은 많은 수를 생성하는 오브젝트를 관찰자가 관찰하여 그 값을 표시해주는 프로그램이다. 다만 표시의 방법은 관찰자에 따라 달라진다.\
값을 숫자로 표현하는 클래스도 있고, 그래프로 표시하는 클래스도 있다. 아래 코드에서 살펴보자.

```java
public interface Observer {
    public abstract void update(NumberGenerator generator);
}
```
Observer 인터페이스는 관찰자를 표현하는 인터페이스이다. 관찰자를 구현하는 클래스들은 모두 이 인터페이스를 구체화 하게 된다.\
update 메소드는 수를 생성하는 클래스인 NumberGenerator에서 호출하는 메소드이다. 이 메소드는 상태가 변화할 때 NumberGenerator 객체를 넘겨 현재 넘겨진\
클래스의 상태가 변하였다는 것을 알려준다.

```java
import java.util.ArrayList;
import java.util.Iterator;

public abstract class NumberGenerator(){
    private ArrayList observers = new ArrayList();
    public void addObserver(Observer observer){
        observers.add(observer);
    }
    public void deleteObserver(Observer observer){
        observers.remove(observer);
    }
    public void notifyObservers(){
        Iterator it = observers.iterator();
        while(it.hasNext()){
            Observer o = (Observer) it.next();
            o.update(this);
        }
    }
    public abstract int getNumber();
    public abstract void execute();
}
```