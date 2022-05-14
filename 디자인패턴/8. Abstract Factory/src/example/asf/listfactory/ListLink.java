package example.asf.listfactory;

import example.asf.factory.*;

public class ListLink extends Link {
    public ListLink (String caption, String url){
        super(caption, url);
    }
    public String makeHtml(){
        return " <li><a href=\""+url+"\">" + caption + "</a></li>\n";
    }
}