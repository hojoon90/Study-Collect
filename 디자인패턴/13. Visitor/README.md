# Visitor 패턴

### Visitor 패턴이란
Visitor 패턴은 데이터 구조와 처리를 분리하는 패턴이다. 보통 우리는 어떤 요소에 대해 처리할 때, 처리하는 코드를 보통 데이터 구조를 표시하는 클래스에서 \
처리를 진행한다. 하지만 이 처리하는 로직이 여러종류이면 처리가 필요할 때 마다 데이터 구조 클래스의 변경이 불가피 해진다. Visitor 패턴은 데이터 구조 안을\
돌아다니는 '방문자'역할의 클래스가 있으며, 그 클래스에게 처리를 위임한다. 처리 로직이 더 필요하게 된다면 데이터 구조 클래스를 변경할 필요 없이 새로운 \
'방문자'를 만들어주면 된다. 그리고 데이터 구조는 그 '방문자'를 받아 처리 로직을 위임해주면 된다.

### 예제코드
이번 예제에서는 Composite 패턴에서 만들었던 예제 코드를 재활용하여 파일과 디렉토리로 구성된 데이터 구조 안을 돌아다니는 방문자 클래스를 만들고 해당\
클래스가 파일의 종류를 표시하는 프로그램을 만든다.
```java
public abstract class Visitor {
    public abstract void visit (File file);
    public abstract void visit (Directory directory);
}
```
Visitor 클래스는 추상클래스로서 총 두개의 추상메소드를 갖는다. 둘 다 이름은 같지만 인자가 각각 File 과 Directory 를 받는다. visit 메소드를 호출 할 때,\
인자에 따라 해당하는 메소드를 호출한다.

```java
public interface Element {
    public abstract void accept (Visitor v);
}
```
Visitor 클래스는 '방문자'를 나타내는 클래스이다. Element 인터페이스는 이 '방문자'를 받아들이는 역할을 하는 인터페이스이다. 클래스 안에 있는 추상 메소드\
accept가 있는데, 인수가 방문자인 Visitor를 받는 것을 알 수 있다.

```java
public abstract class Entry implements Element {
    public abstract String getName();
    public abstract int getSize();
    public Entry add(Entry entry) throws FileTreatmentException{
        throw new FileTreatmentException();
    }
    public Iterator iterator() throws FileTreatmentException {
        throw new FileTreatmentException();
    }
    public String toString(){
        return getName() + " (" + getSize() + ")";
    }
}
```
위 클래스는 11장에서 작성되었던 Entry 클래스이다. 클래스 안의 내용이 조금 변경 되었는데, 기존의 디렉토리 리스트를 보여주는 printList() 메소드가 사라진\
것을 볼 수 있다. 또한 Element 인터페이스를 구현하고 있다. Element에 선언된 추상 메소드 accept는 Entry를 상속 받는 하위 클래스들에서 구체화 된다.\
add 와 iterator 메소드는 Directory 클래스에서 처리가 되어야하므로 에러로 처리한다.

```java
public class File extends Entry {
    private String name;
    private int size;
    public File(String name, int size){
        this.name = name;
        this.size = size;
    }
    public String getName(){
        return name;
    }
    public int getSize(){
        return size;
    }
    public void accept(Visitor v) {
        v.visit(this);
    }
}
```
File 클래스 역시 11장에서 사용한 File 클래스와 거의 유사하다. 아까 위에서 보았던 printList 메소드가 삭제 되었으며, accept 메소드가 구체화 되어있다.\
accept 메소드는 Visitor를 인자로 받으며, v.visit(this); 로 visit 메소드를 호출한다. visit 메소드를 호출하여 Visitor 클래스에게 현재 File 클래스를\
알려준다.

```java
public class Directory extends Entry {
    private String name;
    private ArrayList directory = new ArrayList();
    public Directory (String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public int getSize(){
        int size = 0;
        Iterator it = directory.iterator();
        while(it.hasNext()){
            Entry entry = (Entry) it.next();
            size += entry.getSize();
        }
        return size;
    }
    public Entry add(Entry entry){
        directory.add(entry);
        return this;
    }
    public Iterator iterator(){
        return directory.iterator();
    }
    public void accept(Visitor v) {
        v.visit(this);
    }
}
```
Directory 클래스 역시 크게 변경된 점은 없다. iterator 와 accept 메소드가 추가되었는데, 간단하게 iterator 메소드는 디렉토리에 포함되어있는 디렉토리 엔트리의\
종류를 얻기 위한 Iterator 를 반환한다. (여기서 엔트리는 디렉토리 안에 있는 파일과 하위 디렉토리이다.)