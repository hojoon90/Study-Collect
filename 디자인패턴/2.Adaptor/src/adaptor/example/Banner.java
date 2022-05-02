package adaptor.example;

public class Banner {
    private String text;
    public Banner(String text) {
        this.text = text;
    }

    public void showWithParen(){
        System.out.println("("+this.text+")");
    }
    public void showWithAster(){
        System.out.println("*"+this.text+"*");
    }
}
