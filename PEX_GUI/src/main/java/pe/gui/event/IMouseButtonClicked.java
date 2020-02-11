package pe.gui.event;

import pe.Mouse;

public interface IMouseButtonClicked
{
    boolean fire(Mouse.Button button, int widgetX, int widgetY, boolean doubleClicked);
}
