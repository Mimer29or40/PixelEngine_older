package pe.gui.event;

import pe.Mouse;

public interface IMouseRepeated
{
    boolean fire(Mouse.Button button, int widgetX, int widgetY);
}
