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
프로그램이라는 <program>을 나타내는 ProgramNode 클래스이다. 이 클래스에는 Node 형을 갖는 commandListNode 변수가 있다. 이 변수는 <command list> 에 대응하는
구조를 저장하기 위한 것이다. parse 메소드에서는 인자로 들어오는 context에서 program 이라는 단어를 건너뛰고 있다.(skipToken 메소드) 그리고 만약 program이라는 단어가
없다면 ParseException 예외를 던진다. 그리고 commandListNode 의 parse 메소드를 호출한다. <command list>가 어떤 내용으로 되어있는지는 ProgramNode 클래스의 메소드에는
기술되어있지 않다. toString 메소드는 이 노드의 문자열 표현을 기술하기 위한 것이다. 

```java
public class CommandListNode extends Node {
    private ArrayList list = new ArrayList();
    public void parse(Context context) throws ParseException{
        while(true){
            if(context.currentToken() == null){
                throw new ParseException("missing 'end'");
            }else if(context.currentToken().equals("end")){
                context.skipToken("end");
            }else{
                Node commandNode = new CommandNode();
                commandNode.parse(context);
                list.add(commandNode);
            }
        }
    }
    public String toString(){
        return list.toString();
    }
}
```
다음은 <command list>를 나타내는 CommandListNode 클래스이다. <command list>는 <command>가 0번 이상 반복하고 마지막에 end 가 온다. 반복하는 <command>를
저장하기 위해 CommandListNode 클래스는 list를 갖고 있다. 이 필드에 CommandNode 클래스의 인스턴스를 저장한다. parse 메소드에서는 인자로 넘어온 context의
currentToken()의 값이 null이면 이미 남아있는 토큰은 없게 되는 것이므로 ParseException에 end가 없다는 메세지를 붙여 예외를 던진다. 그리고 만약 현재의 토큰이
end 이면, <commend list>의 마지막에 도달했다는 것이며, 이때는 end 를 건너뛰고 나서 while문을 break한다.

```java
public class CommandNode extends Node {
    private Node node;
    public void parse(Context context) throws ParseException {
        if(context.currentToken().equals("repeat")){
            node = new RepeatCommandNode();
            node.parse(context);
        }else{
            node = new PrimitiveCommandNode();
            node.parse(context);
        }
    }
    public String toString(){
        return node.toString();
    }
}
```
CommandNode 클래스도 위의 클래스들과 크게 다르지 않다. Node 형의 변수 node 는 <repeat command>에 대응하는 RepeatCommandNode 클래스의 인스턴스,
또는 <primitive command>에 대응하는 PrimitiveCommandNode 클래스의 인스턴스를 저장하기 위해 사용된다.

```java
public class RepeatCommandNode extends Node {
    private int number;
    private Node commandListNode;
    public void parse(Context context) throws ParseException{
        context.skipToekn("repeat");
        number = context.currentNumber();
        context.nextToken();
        commandListNode = new CommandListNode();
        commandListNode.parse(context);
    }
    public String toString(){
        return "[repeat" + number + "" + commandListNode + "]";
    }
}
```
RepeatCommandNode 클래스는 <repeat command>에 대응한다. parse 메소드를 따라가보면 다음과 같다.
> RepeatCommandNode 의 parse 메소드의 안에서는 CommandListNode 의 인스턴스를 만들어, parse 메소드를 호출하고\
> CommandListNode 의 parse 메소드의 안에서는 CommandNode 의 인스턴스를 만들어, parse 메소드를 호출하고\
> CommandNode 의 parse 메소드 안에서는 RepeatCommandNode 의 인스턴스를 만들어, parse 메소드를 호출하고\
> RepeatCommandNode 의 parse 메소드 안에서는...

이 parse 메소드의 마지막은 terminal expression이다. CommandNode의 parse 메소드 안의 if문에 의해 언젠가는 RepeatCommandNode 가 아니라 PrimitiveCommandNode를
만드는 쪽으로 진행한다. 그리고 PrimitiveCommandNode의 parse 메소드의 안에서는 다른 parse 메소드를 호출하지 않는다. 재귀 호출로 인해 무한루프가 될 것 같지만
언젠가는 terminal expression에 도달하게 된다. 만약 도달하지 않는가면 그것은 정의가 잘못 된 것이다.

```java
public class PrimitiveCommandNode extends Node {
    private String name;
    public void parse(Context context) throws ParseException{
        name = context.currentToken();
        context.skipToken(name);
        if(!name.equals("go") && !name.equals("right") && !name.equals("left")){
            throw new ParseException(name + " is undifined");
        }
    }
    public String toString(){
        return name;
    }
}
```
PrimitiveCommandNode 클래스의 parse 메소드에서는 다른 parse 메소드를 호출하지 않는다.

```java
public class Context{
    private StringTokenizer tokenizer;
    private String currentToken;
    public Context(String text){
        tokenizer = new StringTokenizer();
        nextToken();
    }
    public String nextToken(){
        if(tokenizer.hasMoreTokens()){
            currentToken = tokenizer.nextToken();
        } else {
          currentToken = null;  
        }
        return currentToken;
    }
    public String currentToken(){
        return currentToken;
    }
    public void skipToken(String token) throws ParseException {
        if(!token.equals(currentToken)){
            throw new ParseException("Warning: " + token + "is expected, but " + currentToken + " is found.");
        }
        nextToken();
    }
    public int currentNumber() throws ParseException {
        int number = 0;
        try{
            number = Integer.parseInt(currentToken);
        }catch (NumberFormatException e) {
            throw new ParseException("Warning: "+e);
        }
        return number;
    }
}
```
Context 클래스는 구문해석을 위해 필요한 메소드를 제공한다. nextToken 은 다음의 토큰을 얻는다. currentToken 은 현재 토큰을 얻으며, skipToken은 
현재 토큰을 검사한 후, 다음 토큰을 얻는다. 마지막으로 currentNumber 는 현재의 토큰을 수치로 얻는다.

```java
public class ParseException extends Exception{
    public ParseException(String msg){
        super(msg);
    }
}
```
ParseException 은 구문해석 안의 예외를 위한 클래스이다.

```java
public class Main {
    public static void main(String[] args){
        try{
            BufferReader reader = new BufferedReader(new FileReader("program.txt"));
            String text;
            while((text = reader.readLine()) != null){
                System.out.println("text = \"" + text + "\"");
                Node node = new ProgramNode();
                node.parse(new Context(text));
                System.out.println("node = " + node);
            }
        }catch (Exception e){
            e.PrintStackTrace();
        }
    }
}
```
Main은 위의 미니 언어의 인터프리터를 작동시키기 위한 클래스이다. "program.txt"라는 파일을 읽어와 구문해석을 한 후 그 결과를 문자열로 표시한다.
표시 중 text = 로 시작하는 부분이 주어진 미니 프로그램이며, node = 로 시작하고 있는 부분이 구문해석 후의 표시이다. 