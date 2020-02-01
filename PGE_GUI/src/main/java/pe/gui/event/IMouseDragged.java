package pe.gui.event;

import pe.Mouse;

public interface IMouseDragged
{
    boolean fire(Mouse.Button mouse, int relX, int relY);
}
