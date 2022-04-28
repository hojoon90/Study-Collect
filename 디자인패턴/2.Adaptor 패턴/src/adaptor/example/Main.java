package adaptor.example;

public class Main{
    /*
     * 예제소스는 Adaptor패턴 중 위임을 사용하였음.
     */
    public static void main(String[] args){
        Print p = new PrintBanner("hello");
        p.printWeak();
        p.printStrong();
    }
}