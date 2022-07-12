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