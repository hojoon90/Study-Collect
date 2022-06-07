# Chain of Responsibility(책임 떠넘기기) 패턴

###책임 떠넘기기 패턴이란
책임 떠넘기기 패턴이란 우리가 일상생활에서 정말 많이 겪는 '일처리'하고 거의 똑같은 형태이다. 가령 우리가 어떤 문의 혹은 서류처리 등을 위해 담당 회사에 전화를\
했다고 생각해보자. 먼저 고객문의 담당부서에서 어떤 내용인지 확인하고 해당 부서에 전화를 돌려준다. 길게 설명할 필요도 없이... 이 영상을 보면 어떤 패턴인지\
대번에 알 수 있다. [참고영상](https://youtu.be/NbHVvd82bsE) \
즉 어떤 요청이 발생했을 때 그 요청을 처리할 오브젝트를 직접 결정할 수 없는 경우, **사슬처럼 연결된 여러 오브젝트들을 돌아다니며 목적한 오브젝트를 결정하는 방법**\
을 Chain of Responsibility(책임 떠넘기기)패턴이라고 한다.

###예제코드
예제는 무언가 문제가 생겨 오브젝트중 하나가 처리해야 하는 상황을 생각해보자.
```java
public class Trouble {
    private int number;
    public Trouble(int number){
        this.number = number;
    }
    public int getNumber(){
        return number;
    }
    public String toString(){
        return "[Trouble "+ number + "]";
    }
}
```
Trouble 클래스는 문제 발생을 나타내는 클래스이다. 변수 number 는 트러블에 대한 번호이다.

```java
public abstract class Support {
    private String name;
    private Support next;
    public Support(String name){
        this.name = name;
    }
    public Support setNext(Support next){
        this.next = next;
        return next;
    }
    public final void support(Trouble trouble){
        if(resolve(trouble)){
            done(trouble);
        }else if(next != null){
            next.support(trouble);
        }else{
            fail(trouble);
        }
    }
    public String toString(){
        return "[" + name + "]";
    }
    protected abstract boolean resolve(Trouble trouble);
    protected void done(Trouble trouble){
        System.out.println(trouble + "is resolved by" + this + ".");
    }
    protected void fail(Trouble trouble){
        System.out.println(trouble + " cannot be resolved.");
    }
}
```
추상클래스인 Support 클래스는 문제를 해결할 사슬을 만들 클래스이다. next 메소드는 문제를 해결 못 할 경우 다음 오브젝트에게 넘겨주기 위한 필드를 지정하며,
setNext 메소드는 떠넘길 곳을 설정한다.\
resolve 메소드는 추상 메소드로서 하위 클래스들에서 각각에 맞는 요구를 처리할 수 있는 내용이 구체화된다. 문제가 처리될 경우 True 값을, 해결되지 못했을 경우\
false 값을 리턴해준다.\
support 메소드는 resolve 반환값에 따라 처리가 달라지며, false일 경우 다음 오브젝트에게 문제를 넘긴다. 만약 맨 마지막에 받은 오브젝트가 해결하지 못 할 경우\
fail 메소드를 호출한다.

```java
public class NoSupport extends Support {
    public NoSupport(String name){
        super(name);
    }
    protected boolean resolve(Trouble trouble){
        return false;
    }
}
```
NoSupport 메소드는 Support 의 하위 클래스이며, 아무문제도 처리하지 않는 클래스이다. resolve 메소드의 리턴값이 false 인 것을 볼 수 있다.

```java
public class LimitSupport extends Support {
    private int limit;
    public LimitSupport(String name, int limit){
        super(name);
        this.limit = limit;
    }
    protected boolean resolve(Trouble trouble){
        if(trouble.getNumber()<limit){
            return true;
        }else{
            return false;
        }
    }
}
```
LimitSupport 클래스는 이름처럼 제한적인 해결만 해주는 클래스이다. 생성 시 limit값을 세팅해주며, Trouble 에서 넘어온 숫자가 limit 값보다 작을 경우 \
문제를 해결한다고 판단하고 True 값을 반환해준다.

```java
public class OddSupport extends Support {
    public OddSupport(String name){
        super(name);
    }
    protected boolean resolve(Trouble trouble){
        if(trouble.getNumber % 2 == 1){
            return true;
        }else{
            return false;
        }
    }
}
```
OddSupport 클래스는 홀수 값만을 처리하는 클래스이다.

```java
public class SpecialSupport extends Support {
    private int number;
    public SpecialSupport(String name, int number){
        super(name);
        this.number = number;
    }
    protected boolean resolve(Trouble trouble){
        if(trouble.getNumber == number){
            return true;
        }else{
            return false;
        }
    }
}
```
SpecialSupport 클래스는 지정한 번호의 문제에 한해 처리하는 클래스이다.

```java
public class Main {
    public static void main (String[] args){
        Support alice = new NoSupport("Alice");
        Support bob = new LimitSupport("Bob", 100);
        Support charlie = new SpecialSupport("Charlie", 429);
        Support diana = new LimitSupport("Diana", 200);
        Support elmo = new OddSupport("Elmo");
        Support fred = new LimitSupport("Fred", 300);
        
        alice.setNext(bob).setNext(charlie).setNext(diana).setNext(elmo).setNext(fred);
        for (int i = 0; i < 500; i += 33) {
            alice.support(new Trouble(i));
        }
    }
}
```
Main 클래스는 6명의 문제해결자를 만들어준다. 변수들은 모두 Support 클래스이지만 생성된 클래스들은 모두 Support 클래스를 상속 받는 하위 클래스들이다. \
인스턴스 생성 후 alice부터 차례대로 문제를 해결할 해결자들을 setNext 메소드로 세팅해준다. 그 후 alice에게 문제(Trouble)를 던져주고, 누가 문제를 해결하는지\
표시한다. 실행결과는 아래와 같다.

> [Trouble 0] is resolved by [Bob].\
> [Trouble 33] is resolved by [Bob].\
> [Trouble 66] is resolved by [Bob].\
> [Trouble 99] is resolved by [Bob].\
> [Trouble 132] is resolved by [Diana].\
> [Trouble 165] is resolved by [Diana].\
> [Trouble 198] is resolved by [Diana].\
> [Trouble 231] is resolved by [Elmo].\
> [Trouble 264] is resolved by [Fred].\
> [Trouble 297] is resolved by [Elmo].\
> [Trouble 330] cannot be resolved.\
> [Trouble 363] is resolved by [Elmo].\
> [Trouble 396] cannot be resolved.\
> [Trouble 429] is resolved by [Charlie].\
> [Trouble 462] cannot be resolved.\
> [Trouble 495] is resolved by [Elmo].

### 패턴 사용 시 고려해봐야할 점.
책임떠넘기기 패턴은 요청자와 응답자를 유연하게 연결해준다. 요청자는 최초의 응답자에게 문제를 넘겨주면 응답자는 알아서 해당 문제를 알맞은 응답자에게 넘겨 \
처리하도록 한다. 만약 이런 패턴이 사용되지 않으면 문제가 발생하였을 때 누가 처리 해주어야 하는지 지정을 해주어야 한다는 것이다. 이렇게 되면 요청자들이 \
응답자들의 역할분담까지 알아야하기 때문에 부품으로서의 독립성이 훼손된다.\
