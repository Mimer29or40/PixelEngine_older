package pe;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static pe.PixelEngine.getScreenHeight;
import static pe.PixelEngine.getScreenWidth;

@SuppressWarnings("unused")
public class Mouse
{
    protected static final Map<Integer, Button> inputs = new HashMap<>();
    
    public static final Button NONE   = new Button("NONE", -1);
    public static final Button LEFT   = new Button("LEFT", GLFW_MOUSE_BUTTON_LEFT);
    public static final Button RIGHT  = new Button("RIGHT", GLFW_MOUSE_BUTTON_RIGHT);
    public static final Button MIDDLE = new Button("MIDDLE", GLFW_MOUSE_BUTTON_MIDDLE);
    public static final Button FOUR   = new Button("FOUR", GLFW_MOUSE_BUTTON_4);
    public static final Button FIVE   = new Button("FIVE", GLFW_MOUSE_BUTTON_5);
    public static final Button SIX    = new Button("SIX", GLFW_MOUSE_BUTTON_6);
    public static final Button SEVEN  = new Button("SEVEN", GLFW_MOUSE_BUTTON_7);
    public static final Button EIGHT  = new Button("EIGHT", GLFW_MOUSE_BUTTON_8);
    
    protected static long holdDelay   = 500_000_000;
    protected static long repeatDelay = 100_000_000;
    
    private static boolean entered = false;
    
    private static int x, y, newX, newY;
    private static int relX, relY;
    private static int scrollX, scrollY, newScrollX, newScrollY;
    
    private Mouse()
    {
    
    }
    
    public static double getHoldDelay()
    {
        return Mouse.holdDelay / 1_000_000_000D;
    }
    
    public static void setHoldDelay(double holdDelay)
    {
        Mouse.holdDelay = (long) (holdDelay * 1_000_000_000L);
    }
    
    public static double getRepeatDelay()
    {
        return Mouse.repeatDelay / 1_000_000_000D;
    }
    
    public static void setRepeatDelay(double repeatDelay)
    {
        Mouse.repeatDelay = (long) (repeatDelay * 1_000_000_000L);
    }
    
    public static Collection<Button> getInputs()
    {
        return Mouse.inputs.values();
    }
    
    public static Button get(int reference)
    {
        return Mouse.inputs.getOrDefault(reference, Mouse.NONE);
    }
    
    public static boolean isEntered()
    {
        return Mouse.entered;
    }
    
    public static int getX()
    {
        return Mouse.x;
    }
    
    public static int getY()
    {
        return Mouse.y;
    }
    
    public static int getRelX()
    {
        return Mouse.relX;
    }
    
    public static int getRelY()
    {
        return Mouse.relY;
    }
    
    public static int getScrollX()
    {
        return Mouse.scrollX;
    }
    
    public static int getScrollY()
    {
        return Mouse.scrollY;
    }
    
    protected static void handleEvents(long time, long delta)
    {
        for (Button button : getInputs())
        {
            button.pressed = false;
            button.released = false;
            button.repeated = false;
            
            if (button.state != button.prevState)
            {
                if (button.state == GLFW_PRESS)
                {
                    button.pressed = true;
                    button.held = true;
                    button.downTime = time;
                }
                else if (button.state == GLFW_RELEASE)
                {
                    button.released = true;
                    button.held = false;
                    button.downTime = Long.MAX_VALUE;
                }
                else if (button.state == GLFW_REPEAT)
                {
                    button.pressed = true;
                    button.repeated = true;
                }
            }
            if (button.held && time - button.downTime > Mouse.holdDelay)
            {
                button.downTime += Mouse.repeatDelay;
                button.repeated = true;
            }
            button.prevState = button.state;
        }
        
        Mouse.relX = Mouse.newX - Mouse.x;
        Mouse.relY = Mouse.newY - Mouse.y;
        Mouse.x = Mouse.newX;
        Mouse.y = Mouse.newY;
        
        Mouse.scrollX = Mouse.newScrollX;
        Mouse.scrollY = Mouse.newScrollY;
        Mouse.newScrollX = 0;
        Mouse.newScrollY = 0;
    }
    
    protected static void stateCallback(int reference, int state)
    {
        get(reference).state = state;
    }
    
    public static void enteredCallback(boolean entered)
    {
        Mouse.entered = entered;
    }
    
    public static void positionCallback(double x, double y)
    {
        Mouse.newX = (int) x;
        Mouse.newY = (int) y;
        
        if (Mouse.newX < 0) Mouse.newX = 0;
        if (Mouse.newY < 0) Mouse.newY = 0;
        
        if (getScreenWidth() <= Mouse.newX) Mouse.newX = getScreenWidth() - 1;
        if (getScreenHeight() <= Mouse.newY) Mouse.newY = getScreenHeight() - 1;
    }
    
    public static void scrollCallback(double x, double y)
    {
        Mouse.newScrollX += x;
        Mouse.newScrollY += y;
    }
    
    public static void captureCallback(boolean captured)
    {
        if (captured)
        {
            Mouse.newX = Mouse.x = getScreenWidth() / 2;
            Mouse.newY = Mouse.y = getScreenHeight() / 2;
        }
    }
    
    public static class Button
    {
        protected final String name;
        protected final int    reference;
        
        protected boolean pressed  = false;
        protected boolean released = false;
        protected boolean held     = false;
        protected boolean repeated = false;
        
        protected long downTime = 0;
        protected int  state, prevState;
        
        private Button(String name, int reference)
        {
            this.name = name;
            this.reference = reference;
            
            Mouse.inputs.put(reference, this);
        }
        
        @Override
        public String toString()
        {
            return getClass().getSimpleName() + "." + this.name;
        }
        
        public String getName()
        {
            return this.name;
        }
        
        public boolean isPressed()
        {
            return this.pressed;
        }
        
        public boolean isReleased()
        {
            return this.released;
        }
        
        public boolean isHeld()
        {
            return this.held;
        }
        
        public boolean isRepeated()
        {
            return this.repeated;
        }
    }
}
