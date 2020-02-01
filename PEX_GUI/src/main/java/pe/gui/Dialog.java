package pe.gui;

public class Dialog extends Modal
{
    public Dialog(String title, String text)
    {
        super(title, text);
    }
    
    @Override
    protected void initButtons()
    {
        Button cancel = new Button(this, "Cancel");
        cancel.onButtonClicked((mouse, widgetX, widgetY, doubleClicked) -> close(0));
        
        Button ok = new Button(this, "Ok");
        ok.onButtonClicked((mouse, widgetX, widgetY, doubleClicked) -> close(1));
    }
}
