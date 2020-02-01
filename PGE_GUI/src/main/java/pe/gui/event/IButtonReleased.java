package pe.gui.event;

import pe.Mouse;

public interface IButtonReleased
{
    void fire(Mouse.Button mouse, int widgetX, int widgetY);
}
