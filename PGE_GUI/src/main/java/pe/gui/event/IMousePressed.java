package pe.gui.event;

import pe.Mouse;

public interface IMousePressed
{
    boolean fire(Mouse.Button button, int widgetX, int widgetY);
}
