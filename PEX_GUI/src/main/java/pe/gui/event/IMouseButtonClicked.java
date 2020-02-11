package pe.gui.event;

import pe.Mouse;

public interface IMouseButtonClicked
{
    boolean fire(Mouse.Button mouse, int widgetX, int widgetY, boolean doubleClicked);
}
