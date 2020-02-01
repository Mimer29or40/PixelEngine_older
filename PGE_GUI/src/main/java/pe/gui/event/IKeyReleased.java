package pe.gui.event;

import pe.Keyboard;

public interface IKeyReleased
{
    boolean fire(Keyboard.Key key);
}
