package pe;

import pe.gfx2d.PEX_GFX2D;
import pe.vector.Vector2;

public class VectorTest extends PixelEngine
{
    @Override
    protected boolean onUserCreate()
    {
        print(Vector2.X.x());
        
        return true;
    }
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        Vector2 v0 = new Vector2(0, 0);
        Vector2 v1 = new Vector2(-1, -1);
        Vector2 v2 = v1.copy().invert();
        
        v0.mul(50).add(getScreenWidth() / 2D, getScreenHeight() / 2D);
        v1.mul(50).add(getScreenWidth() / 2D, getScreenHeight() / 2D);
        v2.mul(50).add(getScreenWidth() / 2D, getScreenHeight() / 2D);
        PEX_GFX2D.drawLine(v0, v1);
        PEX_GFX2D.drawLine(v0, v2, Color.RED);
        
        return true;
    }
    
    public static void main(String[] args)
    {
        start(new VectorTest(), 800, 600, 1, 1);
    }
}
