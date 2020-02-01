package pe.gui.event;

import pe.Mouse;

public interface IMouseReleased
{
    boolean fire(Mouse.Button button, int widgetX, int widgetY);
}
