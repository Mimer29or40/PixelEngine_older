package pe.gui.event;

import pe.Mouse;

public interface IMouseClicked
{
    boolean fire(Mouse.Button mouse, int widgetX, int widgetY, boolean doubleClicked);
}
