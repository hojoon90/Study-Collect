# Composite 패턴

### Composite 패턴이란
Composite 패턴은 간단히 이야기하면 '상자속의 상자'처럼 되는 구조이다. 컴퓨터의 디렉토리를 생각해보자. 디렉토리 안에는 파일 있을수도 있고,\
디렉토리가 있을 수도 있다. 그리고 그 디렉토리 안에는 다시 파일 혹은 디렉토리가 존재할 수 있다. 이처럼 계속 안으로 파고파고 들어가는 재귀적인 모습을\
보여주고 있는데, 이 Composite 패턴 역시 이와 유사한 모습이다. 클래스에 대해 재귀적인 구조를 만들기 위한 디자인 패턴이 바로 Composite 패턴이다.

### 예제 코드
여기서는 Main을 제외한 총 4가지 클래스가 존재한다. 이 중 중요하게 봐야 할 클래스는 Entry, File, Directory 클래스이다. 클래스들에 대해 간단히 설명하면\
File 클래스는 이름 처럼 파일을 나타내는 클래스이다. Directory 역시 디렉토리를 나타내는 클래스이며, 두가지 클래스를 하나로 모은 형태의 클래스가\
Entry 클래스이다. 하나씩 차근차근 살펴보자.

```java
public abstract class Entry {
    public abstract String getName();
    public abstract int getSize();
    public Entry add(Entry entry) throws FileTreatmentException{
        throw new FileTreatmentException();
    }
    public void printList(){
        printList("");
    }
    protected abstract void printList(String prefix);
    public String toString(){
        return getName() + " (" + getSize() + ")";
    }
}
```
Entry 클래스는 추상클래스로서 디렉토리 엔트리를 표현한다. 그와 더불어 하위 클래스로 File과 Directory 클래스를 갖는다. 먼저 자세한 설명을 하기 전에\
디렉토리에 대해 간단히 생각해보자. 디렉토리는 기본적으로 파일의 이름과 크기가 표시된다. 그리고 어떤 파일들이 들어가 있는지 파일 리스트를 볼 수 있으며, \
디렉토리 안에 파일 혹은 디렉토리를 추가할 수 있다. 이 내용들을 생각하면서 코드를 살펴보자.\
\
getName은 디렉토리 엔트리 이름을 얻기 위한 메소드이다. 디렉토리는 각각의 이름이 있으며 이 이름을 얻기 위해 사용하는 메소드이다. \
getSize 메소드는 디렉토리 엔트리의 크기를 얻기 위한 메소드이다. \
디렉토리 안에는 파일 혹은 디렉토리를 추가할 수 있는데, 추가하기 위해 사용하는 메소드가 바로 add 메소드이다. Entry 클래스에서는 Exception을 throw하게\
되어있는데, 이는 Entry 클래스가 아닌 Entry클래스의 하위에 있는 Directory 클래스에서 실행되어야 하기 때문에 여기에서는 예외를 발생시켜 에러가 나도록\
처리하고 있다.\
printList 메소드는 메소드가 인자가 있는 것과 없는 것 두가지가 존재하는데, 이를 메소드의 Overload라고 한다. 여기서는 인수가 없는 printList가 public으로,\
인수가 있는 printList 메소드가 protected로 되어 Entry 하위 클래스에서만 호출할 수 있도록 만들어져 있다. 기능은 디렉토리의 리스트를 보여주는 메소드이다.\
toString 은 파일 이름과 크기를 나열하여 표현한다. getName, getSize는 추상메소드 이지만, 하위클래스에서 구현되어 호출하고 있다.

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
    protected void printList(String prefix){
        System.out.println(prefix + "/" + this);
    }
}
```
File 클래스는 파일을 표현하는 클래스이다. File 생성시에는 이름과 사이즈 두개의 인자를 받아서 만들어진다. getName과 getSize는 파일의 이름과 사이즈를\
리턴해주는 메소드로, Entry에 선언되어있던 추상 메소드들이 구체화된 것이다. 인수를 갖고있는 printList 메소드 역시 여기서 구체화된다. prefix와 자신의\
문자열을 "/"로 구분하여 표시한다. 그리고 "/"뒤에 this를 호출하는데, JAVA는 '문자열 + 오브젝트'가 되면 자동적으로 그 오브젝트의 toString을 호출한다.\

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
    protected void printList(String prefix){
        System.out.println(prefix + "/" + this);
        Iterator it = directory.iterator();
        while(it.hasNext()){
            Entry entry = (Entry) it.next();
            entry.printList(prefix + "/" + name);
        }
    }
}
```
Directory 클래스는 디렉토리를 표현하는 클래스이다. name 필드는 디렉토리의 이름을 나타내는 필드이며 directory 리스트는 디렉토리의 엔트리를\
저장하기 위한 필드이다. File 클래스와는 다르게 getSize 메소드는 계산하는 코드가 들어가 있는데, directory에 있는 엔트리들을 하나씩 꺼내서\
사이즈를 계산하여 합한 값을 리턴한다. 여기서 Entry 로 형변환을 하여 꺼내온 오브젝트에 대해 사이즈 계산을 하는데, directory 안에 들어가있는\
클래스가 File 인지 Directory 인지 신경 쓸 필요가 없다. 어짜피 File 과 Directory 모두 Entry 를 상속 받는 클래스이며, Entry 클래스\
안에는 getSize 메소드가 추상메소드로 선언되어있기 때문에, File 이건 Directory 이건 그냥 getSize를 호출하여 값을 가져오면 된다. 이렇게\
동일한 메소드를 호출하여 리턴값을 받을 수 있는 것이 Composite 패턴의 특징이다. 게다가 Entry를 상속받는 다른 클래스가 만들어져도 getSize부분이\
구현되기 때문에 이 클래스의 계산 로직 부분은 따로 수정할 필요가 없다.\
\
만일 entry변수가 Directory 클래스면 어떻게 될까? entry.getSize()가 호출되면 Directory 클래스 안의 getSize 메소드가 다시 호출되어 호출된\
Directory 클래스 안에 있는 엔트리들의 사이즈들을 다시 하나씩 더한다. 그 중 다시 Directory 클래스의 엔트리가 있으면 다시 사이즈들을 더하고...\
이렇게 재귀적으로 디렉토리 안의 사이즈들을 계속 계산한다. Composite 패턴의 재귀적인 구조가 getSize 메소드 호출을 통해 간접적으로 보여지고 있다.\
\
add 메소드는 directory 리스트에 엔트리들을 저장하는 역할을 한다. printList 메소드는 디렉토리의 종류를 표시하는데, 이 역시 getSize와 마찬가지로\
재귀적인 호출을 하고 있다. 변수의 entry가 Directory 인지 File 인지도 따로 신경쓰지 않는다.\

### 예외처리와 Main 클래스
```java
public class FileTreatmentException extends RuntimeException {
    public FileTreatmentException(){
    }
    public FileTreatmentException (String msg){
        super(msg);
    }
}
```
FileTreatmentException 은 파일에 대해 add 메소드를 잘못 호출할 경우 제공되는 예외이다. Entry 클래스 안에서 호출되는 예외이며 따로 JAVA에서\
제공되는 예외가 아닌 직접 생성하는 예외이다. 

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
            rootdir.printList();

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
            rootdir.printList();
        }catch (FileTreatmentException e) {
            e.printStackTrace();
        }
    }
}
```
Main 코드는 위와 같이 작성해주었다. 출력결과는 아래와 같다.

>Making root entries...\
>/root (30000)\
>/root/bin (30000)\
>/root/bin/vi (10000)\
>/root/bin/latex (20000)\
>/root/tmp (0)\
>/root/usr (0)\
>\
>Making user entries...\
>/root (31500)\
>/root/bin (30000)\
>/root/bin/vi (10000)\
>/root/bin/latex (20000)\
>/root/tmp (0)\
>/root/usr (1500)\
>/root/usr/Kim (300)\
>/root/usr/Kim/diary.html (100)\
>/root/usr/Kim/composite.java (200)\
>/root/usr/Lee (300)\
>/root/usr/Lee/memo.tex (300)\
>/root/usr/Park (900)\
>/root/usr/Park/game.doc (400)\
>/root/usr/Park/junk.mail (500)

### 패턴 사용시 고려해 보아야 할 점
Composite 패턴은 **여러개를 모아서 하나인 것 처럼 취급**하는 패턴이기도 하다. 예를들어 Test1은 키보드의 입력테스트, Test2는 파일의 입력테스트,\
Test3는 네트워크의 입력테스트를 실행한다고 가정해보자. Test1, Test2, Test3 세개를 합쳐서 '입력 테스트'로 하고 싶을 때 Composite 패턴을 사용할 수 있다.\
이 테스트들이 여러개 만들어지면 이걸 다시 '입력 테스트'로 모으고, 또 다른 테스트를 모아서 '출력 테스트'로 하고 이 두 개를 다시 모아 '입출력 테스트'로 한다.\
이렇게 여러개를 모아서 하나인 것 처럼 취급할 수 있다. 

