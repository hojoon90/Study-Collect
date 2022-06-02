package dp.ch12.decorator.example;

public abstract class Border extends Display {
    protected Display display;
    protected Border(Display display){
        this.display = display;
    }
}
