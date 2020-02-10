package pe;

public class ColorTest extends PixelEngine
{
    @Override
    protected boolean onUserCreate()
    {
        return true;
    }
    
    Color color = new Color();
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        for (int j = 0; j < screenHeight(); j++)
        {
            for (int i = 0; i < screenHeight(); i++)
            {
                draw(i, j, color.set(i, j, 255));
            }
        }
        return true;
    }
    
    @Override
    protected void onUserDestroy()
    {
        
    }
    
    public static void main(String[] args)
    {
        int r = 255;
        int g = 255;
        int b = 255;
        int a = 255;
        print(r | (g << 8) | (b << 16) | (a << 24));
        start(new ColorTest(), 256, 256, 1, 1);
    }
}