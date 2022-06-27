# Memento 패턴

### Memento 패턴이란
우리가 코드를 작성하는 에디터 혹은 워드 프로그램과 같은 텍스트 프로그램들에서 내용을 실수로 삭제 했을 때, ctrl+z 를 누르면 지웠던 내용을 다시 복구할 수 있는\
기능이 있다. 이를 undo 기능이라고 하는데, 오브젝트 지향 프로그램에서 이런 undo 기능을 실행하려면 인스턴스가 가지고 있는 정보를 저장해 둘 필요가 있다.\
하지만 저장만 해서는 안되며, 저장한 정보로부터 인스턴스를 원래대로 다시 돌려놓아야 한다. 이렇게 인스턴스를 복원하기 위해서는 인스턴스 내부의 정보에 자유롭게\
접근이 가능하여야 하지만, 원하지 않은 엑세스로 인해 '캡슐화의 파괴'가 일어날 수 있다. Memento 패턴은 인스턴스의 상태를 나타내는 역할을 도입하여 캡슐화의 파괴\
에 빠지지 않고 저장과 복원을 실행하는 것이다.\
어떤 시점의 인스턴스의 상태를 확실하게 기록해서 저장해 두면 나중에 인스턴스를 그 시점의 상태로 되돌릴 수 있게 된다.

### 예제 코드
Memento 패턴을 이용하여 만들 프로그램은 과일을 모으는 주사위 게임이다. 규칙은 다음과 같다.

> 게임은 자동적으로 진행\
> 게임의 주인공은 주사위를 던져 나온 수가 다음 상태를 결정한다.\
> 좋은 수가 나오면 주인공의 돈이 증가\
> 나쁜 수가 나오면 돈이 감소\
> 특별히 좋은수가 나오면 과일을 받음\
> 돈이 모두 없어지면 게임 오버

프로그램 안에서 Memento 클래스를 이용하여 게임의 처음 상태를 저장해둔다. 그 후 만약 게임이 종료될려 한다면, Memento 인스턴스를 이용하여 이전 상태로\
복원한다. 

```java
package game;
import java.util.*;

public class Memento{
    int money;
    ArrayList fruits;
    public int getMoney(){
        return money;
    }
    Memento(int money){
        this.money = money;
        this.fruits = new ArrayList();
    }
    void addFruits(String fruit){
        fruits.add(fruit);
    }
    List getFruits(){
        return (List)fruits.clone();
    }
}
```
Memento 클래스는 주인공의 상태를 표현하는 클래스이다. 주인공 클래스는 Gamer클래스인데, Gamer클래스와 Memento 클래스는 모두 같은 game 패키지에 \
둔다. money 필드는 현재 갖고있는 돈이며 fruits는 현재 갖고있는 과일이다. Memento 클래스의 생성자는 public이 없다. 그래서 동일한 패키지에 있는 클래스에서만\
사용이 가능하다.\
addFruits 메소드도 public이 아닌데, 이는 game 패키지 외부에서 Memento 내부를 변경할 수 없도록 하기 위함이다.

```java
package game;
import java.util.*;

public class Gamer {
    private int money;
    private List fruits = new ArrayList();
    private Random random = new Random();
    private static String[] fruitsname = {
            "사과","포도","바나나","귤"
    };
    public Gamer(int money){
        this.money = money;
    }
    public int getMoney(){
        return money;
    }
    public void bet() {
        int dice = random.nextInt(6) + 1;
        if (dice == 1) {
            money += 100;
            System.out.println("소지금이 증가하였습니다.");
        } else if (dice == 2) {
            money /= 2;
            System.out.println("소지금이 절반이 되었습니다.");
        } else if (dice == 6) {
            String f = getFruit();
            System.out.println("과일(" + f + ")을(를) 받았습니다.");
        } else {
            System.out.println("변한 것이 없습니다.");
        }
    }
    public Memento createMemento() {
        Memento m = new Memento(money);
        Iterator it = fruits.iterator();
        while (it.hasNext()){
            String f = (String) it.next();
            if(f.startsWith("맛있는")){
                m.addFruits(f);
            }
        }
        return m;
    }
    public void restoreMemento(Memento memento){
        this.money = memento.money;
        this.fruits = memento.getFruits();
    }
    public String toString(){
        return "[money = " + money + ", fruits = " + fruits + "]";
    }
    private String getFruit() {
        String prefix = "";
        if (random.nextBoolean()) {
            prefix = "맛있는";
        }
        return prefix + fruitsname[random.nextInt(fruitsname.length)];
    }
}
```
Gamer 클래스는 주인공을 나타내는 클래스이다. 소지금, 과일 그리고 난수발생기를 갖고 있다. 그리고 클래스 필드로 fruitsname 변수를 갖고 있다. 여기서 제일\
중요한 메소드는 bet 메소드인데, 이 bet 메소드 안에서 주인공의 소지금과 과일획득 유무를 처리한다.\
createMemento 는 현재 상태를 저장하는 메소드이다. 여기서는 Memento를 만드는데, 현재 상태의 소지금과 과일들을 갖고 Memento 인스턴스를 생성하고 있다.\
이렇게 생성된 Memento는 현재 주인공의 상태를 마치 저장하듯이 갖고 있는 것이다. restoreMemento는 createMemento 메소드의 반대로 undo를 실행시키는 \
메소드이다. 제공된 Memento 메소드를 기초로 상태를 복원하는 역할을 한다.

```java
import game.Memento;
import game.Gamer;

public class Main {
    public static void main(String[] args){
        Gamer gamer = new Gamer(100);
        Memento memento = gamer.createMemento();
        for (int i = 0; i < 100; i++) {
            System.out.println("==== " + i);
            System.out.println("현상: " + gamer);
            
            gamer.bet();

            System.out.println("소지금은 " + gamer.getMoney() + "원이 되었습니다.");
            
            if (gamer.getMoney() > memento.getMoney()){
                System.out.println(" (많이 증가했으므로 현재의 상태를 저장하자)");
                memento = gamer.createMemento();
            } else if (gamer.getMoney() < memento.getMoney() / 2) {
                System.out.println(" (많이 감소했으므로 이전의 상태로 복원하자)");
                gamer.restoreMemento(memento);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie){
                
            }
            System.out.println("");
        }
    }
}
```
Main 클래스에서는 Gamer의 인스턴스를 생성해서 게임을 실행한다. 반복문을 톨애 gamer의 bet 메소드를 호출하여 상태 변화를 출력하고 있다. 다만 시작부분에서\
gamer의 최초 Memento 인스턴스를 생성한 후, 금액에 따라 memento 를 저장 혹은 복원작업을 계속 진행한다.

결과는 다음과 같다.

> ==== 0\
> 현상: [money = 100, fruits = []]\
> 소지금이 절반이 되었습니다.\
> 소지금은 50원이 되었습니다.\
>\
> ==== 1\
> 현상: [money = 50, fruits = []]\
> 과일(맛있는귤)을(를) 받았습니다.\
> 소지금은 50원이 되었습니다.\
>\
> ==== 2\
> 현상: [money = 50, fruits = []]\
> 변한 것이 없습니다.\
> 소지금은 50원이 되었습니다.\
>\
> ==== 3\
> 현상: [money = 50, fruits = []]\
> 변한 것이 없습니다.\
> 소지금은 50원이 되었습니다.\
>\
> ==== 4\
> 현상: [money = 50, fruits = []]\
> 변한 것이 없습니다.\
> 소지금은 50원이 되었습니다.\
>\
> ==== 5\
> 현상: [money = 50, fruits = []]\
> 변한 것이 없습니다.\
> 소지금은 50원이 되었습니다.\
>\
> ==== 6\
> 현상: [money = 50, fruits = []]\
> 변한 것이 없습니다.\
> 소지금은 50원이 되었습니다.\
>\
> ==== 7\
> 현상: [money = 50, fruits = []]\
> 과일(맛있는포도)을(를) 받았습니다.\
> 소지금은 50원이 되었습니다.\
> ...

### 패턴 사용시 고려할 점
Memento 클래스에서는 접근제한자가 public 인 메소드가 getMoney 한개 뿐이다. 자바는 기본적으로 접근제한자가 없을 경우 같은 패키지 내에서만 접근 가능하다.\
이럴 경우 Main 클래스에서는 Memento 클래스에서 사용할 수 있는 메소드가 getMoney 로 제한되게 된다. Main 클래스에서 함부로 Memento 클래스의 내용을 \
변경할 수 없게 된다는 이야기이다. 인스턴스 또한 만들수가 없다. 생성자 역시 같은 패키지 내에서만 접근할 수 있도록 제한하였기 때문이다. 그렇기 때문에\
Main 클래스 내에서는 Gamer 클래스를 통해 인스턴스를 생성하게 된다. 이렇게 접근을 제한해주어야 Memento 클래스의 무결성을 확보할 수 있게 된다.\
\
Memento 클래스의 경우 위의 예제와 같이 메모리상에 저장해 둘 경우에는 별다른 문제가 없지만, 파일로서 저장할 경우에는 문제가 생길 수 있게 된다. 프로그램의\
버전 업 등으로 인해 기존의 Memento 파일과 내용이 맞지 않는 경우가 생길 수 있기 때문이다. 파일로 남기게 될 경우에는 유효성 체크가 필수적이다.\
