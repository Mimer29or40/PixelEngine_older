package pe.gui;

public class Alert extends Modal
{
    public Alert(String title, String text)
    {
        super(title, text);
    }
    
    @Override
    protected void initButtons()
    {
        Button ok = new Button(this, "Ok");
        ok.onButtonClicked((mouse, widgetX, widgetY, doubleClicked) -> close(1));
    }
}
