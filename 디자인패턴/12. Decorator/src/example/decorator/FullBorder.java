package example.decorator;

public class FullBorder extends Border {
    public FullBorder(Display display){
        super(display);
    }
    public int getColumns(){
        return 1 + display.getColumns() + 1;
    }
    public int getRows(){
        return 1 + display.getRows() + 1;
    }
    public String getRowText(int row){
        if (row == 0) { // 장식 상단
            return "+" + makeLine('-', display.getColumns()) + "+";
        } else if (row == display.getRows() + 1 ){  // 장식 하단
            return "+" + makeLine('-', display.getColumns()) + "+";
        } else {
            return "|" + display.getRowText(row - 1) + "|";
        }
    }
    private String makeLine(char ch, int count){
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < count; i++) {
            sbf.append(ch);
        }
        return sbf.toString();
    }
}
