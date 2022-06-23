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