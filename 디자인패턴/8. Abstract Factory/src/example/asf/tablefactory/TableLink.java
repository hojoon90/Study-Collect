package example.asf.tablefactory;

import example.asf.factory.*;

public class TableLink extends Link {
    public TableLink (String caption, String url){
        super(caption, url);
    }
    public String makeHtml(){
        return " <td><a href=\""+url+"\">" + caption + "</a></td>\n";
    }
}
