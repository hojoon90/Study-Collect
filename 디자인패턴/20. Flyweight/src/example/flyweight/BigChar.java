package example.flyweight;

import java.io.*;

public class BigChar{
    //문자의 이름
    private char charName;
    //큰 문자를 표현하는 문자열 ('#' '.' '\n'의 열)
    private String fontData;
    public BigChar(char charName){
        this.charName = charName;
        try{
            BufferedReader reader = new BufferedReader(
                    new FileReader("big" + charName + ".txt")
            );
            String line;
            StringBuffer sbf = new StringBuffer();
            while((line = reader.readLine()) != null){
                sbf.append(line);
                sbf.append("\n");
            }
            reader.close();
            this.fontData = sbf.toString();
        } catch (IOException e) {
            this.fontData = charName + "?";
        }
    }
    // 큰 문자를 표현한다.
    public void print(){
        System.out.println(fontData);
    }
}