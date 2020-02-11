package pe.gui.event;

import pe.Mouse;

public interface IMouseButtonDown
{
    boolean fire(Mouse.Button button, int widgetX, int widgetY);
}
