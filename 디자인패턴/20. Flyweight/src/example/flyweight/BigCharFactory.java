package example.flyweight;

import java.util.HashMap;

public class BigCharFactory{
    //이미 만들어진 BigChar 인스턴스 관리
    private HashMap pool = new HashMap();
    //Singleton 패턴
    private static BigCharFactory singleton = new BigCharFactory();
    //생성자
    private BigCharFactory(){
    }
    //인스턴스 get
    public static BigCharFactory getInstance(){
        return singleton;
    }
    //BigChar 인스턴스 생성
    public synchronized BigChar getBigChar(char charName){
        BigChar bc = (BigChar) pool.get("" + charName);
        if(bc == null){
            bc = new BigChar(charName); //BigChar 인스턴스 생성
            pool.put("" + charName, bc);
        }
        return bc;
    }
}
