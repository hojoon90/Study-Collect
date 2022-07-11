# Flyweight 패턴

### Flyweight 패턴이란
Flyweight 패턴은 오브젝트를 가볍게 하기 위한 패턴이다. 여기에서 말하는 가볍게란 메모리 사용량을 의미한다. 우리가 일상생활에서 컴퓨터에 부하가 많이가는 작업을 하면
'무거운 작업을 한다.'라는 표현을 사용한다. 이는 메모리를 많이 사용하는 작업을 의미한다. 여기서도 많은 메모리를 사용하는 오브젝트를 '무겁다'라고 표현하며 반대로 적은
메모리를 사용하는 오브젝트를 '가볍다'라고 한다.\
\
Java는 new 라는 표현을 사용하여 인스턴스를 만들 수 있다. 이때, 생성되는 인스턴스를 저장하기 위한 메모리가 확보된다. 만약 인스턴스를 많이 생성해야하는 경우에는
그만큼 메모리를 많이 사용하게 된다. Flyweight 패턴은 이렇게 **인스턴스를 많이 생성하지 않고 최대한 공유하여 사용하는 패턴** 이다.

### 예제 코드
예제 프로그램에서는 무거운 인스턴스를 만드는 클래스로 큰 문자를 표현하는 프로그램을 만들어본다. 
```java
import java.io.*;

public class BigChar{
    //문자의 이름
    private char charName;
    //큰 문자를 표현하는 문자열 ('#' '.' '\n'의 열)
    private String fontData;
    public BigChar(char charName){
        this.charName = charName;
        try{
            BufferedReader reader = new BufferedReader(
                    new FileReader("big" + charName + ".txt")
            );
            String line;
            StringBuffer sbf = new StringBuffer();
            while((line = reader.readLine()) != null){
                sbf.append(line);
                sbf.append("\n");
            }
            reader.close();
            this.fontData = sbf.toString();
        } catch (IOException e) {
            this.fontData = charName + "?";
        }
    }
    // 큰 문자를 표현한다.
    public void print(){
        System.out.println(fontData);
    }
}
```
BigChar 클래스는 '큰 문자'를 나타내는 클래스이다. 생성자에서 인수로 주어진 문자의 큰 문자 버전을 작성한다. 작성된 문자열은 fontData 변수에 저장된다. 
큰 문자를 구성하고 있는 데이터는 파일로 준비한다. 만약 파일이 없을 경우 문자 뒤에 '?'를 붙여준다. 여기서는 아직 Flyweight 패턴중 공유에 대한 내용은 아직 나오지 않았다.
공유에 대한 제어는 아래 나올 BigCharFactory 클래스에서 나온다.

```java
public class BigCharFactory{
    //이미 만들어진 BigChar 인스턴스 관리
    private HashMap pool = new HashMap();
    //Singleton 패턴
    private static BigCharFactory singleton = new BigCharFactory();
    //생성자
    private BigCharFactory(){
    }
    //인스턴스 get
    public static BigCharFactory getInstance(){
        return singleton;
    }
    //BigChar 인스턴스 생성
    public synchronized BigChar getBigChar(char charName){
        BigChar bc = (BigChar) pool.get("" + charName);
        if(bc == null){
            bc = new BigChar(charName); //BigChar 인스턴스 생성
            pool.put("" + charName, bc);
        }
        return bc;
    }
}
```
BigCharFactory 클래스는 BigChar의 인스턴스를 생성하는 공장이다. pool 변수는 현재까지 만들어진 BigChar 인스턴스를 관리한다. 이 pool에는 BigChar의 인스턴스가
모여있다. 해당 인스턴스들은 HashMap으로 저장하고 있으며, 해당하는 문자를 Key로 하여 인스턴스를 저장하고 있다. BigCharFactory 클래스는 싱글턴으로 구성되어있는데,
BigCharFactory 클래스 인스턴스는 하나만 있으면 되기 때문이다. 여기서 getBigChar 메소드는 Flyweight 패턴의 중심이 되는 메소드이다. 여기서 인자로 제공된 문자에 
대응하는 BigChar 인스턴스를 만들어준다. 다만 이미 만들어진 인스턴스가 있으면 갖고 있던 인스턴스를 반환하게 된다.\
\
로직을 자세히 보면 pool.get 으로 주어진 문자에 대응하는 BigChar 인스턴스가 존재하는지 확인한다. 만약 없다면(null 이면) 인스턴스가 만들어진 적이 없기 때문에
새로운 인스턴스를 만든다. 그리고 그 인스턴스를 pool 변수에 저장해둔다. 이후 같은 문자를 다시 호출하면 pool 변수에서 해당 값을 가져와서 리턴해준다. 한가지 특이한 점은
위 메소드는 synchronized로 되어있는데, 이는 인스턴스의 중복 생성을 막기 위함이다. 만약 거의 동시에 여러 스레드에서 같은 문자를 호출했다고 해보자. 근데 해당 문자가
pool 변수에 등록되어있지 않은 문자일 경우 해당 문자를 호출한 스레드 모두 if 문으로 접근하게 될 것이다. 이렇게 되면 같은 문자의 인스턴스를 여러번 생성하는 문제가 생기게 된다.
이를 방지하기 위헤 메소드에 synchronized를 걸어 스레드가 동시에 호출되는걸 방지하는 것이다.

```java
public class BigString {
    private BigChar[] bigChars;
    public BigString(String string){
        bigChars = new BigChar[string.length()];
        BigCharFactory factory = BigCharFactory.getInstance();
        for(int i = 0; i< bigChars.length; i++){
            bigChars[i] = factory.getBigChar(string.charAt(i));
        }
    }
    public void print(){
        for (int i = 0; i < bigChars.length; i++) {
            bigChars[i].print();
        }
    }
}
```
BigString 클래스는 BigChar를 모은 '큰 문자열' 클래스이다. bigChars 변수는 BigChar 인스턴스를 모아두는 배열이다. 생성자에서 for문을 이용하여 factory에서
 인스턴스들을 가져오는 것을 볼 수 있다.

```java
public class Main {
    public static void main(String[] args){
        if(args.length == 0){
            System.out.println("Usage: java Main digits");
            System.out.println("Example: java Main 1212123");
            System.exit(0);
        }
        BigString bs = new BigString(args[0]);
        bs.print();
    }
}
```
Main 클래스에서는 입력받은 값을 출력해주는 역할만 하고 있다.

### 패턴 사용시 고려할 점
Flyweight 패턴은 인스턴스를 공유하는 것이 핵심이다. 여기서 가장 주의해야할 점은 공유하고 있는 것을 변경하면 여러 곳에 영향이 미쳐진다는 것이다. 하나의 인스턴스를 변경하면
그 인스턴스를 사용하고 있는 여러 장소에 영향이 미쳐지게 되는 것이다. 그렇기 때문에 Flyweight 역할을 하는 클래스에 제공하는 정보는 신중히 선택해야 한다.
여러 장소에 공유시켜야 할 정보만을 제공하는 것이 좋다.

공유시키는 정보와 공유시키지 않는 정보는 각각 intrinsic/extrinsic 이라고 한다. 먼저 공유시키는 정보 부터 설명하자면 **인스턴스를 어디에서 가지고 있더라고 변하지 않는 정보**
즉, 상태에 의존하지 않는 정보를 나타낸다. 위의 예제코드 중 BigChar 클래스의 필드 데이터는 BigString의 어디에 등장해도 변하지 않는다. intrinsic한 정보인 것이다.
반면 공유시키지 않는 정보인 extrinsic한 정보는 **인스턴스를 두는 장소에 따라 변화하는 정보,** 상태에 의존하는 정보이다. BigChar 인스턴스가 BigString의 몇 번째 문자인가 하는
정보는 BigChar가 놓이는 장소에 따라 변하기 때문에 BigChar에게 제공할 수 없다. 이러한 정보는 extrinsic한 정보가 된다.

이렇게 Flyweight 패턴을 사용하게 되면 인스턴스를 공유하게 되기 때문에 리소스를 줄일 수 있게 된다. 만약 우리가 인스턴스를 새로 생성하는 new가 일정 시간이 걸린다고 생각해보자.
이렇게 선언할 때 마다 일정 시간이 흐르게 되는데 Flyweight 패턴을 사용하게 되면 인스턴스를 new 하는 수를 줄이면서 시간도 줄일 수 있게 되기 때문에 프로그램의 속도를
올릴 수 있다.

