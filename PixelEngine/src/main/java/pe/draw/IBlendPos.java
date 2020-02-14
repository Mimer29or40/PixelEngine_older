package pe.draw;

import pe.Color;

public interface IBlendPos
{
    Color blend(int x, int y, Color source, Color destination);
}
