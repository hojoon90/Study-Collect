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

실행 결과는 아래와 같다.
> DigitObserver: 39\
> GraphObserver:\
> ***************************************\
> DigitObserver: 38\
> GraphObserver:\
> **************************************\
> DigitObserver: 44\
> GraphObserver:\
> ********************************************\
> DigitObserver: 29\
> GraphObserver:\
> *****************************\
> DigitObserver: 12\
> GraphObserver:\
> ************\
> DigitObserver: 4\
> GraphObserver:\
> ****\
> DigitObserver: 9\
> GraphObserver:\
> *********\
> DigitObserver: 34\
> GraphObserver:\
> **********************************\
> DigitObserver: 21\
> GraphObserver:\
> *********************\
> DigitObserver: 26\
> GraphObserver:\
> **************************\
> DigitObserver: 29\
> GraphObserver:\
> *****************************\
> DigitObserver: 38\
> GraphObserver:\
> **************************************\
> DigitObserver: 0\
> GraphObserver:\
>\
> DigitObserver: 15\
> GraphObserver:\
> ***************\
> DigitObserver: 41\
> GraphObserver:\
> *****************************************\
> DigitObserver: 40\
> GraphObserver:\
> ****************************************\
> DigitObserver: 49\
> GraphObserver:\
> *************************************************\
> DigitObserver: 1\
> GraphObserver:\
> *\
> DigitObserver: 23\
> GraphObserver:\
> ***********************\
> DigitObserver: 23\
> GraphObserver:\
> ***********************\
> \
> Process finished with exit code 0

### 패턴 사용 시 고려할 사항
옵저버 패턴 역시 클래스 간의 교환이 유연하다. 위 예시 코드들을 잘 보자. RandomNumberGenerator 클래스는 현재 자신을 관찰하는 클래스가 \
DigitObserver 클래스인지 GraphObserver 클래스인지 알 필요가 없다. 하지만 부모 클래스인 NumberGenerator 안의 observer 필드에 \
Observer 인터페이스가 구현되어 있다는 것은 알고 있다. 이 인스턴스들은 addObserver 메소드에서 추가된 것이므로 반드시 Observer 인터페이스가 \
구현되어 있으며 update 메소드를 이용해 호출 할 수 있다. 또한 DigitObserver 와 GraphObserver 클래스는 자신이 보고 있는 NumberGenerator 클래스가 \
Random 인지 다른 클래스인지 역시 알 필요가 없다. 단지 NumberGenerator 의 하위 클래스의 인스턴스이며 getNumber 메소드를 갖고 있다는 것만 알고 있다.\
구현 클래스들과 추상 클래스를 분리하여 사용하며 각 클래스에서 보듯이 인수들을 받는 부분을 추상 클래스 혹은 인터페이스로 사용하고 있다.\
이는 곧 클래스간의 교환이 유연하다고 볼 수 있다.\
\
예제 프로그램은 먼저 등록된 Observer 클래서가 먼저 호출 된다. 일반적으로 Observer 클래스가 여러개 등록되어있어도 update 메소드가 호출되는 순서가 \
변경되어도 문제가 없어야 한다. 각 클래스의 독립성이 보장되면 의존성의 혼란이 발생하진 않지만 만약 옵저버의 update 호출로 인해 상태가 변화한다면 문제가\
발생할 수 있다. 예를 들어 NumberGenerator 클래스에서 update 메소드를 호출할 때 Observer 가 NumberGenerator 클래스의 호출을 요청하는 경우도 있다.\
이럴 경우 무한루프 상태에 빠지게 된다. (NumberGenerator가 변화 -> Observer에 알림 -> Observer가 다시 NumberGenerator를 호출하며 상태 변화\
-> 상태 변화로 인해 Observer에 알림...) 이와 같은 상황을 방지하려면 상태값을 알수 있는 플래그 값이 있으면 위와같은 상황을 막을 수 있다.\
\
NumberGenerator 는 update 메소드를 사용해서 갱신되었다고 Observer에게 알려주고 있다. update 메소드에 NumberGenerator 인수만을 넘겨주고 Observer\
는 update 메소드 안에서 getNumber 메소드를 통해 숫자를 얻어내고 있다. 하지만 굳이 NumberGenerator 를 넘겨주지 않고 바로 숫자를 넘겨주어도 상관이 없다.\

> void update(NumberGenerator generator, int number);

또는

> void update(int number);

이런식으로 호출해도 되는 것이다. 일단 NumberGenerator 와 숫자 모두를 넘겨주는 것은 모든 정보를 넘겨주므로 update 메소드가 정보를 얻는 시간을 줄일 수\
있다. 다만 이렇게 정보를 모두 준다는 것은 Observer에서 update 메소드의 처리 내용을 의식하고 있다고 볼 수 있다.\
여기서 만든 예제 프로그램보다 복잡한 프로그램의 경우는 Observer에게 필요한 정보가 무엇인지 알기가 굉장히 어렵다. 그렇기 때문에 어느 정도의 정보를 update\
메소드에게 제공할지는 프로그램의 복잡도에 따라 달라진다고 볼 수 있다.
맨 아래 방법은 단순하게 숫자만 넘겨주는 메소드이다. 이럴 경우 코드는 간결해지지만, 한 Observer가 여러 오브젝트들을 관찰 한다면 저 숫자가 어느 오브젝트에서\
넘어온지 모르기 때문에 사용하기에 부적절하다.
