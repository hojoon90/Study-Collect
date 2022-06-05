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
Directory 클래스 역시 크게 변경된 점은 없다. iterator 와 accept 메소드가 추가되었는데, 간단하게 iterator 메소드는 디렉토리에 포함되어있는 디렉토리\
엔트리의 종류를 얻기 위한 Iterator 를 반환한다. (여기서 엔트리는 디렉토리 안에 있는 파일과 하위 디렉토리이다.) 그리고 위의 코드와 동일하게 \
Visitor 클래스에게 어떤 클래스가 방문하는지 알 수 있도록 accept 메소드를 구현해준다.

```java
public class ListVisitor extends Visitor {
    private String currentDir = "";
    public void visit(File file){
        System.out.println(currentDir + "/" + file);
    }
    public void visit(Directory directory){
        System.out.println(currentDir + "/" + directory);
        String saveDir = currentDir;
        currentDir = currentDir + "/" + directory.getName();
        Iterator it = directory.iterator();
        while(it.hasNext()){
            Entry entry = (Entry) it.next();
            entry.accept(this);
        }
        currentDir = saveDir;
    }
}
```
ListVisitor 클래스는 Visitor 클래스의 하위클래스이며, 데이터 구조를 돌아다니며 종류를 표시하기 위한 클래스이다. currentDir 변수는 현재의 디렉토리를\
저장하는 변수이며 visit(File)메소드는 파일을 방문 했을 때 해당 파일의 경로와 파일 이름을 나타내는 메소드이다(객체 호출 시 toString 자동 호출).\
이 메소드는 File 클래스의 accept 메소드에서 호출하고 있는데, accept 메소드가 호출 될 때 visit 메소드에서 필요한 '처리'를 진행한다.\
\
visit(Directory) 메소드에서 위에 이야기한 '처리'에 대해 좀 더 자세하게 코드로 나온다. 이 메소드에서는 디렉토리의 이름을 반환함과 동시에 한가지 작업을 \
더 진행해주는데, 해당 디렉토리 안에 있는 File과 Directory 데이터를 Entry 클래스 형으로 불러온 후 accept 메소드를 호출하여 다시 Visitor 클래스 안의\
visit 메소드들을 호출 하도록 하고 있다. 각각의 파일과 디렉토리에 방문하여 해당 이름을 호출 하도록 하는 기능을 하는 것이다.
\
자세히 보면 accept 는 visit 메소드를 호출하고, visit 메소드 안에서는 iterator 를 통해 accept 메소드를 다시 호출하고 있다. Visitor 패턴에서는 이처럼\
**visit 메소드와 accept 메소드가 서로서로를 호출**하고 있는 것이다.\

```java
public class FileTreatmentException extends RuntimeException {
    public FileTreatmentException(){
    }
    public FileTreatmentException (String msg){
        super(msg);
    }
}
```
FileTreatmentException은 Composite 패턴과 동일하다.
```java
public class Main {
    public static void main (String[] args){
        try{
            System.out.println("Making root entries...");
            Directory rootdir = new Directory("root");
            Directory bindir = new Directory("bin");
            Directory tmpdir = new Directory("tmp");
            Directory usrdir = new Directory("usr");
            rootdir.add(bindir);
            rootdir.add(tmpdir);
            rootdir.add(usrdir);
            bindir.add(new File("vi", 10000));
            bindir.add(new File("latex", 20000));
            rootdir.accept(new ListVisitor());

            System.out.println("");
            System.out.println("Making user entries...");
            Directory kim = new Directory("Kim");
            Directory lee = new Directory("Lee");
            Directory park = new Directory("Park");
            usrdir.add(kim);
            usrdir.add(lee);
            usrdir.add(park);
            kim.add(new File("diary.html", 100));
            kim.add(new File("composite.java", 200));
            lee.add(new File("memo.tex", 300));
            park.add(new File("game.doc", 400));
            park.add(new File("junk.mail", 500));
            rootdir.accept(new ListVisitor());
        }catch (FileTreatmentException e) {
            e.printStackTrace();
        }
    }
}
```
Main 클래스 역시 Composite 패턴과 동일하다. 다른점이라고 한다면 아래 rootdir.accept 메소드를 통해 ListVisitor 클래스가 모든 디렉토리와 파일을 방문해\
디렉토리와 파일의 이름을 출력한다(visit 메소드). 

### Visitor 패턴 사용시 고려해야할 점
Visitor 패턴의 목적은 처리를 데이터 구조에서 분리하는 일이다. File과 Directory 클래스를 자세히 보면 accept 메소드에서 visit 메소드를 호출하여 \
Visitor 클래스에서 처리작업을 할 수 있도록 해주고 있다. File 클래스와 Directory 클래스는 단지 visit 메소드를 호출할때 자기자신(this)만 넘겨주면 된다.\
여기서 실제 처리는 ListVisitor 클래스가 하고 있으며, 다른 처리가 필요할 경우 Visitor 를 상속 받는 하위 클래스를 만들어주면 된다. 이렇게 되면 File과 \
Directory 클래스는 새로운 '처리' 를 추가해서 기능 확장을 할 때 마다 별도의 수정이 필요 없다. File 클래스나 Directory 클래스의 부품으로써의 독립성을\
높일 수 있다.\
\
여기서 고민해볼 수 있는 것이 바로 '확장에는 열려있고 수정에는 닫혀있다'는 원칙을 생각해볼 수 있다. 기본적으로 클래스 설계시에는 특별한 이유가 없는 이상 \
확장을 허용해주어야 한다. 이유없이 확장을 금지할 수 없으며 이를 '확장에 대해 열려있다.'라고 한다. 하지만 확장을 할 때마다 기존의 클래스를 수정하는 것도 \
곤란하다. 확장을 해도 기존 클래스는 수정하지 않는 것이 '수정에는 닫혀있다'는 의미이다. 프로그램 개발을 하게 되면 거의 대부분 기능을 확장하고 싶어 한다.\
그렇기 때문에 클래스가 기능 확장을 할 수 없으면 곤란하다. 이와 동시에 기존에 완성되어 테스트까지 마친 클래스를 수정하는 것은 그 프로그램에 대한 신뢰성과\
품질을 떨어트릴 우려가 있다. 확장에 대해서는 열려있으며 수정에 대해서는 닫혀있어야 클래스가 부품으로서의 재이용 가치가 높아지게 된다.\
\
Visitor가 제대로 된 처리를 하기 위해선 Element가 필요한 정보들을 전달해주어야 한다. Entry 클래스가 Elements 인터페이스를 상속 받은 것을 생각해보자.\
Entry 클래스가 Elements 인터페이스를 상속받음으로 인해 Entry 클래스를 상속 받는 Directory 클래스와 File 클래스는 Elements 인터페이스에 자신들의 \
클래스 안에 있는 정보들의 접근이 가능하도록 하고 있다. 특히 Directory 클래스에서 해당 디렉토리 안에 있는 내용을 확인하기 위해 Iterator 를 이용하여\
디렉토리 엔트리를 얻을 수 있도록 해주고 있다. accept 메소드 호출 시 visit(Directory) 메소드를 호출 할 때 iterator()메소드에 접근 한 것을 생각해보자.\
이렇게 방문자는 데이터 구조에서 필요한 정보를 얻어 처리 동작을 하게 된다. 필요한 정보를 얻을 수 없으면 방문자는 제대로 처리를 할 수 없으며, 필요 없는\
정보까지 공개하게 된다면 미래의 데이터 구조를 개량하기 어렵게 된다.

