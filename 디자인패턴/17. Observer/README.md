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

public abstract class NumberGenerator {
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
NumberGenerator 클래스는 수를 생성하는 추상클래스이다. 실제 수를 조회하는 메소드와 수를 생성하는 메소드는 하위 클래스에서 처리하도록 추상메소드로\
되어있다. observers 필드는 NumberGenerator 의 변화를 관찰하는 Observer를 리스트로 보관하는 변수이다. add 와 delete는 각각 observer 를 추가하거나\
삭제하는 메소드이며, notifyObservers는 변화 상태를 옵저버들에게 전달하는 메소드이다. while문을 통해 모든 옵저버에게 update를 실행하는 것을 볼 수 있다.

```java
import java.util.Random;

public class RandomNumberGenerator extends NumberGenerator {
    private Random random = new Random();
    private int number;
    public int getNumber() {
        return number;
    }
    public void execute() {
        for (int i = 0; i < 20; i++) {
            number = random.nextInt(50);
            notifyObservers();
        }
    }
}
```
RandomNumberGenerator 클래스는 랜덤 번호를 생성하는 클래스로, NumberGenerator 클래스의 하위 클래스이다. random 변수는 Random 인스턴스가, \
number 변수는 현재 랜덤 번호값이 저장된다. execute 메소드는 랜덤번호를 생성하는 메소드이며 notifyObservers()메소드를 통해 랜덤 번호가 발생할 때 마다 \
옵저버에 통보한다.

```java
public class DigitObserver implements Observer {
    public void update(NumberGenerator generator){
        System.out.println("DigitObserver: " + generator.getNumber());
        try{
            Thread.sleep(100);
        }catch (InterruptedException e){
        }
    }
}
```
DigitObserver 클래스는 Observer 인터페이스가 구현된 클래스이며 관찰한 수를 숫자로 표시하는 클래스이다. update 메소드를 통해 인수로 주어진\
NumberGenerator의 getNumber를 통해 수를 얻어와서 표시한다. 여기서 Thread 는 숫자 표시를 잘 볼 수 있도록 걸어놓은 것이다.

```java
public class GraphObserver implements Observer{
    public void update(NumberGenerator generator){
        System.out.println("GraphObserver:");
        int count = generator.getNumber();
        for (int i = 0; i < count; i++) {
            System.out.println("*");
        }
        System.out.println("");
        try{
            Thread.sleep(100);
        }catch (InterruptedException e){
        }
    }
}
```
GraphObserver 클래스는 위의 DigitObserver 와 크게 다르진 않으며, 간이그래프로 수를 표시한다.

```java
public class Main{
    public static void main(String[] args){
        NumberGenerator generator = new RandomNumberGenerator();
        Observer observer1 = new DigitObserver();
        Observer observer2 = new GraphObserver();
        generator.addObserver(observer1);
        generator.addObserver(observer2);
        generator.execute();
    }
}
```
Main 클래스에서는 RandomNumberGenerator 인스턴스를 하나 만들고 거기에 각각 DigitObserver과 GraphObserver를 추가해준다. 그 후 execute를 통해\
수를 생성한다.
