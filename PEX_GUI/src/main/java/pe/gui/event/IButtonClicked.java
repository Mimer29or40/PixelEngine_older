package pe.gui.event;

import pe.Mouse;

public interface IButtonClicked
{
    void fire(Mouse.Button mouse, int widgetX, int widgetY, boolean doubleClicked);
}
