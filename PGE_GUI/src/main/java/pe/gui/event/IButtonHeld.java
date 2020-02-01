package pe.gui.event;

import pe.Mouse;

public interface IButtonHeld
{
    void fire(Mouse.Button mouse, int widgetX, int widgetY);
}
