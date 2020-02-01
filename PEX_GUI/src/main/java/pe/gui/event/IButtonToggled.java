package pe.gui.event;

import pe.Mouse;

public interface IButtonToggled
{
    void fire(Mouse.Button button, int widgetX, int widgetY, boolean pressed);
}
