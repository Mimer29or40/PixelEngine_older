package pe.gui.event;

import pe.Mouse;

public interface IButtonPressed
{
    void fire(Mouse.Button mouse, int widgetX, int widgetY);
}
