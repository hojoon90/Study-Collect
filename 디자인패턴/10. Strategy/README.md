# Strategy 패턴

### Strategy 패턴이란
Strategy는 '전략'이란 뜻을 가지고 있다. 프로그래밍적인 의미로 생각하자면 '문제를 해결하고자 하는 방법' 정도의 의미가 알맞을 것 같다.\
모든 프로그램은 알고리즘이 적용되어 있다. 문제를 해결하기 위해 적용되는 것이다. Strategy 패턴은 구현되어있는 알고리즘 부분들을 모두 교환할 수 있다.\
알고리즘을 교체하여 같은 문제를 다른 방법으로 해결할 수 있도록 해주는 패턴이 바로 Strategy 패턴이다.

### 예제 코드
'가위바위보'를 실행하는 예제코드를 만들어 보자. 이 예제에서는 두가지 가위바위보 전략이 나온다. 하나는 '이길 경우 다음에도 같은 손을 낸다'라는 전략과\
다른 하나는 '직전에 냈던 손에서 다음 낼 손을 확률적으로 계산한다'는 전략이다.
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
getHand 메소드를 이용하여 인스턴스를 얻을 수 있다. 가위바위보의 승패를 확인하는 메소드는 isStrongerThan과 isWeakerThan이며,\
실제 처리는 아래 fight 메소드에서 처리해준다. fight 안에서는 if 절에 따라 값을 리턴해주는데, 중간의 
>(this.handValue + 1) % 3 == h.handValue

는 this.handValue가 h.handValue를 이길 경우를 나타낸다. 조금 자세히 설명하자면 this.handValue가 바위라고 생각해보자.\
this.handValue는 0이고, 0+1 을 3으로 나눈 나머지는 1이 나온다. 1은 가위를 의미한다.\
즉 (0+1)%3 의 값이 1과 같으면 this.handValue가 바위(0)이고 h.handValue가 가위(1)이니 this.handValue가 이기는 case로 볼 수 있다.\
이 Hand 클래스는 아래 나올 다른 클래스들에서 사용되지만, Strategy 패턴에 포함되진 않는다.

### Strategy 코드들

