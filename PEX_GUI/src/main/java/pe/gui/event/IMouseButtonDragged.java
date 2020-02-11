package pe.gui.event;

import pe.Mouse;

public interface IMouseButtonDragged
{
    boolean fire(Mouse.Button button, int widgetX, int widgetY, int dragX, int dragY, int relX, int relY);
}
