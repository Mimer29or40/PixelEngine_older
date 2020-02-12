package pe.gui.event;

import pe.Mouse;

public interface IMouseButtonRepeated
{
    boolean fire(Mouse.Button button, int widgetX, int widgetY);
}
