# Abstract Factory 패턴

### Abstract Factory 패턴이란
먼저 **추상적**이라는 단어에 대해 정확하게 생각하고 가자. 추상적이라는 단어는 객체지향에서는 구체적인 구현은 생각하지 않고,\
**인터페이스만을 생각하는 상태**를 말한다. 즉, '어떻게 할 것이다~' 라는 내용만 정의하여 큰 밑그림을 그리는 것과 비슷하다고 볼 수 있다.\
이는 앞에서 보았던 Builder 패턴과 Template Method 패턴에서도 나타난다.\
Abstract Factory 패턴은 말 그대로 추상적으로 모든것을 만드는 패턴이라고 보면 된다.\
부품의 구체적인 구현은 신경쓰지 않고, 인터페이스만 신경써서 만든다. 그리고 이 인터페이스만을 사용하여 부품을 조립하고 제품을 만든다.\
Abstract Factory 패턴 역시 Template Method, Builder 와 같이 구체적인 구현은 하위 클래스들에서 이루어진다.\
예제 코드를 보면서 확인해보자.

### 예제 코드와 패키지 구성
이번 예제에서는 계층구조를 가진 링크페이지를 html페이지로 만드는 예제이다.\
이번 예제는 클래스들이 조금 많기 때문에 먼저 필요한 클래스들에 대해 한번 정리 후 코드 작성을 진행한다.

> * Main.java
> * factory
>   * Factory.java
>   * Item.java
>   * Link.java
>   * Tray.java
>   * Page.java
> * listfactory
>   * ListFactory.java
>   * ListLink.java
>   * ListTray.java
>   * ListPage.java

위의 factory 패키지들은 모두 추상 클래스들로 이루어져 있고, 구체적인 구현들은 아래 listFactory의 패키지에서 진행된다.\
먼저 factory 패키지 부터 살펴보자.

### 추상 메소드들로 이루어진 factory 패키지
```java
package example.asf.factory;

public abstract class Item {
    protected String caption;
    public Item(String caption){
        this.caption = caption;
    }
    public abstract String makeHtml();
}
```
```java
package example.asf.factory;

public abstract class Link extends Item{
    protected String url;
    public Link(String caption, String url){
        super(caption);
        this.url = url;
    }
}
```
```java
package example.asf.factory;

public abstract class Tray extends Item {
    protected ArrayList tray = new ArrayList();
    public Tray(String caption){
        super(caption);
    }
    public void add(Item item){
        tray.add(item);
    }
}
```
먼저 Item 클래스는 Link와 Tray가 상속받아 사용하고 있는 클래스이다. 이는 Link와 Tray 클래스를 동일시 하기 위함이다.\
caption은 목차를 나타내는 필드이며, makeHtml()는 하위클래스에서 구현되는 추상메소드이다. 메소드 호출 시 Html의 문자열이 반환된다.

Link 클래스는 하이퍼링크를 추상적으로 나타낸 클래스이다. url 필드는 말 그대로 URL을 저장하기 위한 필드이다.\
Item을 상속받고 있지만, Item클래스에 있는 makeHtml()을 구현하고 있지는 않는다. 즉 Link 클래스 역시 추상 클래스라는 것을 알 수 있다.

Tray 클래스는 복수의 Link나 Tray를 모아서 합친 것을 표시하는 클래스이다. 클래스 안에 ArrayList를 인스턴스로 생성하는 tray 필드가 있으며,
이 필드안에 Tray나 Link들을 모은다. 해당 클래스들을 모으는 메소드는 add메소드로 모으는데, Link 와 Tray 모두 모아야 하기 때문에,
인자로 Item 을 받는다.

```java
package example.asf.factory;

public abstract class Page{
    protected String title;
    protected String author;
    protected ArrayList content = new ArrayList();
    public Page(String title, String author){
        this.title = title;
        this.author = author;
    }
    public void add(Item item){
        content.add(item);
    }
    public void output(){
        try{
            String filename = title+".html";
            Writer writer = new FileWriter(filename);
            writer.write(this.makeHtml());
            writer.close();
            System.out.println(filename + " 을 작성하였습니다.");
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }
    public abstract String makeHtml();
}
```
Page 클래스는 Html 페이지 전체를 추상적으로 표현한 클래스이다. 이 클래스는 위의 두 클래스(Link, Tray)와는 다르게 **제품**의 역할을 하는 클래스이다.\
페이지 역시 add()를 이용해서 Link와 Tray를 추가한다. output()은 title필드를 이용하여 파일명을 만들고, 자기 자신의 클래스 안에 있는
makeHtml()을 이용해서 내용을 작성한다. 여기서 호출하는 makeHtml()은 추상 메소드인데, output()만 놓고 본다면 간단한 Template Method 패턴이
이용되고 있다.

```java
package example.asf.factory;

public abstract class Factory {
    public static Factory getFactory(String classname){
        Factory factory = null;
        try{
            factory = (Factory)Class.forName(classname).newInstance();
        }catch (ClassNotFoundException cfe){
            System.err.println("클래스" + classname + "이 발견되지 않았습니다.");
        }catch (Exception e){
            e.printStackTrace();
        }
        return factory;
    }
    public abstract Link createLink(String caption, String url);
    public abstract Tray createTray(String caption);
    public abstract Page createPage(String title, String author);
}

```
Factory 클래스는 위에서 만든 부품들과 제품을 조립하는 클래스라고 생각하면 된다. getFactory 메소드는 클래스 이름을 인자로 받아서
Factory 클래스로 리턴하는데, **인자로 받은 클래스 이름을 Class.forName()을 이용하여 동적으로 클래스를 읽은 후, newInstance를 통해
읽은 클래스의 인스턴스를 한개 생성한다.**\
getFactory()에서는 인자로 넘어온 클래스 이름을 동적으로 읽어 인스턴스로 만들지만, 리턴은 Factory클래스로 리턴하는 것에 주의한다.\
createLink, createTray, createPage는 부품이나 제품을 생성할때 사용하는 메소드이다. 실제 구현은 모두 Factory의 하위 클래스에서 구현된다.

### Main 클래스
```java
import example.asf.factory.*;

public class Main{
    public static void main(String[] args){
        if(args.length != 1){
            System.out.println("Usage: java Main class.name.of.ConcreteFactory");
            System.out.println("Example1: java Main listfactory.ListFactory");
            System.out.println("Example2: java Main tablefactory.TableFactory");
            System.exit(0);
        }
        Factory factory = Factory.getFactory(args[0]);
        
        Link daum = factory.createLink("다음", "https://www.daum.net/");     
        Link naver = factory.createLink("네이버", "https://www.naver.com/");     
        Link google = factory.createLink("구글", "https://www.google.com/");     
        
        Link youtube = factory.createLink("유튜브", "https://www.youtube.com/");     
        
        Link github = factory.createLink("깃허브", "https://github.com/");
        
        Tray trayPotal = factory.createTray("포털");
        trayPotal.add(daum);
        trayPotal.add(naver);
        trayPotal.add(google);

        Tray trayStream = factory.createTray("스트리밍");
        trayStream.add(youtube);

        Tray trayStorage = factory.createTray("저장소");
        trayStorage.add(github);
        
        Page page = factory.createPage("LinkPage", "H.J.CHOI");
        page.add(trayPotal);
        page.add(trayStream);
        page.add(trayStorage);
        page.output();
    }
}
```
Main 클래스는 실제 부품과 제품을 조립하여 html페이지를 만드는 클래스이다. 클래스 내용을 자세히 보면 구체적인 클래스가 없는 것을 알 수 있다.\
구체적인 클래스는 Main을 실행할 때 커맨드 라인에 인수로 작성하게 된다. 각 링크 생성 후, Tray에 알맞는 링크들을 넣고 페이지에 Tray들을 넣어
페이지를 완성한다.

### 구체적으로 구현된 클래스들
위에서는 추상적인 클래스와 추상적인 메소드들에 대한 예제들을 작성했다. 이번에는 구체적으로 구현된 클래스들에 대한 예제들이다.
```java
package example.asf.listfactory;

import example.asf.factory.*;

public class ListFactory extends Factory{
    public Link createLink (String caption, String url){
        return new ListLink (caption, url);
    }
    public Tray createTray (String caption){
        return new ListTray(caption);
    }
    public Page createPage (String title, String author){
        return new ListPage(title, author);
    }
}
```
ListFactory 클래스는 Factory 클래스를 상속받은 클래스로서, 단순하게 각각 ListLink, ListTray, ListPage 클래스의 인스턴스들을 새롭게 생성하는 역할만 하고 있다.

```java
package example.asf.listfactory;

import example.asf.factory.*;

public class ListLink extends Link{
    public ListLink (String caption, String url){
        super(caption, url);
    }
    public String makeHtml(){
        return " <li><a href=\""+url+"\">" + caption + "</a></li>\n";
    }
}
```
ListLink 클래스는 Link 클래스를 상속받은 클래스이다. Link 클래스가 상속받던 Item 클래스 안에 있는 makeHtml 메소드를 구체화한다.\
makeHtml메소드에서는 li 태그와 a 태그를 사용해서 html을 만들어주고 있다.

```java
package example.asf.listfactory;

import example.asf.factory.*;
import java.util.Iterator;

public class ListTray extends Tray{
    public ListTray(String caption){
        super(caption);
    }
    public String makeHtml(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<li>\n");
        buffer.append(caption + "\n");
        buffer.append("<ul>\n");
        Iterator it = tray.iterator();
        while(it.hasNext()){
            Item item = (Item)it.next();
            buffer.append(item.makeHtml());
        }
        buffer.append("</ul>\n");
        buffer.append("</li>\n");
        return buffer.toString();
    }
}
```
ListTray 역시 Tray 클래스를 상속받은 클래스로서, 위의 ListLink와 마찬가지로 makeHtml()이 구체화되어있다. li 태그와 ul 태그를 이용하여 각각의 Item들을 출력해준다.
출력결과는 StringBuffer에 모아두었다가, 마지막에 toString으로 변환해준다.
여기서 while문을 자세하게 보자. tray에 담겨있는 데이터들을 Iterator를 이용해 하나씩 가져오고 가져온 데이터들을 Item 에 담아준 후,
makeHtml 메소드를 이용해 html을 구성한다.

여기서 item 을 이용하여 makeHtml() 메소드를 호출하고 있다. 이는 객체지향을 활용한 프로그래밍인데, Link, Tray모두 Item을 상속받고 있다.\
그렇기 때문에 Item형으로 데이터를 받아 makeHtml을 호출 하면 구현 클래스가 어떤 건지 신경 쓸 필요없이 알아서 html이 만들어지게 된다.\
가령 ListTray에서 사용하는 Item.makeHtml은 tray라는 ArrayList안에 있는 ListLink의 makeHtml()이 실행된다.\
ListTray 역시 아래에 작성할 ListPage 안에서 makeHtml()이 실행되고, 이 역시 item.makeHtml() 로 호출된다. 아래 코드를 보자.

```java
package example.asf.listfactory;

import example.asf.factory.*;
import java.util.Iterator;

public class ListPage extends Page{
    public ListPage(String title, String author){
        super(title, author);
    }
    public String makeHtml(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><head><title>"+title+"</title></head>\n");
        buffer.append("<body>\n");
        buffer.append("<h1>"+title+"</h1>\n");
        buffer.append("<ul>\n");
        Iterator it = content.iterator();
        while(it.hasNext()){
            Item item = (Item)it.next();
            buffer.append(item.makeHtml());
        }
        buffer.append("</ul>\n");
        buffer.append("<hr><address>"+author+"</address>");
        buffer.append("</body></html>\n");
        return buffer.toString();
    }
}
```
Iterator 부분부터 보자. 여기도 content 리스트를 Iterator를 이용해 하나씩 makeHtml을 실행하는데, ListTray안에 있는 makeHtml이 실행된다.\
Iterator 위 아래에 'ul' 태그가 존재하는데, 이는 ListLink와 ListTray의 시작이 'li' 태그로 시작하기 때문이다.\
이 때문에 ListPage에서 Iterator 를 통해 html이 만들어 질 때, 리스트 형태로 알맞게 데이터가 생성이 된다.

### abstract factory를 이용해 다른 형태로 구체화해보기
abstract factory를 이용하면 추상화된 factory를 이용하여 다른 형태로 만들어 낼 수도 있다. 아래는 테이블 형태로 만드는 코드 예제이다.
위의 내용과 크게 차이는 없으니 코드만 남기도록 하겠다.

```java
package example.asf.tablefactory;

import example.asf.factory.*;

public class TableFactory extends Factory{
    public Link createLink (String caption, String url){
        return new TableLink (caption, url);
    }
    public Tray createTray (String caption){
        return new TableTray(caption);
    }
    public Page createPage (String title, String author){
        return new TablePage(title, author);
    }
}
```
```java
package example.asf.tablefactory;

import example.asf.factory.*;

public class TableLink extends Link{
    public TableLink (String caption, String url){
        super(caption, url);
    }
    public String makeHtml(){
        return " <td><a href=\""+url+"\">" + caption + "</a></td>\n";
    }
}
```
```java
package example.asf.tablefactory;

import example.asf.factory.*;
import java.util.Iterator;

public class TableTray extends Tray{
    public TableTray(String caption){
        super(caption);
    }
    public String makeHtml(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<td>");
        buffer.append("<table width=\"100%\" border=\"1\"><tr>");
        buffer.append("<td bgcolor=\"#cccccc\" align=\"center\" colspan=\""
                        +tray.size() +"\"><b>" + caption + "</b></td>");
        buffer.append("</tr>\n");
        buffer.append("<tr>\n");
        Iterator it = tray.iterator();
        while(it.hasNext()){
            Item item = (Item)it.next();
            buffer.append(item.makeHtml());
        }
        buffer.append("</tr></table>");
        buffer.append("</td>");
        return buffer.toString();
    }
}
```
```java
package example.asf.tablefactory;

import example.asf.factory.*;
import java.util.Iterator;

public class TablePage extends Page{
    public TablePage(String title, String author){
        super(title, author);
    }
    public String makeHtml(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><head><title>"+title+"</title></head>\n");
        buffer.append("<body>\n");
        buffer.append("<h1>"+title+"</h1>\n");
        buffer.append("<table width=\"80%\" border=\"3\">\n");
        Iterator it = content.iterator();
        while(it.hasNext()){
            Item item = (Item)it.next();
            buffer.append(item.makeHtml());
        }
        buffer.append("</table>\n");
        buffer.append("<hr><address>"+author+"</address>");
        buffer.append("</body></html>\n");
        return buffer.toString();
    }
}
```

### Abstract Factory 패턴의 장점과 단점
Abstract Factory 패턴의 장점은 구체화하는 클래스를 새로 만드는게 편하다는 것이다. 위의 예제 코드들을 보자. 공통점은 모두 factory 패키지 안의
Factory, Link, Tray, Page 클래스의 구체화된 클래스들을 만들었다는 것이다. \
즉 어떤 클래스를 만들어야 할지 명확하게 정해져 있다는 것이다.\
이는 factory 패키지 안의 클래스들만 구체화 시키면 얼마든지 쉽게 구체화된 내용을 추가할 수 있다는 것이다.\
또한 구체적인 클래스를 추가 혹은 수정하더라도 추상적인 부분은 수정할 필요가 없다는 것이다.

하지만 단점도 존재하는데, 새로운 클래스를 추가하는 것이 어렵다. 예를 들어 factory 패키지 안에 새로운 추상 클래스를 추가한다고 생각해보자.\
해당 클래스가 추가되어 사용하려면 연관되는 추상 클래스들을 모두 수정해줘야 한다. 또한, 해당 클래스들을 상속받는 구체화된 기존 클래스들의 변경이 불가피 하다.\
이미 만들어진 클래스들이 많으면 많을수록 작업이 더욱더 늘어나게 된다.