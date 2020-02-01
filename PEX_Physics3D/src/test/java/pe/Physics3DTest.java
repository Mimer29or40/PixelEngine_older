package pe;

public class Physics3DTest extends PixelEngine
{
    @Override
    protected boolean onUserCreate()
    {
        return true;
    }
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        return true;
    }
    
    @Override
    protected void onUserDestroy()
    {
    
    }
    
    public static void main(String[] args)
    {
        start(new Physics3DTest());
    }
}
