package pe;

import pe.event.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static pe.PixelEngine.screenHeight;
import static pe.PixelEngine.screenWidth;

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
    
    private static boolean entered, newEntered;
    
    private static int x, y, newX, newY;
    private static int relX, relY;
    private static int scrollX, scrollY, newScrollX, newScrollY;
    
    private static Button drag;
    private static int    dragX, dragY;
    
    private Mouse() { }
    
    public static double holdDelay()
    {
        return Mouse.holdDelay / 1_000_000_000D;
    }
    
    public static void holdDelay(double holdDelay)
    {
        Mouse.holdDelay = (long) (holdDelay * 1_000_000_000L);
    }
    
    public static double repeatDelay()
    {
        return Mouse.repeatDelay / 1_000_000_000D;
    }
    
    public static void repeatDelay(double repeatDelay)
    {
        Mouse.repeatDelay = (long) (repeatDelay * 1_000_000_000L);
    }
    
    public static Collection<Button> inputs()
    {
        return Mouse.inputs.values();
    }
    
    public static Button get(int reference)
    {
        return Mouse.inputs.getOrDefault(reference, Mouse.NONE);
    }
    
    public static boolean entered()
    {
        return Mouse.entered;
    }
    
    public static int x()
    {
        return Mouse.x;
    }
    
    public static int y()
    {
        return Mouse.y;
    }
    
    public static int relX()
    {
        return Mouse.relX;
    }
    
    public static int relY()
    {
        return Mouse.relY;
    }
    
    public static int scrollX()
    {
        return Mouse.scrollX;
    }
    
    public static int scrollY()
    {
        return Mouse.scrollY;
    }
    
    @SuppressWarnings("DuplicatedCode")
    protected static void handleEvents(long time, long delta)
    {
        if (Mouse.entered != Mouse.newEntered) Events.post(EventMouseEntered.class, Mouse.newEntered);
        Mouse.entered = Mouse.newEntered;
        
        Mouse.relX = Mouse.newX - Mouse.x;
        Mouse.relY = Mouse.newY - Mouse.y;
        Mouse.x    = Mouse.newX;
        Mouse.y    = Mouse.newY;
        if (Mouse.relX != 0 || Mouse.relY != 0) Events.post(EventMouseMoved.class, Mouse.x, Mouse.y, Mouse.relX, Mouse.relY);
        
        Mouse.scrollX    = Mouse.newScrollX;
        Mouse.scrollY    = Mouse.newScrollY;
        Mouse.newScrollX = 0;
        Mouse.newScrollY = 0;
        if (Mouse.scrollX != 0 || Mouse.scrollY != 0) Events.post(EventMouseScrolled.class, Mouse.scrollX, Mouse.scrollY);
        
        for (Button button : inputs())
        {
            button.down   = false;
            button.up     = false;
            button.repeat = false;
            
            if (button.state != button.prevState)
            {
                if (button.state == GLFW_PRESS)
                {
                    button.down     = true;
                    button.held     = true;
                    button.downTime = time;
                }
                else if (button.state == GLFW_RELEASE)
                {
                    button.up       = true;
                    button.held     = false;
                    button.downTime = Long.MAX_VALUE;
                }
            }
            if (button.state == GLFW_REPEAT || button.held && time - button.downTime > Mouse.holdDelay)
            {
                button.downTime += Mouse.repeatDelay;
                button.repeat = true;
            }
            button.prevState = button.state;
            
            if (button.down)
            {
                Events.post(EventMouseButtonDown.class, button, Mouse.x, Mouse.y);
                
                button.clickX = Mouse.x;
                button.clickY = Mouse.y;
                if (Mouse.drag == null)
                {
                    Mouse.drag  = button;
                    Mouse.dragX = Mouse.x;
                    Mouse.dragY = Mouse.y;
                }
            }
            if (button.up)
            {
                Events.post(EventMouseButtonUp.class, button, Mouse.x, Mouse.y);
                
                boolean inClickRange  = Math.abs(Mouse.x - button.clickX) < 2 && Math.abs(Mouse.y - button.clickY) < 2;
                boolean inDClickRange = Math.abs(Mouse.x - button.dClickX) < 2 && Math.abs(Mouse.y - button.dClickY) < 2;
                
                if (inDClickRange && time - button.clickTime < 500_000_000)
                {
                    Events.post(EventMouseButtonClicked.class, button, Mouse.x, Mouse.y, true);
                }
                else if (inClickRange)
                {
                    Events.post(EventMouseButtonClicked.class, button, Mouse.x, Mouse.y, false);
                    button.dClickX   = Mouse.x;
                    button.dClickY   = Mouse.y;
                    button.clickTime = time;
                }
                if (Mouse.drag == button) Mouse.drag = null;
            }
            if (button.held)
            {
                Events.post(EventMouseButtonHeld.class, button, Mouse.x, Mouse.y);
                
                if (Mouse.drag == button && (Mouse.relX != 0 || Mouse.relY != 0))
                {
                    Events.post(EventMouseButtonDragged.class, button, Mouse.dragX, Mouse.dragY, Mouse.x, Mouse.y, Mouse.relX, Mouse.relY);
                }
            }
            if (button.repeat) Events.post(EventMouseButtonRepeat.class, button, Mouse.x, Mouse.y);
        }
    }
    
    protected static void stateCallback(int reference, int state)
    {
        get(reference).state = state;
    }
    
    public static void enteredCallback(boolean entered)
    {
        Mouse.newEntered = entered;
    }
    
    public static void positionCallback(double x, double y)
    {
        Mouse.newX = (int) x;
        Mouse.newY = (int) y;
    
        if (Mouse.newX < 0) Mouse.newX = 0;
        if (Mouse.newY < 0) Mouse.newY = 0;
    
        if (screenWidth() <= Mouse.newX) Mouse.newX = screenWidth() - 1;
        if (screenHeight() <= Mouse.newY) Mouse.newY = screenHeight() - 1;
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
            Mouse.newX = Mouse.x = screenWidth() / 2;
            Mouse.newY = Mouse.y = screenHeight() / 2;
        }
    }
    
    public static class Button
    {
        private final String name;
        
        private boolean down   = false;
        private boolean up     = false;
        private boolean held   = false;
        private boolean repeat = false;
        
        private int state, prevState;
        private long downTime, clickTime;
        private int clickX, clickY, dClickX, dClickY;
        
        private Button(String name, int reference)
        {
            this.name = name;
            
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
        
        public boolean down()
        {
            return this.down;
        }
        
        public boolean up()
        {
            return this.up;
        }
        
        public boolean held()
        {
            return this.held;
        }
        
        public boolean repeat()
        {
            return this.repeat;
        }
    }
}
