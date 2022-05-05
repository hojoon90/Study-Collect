# Prototype 패턴

### framework
```java
public interface Product extends Cloneable{
    public abstract void use(String s);
    public abstract Product createClone();
}
```
```java
public class Manager{
    private HashMap showcase = new HashMap();
    public void register(String name, Product proto){
        showcase.put(name, proto);
    }
    public Product create(String protoname){
        Product p = (Product) showcase.get(protoname);
        return p.createClone();
    }
}
```
### 구현체 부분
```java
public class MessageBox implements Product {
    private char decochar;
    public MessageBox(char decochar){
        this.decochar = decochar;
    }
    public void use(String s){
        int length = s.getBytes().length;
        for (int i = 0; i < length + 4; i++) {
            System.out.println(decochar);
        }
        System.out.println(" ");
        System.out.println(decochar + " " + s + " " + decochar);
        for (int i = 0; i < length + 4; i++) {
            System.out.println(decochar);
        }
        System.out.println(" ");
    }
    public Product createClone(){
        Product p = null;
        try {
            p = (Product)clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return p;
    }
}
```
```java
public class UnderlinePen implements Product {
    private char ulchar;
    public UnderlinePen(char ulchar){
        this.ulchar = ulchar;
    }
    public void use(String s){
        int length = s.getBytes().length;
        System.out.println("\"" + s + "\"");
        System.out.println(" ");
        for (int i = 0; i < length; i++) {
            System.out.println(ulchar);
        }
        System.out.println(" ");
    }
    public Product createClone(){
        Product p = null;
        try {
            p = (Product)clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return p;
    }
}
```
```java
public class Main{
    public static void main(String[] args){
        Manager manager = new Manager();
        UnderlinePen upen = new UnderlinePen('~');
        MessageBox mbox = new MessageBox('*');
        MessageBox sbox = new MessageBox('/');
        manager.register("strong message", upen);
        manager.register("warning box", mbox);
        manager.register("slash box", sbox);
        
        Product p1 = manager.create("strong message");
        p1.use("Hello World");
        Product p2 = manager.create("warning box");
        p2.use("Hello World");
        Product p3 = manager.create("slash box");
        p3.use("Hello World");
    }
}
```