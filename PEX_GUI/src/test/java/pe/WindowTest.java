package pe;

import pe.gui.*;

import java.util.ArrayList;
import java.util.List;

public class WindowTest extends PixelEngine
{
    static List<String> events = new ArrayList<>();
    
    Pane        menu;
    ProgressBar progressBar;
    Alert       alert;
    
    static void addEvent(String event)
    {
        events.add(event);
        events.remove(0);
    }
    
    @Override
    protected boolean onUserCreate()
    {
        int lines = (getScreenHeight() - 2) / 8;
        for (int i = 0; i < lines; i++) events.add("");
        
        menu = new Pane(PEX_GUI.ROOT, getScreenWidth() - getScreenHeight(), getScreenHeight(), "Menu");
        menu.setMarginSize(1);
        menu.getTooltipText(() -> "Test");
        menu.onMouseClicked((mouse, widgetX, widgetY, doubleClicked) -> {
            if (!doubleClicked) addEvent("Menu Clicked: (" + widgetX + ", " + widgetY + ")");
            if (doubleClicked) addEvent("Menu Double Clicked: (" + widgetX + ", " + widgetY + ")");
            return true;
        });
        
        int margin = 1;
        
        Button button = new Button(menu, "Button");
        //button.setPosition(menu.getForegroundOriginX(), menu.getForegroundOriginY());
        button.setWidth(menu.getForegroundWidth());
        button.onButtonPressed((mouse, widgetX, widgetY) -> addEvent("Button Pressed: " + mouse));
        button.onButtonReleased((mouse, widgetX, widgetY) -> addEvent("Button Released: " + mouse));
        button.onButtonClicked((mouse, widgetX, widgetY, doubleClicked) -> addEvent("Button Clicked: " + mouse));
        
        Button toggle = new Button(menu, "Toggle");
        toggle.setToggleable(true);
        //toggle.setPosition(menu.getForegroundOriginX(), button.getMaxY() + 1 + margin);
        toggle.setY(button.getMaxY() + 1 + margin);
        toggle.setWidth(menu.getForegroundWidth());
        toggle.onButtonToggled((mouse, widgetX, widgetY, pressed) -> addEvent("Toggle: " + pressed));
        
        Slider slider = new Slider(menu, 1, 32);
        //slider.setPosition(menu.getForegroundOriginX(), toggle.getMaxY() + 1 + margin);
        slider.setY(toggle.getMaxY() + 1 + margin);
        slider.setSize(menu.getForegroundWidth(), 12);
        slider.onSliderChanged(value -> addEvent("Slider Changed: " + value));
        
        TextBox textBox = new TextBox(menu, "Text");
        //textBox.setPosition(menu.getForegroundOriginX(), slider.getMaxY() + 1 + margin);
        textBox.setY(slider.getMaxY() + 1 + margin);
        textBox.setWidth(menu.getForegroundWidth());
        textBox.onTextChanged(text -> addEvent("Text Changed: " + text));
        textBox.onTextEntered(text -> addEvent("Text Entered: " + text));
        
        TextBox numberBox = new TextBox(menu);
        numberBox.setValidChars("1234567890");
        //numberBox.setPosition(menu.getForegroundOriginX(), textBox.getMaxY() + 1 + margin);
        numberBox.setY(textBox.getMaxY() + 1 + margin);
        numberBox.setWidth(menu.getForegroundWidth());
        numberBox.onTextChanged(text -> {
            if (!text.equals("")) addEvent("Number Changed: " + Long.parseUnsignedLong(text));
        });
        
        progressBar = new ProgressBar(menu);
        //progressBar.setPosition(menu.getForegroundOriginX(), numberBox.getMaxY() + 1 + margin);
        progressBar.setY(numberBox.getMaxY() + 1 + margin);
        progressBar.setWidth(menu.getForegroundWidth());
        progressBar.onProgressCompleted(() -> addEvent("Progress Completed"));
        
        Dialog dialog = new Dialog("TEST", "This is an example Dialog Box.");
        dialog.getTooltipText(() -> "This is a very long tooltip.");
        dialog.onModalOpened(() -> addEvent("Dialog Opened"));
        dialog.onModalClosed(result -> addEvent("Dialog Closed: " + result));
        
        Button open = new Button(menu, "Open Dialog");
        //open.setPosition(menu.getForegroundOriginX(), progressBar.getMaxY() + 1 + margin);
        open.setY(progressBar.getMaxY() + 1 + margin);
        open.setWidth(menu.getForegroundWidth());
        open.onButtonClicked((mouse, widgetX, widgetY, doubleClicked) -> dialog.open());
        
        //ScrollBar scrollBar = new ScrollBar(Orientation.HORIZONTAL);
        //scrollBar.setPosition(menu.getInternalOriginX(), open.getMaxY() + 1 + margin);
        //scrollBar.setWidth(menu.getInternalWidth());
        //scrollBar.onScrolled(value -> addEvent("Scrolled: " + value));
        //menu.addWidget(scrollBar);
        
        //Dropdown dropdown = new Dropdown();
        //dropdown.setPosition(menu.getInternalOriginX(), scrollBar.getMaxY() + 1 + margin);
        //dropdown.setWidth(menu.getInternalWidth());
        //menu.addWidget(dropdown);
        
        alert = new Alert("ALERT", "ALERT MESSAGE");
        alert.onModalOpened(() -> addEvent("Alert Opened"));
        alert.onModalClosed((result) -> addEvent("Alert Closed: " + result));
        
        return true;
    }
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        clear();
        
        if (Keyboard.M.pressed) menu.setVisible(!menu.isVisible());
        
        if (menu.isVisible())
        {
            int nLog = 0;
            for (String s : events)
            {
                int c = (int) map(nLog, 0, events.size() - 1, 60, 255);
                drawString(menu.getWidth() + 2, nLog * 8 + 2, s, new Color(c, c, c));
                nLog++;
            }
        }
        
        if (Keyboard.SPACE.held)
        {
            progressBar.setValue(progressBar.getValue() + 0.001);
            if (progressBar.getValue() >= 1.0) progressBar.setValue(0.0);
        }
        
        if (Keyboard.A.pressed) alert.open();
        
        return true;
    }
    
    public static void main(String[] args)
    {
        Logger.setLevel(Logger.Level.DEBUG);
        //start(new WindowTest(), 600, 400, 2, 2);
        start(new WindowTest());
    }
}
