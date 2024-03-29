package example.interpreter;

import java.util.ArrayList;

public class CommandListNode extends Node {
    private ArrayList list = new ArrayList();
    public void parse(Context context) throws ParseException{
        while(true){
            if(context.currentToken() == null){
                throw new ParseException("missing 'end'");
            }else if(context.currentToken().equals("end")){
                context.skipToken("end");
            }else{
                Node commandNode = new CommandNode();
                commandNode.parse(context);
                list.add(commandNode);
            }
        }
    }
    public String toString(){
        return list.toString();
    }
}
