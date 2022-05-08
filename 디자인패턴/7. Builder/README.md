# Builder 패턴


```java
public abstract class Builder {
    public abstract void makeTitle(String title);
    public abstract void makeString(String str);
    public abstract void makeItems(String[] items);
    public abstract void close();
}
```

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