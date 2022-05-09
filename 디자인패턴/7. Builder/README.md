# Builder 패턴

### Builder 패턴이란
Builder 패턴은 구조를 만들어가는 패턴이라고 이해하면 쉽다. 우리가 흔히 이야기하는 '빌드업'이라는 단어는\
가장 기초부분부터 차근차근 쌓아 올라간다는 의미로 많이 사용하고 있다. Builder 패턴은 이와 유사하게 전체를 구성하는 각 부분을 만들고\
단계를 밟아가며 만들어 나가는 패턴을 이야기 한다.\
예제를 보며 자세하게 알아보자.

### 구조의 구성과 그 구조를 실제로 만드는 Builder, Director
먼저 예제 코드로 문서를 만드는 프로그램을 만들어 보려한다.\
Builder 클래스는 문서를 만들기 위한 기초 구조를 갖고 있다.\
```java
public abstract class Builder {
    public abstract void makeTitle(String title);
    public abstract void makeString(String str);
    public abstract void makeItems(String[] items);
    public abstract void close();
}
```
Builder 는 추상 클래스로 선언되어 있으며, 안에 있는 메소드들 역시 추상 메소드로 이루어져 있다.\
이 메소드들은 Builder 를 상속 받는 하위클래스들에서 구체화되어 만들어진다.\
여기서는 각각 타이틀, 내용, 개별항목을 구성할 수 있도록 메소드들이 선언되어져 있다.

```java
public class Director {
    private Builder builder;
    public Director(Builder builder) {
        this.builder = builder;
    }

    public void construct(){
        builder.makeTitle("Greeting");
        builder.makeString("아침과 낮에");
        builder.makeItems(new String[]{
                "좋은 아침입니다.",
                "안녕하세요.",
        });
        builder.makeString("밤에");
        builder.makeItems(new String[]{
                "안녕하세요.",
                "안녕히 주무세요.",
                "안녕히 계세요.",
        });
        builder.close();
    }
}
```
Director 클래스는 Builder 를 생성자로 생성하여 실제 문서를 만드는 클래스이다.\
Director 인스턴스 생성 시 Builder 를 상속 받는 하위 클래스들을 인자로 받으며, 어떤 하위 클래스가 넘어오냐에 따라 문서 형식이 달라지게 된다.\
construct()는 실제 문서를 만드는 메소드이며, Builder 에 선언되어있는 메소드들이 실행된다.\
Builder에 선언되어있는 메소드들을 실행하지만 결과는 인자로 넘어온 하위 클래스들에 구체화된 코드들로 실행되게 된다.\
여기서도 주목할 점은 실제 상속 받은 하위 클래스들은 선언되어있지 않다는 것이다.\
이는 곧 하위 클래스들에 의존하지 않게 된다는 것을 의미한다.


### 실제 구현 클래스들 및 Main 클래스

아래 클래스들은 Builder 클래스를 상속 받아서 구현하는 구현체 클래스들이다.\
Builder 에서 추상메소드로 선언된 메소드들이 구현되어 있으며, Director 생성 시 받을 하위 구현체 클래스가 바로 아래 두 클래스들이다.

```java
public class TextBuilder extends Builder{
    private StringBuffer buffer = new StringBuffer();
    @Override
    public void makeTitle(String title) {
        buffer.append("=============================\n");
        buffer.append("⌜"+title+"⌟\n");
        buffer.append("\n");
    }

    @Override
    public void makeString(String str) {
        buffer.append('■' + str + "\n");
        buffer.append("\n");
    }

    @Override
    public void makeItems(String[] items) {
        for (int i = 0 ; i < items.length; i++){
            buffer.append(" •" + items[i] + "\n");
        }
        buffer.append("\n");
    }

    @Override
    public void close() {
        buffer.append("=============================\n");
    }

    public String getResult(){
        return buffer.toString();
    }
}
```
위의 TextBuilder 클래스는 일반 텍스트를 사용해서 문서를 구축하며, 결과를 String을 반환한다.

```java
public class HTMLBuilder extends Builder{
    private String filename;
    private PrintWriter writer;
    @Override
    public void makeTitle(String title) {
        filename = title + ".html";
        try{
            writer = new PrintWriter(new FileWriter(filename));
        }catch (IOException ie){
            ie.printStackTrace();
        }
        writer.println("<html><head><title>" + title + "</title></head>                     </body>");
        writer.println("<h1>" + title + "</h1>");
    }

    @Override
    public void makeString(String str) {
        writer.println("<p>" + str + "</p>");
    }

    @Override
    public void makeItems(String[] items) {
        writer.println("<ul>");
        for (int i = 0; i < items.length; i++){
            writer.println("<li>" + items[i] + "</li>");
        }
        writer.println("</ul>");
    }

    @Override
    public void close() {
        writer.println("</body></html>");
        writer.close();
    }

    public String getResult(){
        return filename;
    }
}
```
위의 HTMLBuilder 클래스는 결과를 Html 형식으로 문서를 구축하며, 결과는 Html 파일의 파일명을 반환한다.

그럼 이 클래스들을 실제로 사용하는 Main 클래스를 만들어보자.
```java
public class Main {
    public static void main(String[] args){
        if (args.length != 1){
            usage();
            System.exit(0);
        }
        if (args[0].equals("plain")){
            TextBuilder textBuilder = new TextBuilder();
            Director director = new Director(textBuilder);
            director.construct();
            String result = textBuilder.getResult();
            System.out.println(result);
        } else if (args[0].equals("html")) {
            HTMLBuilder htmlBuilder = new HTMLBuilder();
            Director director = new Director(htmlBuilder);
            director.construct();
            String filename = htmlBuilder.getResult();
            System.out.println(filename + "가 작성되었습니다.");
        } else {
            usage();
            System.exit(0);
        }
    }
    public static void usage(){
        System.out.println("Usage: java Main plain 일반 텍스트로 문서작성");
        System.out.println("Usage: java Main html  HTML 파일로 문서작성");
    }
}
```
Main 클래스는 다음 실행 커맨드에 따라 결과를 다르게 나타낸다. 
* java Main plain - 일반 텍스트 문서로 작성
* java Main html - HTML 파일로 문서를 작성

Main 클래스 안의 if 구문안을 자세하게 보면 조건에 따라서 TextBuilder 혹은 HTMLBuilder 클래스를 인스턴스로 생성한다.\
그 후 Director 클래스의 인스턴스 생성 시 위에서 생성한 클래스들을 인자로 넘겨 인스턴스를 생성한다.\
실행되는 메소드는 Director 의 construct() 인데, construct 메소드는 Builder 안의 메소드만을 사용해서 문서를 작성한다.\
이는 Director 클래스는 실제로 만들어지는 문서가 TextBuilder 인지, HTMLBuilder 인지 알지 못한다는 것이다.\
하지만 Builder 에서 선언된 메소드들을 이용해서 문서를 구축할 수 있게 Builder 는 필요한 메소드들을 선언 해 놓아야 한다.\
만약 위의 클래스들 중 일반 텍스트 혹은 HTML 에서만 사용되는 고유 메소드가 Builder 에 선언되어서는 안된다.

### Builder 패턴 구현시 고려할 점
위의 Main 클래스는 Director의 construct()만 호출한다. Builder 클래스를 전혀 호출하지 않고 construct 호출만으로 문서를 작성하게 된다.\
Director 클래스도 Builder 클래스안에 있는 메소드들을 이용해서 문서를 만들지만, 인자로 받는 builder의 하위 클래스가 어떤건지는 모른다.\
단지 Builder 클래스안에 있는 메소드만으로 문서 작성이 가능할 뿐이다.\
이렇게 서로에게 결합이 느슨한 경우 부품으로서의 활용이 높아지고, 교체가 수월해진다.

Builder 구현 시에도 메소드 작성은 중요하다. Builder는 Director에서 문서를 만들때 필요한 클래스이므로,\
문서를 구축할 때 필요한 메소드들을 충분하게 제공해주어야 한다. 또한 위에 만든 두개의 클래스뿐만 아니라 나중에 추후로 추가될 클래스들에 대해서도\
확장 가능할 수 있도록 설계해주어야 한다.