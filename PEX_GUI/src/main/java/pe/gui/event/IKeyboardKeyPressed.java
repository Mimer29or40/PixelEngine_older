package pe.gui.event;

import pe.Keyboard;

public interface IKeyboardKeyPressed
{
    boolean fire(Keyboard.Key key, boolean doublePressed);
}
