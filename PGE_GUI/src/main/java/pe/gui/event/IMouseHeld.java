package pe.gui.event;

import pe.Mouse;

public interface IMouseHeld
{
    boolean fire(Mouse.Button button, int widgetX, int widgetY);
}
