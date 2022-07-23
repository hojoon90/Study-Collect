# Interpreter 패턴

### Interpreter 패턴이란

디자인 패턴 목적중의 하나는 클래스의 재이용성을 높이기 위함이다. 재이용성이란 한 번 작성한 클래스를 별로 수정하지 않고 몇번이고 사용할 수 있도록 하는 것이다.
Interpreter 패턴은 프로그램이 해결하려는 문제를 간단한 '미니 언어'로 표현한다. 미니 프로그램은 그 자체만으로는 동작하지 않아서 Java 언어로 '통역'역할을 하는
프로그램을 만들어둔다. 이 통역 프로그램을 인터프리터라고 부른다. 문제가 생길 경우엔 미니 프로그램쪽을 수정하여 대처한다.

### 미니 언어
예제 프로그램을 만들기 전에 미니언어에 대해 간단하게 알아보자. 여기에서는 무선 조종으로 움직이는 자동차를 예시로 한다. 자동차를 움직이는 기능은 아래 4가지 뿐이다.
> 앞으로 1미터 전진(go)\
> 우회전(right)\
> 좌회전(left)\
> 반복(repeat)

이 세가지가 자동차에 대해 할 수 있는 명령이다. go는 1미터 전진하고 멈추는 명령어이며 left, right 는 각각 그자리에서 좌,우로 도는 명령어다. repeat 는 반복하는 명령어이다.
이 명령어들을 조합하여 자동차가 움직이는 언어가 바로 미니언어이다.

미니 언어로 작성된 미니 프로그램의 예를 몇가지 살펴보자. 아래 언어는 자동차를 앞으로 전진시키고 멈추게 하는 미니 프로그램이다.
> program go end

프로그램의 시작과 종료를 알 수 있게 언어를 program과 end 사이에 작성하기로 한다. 다음은 자동차가 앞으로 전진한 후 제자리에서 오른쪽으로 회전해서 되돌아오는
미니 프로그램이다. 
> program go right right go end

다음은 정사각형을 그리고 제자리로 돌아오는 미니 언어이다.
> program go right go right go right go right end

위 언어 중 마지막에 right를 포함하는 것은 자동차의 방향을 처음으로 돌리기 위해서이다. 위 언어의 경우 'go right'를 4번 반복하고 있다. 아래와 같이 변경할 수 있다.
> program repeat 4 go right end end

위 언어에서 처음 end 는 반복문의 마지막을 나타내고 그다음 end 는 program의 끝을 나타낸다. 아래와 같은 모습이다.
> program\
> &nbsp;&nbsp;&nbsp;&nbsp; repeat\
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 4\
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; go\
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; right\
> &nbsp;&nbsp;&nbsp;&nbsp; end\
> end

여기서 미니 언어의 문법을 확인해보자. 여기서 사용할 표기법은 'BNF'라고 불리는 것의 변형이다. 
> &lt;program&gt; : : = program &lt;command list&gt;\
> &lt;command list&gt; : : = &lt;command&gt;&#42; end\
> &lt;command&gt; : : = &lt;repeat command&gt; | &lt;primitive command&gt;\
> &lt;repeat command&gt; : : = repeat &lt;number&gt; &lt;command list&gt;\
> &lt;primitive command&gt; : : = go | right | left

하나씩 아래에서 살펴보자
> &lt;program&gt; : : = program &lt;command list&gt;

여기에서는 프로그램이라는 '&lt;program&gt;'을 정의한다. '&lt;program&gt;이란 program 이라는 단어 뒤에 커맨드 리스트인 &lt;command list&gt;가 이어진 것'을 나타낸 것이다.
: : = 의 왼쪽이 정의되는 대상이며, 오른쪽이 정의되는 내용이다.

> &lt;command list&gt; : : = &lt;command&gt;&#42; end

여기는 커맨드리스트를 정의한다. '&lt;command list&gt;는 &lt;command&gt;가 0개이상 반복된 후 end가 오는 것'이라고 정의한다. * 는 바로 직전의 것을 0번이상 반복한다는 뜻이다.

> &lt;command&gt; : : = &lt;repeat command&gt; | &lt;primitive command&gt;

&lt;command&gt;는 반복 커맨드 &lt;repeat command&gt; 또는 기본 커맨드 &lt;primitive command&gt; 둘 중 하나 라고 정의하고 있다. | 는 '또는'을 나타낸다.

> &lt;repeat command&gt; : : = repeat &lt;number&gt; &lt;command list&gt;

&lt;repeat command&gt;는 repeat이라는 단어 뒤에 반복횟수 &lt;number&gt;가 이어지고 다시 &lt;command list&gt;가 이어진 것으로 정의하고 있다. 
여기서 &lt;command&gt; 정의 중 &lt;repeat command&gt;가 사용되었으며, &lt;repeat command&gt; 정의 중에 &lt;command list&gt;가 사용되고 있다.
이처럼 어떤 정의 도중에 자신이 등장하는 정의를 '재귀적인 정의'라고 한다.

> &lt;primitive command&gt; : : = go | right | left

기본 커맨드인 &lt;primitive command&gt;를 정의하고 있다. &lt;primitive command&gt;는 go 또는 left 또는 right 라고 정의하고 있다.

### 예제 코드
여기에서 만들 코드는 위의 미니 언어를 구문해석한 프로그램이다. 문자열로 구성된 미니 프로그램을 분해해서 각 부분이 어떤 구조로 되어있는지 해석하는 것이 구문해석이다.
```java
public abstract class Node{
    public abstract void parse(Context context) throws ParseException;
}
```
Node 클래스는 구문 트리의 각 부분을 구성하는 최상위 클래스이다. 여기에 선언된 parse 메소드는 '구문해석이라는 처리를 실행하기'위한 메소드이다. 해당 메소드는 
Node 클래스를 상속 받는 클래스들에서 구현된다. 인자로 있는 context는 구문해석을 실행하고 있는 '상황'을 나타내는 클래스이다. 그리고 구문 해석중 예외가 발생할 경우
ParseException 을 throw 한다.

```java
public class ProgramNode extends Node {
    private Node commandListNode;
    public void parse(Context context) throws ParseException {
        context.skipToken("program");
        commandListNode = new CommandListNode();
        commandListNode.parse(context);
    }
    public String toString(){
        return "[program " + commandListNode + ']';
    }
}
```