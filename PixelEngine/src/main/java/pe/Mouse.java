package pe;

import org.joml.Vector2i;
import org.joml.Vector2ic;
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
    
    private static final Vector2i pos       = new Vector2i();
    private static final Vector2i newPos    = new Vector2i();
    private static final Vector2i rel       = new Vector2i();
    private static final Vector2i scroll    = new Vector2i();
    private static final Vector2i newScroll = new Vector2i();
    
    private static       Button   drag;
    private static final Vector2i dragPos = new Vector2i();
    
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
    
    public static Vector2ic pos()
    {
        return Mouse.pos;
    }
    
    public static int x()
    {
        return Mouse.pos.x;
    }
    
    public static int y()
    {
        return Mouse.pos.y;
    }
    
    public static Vector2ic rel()
    {
        return Mouse.rel;
    }
    
    public static int relX()
    {
        return Mouse.rel.x;
    }
    
    public static int relY()
    {
        return Mouse.rel.y;
    }
    
    public static Vector2ic scroll()
    {
        return Mouse.scroll;
    }
    
    public static int scrollX()
    {
        return Mouse.scroll.x;
    }
    
    public static int scrollY()
    {
        return Mouse.scroll.y;
    }
    
    @SuppressWarnings("DuplicatedCode")
    protected static void handleEvents(long time, long delta)
    {
        if (Mouse.entered != Mouse.newEntered) Events.post(EventMouseEntered.class, Mouse.newEntered);
        Mouse.entered = Mouse.newEntered;
    
        Mouse.newPos.sub(Mouse.pos, Mouse.rel);
        Mouse.pos.set(Mouse.newPos);
        if (Mouse.rel.x != 0 || Mouse.rel.y != 0) Events.post(EventMouseMoved.class, Mouse.pos, Mouse.rel);
    
        Mouse.scroll.set(Mouse.newScroll);
        Mouse.newScroll.set(0);
        if (Mouse.scroll.x != 0 || Mouse.scroll.y != 0) Events.post(EventMouseScrolled.class, Mouse.scroll);
    
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
                Events.post(EventMouseButtonDown.class, button, Mouse.pos);
    
                button.click.set(Mouse.pos);
                if (Mouse.drag == null)
                {
                    Mouse.drag = button;
                    Mouse.dragPos.set(Mouse.pos);
                }
            }
            if (button.up)
            {
                Events.post(EventMouseButtonUp.class, button, Mouse.pos);
    
                boolean inClickRange  = Math.abs(Mouse.pos.x - button.click.x) < 2 && Math.abs(Mouse.pos.y - button.click.y) < 2;
                boolean inDClickRange = Math.abs(Mouse.pos.x - button.dClick.x) < 2 && Math.abs(Mouse.pos.y - button.dClick.y) < 2;
    
                if (inDClickRange && time - button.clickTime < 500_000_000)
                {
                    Events.post(EventMouseButtonClicked.class, button, Mouse.pos, true);
                }
                else if (inClickRange)
                {
                    Events.post(EventMouseButtonClicked.class, button, Mouse.pos, false);
                    button.dClick.set(Mouse.pos);
                    button.clickTime = time;
                }
                if (Mouse.drag == button) Mouse.drag = null;
            }
            if (button.held)
            {
                Events.post(EventMouseButtonHeld.class, button, Mouse.pos);
            
                if (Mouse.drag == button && (Mouse.rel.x != 0 || Mouse.rel.y != 0))
                {
                    Events.post(EventMouseButtonDragged.class, button, Mouse.dragPos, Mouse.pos, Mouse.rel);
                }
            }
            if (button.repeat) Events.post(EventMouseButtonRepeat.class, button, Mouse.pos);
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
        Mouse.newPos.set((int) x, (int) y);
    
        if (Mouse.newPos.x < 0) Mouse.newPos.x = 0;
        if (Mouse.newPos.y < 0) Mouse.newPos.y = 0;
    
        if (screenWidth() <= Mouse.newPos.x) Mouse.newPos.x = screenWidth() - 1;
        if (screenHeight() <= Mouse.newPos.y) Mouse.newPos.y = screenHeight() - 1;
    }
    
    public static void scrollCallback(double x, double y)
    {
        Mouse.newScroll.add((int) x, (int) y);
    }
    
    public static void captureCallback(boolean captured)
    {
        if (captured) Mouse.newPos.set(Mouse.pos.set(screenWidth() / 2, screenHeight() / 2));
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
        private final Vector2i click  = new Vector2i();
        private final Vector2i dClick = new Vector2i();
    
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
