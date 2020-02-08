package pe;

import org.joml.Vector2d;
import pe.gfx2d.PEX_GFX2D;
import pe.vector.Vector2;

public class VectorTest extends PixelEngine
{
    @Override
    protected boolean onUserCreate()
    {
        print(Vector2.Xf.x());
        
        return true;
    }
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        Vector2d v0 = new Vector2d(0, 0);
        Vector2d v1 = new Vector2d(-1, -1);
        Vector2d v2 = new Vector2d(v1).perpendicular();
    
        v0.mul(50).add(screenWidth() / 2D, screenHeight() / 2D);
        v1.mul(50).add(screenWidth() / 2D, screenHeight() / 2D);
        v2.mul(50).add(screenWidth() / 2D, screenHeight() / 2D);
        PEX_GFX2D.drawLine(v0, v1);
        PEX_GFX2D.drawLine(v0, v2, Color.RED);
    
        return true;
    }
    
    public static void main(String[] args)
    {
        start(new VectorTest(), 800, 600, 1, 1);
    }
}
