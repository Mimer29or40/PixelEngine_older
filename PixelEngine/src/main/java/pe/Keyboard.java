package pe;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("unused")
public class Keyboard
{
    protected static final Map<Integer, Key> inputs = new HashMap<>();
    
    public static final Key NONE = new Key("NONE", GLFW_KEY_UNKNOWN, 0, 0);
    
    public static final Key A = new Key("A", GLFW_KEY_A, 'a', 'A');
    public static final Key B = new Key("B", GLFW_KEY_B, 'b', 'B');
    public static final Key C = new Key("C", GLFW_KEY_C, 'c', 'C');
    public static final Key D = new Key("D", GLFW_KEY_D, 'd', 'D');
    public static final Key E = new Key("E", GLFW_KEY_E, 'e', 'E');
    public static final Key F = new Key("F", GLFW_KEY_F, 'f', 'F');
    public static final Key G = new Key("G", GLFW_KEY_G, 'g', 'G');
    public static final Key H = new Key("H", GLFW_KEY_H, 'h', 'H');
    public static final Key I = new Key("I", GLFW_KEY_I, 'i', 'I');
    public static final Key J = new Key("J", GLFW_KEY_J, 'j', 'J');
    public static final Key K = new Key("K", GLFW_KEY_K, 'k', 'K');
    public static final Key L = new Key("L", GLFW_KEY_L, 'l', 'L');
    public static final Key M = new Key("M", GLFW_KEY_M, 'm', 'M');
    public static final Key N = new Key("N", GLFW_KEY_N, 'n', 'N');
    public static final Key O = new Key("O", GLFW_KEY_O, 'o', 'O');
    public static final Key P = new Key("P", GLFW_KEY_P, 'p', 'P');
    public static final Key Q = new Key("Q", GLFW_KEY_Q, 'q', 'Q');
    public static final Key R = new Key("R", GLFW_KEY_R, 'r', 'R');
    public static final Key S = new Key("S", GLFW_KEY_S, 's', 'S');
    public static final Key T = new Key("T", GLFW_KEY_T, 't', 'T');
    public static final Key U = new Key("U", GLFW_KEY_U, 'u', 'U');
    public static final Key V = new Key("V", GLFW_KEY_V, 'v', 'V');
    public static final Key W = new Key("W", GLFW_KEY_W, 'w', 'W');
    public static final Key X = new Key("X", GLFW_KEY_X, 'x', 'X');
    public static final Key Y = new Key("Y", GLFW_KEY_Y, 'y', 'Y');
    public static final Key Z = new Key("Z", GLFW_KEY_Z, 'z', 'Z');
    
    public static final Key K1 = new Key("K1", GLFW_KEY_1, '1', '!');
    public static final Key K2 = new Key("K2", GLFW_KEY_2, '2', '@');
    public static final Key K3 = new Key("K3", GLFW_KEY_3, '3', '#');
    public static final Key K4 = new Key("K4", GLFW_KEY_4, '4', '$');
    public static final Key K5 = new Key("K5", GLFW_KEY_5, '5', '%');
    public static final Key K6 = new Key("K6", GLFW_KEY_6, '6', '^');
    public static final Key K7 = new Key("K7", GLFW_KEY_7, '7', '&');
    public static final Key K8 = new Key("K8", GLFW_KEY_8, '8', '*');
    public static final Key K9 = new Key("K9", GLFW_KEY_9, '9', '(');
    public static final Key K0 = new Key("K0", GLFW_KEY_0, '0', ')');
    
    public static final Key GRAVE      = new Key("GRAVE", GLFW_KEY_GRAVE_ACCENT, '`', '~');
    public static final Key MINUS      = new Key("MINUS", GLFW_KEY_MINUS, '-', '_');
    public static final Key EQUALS     = new Key("EQUALS", GLFW_KEY_EQUAL, '=', '+');
    public static final Key L_BRACKET  = new Key("L_BRACKET", GLFW_KEY_LEFT_BRACKET, '[', '{');
    public static final Key R_BRACKET  = new Key("R_BRACKET", GLFW_KEY_RIGHT_BRACKET, ']', '}');
    public static final Key BACKSLASH  = new Key("BACKSLASH", GLFW_KEY_BACKSLASH, '\\', '|');
    public static final Key SEMICOLON  = new Key("SEMICOLON", GLFW_KEY_SEMICOLON, ';', ':');
    public static final Key APOSTROPHE = new Key("APOSTROPHE", GLFW_KEY_APOSTROPHE, '\'', '"');
    public static final Key COMMA      = new Key("COMMA", GLFW_KEY_COMMA, ',', '<');
    public static final Key PERIOD     = new Key("PERIOD", GLFW_KEY_PERIOD, '.', '>');
    public static final Key SLASH      = new Key("SLASH", GLFW_KEY_SLASH, '/', '?');
    
    public static final Key F1  = new Key("F1", GLFW_KEY_F1, 0, 0);
    public static final Key F2  = new Key("F2", GLFW_KEY_F2, 0, 0);
    public static final Key F3  = new Key("F3", GLFW_KEY_F3, 0, 0);
    public static final Key F4  = new Key("F4", GLFW_KEY_F4, 0, 0);
    public static final Key F5  = new Key("F5", GLFW_KEY_F5, 0, 0);
    public static final Key F6  = new Key("F6", GLFW_KEY_F6, 0, 0);
    public static final Key F7  = new Key("F7", GLFW_KEY_F7, 0, 0);
    public static final Key F8  = new Key("F8", GLFW_KEY_F8, 0, 0);
    public static final Key F9  = new Key("F9", GLFW_KEY_F9, 0, 0);
    public static final Key F10 = new Key("F10", GLFW_KEY_F10, 0, 0);
    public static final Key F11 = new Key("F11", GLFW_KEY_F11, 0, 0);
    public static final Key F12 = new Key("F12", GLFW_KEY_F12, 0, 0);
    
    public static final Key UP    = new Key("UP", GLFW_KEY_UP, 0, 0);
    public static final Key DOWN  = new Key("DOWN", GLFW_KEY_DOWN, 0, 0);
    public static final Key LEFT  = new Key("LEFT", GLFW_KEY_LEFT, 0, 0);
    public static final Key RIGHT = new Key("RIGHT", GLFW_KEY_RIGHT, 0, 0);
    
    public static final Key TAB       = new Key("TAB", GLFW_KEY_TAB, '\t', '\t');
    public static final Key CAPS_LOCK = new Key("CAPS_LOCK", GLFW_KEY_CAPS_LOCK, 0, 0);
    public static final Key ENTER     = new Key("ENTER", GLFW_KEY_ENTER, '\n', '\n');
    public static final Key BACK      = new Key("BACK", GLFW_KEY_BACKSPACE, '\b', '\b');
    public static final Key SPACE     = new Key("SPACE", GLFW_KEY_SPACE, ' ', ' ');
    
    public static final Key R_SHIFT = new Key("R_SHIFT", GLFW_KEY_LEFT_SHIFT, 0, 0);
    public static final Key L_SHIFT = new Key("L_SHIFT", GLFW_KEY_RIGHT_SHIFT, 0, 0);
    public static final Key R_CTRL  = new Key("R_CTRL", GLFW_KEY_LEFT_CONTROL, 0, 0);
    public static final Key L_CTRL  = new Key("L_CTRL", GLFW_KEY_RIGHT_CONTROL, 0, 0);
    public static final Key R_ALT   = new Key("R_ALT", GLFW_KEY_LEFT_ALT, 0, 0);
    public static final Key L_ALT   = new Key("L_ALT", GLFW_KEY_RIGHT_ALT, 0, 0);
    public static final Key L_SUPER = new Key("L_SUPER", GLFW_KEY_LEFT_SUPER, 0, 0);
    public static final Key R_SUPER = new Key("R_SUPER", GLFW_KEY_RIGHT_SUPER, 0, 0);
    
    public static final Key MENU         = new Key("MENU", GLFW_KEY_MENU, 0, 0);
    public static final Key ESCAPE       = new Key("ESCAPE", GLFW_KEY_ESCAPE, 0, 0);
    public static final Key PRINT_SCREEN = new Key("PRINT_SCREEN", GLFW_KEY_PRINT_SCREEN, 0, 0);
    public static final Key SCROLL_LOCK  = new Key("SCROLL_LOCK", GLFW_KEY_SCROLL_LOCK, 0, 0);
    public static final Key PAUSE        = new Key("PAUSE", GLFW_KEY_PAUSE, 0, 0);
    public static final Key INS          = new Key("INS", GLFW_KEY_INSERT, 0, 0);
    public static final Key DEL          = new Key("DEL", GLFW_KEY_DELETE, 0, 0);
    public static final Key HOME         = new Key("HOME", GLFW_KEY_HOME, 0, 0);
    public static final Key END          = new Key("END", GLFW_KEY_END, 0, 0);
    public static final Key PAGE_UP      = new Key("PAGE_UP", GLFW_KEY_PAGE_UP, 0, 0);
    public static final Key PAGE_DN      = new Key("PAGE_DN", GLFW_KEY_PAGE_DOWN, 0, 0);
    
    public static final Key NP0 = new Key("NP0", GLFW_KEY_KP_0, '0', 0);
    public static final Key NP1 = new Key("NP1", GLFW_KEY_KP_1, '1', 0);
    public static final Key NP2 = new Key("NP2", GLFW_KEY_KP_2, '2', 0);
    public static final Key NP3 = new Key("NP3", GLFW_KEY_KP_3, '3', 0);
    public static final Key NP4 = new Key("NP4", GLFW_KEY_KP_4, '4', 0);
    public static final Key NP5 = new Key("NP5", GLFW_KEY_KP_5, '5', 0);
    public static final Key NP6 = new Key("NP6", GLFW_KEY_KP_6, '6', 0);
    public static final Key NP7 = new Key("NP7", GLFW_KEY_KP_7, '7', 0);
    public static final Key NP8 = new Key("NP8", GLFW_KEY_KP_8, '8', 0);
    public static final Key NP9 = new Key("NP9", GLFW_KEY_KP_9, '9', 0);
    
    public static final Key NUM_LOCK   = new Key("NUM_LOCK", GLFW_KEY_NUM_LOCK, 0, 0);
    public static final Key NP_DIV     = new Key("NP_DIV", GLFW_KEY_KP_DIVIDE, '/', '/');
    public static final Key NP_MUL     = new Key("NP_MUL", GLFW_KEY_KP_MULTIPLY, '*', '*');
    public static final Key NP_SUB     = new Key("NP_SUB", GLFW_KEY_KP_SUBTRACT, '-', '-');
    public static final Key NP_ADD     = new Key("NP_ADD", GLFW_KEY_KP_ADD, '+', '+');
    public static final Key NP_DECIMAL = new Key("NP_DECIMAL", GLFW_KEY_KP_DECIMAL, '.', '.');
    public static final Key NP_EQUALS  = new Key("NP_EQUALS", GLFW_KEY_KP_EQUAL, '=', '=');
    public static final Key NP_ENTER   = new Key("NP_ENTER", GLFW_KEY_KP_ENTER, '\n', '\n');
    
    protected static long holdDelay   = 500_000_000;
    protected static long repeatDelay = 100_000_000;
    
    private static boolean captureText  = false;
    private static String  capturedText = "";
    
    private Keyboard()
    {
    
    }
    
    public static double getHoldDelay()
    {
        return Keyboard.holdDelay / 1_000_000_000D;
    }
    
    public static void setHoldDelay(double holdDelay)
    {
        Keyboard.holdDelay = (long) (holdDelay * 1_000_000_000L);
    }
    
    public static double getRepeatDelay()
    {
        return Keyboard.repeatDelay / 1_000_000_000D;
    }
    
    public static void setRepeatDelay(double repeatDelay)
    {
        Keyboard.repeatDelay = (long) (repeatDelay * 1_000_000_000L);
    }
    
    public static Collection<Key> getInputs()
    {
        return Keyboard.inputs.values();
    }
    
    public static Key get(int reference)
    {
        return Keyboard.inputs.getOrDefault(reference, Keyboard.NONE);
    }
    
    public static Key getKey(char key)
    {
        if (key == 0) return Keyboard.NONE;
        for (Key k : getInputs()) if (k.baseChar == key || k.shiftChar == key) return k;
        return Keyboard.NONE;
    }
    
    public static void captureText(boolean captureText)
    {
        Keyboard.captureText = captureText;
        Keyboard.capturedText = "";
    }
    
    public static String getCapturedText()
    {
        String text = Keyboard.capturedText;
        Keyboard.capturedText = "";
        return text;
    }
    
    public static void handleEvents(long time, long delta)
    {
        for (Key key : getInputs())
        {
            key.pressed = false;
            key.released = false;
            key.repeated = false;
            
            if (key.state != key.prevState)
            {
                if (key.state == GLFW_PRESS)
                {
                    key.pressed = true;
                    key.held = true;
                    key.downTime = time;
                }
                else if (key.state == GLFW_RELEASE)
                {
                    key.released = true;
                    key.held = false;
                    key.downTime = Long.MAX_VALUE;
                }
                else if (key.state == GLFW_REPEAT)
                {
                    key.pressed = true;
                    key.repeated = true;
                }
            }
            if (key.held && time - key.downTime > Keyboard.holdDelay)
            {
                key.downTime += Keyboard.repeatDelay;
                key.repeated = true;
            }
            key.prevState = key.state;
        }
    }
    
    public static boolean isShiftDown()
    {
        return Keyboard.L_SHIFT.held || Keyboard.R_SHIFT.held;
    }
    
    public static boolean isCtrlDown()
    {
        return Keyboard.L_CTRL.held || Keyboard.R_CTRL.held;
    }
    
    public static boolean isAltDown()
    {
        return Keyboard.L_ALT.held || Keyboard.R_ALT.held;
    }
    
    public static boolean isSuperDown()
    {
        return Keyboard.L_SUPER.held || Keyboard.R_SUPER.held;
    }
    
    protected static void stateCallback(int reference, int state)
    {
        get(reference).state = state;
    }
    
    public static void charCallback(int codePoint)
    {
        if (Keyboard.captureText) Keyboard.capturedText += Character.toString(codePoint);
    }
    
    public static class Key
    {
        public final    int     scancode;
        public final    char    baseChar;
        public final    char    shiftChar;
        protected final String  name;
        protected final int     reference;
        protected       boolean pressed  = false;
        protected       boolean released = false;
        protected       boolean held     = false;
        protected       boolean repeated = false;
        protected       long    downTime = 0;
        protected       int     state, prevState;
        
        private Key(String name, int reference, int baseChar, int shiftChar)
        {
            this.name = name;
            this.reference = reference;
            
            this.scancode = this.reference > 0 ? glfwGetKeyScancode(this.reference) : 0;
            
            this.baseChar = (char) baseChar;
            this.shiftChar = (char) shiftChar;
            
            Keyboard.inputs.put(reference, this);
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
