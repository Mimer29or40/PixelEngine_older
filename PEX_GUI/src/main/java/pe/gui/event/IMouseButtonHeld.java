package pe.gui.event;

import pe.Mouse;

public interface IMouseButtonHeld
{
    boolean fire(Mouse.Button button, int widgetX, int widgetY);
}
