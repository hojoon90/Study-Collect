# Facade 패턴

### Facade 패턴이란
프로그램은 개발과 유지보수를 거치면서 점점 규모가 커지게 된다. 규모가 커지게 되면 클래스의 개수도 늘어나게 되며 그 클래스들끼리 서로의 관계를 맺으면서\
복잡해지게 된다. 이러한 커다란 프로그램을 사용하여 처리를 실행하기 위해선 서로 관련되어있는 클래스들을 적절하게 제어해주어야 한다. 그러기 위해선 이 처리를\
실행하기 위한 '창구'가 있는 것이 좋다. 그렇게 되면 '창구'에서 요구만 하면 알아서 클래스를 제어하기 때문이다. 이러한 패턴을 **Facade 패턴**이라고 한다.\
Facade 역할은 시스템 외부에 대해서 단순한 인터페이스만 제공해준다. 또한 시스템 내부에 있는 각 클래스의 역할이나 의존관계를 생각하여 정확한 순서로 클래스를\
이용한다.

### 예제 코드
Facade 패턴의 예제를 위해선 복잡하게 들어있는 많은 클래스가 필요하지만 여기서는 3개만 사용하여 간단한 시스템을 만들어본다.
```java
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Database {
    private Database(){}
    public static Properties getProperties(String dbName){
        String fileName = dbName + ".txt";
        Properties prop = new Properties();
        try{
            prop.load(new FileInputStream(fileName));
        }catch (IOException ie){
            System.out.println("Warning: " + fileName + "is not found.");
        }
        return prop;
    }
}
```
Database 클래스는 데이터베이스 이름을 지정하고 그에 대응하는 프로퍼티를 작성하는 클래스이다. 이 클래스는 인스턴스를 생성하지 않고, getProperties 메소드를\
이용하여 파일을 읽어온 후 Properties 인스턴스에 담는다. 맨 위에 private으로 Database 클래스를 만들어 new에서 인스턴스를 생성시키지 않게 한다.

```java
import java.io.Writer;
import java.io.IOException;

public class HtmlWriter {
    private Writer writer;
    public HtmlWriter(Writer writer){
        this.writer = writer;
    }
    public void title(String title) throws IOException {
        writer.write("<html>");
        writer.write("<head>");
        writer.write("<title>" + title + "</title>");
        writer.write("</head>");
        writer.write("<body>\n");
        writer.write("<h1>" + title + "</h1>\n");
    }
    public void paragraph(String msg) throws IOException{
        writer.write("<p>" + msg + "</p>\n");
    }
    public void link(String href, String caption) throws IOException {
        paragraph("<a href=\"" + href + "\">" + caption + "</a>");
    }
    public void mailto(String mailAddr, String userName) throws IOException {
        link("mailto:" + mailAddr, userName);
    }
    public void close() throws IOException {
        writer.write("</body>");
        writer.write("</html>\n");
        writer.close();
    }
}
```
HtmlWriter 클래스는 간단한 웹페이지를 만드는 코드이다. 인스턴스 생성 시 Writer 클래스를 받아오며 Writer를 이용해 HTML을 출력한다. 각 메소드는 이름에\
맞는 역할들을 수행한다.그리고 마지막 close 메소드는 HTML의 출력을 끝낸다.

```java
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PageMaker {
    private PageMaker() {}
    public static void makeWelcomePage(String mailAddr, String fileName){
        try{
            Properties mailProp = Database.getProperties("mailData");
            String userName = mailProp.getProperties(mailAddr);
            HtmlWriter writer = new HtmlWriter(new FileWriter(fileName));
            writer.title("Welcome to " + userName + "'s page!");
            writer.paragraph(username + "의 페이지에 오신것을 환영합니다.");
            writer.paragraph("메일을 기다리고 있습니다.");
            writer.mailto(mailAddr, userName);
            writer.close();
            System.out.println(fileName + " is created for " + mailAddr + " (" + userName + ")");
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }
}
```
PageMaker 클래스는 Database 클래스와 HtmlWriter 클래스를 조합하여 사용자의 웹페이지를 작성하는 역할을 한다. 여기서 접근할 수 있는 메소드는\
makeWelcomePage 단 하나뿐이다. 이 메소드가 바로 창구 역할을 하는 메소드이다. 이 메소드 안에서 HtmlWriter 클래스를 호출 하며, 사용자는 \
makeWelcomePage 메소드를 호출함으로써 HtmlWriter 의 메소드까지 호출해주고 있는 것이다.

```java
import pagemaker.PageMaker;

public class Main {
    public static void main (String[] args){
        PageMaker.makeWelcomePage("youngjin@youngjin.com", "welcome.html");
    }
}
```
Main에서는 PageMaker 의 makeWelcomePage 하나만을 호출한다. 이 메소드가 창구역할을 하기 때문이다.

### 패턴 사용시 고려할 점
Facade 패턴은 복잡한 것을 단순하게 보여주는 패턴이다. 내부에서 어떤일이 일어나는지 사용자는 알 필요 없이 창구역할의 메소드 하나만을 호출해주면 알아서\
결과값을 리턴해준다. 복잡한 것을 의식하지 않도록 해주는 것이다. 여기서의 핵심은 인터페이스를 적게(less) 하는 것이다. 클래스 혹은 메소드가 많으면\
개발자는 어떤것을 써야 할지 망설이게 되고 그만큼 메소드 혹은 클래스 사용에 대해 주의할 수 밖에 없게 된다. \
\
클래스를 설계할 때 어떤 메소드를 public 으로 사용할 것인지에 대한 고민도 필요하다. 만약 너무 많은 메소드가 public 으로 사용된다면, 클래스 내부의 수정이\
힘들게 된다. 필드 또한 public으로 하면 다른 클래스에서 해당 필드를 참조할 수도 있게 되기 때문에 수정이 어려워지게 된다. 패키지 역시 클래스를 많이\
노출시키면 패키지 내부 수정이 힘들어지게 된다.\
\

