package singleton.example;

public class Main(){
    public static void main(String[] args){
        System.out.println("Start.");
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        if(s1 == s2){
            System.out.println("s1과 s2는 같은 인스턴스 입니다.");
        }else{
            System.out.println("s1과 s2는 다른 인스턴스 입니다.");
        }
        System.out.println("End.");
    }
}