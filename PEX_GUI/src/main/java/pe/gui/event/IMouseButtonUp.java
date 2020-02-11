package pe.gui.event;

import pe.Mouse;

public interface IMouseButtonUp
{
    boolean fire(Mouse.Button button, int widgetX, int widgetY);
}
