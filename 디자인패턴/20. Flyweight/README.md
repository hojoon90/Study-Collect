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
    public Bigchar(char charName){
        this.charName = charName;
        try{
            BufferReader reader = new BufferReader(
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
        return singleton();
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