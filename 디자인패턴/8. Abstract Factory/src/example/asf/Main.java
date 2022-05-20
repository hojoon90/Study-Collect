package example.asf;

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