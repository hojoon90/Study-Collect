# Proxy 패턴

### Proxy 패턴이란
proxy라는 단어는 '대리인'이라는 의미를 갖는다. 우리는 본인이 아니더라도 맡길 수 있는 일은 대리인에게 맡긴다. 하지만 대리인이 처리할 수 있는 범위를 넘어서는 일이 생기면
대리인은 본인에게 와서 상담을 하게 된다. Proxy 패턴은 이와 같이 본인 Object 대신에 대리인 Object가 일정 일을 처리해주는 패턴이다.

### 예제 코드
이번 예제 프로그램은 '이름있는 프린터'를 만들어본다. Main 클래스는 PrinterProxy 클래스의 인스턴스를 생성한다. 이 클래스는 '대리인'의 역할을 한다.
그 인스턴스에 처음엔 Alice 라는 이름을 붙이고 그 이름을 출력한다. 그 후 Bob 이라는 이름으로 변경하여 그 이름을 출력한다. 여기서 이름의 설정과 취득부분은
대리인인 PrinterProxy 클래스가 대리로 실행한다. 그 후 마지막에 실제 프린트를 실행할 때 Printer 인스턴스를 생성하여 실제 출력을 진행한다. 여기서는 Printer
클래스의 인스턴스가 생성하는데 많은 시간이 걸린다는 가정을 하여 프로그램을 만든다.

```java
public class Printer implements Printable{
    private String name;
    public Printer(){
        heavyJob("Printer의 인스턴스를 생성 중");
    }
    public Printer(String name){
        this.name = name;
        heavyJob("Printer의 인스턴스 (" + name + ")을 생성 중");
    }
    public void setPrinterName(String name) {
        this.name = name;
    }
    public String getPrinterName(){
        return name;
    }
    public void print(String string){
        System.out.println("=== " + name + " ===");
        System.out.println(string);
    }
    private void heavyJob(String msg){
        System.out.println(msg);
        for (int i = 0; i < 5; i++) {
            try{
                Thread.sleep(1000);
            }catch (InterruptedException ie){
                
            }
            System.out.println(".");
        }
        System.out.println("완료.");
    }
}
```
Print 클래스는 '본인'을 표시하는 클래스이다. 생성자에서는 무거운 작업인 heavyJob을 실행한다. 그리고 이름에 대한 getter/setter가 세팅되어있으며, 
print 메소드에서는 프린트의 이름을 붙여서 인자로 받은 글자를 출력하고 있다. heavyJob은 무거운 작업을 표현하는 메소드이며 1초마다 .이 1개씩 찍힌다.

```java
public interface Printable{
    public abstract void setPrinterName(String name);
    public abstract String getPrinterName();
    public abstract void print(String string);
}
```
Printable 인터페이스는 PrinterProxy 클래스와 Printer 클래스를 동일시 하기 위한 것이다. 위 클래스와 같이 getter, setter 그리고 print메소드가 있다.

```java
public class PrinterProxy implements Printable{
    private String name;
    private Printer real;
    public PrinterProxy(){
    }
    public PrinterProxy(String name){
        this.name = name;
    }
    public synchronized void setPrinterName(String name){
        if(real != null){
            real.setPrinterName(name);
        }
        this.name = name;
    }
    public String getPrinterName(){
        return name;
    }
    public void print(String string){
        realize();
        real.print(string);
    }
    private synchronized void realize(){
        if(real == null){
            real = new Printer(name);
        }
    }
}
```
PrinterProxy 클래스는 대리인의 역할을 수행하며 Printable 인터페이스를 구현한다. name 변수는 이름을, real 필드는 Printer 인스턴스를 저장한다. 생성자에서는
이름을 설정한다. setPrinterName은 이름을 세팅하는 메소드이며, real 변수가 null이 아니면 인자로 받은 이름을 세팅해준다. real이 null 일 경우는 name만
세팅해준다. print 메소드는 대리인의 역할을 벗어나기 때문에 실제 Printer 클래스 인스턴스를 생성하여 real.print() 메소드를 실행한다.아무리 setPrinterName 과
getPrinterName 메소드를 호출해도 Printer 인스턴스는 생성되지 않는다. Printer 클래스 인스턴스는 실제 클래스가 필요할 경우에만 호출된다. realize 메소드는
real 변수가 null 일 경우에만 인스턴스를 생성한다.\
PrinterProxy 클래스는 Printer 클래스를 알고 있다. 왜냐하면 PrinterProxy 클래스는 real 변수가 Printer형이며 realize 메소드에서도 Printer 인스턴스를
생성하기 때문이다. 이처럼 PrinterProxy 클래스는 Printer 클래스와 연관되어있는 클래스이다. 반면 Printer 클래스는 PrinterProxy 클래스의 존재를 전혀 모른다.

```java
public class Main {
    public static void main(String[] args){
        Printable p = new PrinterProxy("Alice");
        System.out.println("이름은 현재" + p.getPrinterName() + "입니다.");
        p.setPrinterName("Bob");
        System.out.println("이름은 현재" + p.getPrinterName() + "입니다.");
        p.print("Hello, World.");
    }
}
```
Main 클래스는 PrinterProxy 를 경유하여 Printer를 이용하는 클래스이다. 처음에 PrinterProxy 인스턴스 생성 후 이름을 출력하고, 다시 다른이름으로 세팅한 후 
마지막에 Hello World 를 출력한다.

### 패턴 사용시 고려할 사항
Proxy 패턴은 대리인이 할수 있는 일만 처리를 대신해준다. 예제 프로그램에서는 대리인을 사용하여 실제 print할때 까지의 무거운 처리를 지연시킬 수 있었다.
예제 프로그램에서는 무거운 처리라고 해봤자 그렇게 큰 처리는 아니지만, 실제 대규모 시스템에서는 이야기가 다르다. 만약 초기 기동 시 모든 기능들을 초기화 하려면
시간이 오래 걸리게 된다. 이러기 보단 실제 그 기능을 사용 할 때 기능을 초기화 해주는 것이 시간을 좀더 절약할 수 있는 방법이 된다. 예시의 realize 메소드처럼 말이다.\
\
