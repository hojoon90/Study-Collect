# Strategy 패턴

### Strategy 패턴이란
Strategy는 '전략'이란 뜻을 가지고 있다. 프로그래밍적인 의미로 생각하자면 '문제를 해결하고자 하는 방법' 정도의 의미가 알맞을 것 같다.\
모든 프로그램은 알고리즘이 적용되어 있다. 문제를 해결하기 위해 적용되는 것이다. Strategy 패턴은 구현되어있는 알고리즘 부분들을 모두 교환할 수 있다.\
알고리즘을 교체하여 같은 문제를 다른 방법으로 해결할 수 있도록 해주는 패턴이 바로 Strategy 패턴이다.

### 예제 코드
'가위바위보'를 실행하는 예제코드를 만들어 보자. 이 예제에서는 두가지 가위바위보 전략이 나온다. 하나는 '이길 경우 다음에도 같은 손을 낸다'라는 전략과 
다른 하나는 '직전에 냈던 손에서 다음 낼 손을 확률적으로 계산한다'는 전략이다.\
먼저 손을 나타내는 Hand 클래스부터 작성한다.

```java
public class Hand{
    public static final int HANDVALUE_GUU=0;    // 바위
    public static final int HANDVALUE_CHO=1;    // 가위
    public static final int HANDVALUE_PAA=2;    // 보
    public static final Hand[] hand = {
            new Hand(HANDVALUE_GUU),
            new Hand(HANDVALUE_CHO),
            new Hand(HANDVALUE_PAA),
    };
    private static final String[] name = {
            "바위", "가위", "보",
    };
    private int handValue;
    private Hand(int handValue){
        this.handValue = handValue;
    }
    public static Hand getHand(int handValue){
        return hand[handValue];
    }
    public boolean isStrongerThan (Hand h){
        return fight(h) == 1;
    }
    public boolean isWeakerThan (Hand h){
        return fight(h) == -1;
    }
    private int fight(Hand h){
        if(this == h){
            return 0;
        } else if ((this.handValue + 1) % 3 == h.handValue){
            return 1;
        } else{
            return -1;
        }
    }
    public String toString(){
        return name[handValue];
    }
}
```
Hand 클래스의 바위, 가위, 보는 각각 0,1,2로 표현한다. 인스턴스 생성시 hand 배열에 각각의 Hand의 값 (0,1,2)가 저장된다.\
getHand 메소드를 이용하여 인스턴스를 얻을 수 있다. 가위바위보의 승패를 확인하는 메소드는 isStrongerThan과 isWeakerThan이며, 
실제 처리는 아래 fight 메소드에서 처리해준다. fight 안에서는 if 절에 따라 값을 리턴해주는데, 중간의 
>(this.handValue + 1) % 3 == h.handValue

는 this.handValue가 h.handValue를 이길 경우를 나타낸다. 조금 자세히 설명하자면 this.handValue가 바위라고 생각해보자.\
this.handValue는 0이고, 0+1 을 3으로 나눈 나머지는 1이 나온다. 1은 가위를 의미한다.\
즉 (0+1)%3 의 값이 1과 같으면 this.handValue가 바위(0)이고 h.handValue가 가위(1)이니 this.handValue가 이기는 case로 볼 수 있다.\
이 Hand 클래스는 아래 나올 다른 클래스들에서 사용되지만, Strategy 패턴에 포함되진 않는다.

### Strategy 코드들

다음은 가위바위보의 전략을 위한 추상 메소드와 구현 메소드들을 작성해준다.
```java
public interface Strategy {
    public abstract Hand nextHand();
    public abstract void study(boolean win);
}
```
위의 추상클래스 Strategy 는 각 전략들을 구현하기 위해 만든 인터페이스이다. 인터페이스 내부에 nextHand 와 study 라는 추상메소드가 존재하는데, 
nextHand 메소드는 다음에 낼 손을 리턴하는 메소드이며, study 메소드는 이겼는지 졌는지에 대해 학습하는 메소드이다. 이 study 메소드에 따라 
nextHand 메소드의 반환값이 결정된다.

```java
public class WinningStrategy implements Strategy{
    private Random random;
    private boolean won = false;
    private Hand prevHand;
    public WinningStrategy(int seed){
        random = new Random(seed);
    }
    @Override
    public Hand nextHand(){
        if(!won){
            prevHand = Hand.getHand(random.nextInt(3));
        }
        return prevHand;
    }
    @Override
    public void study(boolean win){
        won = win;
    }
}
```
WinningStrategy 클래스는 위에서 설명했던 것과 같이 이전 가위바위보에서 이겼다면 다음번에도 똑같은 손을 내는 클래스이다. 클래스를 자세히 보면 
Strategy 클래스를 implements 하고 있다. 그렇기 때문에 아래 nextHand 와 study 메소드가 구현되어 있는 것을 확인할 수 있다.\
nextHand 에서는 가위바위보에 졌을 경우 난수를 생성하여 새로운 손을 내며, study 메소드에서는 이겼을 때 전과 똑같은 손을 낼 수 있도록 boolean 값을 
세팅해준다.

```java
public class ProbStrategy implements Strategy {
    private Random random;
    private int prevHandValue = 0;
    private int currentHandValue = 0;
    private int [][] history = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1},
    };
    public ProbStrategy(int seed){
        random = new Random(seed);
    }
    @Override
    public Hand nextHand(){
        int bet = random.nextInt(getSum(currentHandValue));
        int handValue = 0;
        if (bet < history[currentHandValue][0]){
            handValue = 0;
        } else if (bet < history[currentHandValue][0] + history[currentHandValue][1]){
            handValue = 1;
        }else{
            handValue = 2;
        }
        prevHandValue = currentHandValue;
        currentHandValue = handValue;
        return Hand.getHand(handValue);
    }
    private int getSum(int hv){
        int sum = 0;
        for (int i = 0; i < 3; i++){
            sum += history[hv][i];
        }
        return sum;
    }
    @Override
    public void study(boolean win){
        if(win){
            history[prevHandValue][currentHandValue]++;
        } else {
            history[prevHandValue][(currentHandValue + 1) % 3]++;
            history[prevHandValue][(currentHandValue + 2) % 3]++;
        }
    }
}
```
ProbStrategy 클래스는 다음손은 난수로 결정하지만, 과거 승패 이력을 이용해 각각 손을 낼 확률을 바꾸는 클래스이다. 이력을 확인하는 변수는 
history 인데, 과거의 승패를 반영한 확률 계산을 하기 위한 표를 만든다. history를 조금 자세히 설명하자면
>history[이전에 낸 손][이번에 낼 손]

이 식의 값이 크면 클 수록 과거의 승률이 높다는 뜻이다. 예를들어 history[0][0]은 바위, 바위를 냈을때 과거의 승수이다. 만약 이전에 주먹을 냈을 때,\
history[0][0] (바위),\
history[0][1] (가위),\
history[0][2] (보)\
의 승률을 각각 구해서 어떤 게 더 이길 확률이 높은지 구하는 것이다. 만약 \
history[0][0] 의 값이 3\
history[0][1] 의 값이 5\
history[0][2] 의 값이 7\
일 경우 각각 3:5:7의 확률로 바위나 가위 또는 보를 선택하게 된다. 그 확률을 구하기 위해 먼저 getSum 메소드로 값들을 모두 더한 후,
nextHand 메소드에서 랜덤으로 나오는 수에 따라 바위 가위 보를 정하게 된다. 그내용에 해당하는 변수가 'bet' 변수이다. 그러고 나서 랜덤으로 나온
숫자가 0-3사이이면 주먹, 3-8사이이면 가위, 8-15사이이면 보를 내게 된다.

### 실제 가위바위보를 하는 Player와 Main

Player 클래스는 실제 가위바위보를 하는 플레이어이다. 
```java
public class Player {
    private String name;
    private Strategy strategy;
    private int winCount;
    private int loseCount;
    private int gameCount;
    public Player(String name, Strategy strategy){
        this.name = name;
        this.strategy = strategy;
    }
    public Hand nextHand(){
        return strategy.nextHand();
    }
    public void win(){
        strategy.study(true);
        winCount++;
        gameCount++;
    }
    public void lose(){
        strategy.study(false);
        loseCount++;
        gameCount++;
    }
    public void even(){
        gameCount++;
    }
    public String toString(){
        return "[" + name + ":" + gameCount + "games, " + winCount + "win, " + loseCount + "lose" + "]";
    }
}
```
nextHand 메소드는 각 플레이어가 가진 전략에 있는 nextHand 를 가져온다. 즉 Strategy 클래스에 있는 nextHand를 호출하는데, 앞장에서 보았던 
**위임**을 하고 있다. 플레이어는 이길 경우와 질 경우 각각 study를 통해 전략을 다시 세우고 세운 전략으로 선택한 손 중 하나를 다시 내게 된다.

```java
public class Main {
    public static void main(String[] args){
        if (args.length != 2){
            System.out.println("Usage: java Main randomSeed1 randomSeed2");
            System.out.println("Example: java Main 314 15");
            System.exit(0);
        }
        int seed1 = Integer.parseInt(args[0]);
        int seed2 = Integer.parseInt(args[1]);
        Player player1 = new Player("두리", new WinningStrategy(seed1));
        Player player2 = new Player("하나", new ProbStrategy(seed2));
        for (int i = 0; i < 10000; i++) {
            Hand nextHand1 = player1.nextHand();
            Hand nextHand2 = player2.nextHand();
            if (nextHand1.isStrongerThan(nextHand2)){
                System.out.println("Winner: "+player1);
                player1.win();
                player2.lose();
            }else if (nextHand2.isStrongerThan(nextHand1)){
                System.out.println("Winner: "+player2);
                player2.win();
                player1.lose();
            }else{
                System.out.println("Even...");
                player1.even();
                player2.even();
            }
        }
        System.out.println("total Result: ");
        System.out.println(player1.toString());
        System.out.println(player2.toString());
    }
}
```
Main 클래스에서는 총 10,000 번을 실행하여 결과를 표시해준다.

### 고려해보아야 할 사항들
Strategy 패턴은 실제 알고리즘이 들어가는 부분들을 별도로 분리하여 사용하는 패턴이다. \
위에서 보았던 WinningStrategy와 ProbStrategy 클래스 모두 Strategy 인터페이스를 상속 받아 구현된 클래스들이다. \
또한 Player 클래스를 보면 Strategy 인터페이스의 메소드를 호출 하고 있는데 위임을 통해서 생성된 인스턴스에 맞는 알고리즘이 실행된다. \
이렇게 되면 사용자가 추가적인 알고리즘을 만들고 싶을 때 굉장히 유용해진다. \
Strategy 인터페이스를 상속 받는 새로운 클래스를 만들어주고 해당 클래스를 사용하도록 바꿔주기만 하면 되기 때문에 편하게 알고리즘 변경이 가능하다.
