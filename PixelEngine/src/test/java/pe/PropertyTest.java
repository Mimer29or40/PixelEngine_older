package pe;

public class PropertyTest extends PixelEngine
{
    private static final Logger LOGGER = Logger.getLogger();
    
    @Override
    protected boolean onUserCreate()
    {
        LOGGER.info("Monitor Width: %s Expected: %s", Window.monitorWidth(), 0);
    
        LOGGER.info("Monitor Height: %s Expected: %s", Window.monitorHeight(), 0);
    
        LOGGER.info("Window Width: %s Expected: %s", Window.windowWidth(), 0);
        assert Window.windowWidth() == 0;
    
        LOGGER.info("Window Height: %s Expected: %s", Window.windowHeight(), 0);
        assert Window.windowHeight() == 0;
    
        LOGGER.info("Window X: %s Expected: %s", Window.windowX(), 0);
        assert Window.windowX() == 0;
    
        LOGGER.info("Window Y: %s Expected: %s", Window.windowY(), 0);
        assert Window.windowY() == 0;
    
        LOGGER.info("Screen Width: %s Expected: %s", screenWidth(), 400);
        assert screenWidth() == 400;
    
        LOGGER.info("Screen Height: %s Expected: %s", screenHeight(), 300);
        assert screenHeight() == 300;
    
        LOGGER.info("Pixel Width: %s Expected: %s", pixelWidth(), 2);
        assert pixelWidth() == 2;
    
        LOGGER.info("Pixel Height: %s Expected: %s", pixelHeight(), 2);
        assert pixelHeight() == 2;
    
        LOGGER.info("Fullscreen: %s Expected: %s", Window.fullscreen(), false);
        assert !Window.fullscreen();
    
        LOGGER.info("V-Sync: %s Expected: %s", Window.vsync(), false);
        assert !Window.vsync();
    
        return true;
    }
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        return false;
    }
    
    @Override
    protected void onUserDestroy()
    {
    
    }
    
    public static void main(String[] args)
    {
        start(new PropertyTest(), 400, 300, 2, 2);
    }
}
