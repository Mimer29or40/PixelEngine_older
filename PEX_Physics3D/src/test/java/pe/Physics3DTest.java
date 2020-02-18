package pe;

public class Physics3DTest extends PixelEngine
{
    @Override
    protected boolean setup()
    {
        return true;
    }
    
    @Override
    protected boolean draw(double elapsedTime)
    {
        return true;
    }
    
    @Override
    protected void destroy()
    {
    
    }
    
    public static void main(String[] args)
    {
        start(new Physics3DTest());
    }
}
