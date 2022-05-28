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
문자열을 "/"로 구분하여 표시한다. 그리고 "/"뒤에 this를 호출하는데, JAVA에서는 '문자열 + 오브젝트'가 되면 자동적으로 그 오브젝트의 toString을 호출한다.\
