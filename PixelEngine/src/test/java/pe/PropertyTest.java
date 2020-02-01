package pe;

public class PropertyTest extends PixelEngine
{
    private static final Logger LOGGER = Logger.getLogger();
    
    @Override
    protected boolean onUserCreate()
    {
        LOGGER.info("Monitor Width: %s Expected: %s", getMonitorWidth(), 0);
        
        LOGGER.info("Monitor Height: %s Expected: %s", getMonitorHeight(), 0);
        
        LOGGER.info("Window Width: %s Expected: %s", getWindowWidth(), 0);
        assert getWindowWidth() == 0;
        
        LOGGER.info("Window Height: %s Expected: %s", getWindowHeight(), 0);
        assert getWindowHeight() == 0;
        
        LOGGER.info("Window X: %s Expected: %s", getWindowX(), 0);
        assert getWindowX() == 0;
        
        LOGGER.info("Window Y: %s Expected: %s", getWindowY(), 0);
        assert getWindowY() == 0;
        
        LOGGER.info("Screen Width: %s Expected: %s", getScreenWidth(), 400);
        assert getScreenWidth() == 400;
        
        LOGGER.info("Screen Height: %s Expected: %s", getScreenHeight(), 300);
        assert getScreenHeight() == 300;
        
        LOGGER.info("Pixel Width: %s Expected: %s", getPixelWidth(), 2);
        assert getPixelWidth() == 2;
        
        LOGGER.info("Pixel Height: %s Expected: %s", getPixelHeight(), 2);
        assert getPixelHeight() == 2;
        
        LOGGER.info("Fullscreen: %s Expected: %s", isFullscreen(), false);
        assert !isFullscreen();
        
        LOGGER.info("V-Sync: %s Expected: %s", isVSync(), false);
        assert !isVSync();
        
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
