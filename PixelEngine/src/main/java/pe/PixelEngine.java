package pe;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.reflections.Reflections;
import pe.util.Pair;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@SuppressWarnings({"unused", "SameParameterValue"})
public class PixelEngine
{
    private static final Logger   LOGGER   = Logger.getLogger();
    private static final Profiler profiler = new Profiler("Engine");
    
    private static PixelEngine logic;
    
    private static final Map<String, PEX> extensions = new HashMap<>();
    
    private static final Color COLOR = Color.WHITE.copy();
    
    private static final Pattern bottomChars = Pattern.compile(".*[gjpqy,]+.*");
    
    private static final String TITLE = "Pixel Game Engine - %s - FPS(%s) SPF(Avg: %s us, Min: %s us, Max: %s us)";
    
    private static int    overdraw = 0;
    private static Random random   = new Random();
    
    private static String printFrame = null;
    
    private static long glfwWindow;
    private static long startTime;
    
    private static boolean engineRunning = false;
    
    private static int monitorW, monitorH;
    
    private static int windowX, windowY;
    private static int windowW, windowH;
    
    private static int viewX, viewY;
    private static int viewW, viewH;
    
    private static int screenW, screenH;
    
    private static int pixelW, pixelH;
    
    private static boolean fullscreen, vsync;
    
    private static boolean updateWindow = true;
    private static boolean focused      = false;
    
    private static Sprite font;
    private static Sprite prev;
    private static Sprite window;
    private static Sprite target;
    
    private static DrawMode drawMode = DrawMode.NORMAL;
    
    private static IBlendPos blendFunc;
    
    private String name;
    
    protected PixelEngine()
    {
        PixelEngine.LOGGER.trace("PixelEngine instance created");
        String        className = this.getClass().getSimpleName();
        StringBuilder name      = new StringBuilder();
        for (int i = 0; i < className.length(); i++)
        {
            char ch = className.charAt(i);
            if (i > 0 && Character.isUpperCase(ch)) name.append(" ");
            if (ch == '_')
            {
                name.append(" - ");
            }
            else
            {
                name.append(ch);
            }
        }
        this.name = name.toString();
    }
    
    /**
     * Called once before engine enter loop. Use to initialize user variables.
     *
     * @return True if engine can continue to run
     */
    protected boolean onUserCreate()
    {
        return false;
    }
    
    /**
     * Called every frame.
     *
     * @param elapsedTime time in seconds since that last frame. Must be overridden for engine to run
     *
     * @return True if engine can continue to run
     */
    protected boolean onUserUpdate(double elapsedTime)
    {
        return false;
    }
    
    /**
     * Called once after engine is put into a stopped state.
     */
    protected void onUserDestroy()
    {
        
    }
    
    /**
     * This will create and initialize the window size, stats and pixel dimensions.
     *
     * @param logic      Sub-Classed PixelEngine providing onUser methods
     * @param screenW    Screen width in pixels
     * @param screenH    Screen height in pixels
     * @param pixelW     Width of pixel in actual pixels
     * @param pixelH     Height of pixel in actual pixels
     * @param fullscreen Engine should be fullscreen
     * @param vsync      Engine should lock to monitor refresh rate
     */
    protected static void start(PixelEngine logic, int screenW, int screenH, int pixelW, int pixelH, boolean fullscreen, boolean vsync)
    {
        PixelEngine.LOGGER.info("Engine Started");
        
        if (PixelEngine.logic != null) throw new RuntimeException("PixelEngine can only be constructed once.");
        PixelEngine.LOGGER.trace("Setting Logic");
        PixelEngine.logic = logic;
        
        PixelEngine.screenW = screenW;
        PixelEngine.screenH = screenH;
        PixelEngine.LOGGER.trace("Screen Size (%s, %s)", screenW, screenH);
        
        PixelEngine.pixelW = pixelW;
        PixelEngine.pixelH = pixelH;
        PixelEngine.LOGGER.trace("Color Dimensions (%s, %s)", pixelW, pixelH);
        
        PixelEngine.fullscreen = fullscreen;
        PixelEngine.LOGGER.trace("Fullscreen: %s)", fullscreen);
        
        PixelEngine.vsync = vsync;
        PixelEngine.LOGGER.trace("VSync: %s)", vsync);
        
        if (PixelEngine.screenW == 0 || PixelEngine.screenH == 0) throw new RuntimeException("Screen dimension must be > 0");
        if (PixelEngine.pixelW == 0 || PixelEngine.pixelH == 0) throw new RuntimeException("Color dimension must be > 0");
        PixelEngine.LOGGER.trace("Screen Size and Color Dimensions pass initial test");
        
        createFontSheet();
        
        loadExtensions();
        
        PixelEngine.window = PixelEngine.target = new Sprite(screenW, screenH);
        PixelEngine.prev = new Sprite(screenW, screenH);
        
        PixelEngine.engineRunning = true;
        PixelEngine.startTime = System.nanoTime();
        
        try
        {
            PixelEngine.LOGGER.debug("Initializing Extensions");
            PixelEngine.extensions.values().forEach(PEX::initialize);
            
            PixelEngine.LOGGER.debug("User Initialization");
            if (PixelEngine.logic.onUserCreate())
            {
                setupWindow();
                
                new Thread(PixelEngine::renderLoop, "Render Loop").start();
                
                while (PixelEngine.engineRunning) glfwPollEvents();
            }
        }
        finally
        {
            PixelEngine.LOGGER.debug("User Initialization");
            PixelEngine.logic.onUserDestroy();
    
            PixelEngine.LOGGER.trace("Extension Destruction");
            PixelEngine.extensions.values().forEach(PEX::destroy);
            
            if (PixelEngine.glfwWindow > 0)
            {
                glfwFreeCallbacks(PixelEngine.glfwWindow);
                glfwDestroyWindow(PixelEngine.glfwWindow);
                
                glfwTerminate();
                glfwSetErrorCallback(null);
            }
        }
        
        PixelEngine.LOGGER.info("Engine Finished");
    }
    
    protected static void start(PixelEngine gameLogic, int screenW, int screenH, int pixelW, int pixelH)
    {
        start(gameLogic, screenW, screenH, pixelW, pixelH, false, false);
    }
    
    protected static void start(PixelEngine gameLogic, int screenW, int screenH)
    {
        start(gameLogic, screenW, screenH, 4, 4, false, false);
    }
    
    protected static void start(PixelEngine gameLogic)
    {
        start(gameLogic, 300, 200, 4, 4, false, false);
    }
    
    public static void stop()
    {
        PixelEngine.engineRunning = false;
    }
    
    /**
     * DEBUG
     * Gets the number of times that a pixel was drawn. Reset to 0 before onUserUpdate
     *
     * @return Overdraw count if engine is in debug mode else 0
     */
    public static int getOverdrawCount()
    {
        return PixelEngine.overdraw;
    }
    
    /**
     * DEBUG
     * Increments overdraw count by amount. Used in clear method since that writes to sprite's buffer directly
     */
    public static void incOverdrawCount(int amount)
    {
        PixelEngine.overdraw += amount;
    }
    
    /**
     * DEBUG
     * Increments overdraw count by 1.
     */
    public static void incOverdrawCount()
    {
        incOverdrawCount(1);
    }
    
    public static long getTime()
    {
        return System.nanoTime() - PixelEngine.startTime;
    }
    
    // --------------
    // - Properties -
    // --------------
    
    public static int getMonitorWidth()
    {
        return PixelEngine.monitorW;
    }
    
    public static int getMonitorHeight()
    {
        return PixelEngine.monitorH;
    }
    
    public static int getWindowX()
    {
        return PixelEngine.windowX;
    }
    
    public static int getWindowY()
    {
        return PixelEngine.windowY;
    }
    
    public static int getWindowWidth()
    {
        return PixelEngine.windowW;
    }
    
    public static int getWindowHeight()
    {
        return PixelEngine.windowH;
    }
    
    public static int getScreenWidth()
    {
        return PixelEngine.screenW;
    }
    
    public static int getScreenHeight()
    {
        return PixelEngine.screenH;
    }
    
    public static int getPixelWidth()
    {
        return PixelEngine.pixelW;
    }
    
    public static int getPixelHeight()
    {
        return PixelEngine.pixelH;
    }
    
    public static boolean isFullscreen()
    {
        return PixelEngine.fullscreen;
    }
    
    public static void setFullscreen(boolean fullscreen)
    {
        PixelEngine.fullscreen = fullscreen;
        PixelEngine.updateWindow = true;
    }
    
    public static boolean isVSync()
    {
        return PixelEngine.vsync;
    }
    
    public static void setVSync(boolean vsync)
    {
        PixelEngine.vsync = vsync;
        PixelEngine.updateWindow = true;
    }
    
    public static boolean isFocused()
    {
        return PixelEngine.focused;
    }
    
    public static DrawMode getDrawMode()
    {
        return PixelEngine.drawMode;
    }
    
    public static void setDrawMode(DrawMode mode)
    {
        PixelEngine.drawMode = mode;
    }
    
    public static void setColorMode(IBlendPos pixelFunc)
    {
        PixelEngine.drawMode = DrawMode.CUSTOM;
        PixelEngine.blendFunc = pixelFunc;
    }
    
    public static Sprite getTarget()
    {
        return PixelEngine.target;
    }
    
    public static void setTarget(Sprite target)
    {
        PixelEngine.target = target != null ? target : PixelEngine.window;
    }
    
    public static int getTargetWidth()
    {
        return PixelEngine.target != null ? PixelEngine.target.width : 0;
    }
    
    public static int getTargetHeight()
    {
        return PixelEngine.target != null ? PixelEngine.target.height : 0;
    }
    
    public static int scaleToPixels(double scale)
    {
        return (int) round(8 * scale, 0);
    }
    
    // -------------
    // - Functions -
    // -------------
    
    public static int getTextWidth(String text, double scale)
    {
        if (text.contains("\n"))
        {
            int max = 0;
            for (String s : text.split("\n")) max = Math.max(max, getTextWidth(s, scale));
            return max;
        }
        return scaleToPixels((text.length() * 8 - 1) / 8.0 * scale);
    }
    
    public static int getTextWidth(String text)
    {
        return getTextWidth(text, 1);
    }
    
    public static int getTextHeight(String text, double scale)
    {
        int size = Math.max(scaleToPixels(scale), 1);
        
        if (text.contains("\n"))
        {
            String[] lines  = text.split("\n");
            int      bottom = bottomChars.matcher(lines[lines.length - 1]).matches() ? 0 : 1;
            return scaleToPixels((lines.length * 8 - bottom) / 8.0 * scale);
        }
        int bottom = bottomChars.matcher(text).matches() ? 0 : 1;
        return scaleToPixels((8.0 - bottom) / 8.0 * scale);
    }
    
    public static int getTextHeight(String text)
    {
        return getTextHeight(text, 1);
    }
    
    public static List<String> clipTextWidth(String text, double scale, int maxWidth)
    {
        List<String> lines = new ArrayList<>();
        
        if (text.contains("\n"))
        {
            lines.addAll(Arrays.asList(text.split("\n")));
        }
        else
        {
            lines.add(text);
        }
        
        for (int i = 0; i < lines.size(); i++)
        {
            String line = lines.remove(i);
            
            if (getTextWidth(line, scale) > maxWidth)
            {
                String[]      subLines = line.split(" ");
                StringBuilder newLine  = new StringBuilder(subLines[0]);
                for (int j = 1; j < subLines.length; j++)
                {
                    if (getTextWidth(newLine.toString() + " " + subLines[j], scale) > maxWidth)
                    {
                        lines.add(i, newLine.toString());
                        i++;
                        newLine.setLength(0);
                        newLine.append(subLines[j]);
                        continue;
                    }
                    newLine.append(" ").append(subLines[j]);
                }
                lines.add(i, newLine.toString());
            }
            else
            {
                lines.add(i, line);
            }
        }
        return lines;
    }
    
    public static void print(Object object)
    {
        System.out.println(object);
    }
    
    public static void print(String format, Object... objects)
    {
        System.out.println(String.format(format, objects));
    }
    
    public static void print(Object... objects)
    {
        StringBuilder builder = new StringBuilder();
        for (Object object : objects)
        {
            builder.append(object);
            builder.append(" ");
        }
        
        System.out.println(builder.substring(0, builder.length() - 1));
    }
    
    public static void setSeed(long seed)
    {
        PixelEngine.random.setSeed(seed);
    }
    
    public static double random()
    {
        return PixelEngine.random.nextDouble();
    }
    
    public static double random(double upper)
    {
        return PixelEngine.random.nextDouble() * upper;
    }
    
    public static double random(double lower, double upper)
    {
        return PixelEngine.random.nextDouble() * (upper - lower) + lower;
    }
    
    public static int randInt(int upper)
    {
        return PixelEngine.random.nextInt(upper);
    }
    
    public static int randInt(int lower, int upper)
    {
        return PixelEngine.random.nextInt(upper - lower + 1) + lower;
    }
    
    public static double map(double x, double xMin, double xMax, double yMin, double yMax)
    {
        return (x - xMin) * (yMax - yMin) / (xMax - xMin) + yMin;
    }
    
    public static Pair<Integer, Integer> getFormatNumbers(double[] values)
    {
        int numI = 1, numD = 0;
        for (double val : values)
        {
            String[] num = String.valueOf(val).split("\\.");
            numI = Math.max(numI, num[0].length());
            if (val != (int) val) numD = Math.max(numD, num[1].length());
        }
        return new Pair<>(numI, numD);
    }
    
    // TODO - Negative Numbers
    public static String format(double x, int numI, int numD)
    {
        String I  = String.valueOf((int) x);
        String D  = numD > 0 ? String.valueOf((int) Math.round((x - (int) x) * Math.pow(10, numD))) : "";
        String fI = numI > I.length() ? "%" + (numI - I.length()) + "s" : "%s";
        String fD = numD > D.length() ? "%" + (numD - D.length()) + "s" : "%s";
        return String.format(fI + "%s%s%s" + fD, "", I, numD > 0 ? "." : "", D, "");
    }
    
    public static String format(double x, Pair<Integer, Integer> numbers)
    {
        return format(x, numbers.a, numbers.b);
    }
    
    public static String join(Collection<?> lines, String between, String prefix, String suffix)
    {
        if (lines.size() == 0) return "";
        Object[]      array = lines.toArray();
        StringBuilder b     = new StringBuilder(prefix);
        b.append(array[0]);
        for (int i = 1, n = array.length; i < n; i++) b.append(between).append(array[i]);
        return b.append(suffix).toString();
    }
    
    public static String join(Collection<?> lines, String between)
    {
        return join(lines, between, "", "");
    }
    
    public static String join(Collection<?> lines)
    {
        return join(lines, ", ", "", "");
    }
    
    public static double round(double value, int places)
    {
        if (places <= 0) return Math.round(value);
        double pow = Math.pow(10, places);
        return Math.round(value * pow) / pow;
    }
    
    public static Path getPath(String filePath)
    {
        URL file = PixelEngine.class.getClassLoader().getResource(filePath);
        if (file != null)
        {
            try
            {
                return Paths.get(file.toURI());
            }
            catch (URISyntaxException ignored)
            {
    
            }
        }
        return Paths.get(filePath);
    }
    
    public static void enableProfiler()
    {
        PixelEngine.profiler.enabled = true;
    }
    
    public static void printFrameData(String parent)
    {
        if (PixelEngine.profiler.enabled) PixelEngine.printFrame = parent;
    }
    
    // --------
    // - Draw -
    // --------
    
    public static void clear(Color p)
    {
        PixelEngine.target.clear(p);
    }
    
    public static void clear()
    {
        clear(Color.BACKGROUND_GREY);
    }
    
    public static void draw(int x, int y, Color p)
    {
        if (PixelEngine.target == null) return;
        
        switch (PixelEngine.drawMode)
        {
            case NORMAL:
                PixelEngine.target.setPixel(x, y, p);
                break;
            case MASK:
                if (p.a() == 255) PixelEngine.target.setPixel(x, y, p);
                break;
            case ALPHA:
                Color d = PixelEngine.target.getPixel(x, y);
                float a = (float) p.a() / 255.0F;
                float c = 1.0F - a;
                PixelEngine.COLOR.r((int) (a * (float) p.r() + c * (float) d.r()));
                PixelEngine.COLOR.g((int) (a * (float) p.g() + c * (float) d.g()));
                PixelEngine.COLOR.b((int) (a * (float) p.b() + c * (float) d.b()));
                PixelEngine.COLOR.a(255);
                PixelEngine.target.setPixel(x, y, PixelEngine.COLOR);
                break;
            case CUSTOM:
                Color result = PixelEngine.blendFunc.blend(x, y, p, PixelEngine.target.getPixel(x, y));
                PixelEngine.target.setPixel(x, y, result);
                break;
        }
    }
    
    public static void draw(int x, int y)
    {
        draw(x, y, Color.WHITE);
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2, Color p, int pattern)
    {
        // Bresenham's Algorithm
        
        int dx, dy, x, y, xi, yi, d, tmp;
        
        dx = x2 - x1;
        dy = y2 - y1;
        
        if (dx == 0)
        {
            if (y2 < y1)
            {
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            for (y = y1; y <= y2; y++)
            {
                if (((pattern = (pattern << 1) | (pattern >>> 31)) & 1) != 0) draw(x1, y, p);
            }
            return;
        }
        
        if (y2 - y1 == 0)
        {
            if (x2 < x1)
            {
                tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            for (x = x1; x <= x2; x++)
            {
                if (((pattern = (pattern << 1) | (pattern >>> 31)) & 1) != 0) draw(x, y1, p);
            }
            return;
        }
        
        if (Math.abs(dy) < Math.abs(dx))
        {
            if (x1 > x2)
            {
                tmp = x1;
                x1 = x2;
                x2 = tmp;
                
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            
            dx = x2 - x1;
            dy = y2 - y1;
            yi = 1;
            if (dy < 0)
            {
                yi = -1;
                dy = -dy;
            }
            d = 2 * dy - dx;
            y = y1;
            
            for (x = x1; x <= x2; x++)
            {
                if (((pattern = (pattern << 1) | (pattern >>> 31)) & 1) != 0) draw(x, y, p);
                if (d > 0)
                {
                    y += yi;
                    d = d - 2 * dx;
                }
                d = d + 2 * dy;
            }
        }
        else
        {
            if (y1 > y2)
            {
                tmp = x1;
                x1 = x2;
                x2 = tmp;
                
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            
            dx = x2 - x1;
            dy = y2 - y1;
            xi = 1;
            if (dx < 0)
            {
                xi = -1;
                dx = -dx;
            }
            d = 2 * dx - dy;
            x = x1;
            
            for (y = y1; y <= y2; y++)
            {
                if (((pattern = (pattern << 1) | (pattern >>> 31)) & 1) != 0) draw(x, y, p);
                if (d > 0)
                {
                    x = x + xi;
                    d = d - 2 * dy;
                }
                d = d + 2 * dx;
            }
        }
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2, Color p)
    {
        drawLine(x1, y1, x2, y2, p, 0xFFFFFFFF);
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2, int pattern)
    {
        drawLine(x1, y1, x2, y2, Color.WHITE, pattern);
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2)
    {
        drawLine(x1, y1, x2, y2, Color.WHITE, 0xFFFFFFFF);
    }
    
    public static void drawCircle(int x, int y, int radius, Color p, int mask)
    {
        int x0 = 0;
        int y0 = radius;
        int d  = 3 - 2 * radius;
        if (radius < 0) return;
        
        while (y0 >= x0) // only formulate 1/8 of circle
        {
            if ((mask & 0x01) > 0) draw(x + x0, y - y0, p);
            if ((mask & 0x02) > 0) draw(x + y0, y - x0, p);
            if ((mask & 0x04) > 0) draw(x + y0, y + x0, p);
            if ((mask & 0x08) > 0) draw(x + x0, y + y0, p);
            if ((mask & 0x10) > 0) draw(x - x0, y + y0, p);
            if ((mask & 0x20) > 0) draw(x - y0, y + x0, p);
            if ((mask & 0x40) > 0) draw(x - y0, y - x0, p);
            if ((mask & 0x80) > 0) draw(x - x0, y - y0, p);
            
            if (d < 0)
            {
                d += 4 * x0++ + 6;
            }
            else
            {
                d += 4 * (x0++ - y0--) + 10;
            }
        }
    }
    
    public static void drawCircle(int x, int y, int radius, Color p)
    {
        drawCircle(x, y, radius, p, 0xFF);
    }
    
    public static void drawCircle(int x, int y, int radius, int mask)
    {
        drawCircle(x, y, radius, Color.WHITE, mask);
    }
    
    public static void drawCircle(int x, int y, int radius)
    {
        drawCircle(x, y, radius, Color.WHITE, 0xFF);
    }
    
    public static void fillCircle(int x, int y, int radius, Color p)
    {
        int x0 = 0;
        int y0 = radius;
        int d  = 3 - 2 * radius;
        if (radius < 0) return;
        
        while (y0 >= x0)
        {
            for (int i = x - x0; i <= x + x0; i++) draw(i, y - y0, p);
            for (int i = x - y0; i <= x + y0; i++) draw(i, y - x0, p);
            for (int i = x - x0; i <= x + x0; i++) draw(i, y + y0, p);
            for (int i = x - y0; i <= x + y0; i++) draw(i, y + x0, p);
            
            if (d < 0)
            {
                d += 4 * x0++ + 6;
            }
            else
            {
                d += 4 * (x0++ - y0--) + 10;
            }
        }
    }
    
    public static void fillCircle(int x, int y, int radius)
    {
        fillCircle(x, y, radius, Color.WHITE);
    }
    
    public static void drawRect(int x, int y, int w, int h, Color p)
    {
        drawLine(x, y, x + w - 1, y, p);
        drawLine(x + w - 1, y, x + w - 1, y + h - 1, p);
        drawLine(x + w - 1, y + h - 1, x, y + h - 1, p);
        drawLine(x, y + h - 1, x, y, p);
    }
    
    public static void drawRect(int x, int y, int w, int h)
    {
        drawRect(x, y, w, h, Color.WHITE);
    }
    
    public static void fillRect(int x, int y, int w, int h, Color p)
    {
        int x2 = x + w;
        int y2 = y + h;
        
        x = Math.max(0, Math.min(x, PixelEngine.screenW));
        y = Math.max(0, Math.min(y, PixelEngine.screenH));
        x2 = Math.max(0, Math.min(x2, PixelEngine.screenW));
        y2 = Math.max(0, Math.min(y2, PixelEngine.screenH));
        
        for (int i = x; i < x2; i++)
        {
            for (int j = y; j < y2; j++)
            {
                draw(i, j, p);
            }
        }
    }
    
    public static void fillRect(int x, int y, int w, int h)
    {
        fillRect(x, y, w, h, Color.WHITE);
    }
    
    public static void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color p)
    {
        drawLine(x1, y1, x2, y2, p);
        drawLine(x2, y2, x3, y3, p);
        drawLine(x3, y3, x1, y1, p);
    }
    
    public static void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        drawTriangle(x1, y1, x2, y2, x3, y3, Color.WHITE);
    }
    
    public static void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color p)
    {
        int minX, minY, maxX, maxY;
        
        minX = Math.min(x1, Math.min(x2, x3));
        minY = Math.min(y1, Math.min(y2, y3));
        maxX = Math.max(x1, Math.max(x2, x3));
        maxY = Math.max(y1, Math.max(y2, y3));
        
        minX = Math.max(0, Math.min(minX, PixelEngine.screenW));
        minY = Math.max(0, Math.min(minY, PixelEngine.screenH));
        maxX = Math.max(0, Math.min(maxX, PixelEngine.screenW));
        maxY = Math.max(0, Math.min(maxY, PixelEngine.screenH));
        
        int abc = Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
        int abp, apc, pbc;
        
        for (int i = minX; i <= maxX; i++)
        {
            for (int j = minY; j <= maxY; j++)
            {
                pbc = Math.abs(i * (y2 - y3) + x2 * (y3 - j) + x3 * (j - y2));
                apc = Math.abs(x1 * (j - y3) + i * (y3 - y1) + x3 * (y1 - j));
                abp = Math.abs(x1 * (y2 - j) + x2 * (j - y1) + i * (y1 - y2));
                if (abc == pbc + apc + abp) PixelEngine.draw(i, j, p);
            }
        }
    }
    
    public static void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        fillTriangle(x1, y1, x2, y2, x3, y3, Color.WHITE);
    }
    
    public static void drawPartialSprite(int x, int y, Sprite sprite, int ox, int oy, int w, int h, double scale)
    {
        if (scale <= 0.0 || sprite == null) return;
        
        if (scale == (int) scale)
        {
            if (scale == 1)
            {
                for (int i = 0; i < w; i++)
                {
                    for (int j = 0; j < h; j++)
                    {
                        draw(x + i, y + j, sprite.getPixel(i + ox, j + oy));
                    }
                }
            }
            else
            {
                for (int i = 0; i < w; i++)
                {
                    for (int j = 0; j < h; j++)
                    {
                        for (int is = 0; is < scale; is++)
                        {
                            for (int js = 0; js < scale; js++)
                            {
                                draw(x + (i * (int) scale) + is, y + (j * (int) scale) + js, sprite.getPixel(i + ox, j + oy));
                            }
                        }
                    }
                }
            }
        }
        else
        {
            int newW = Math.max((int) Math.round(w * scale), 1);
            int newH = Math.max((int) Math.round(h * scale), 1);
            
            Color p = new Color();
            
            double r, g, b, a, total;
            double uMin, uMax, vMin, vMax;
            double xPercent, yPercent, pPercent;
            
            for (int j = 0; j < newH; j++)
            {
                for (int i = 0; i < newW; i++)
                {
                    r = g = b = a = total = 0.0;
                    
                    uMin = i / (double) newW * w;
                    uMax = (i + 1) / (double) newW * w;
                    vMin = j / (double) newH * h;
                    vMax = (j + 1) / (double) newH * h;
                    
                    for (int v = (int) vMin; v < (int) Math.ceil(vMax); v++)
                    {
                        for (int u = (int) uMin; u < (int) Math.ceil(uMax); u++)
                        {
                            xPercent = u < uMin ? 1.0 - uMin + u : u + 1 > uMax ? uMax - u : 1.0;
                            yPercent = v < vMin ? 1.0 - vMin + v : v + 1 > vMax ? vMax - v : 1.0;
                            pPercent = xPercent * yPercent;
                            
                            sprite.getPixel(ox + u, oy + v, p);
                            
                            r += pPercent * p.r();
                            g += pPercent * p.g();
                            b += pPercent * p.b();
                            a += pPercent * p.a();
                            
                            total += pPercent * 255;
                        }
                    }
                    
                    p.set((float) (r / total), (float) (g / total), (float) (b / total), (float) (a / total));
                    draw(x + i, y + j, p);
                }
            }
        }
    }
    
    public static void drawPartialSprite(int x, int y, Sprite sprite, int ox, int oy, int w, int h)
    {
        drawPartialSprite(x, y, sprite, ox, oy, w, h, 1);
    }
    
    public static void drawSprite(int x, int y, Sprite sprite, double scale)
    {
        drawPartialSprite(x, y, sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), scale);
    }
    
    public static void drawSprite(int x, int y, Sprite sprite)
    {
        drawPartialSprite(x, y, sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), 1);
    }
    
    public static void drawString(int x, int y, String text, Color color, double scale)
    {
        if (scale <= 0.0) return;
        
        int  sx = 0, sy = 0;
        char c;
        int  ox, oy;
        
        if (scale == (int) scale)
        {
            setDrawMode(color.a() == 255 ? DrawMode.MASK : DrawMode.ALPHA);
            
            for (int ci = 0; ci < text.length(); ci++)
            {
                c = text.charAt(ci);
                
                if (c == '\n')
                {
                    sx = 0;
                    sy += 8 * scale;
                }
                else
                {
                    ox = (c - 32) % 16;
                    oy = (c - 32) / 16;
                    
                    if (scale == 1)
                    {
                        for (int i = 0; i < 8; i++)
                        {
                            for (int j = 0; j < 8; j++)
                            {
                                if (PixelEngine.font.getPixel(i + ox * 8, j + oy * 8).r() > 0) draw(x + sx + i, y + sy + j, color);
                            }
                        }
                    }
                    else
                    {
                        for (int i = 0; i < 8; i++)
                        {
                            for (int j = 0; j < 8; j++)
                            {
                                if (PixelEngine.font.getPixel(i + ox * 8, j + oy * 8).r() > 0)
                                {
                                    for (int is = 0; is < scale; is++)
                                    {
                                        for (int js = 0; js < scale; js++)
                                        {
                                            draw(x + sx + (i * (int) scale) + is, y + sy + (j * (int) scale) + js, color);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    sx += 8 * scale;
                }
            }
        }
        else
        {
            setDrawMode(DrawMode.ALPHA);
            
            int size = Math.max(scaleToPixels(scale), 1);
            
            double p, total;
            double uMin, uMax, vMin, vMax;
            double xPercent, yPercent, pPercent;
            
            for (int ci = 0; ci < text.length(); ci++)
            {
                c = text.charAt(ci);
                
                if (c == '\n')
                {
                    sx = 0;
                    sy += 8 * scale;
                }
                else
                {
                    ox = (c - 32) % 16;
                    oy = (c - 32) / 16;
                    
                    for (int j = 0; j < size; j++)
                    {
                        for (int i = 0; i < size; i++)
                        {
                            p = total = 0.0;
                            
                            uMin = i / (double) size * 8.0;
                            uMax = (i + 1) / (double) size * 8.0;
                            vMin = j / (double) size * 8.0;
                            vMax = (j + 1) / (double) size * 8.0;
                            
                            for (int v = (int) vMin; v < (int) Math.ceil(vMax); v++)
                            {
                                for (int u = (int) uMin; u < (int) Math.ceil(uMax); u++)
                                {
                                    xPercent = u < uMin ? 1.0 - uMin + u : u + 1 > uMax ? uMax - u : 1.0;
                                    yPercent = v < vMin ? 1.0 - vMin + v : v + 1 > vMax ? vMax - v : 1.0;
                                    pPercent = xPercent * yPercent;
                                    
                                    if (PixelEngine.font.getPixel(ox * 8 + u, oy * 8 + v).r() > 0) p += pPercent;
                                    total += pPercent;
                                }
                            }
                            
                            if (p > 0)
                            {
                                PixelEngine.COLOR.set(color);
                                PixelEngine.COLOR.a((int) (color.a() * p / total));
                                draw(x + sx + i, y + sy + j, PixelEngine.COLOR);
                            }
                        }
                    }
                    sx += 8 * scale;
                }
            }
        }
        
        setDrawMode(DrawMode.NORMAL);
    }
    
    public static void drawString(int x, int y, String text, Color color)
    {
        drawString(x, y, text, color, 1);
    }
    
    public static void drawString(int x, int y, String text, double scale)
    {
        drawString(x, y, text, Color.WHITE, scale);
    }
    
    public static void drawString(int x, int y, String text)
    {
        drawString(x, y, text, Color.WHITE, 1);
    }
    
    private static void loadExtensions()
    {
        PixelEngine.LOGGER.info("Loading Extensions");
        
        Reflections reflections = new Reflections("pe");
        for (Class<? extends PEX> ext : reflections.getSubTypesOf(PEX.class))
        {
            try
            {
                String name = ext.getSimpleName();
                PixelEngine.LOGGER.debug("Found: %s", name);
                PixelEngine.extensions.put(name, ext.getConstructor(Profiler.class).newInstance(PixelEngine.profiler));
            }
            catch (ReflectiveOperationException ignored)
            {
    
            }
        }
    }
    
    private static void createFontSheet()
    {
        PixelEngine.LOGGER.debug("Generating Font Data");
        
        String data = "";
        data += "?Q`0001oOch0o01o@F40o0<AGD4090LAGD<090@A7ch0?00O7Q`0600>00000000";
        data += "O000000nOT0063Qo4d8>?7a14Gno94AA4gno94AaOT0>o3`oO400o7QN00000400";
        data += "Of80001oOg<7O7moBGT7O7lABET024@aBEd714AiOdl717a_=TH013Q>00000000";
        data += "720D000V?V5oB3Q_HdUoE7a9@DdDE4A9@DmoE4A;Hg]oM4Aj8S4D84@`00000000";
        data += "OaPT1000Oa`^13P1@AI[?g`1@A=[OdAoHgljA4Ao?WlBA7l1710007l100000000";
        data += "ObM6000oOfMV?3QoBDD`O7a0BDDH@5A0BDD<@5A0BGeVO5ao@CQR?5Po00000000";
        data += "Oc``000?Ogij70PO2D]??0Ph2DUM@7i`2DTg@7lh2GUj?0TO0C1870T?00000000";
        data += "70<4001o?P<7?1QoHg43O;`h@GT0@:@LB@d0>:@hN@L0@?aoN@<0O7ao0000?000";
        data += "OcH0001SOglLA7mg24TnK7ln24US>0PL24U140PnOgl0>7QgOcH0K71S0000A000";
        data += "00H00000@Dm1S007@DUSg00?OdTnH7YhOfTL<7Yh@Cl0700?@Ah0300700000000";
        data += "<008001QL00ZA41a@6HnI<1i@FHLM81M@@0LG81?O`0nC?Y7?`0ZA7Y300080000";
        data += "O`082000Oh0827mo6>Hn?Wmo?6HnMb11MP08@C11H`08@FP0@@0004@000000000";
        data += "00P00001Oab00003OcKP0006@6=PMgl<@440MglH@000000`@000001P00000000";
        data += "Ob@8@@00Ob@8@Ga13R@8Mga172@8?PAo3R@827QoOb@820@0O`0007`0000007P0";
        data += "O`000P08Od400g`<3V=P0G`673IP0`@3>1`00P@6O`P00g`<O`000GP800000000";
        data += "?P9PL020O`<`N3R0@E4HC7b0@ET<ATB0@@l6C4B0O`H3N7b0?P01L3R000000020";
        
        PixelEngine.LOGGER.trace("Font Sheet Sprite Generated");
        PixelEngine.font = new Sprite(128, 48);
        int   px = 0, py = 0;
        Color p  = new Color();
        for (int b = 0; b < 1024; b += 4)
        {
            int sym1 = (int) data.charAt(b) - 48;
            int sym2 = (int) data.charAt(b + 1) - 48;
            int sym3 = (int) data.charAt(b + 2) - 48;
            int sym4 = (int) data.charAt(b + 3) - 48;
            int r    = sym1 << 18 | sym2 << 12 | sym3 << 6 | sym4;
            
            for (int i = 0; i < 24; i++)
            {
                int k = (r & (1 << i)) != 0 ? 255 : 0;
                PixelEngine.font.setPixel(px, py, p.set(k, k, k, k));
                if (++py == 48)
                {
                    px++;
                    py = 0;
                }
            }
        }
        PixelEngine.LOGGER.trace("Font Sheet Generation Finished");
    }
    
    private static void setupWindow()
    {
        PixelEngine.LOGGER.debug("Window Creation Started");
        
        PixelEngine.windowW = PixelEngine.screenW * PixelEngine.pixelW;
        PixelEngine.windowH = PixelEngine.screenH * PixelEngine.pixelH;
        
        PixelEngine.LOGGER.trace("Window Size: (%s, %s)", PixelEngine.windowW, PixelEngine.windowH);
        
        PixelEngine.LOGGER.trace("GLFW: Init");
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        
        PixelEngine.LOGGER.trace("GLFW: Hints");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        
        PixelEngine.LOGGER.trace("GLFW: Checking Window Size");
        GLFWVidMode videoMode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));
        PixelEngine.monitorW = videoMode.width();
        PixelEngine.monitorH = videoMode.height();
        
        if (PixelEngine.fullscreen)
        {
            PixelEngine.windowW = videoMode.width();
            PixelEngine.windowH = videoMode.height();
        }
        
        if (PixelEngine.windowW > PixelEngine.monitorW)
        {
            throw new RuntimeException(String.format("Window width (%s) is greater than monitor width", PixelEngine.windowW));
        }
        if (PixelEngine.windowH > PixelEngine.monitorH)
        {
            throw new RuntimeException(String.format("Window height (%s) is greater than monitor height", PixelEngine.windowH));
        }
        
        PixelEngine.LOGGER.trace("GLFW: Creating Window");
        PixelEngine.glfwWindow = glfwCreateWindow(PixelEngine.windowW, PixelEngine.windowH, "", NULL, NULL);
        
        if (PixelEngine.glfwWindow == NULL) throw new RuntimeException("Failed to create the GLFW window");
        
        PixelEngine.windowX = (PixelEngine.monitorW - PixelEngine.windowW) >> 1;
        PixelEngine.windowY = (PixelEngine.monitorH - PixelEngine.windowH) >> 1;
        glfwSetWindowPos(PixelEngine.glfwWindow, PixelEngine.windowX, PixelEngine.windowY);
        
        PixelEngine.LOGGER.trace("GLFW: Event Handling");
        
        glfwSetWindowCloseCallback(PixelEngine.glfwWindow, window -> {
            if (window != PixelEngine.glfwWindow) return;
            stop();
        });
        
        glfwSetWindowPosCallback(PixelEngine.glfwWindow, (window, x, y) -> {
            if (window != PixelEngine.glfwWindow) return;
            if (PixelEngine.fullscreen) return;
            PixelEngine.windowX = x;
            PixelEngine.windowY = y;
        });
        
        glfwSetWindowSizeCallback(PixelEngine.glfwWindow, (window, w, h) -> {
            if (window != PixelEngine.glfwWindow) return;
            PixelEngine.updateWindow = true;
            if (PixelEngine.fullscreen) return;
            PixelEngine.windowW = w;
            PixelEngine.windowH = h;
        });
        
        glfwSetWindowFocusCallback(PixelEngine.glfwWindow, (window, focused) -> {
            if (window != PixelEngine.glfwWindow) return;
            PixelEngine.focused = focused;
        });
        
        glfwSetCursorEnterCallback(PixelEngine.glfwWindow, (window, entered) -> {
            if (window != PixelEngine.glfwWindow) return;
            Mouse.enteredCallback(entered);
        });
        
        glfwSetCursorPosCallback(PixelEngine.glfwWindow, (window, x, y) -> {
            if (window != PixelEngine.glfwWindow) return;
            x = (x - PixelEngine.viewX) * (double) PixelEngine.screenW / (double) PixelEngine.viewW;
            y = (y - PixelEngine.viewY) * (double) PixelEngine.screenH / (double) PixelEngine.viewH;
            Mouse.positionCallback(x, y);
        });
        
        glfwSetScrollCallback(PixelEngine.glfwWindow, (window, x, y) -> {
            if (window != PixelEngine.glfwWindow) return;
            Mouse.scrollCallback(x, y);
        });
        
        glfwSetMouseButtonCallback(PixelEngine.glfwWindow, (window, mouse, action, mods) -> {
            if (window != PixelEngine.glfwWindow) return;
            Mouse.stateCallback(mouse, action);
        });
        
        glfwSetKeyCallback(PixelEngine.glfwWindow, (window, key, scancode, action, mods) -> {
            if (window != PixelEngine.glfwWindow) return;
            Keyboard.stateCallback(key, action);
        });
        
        glfwSetCharCallback(PixelEngine.glfwWindow, (window, codePoint) -> {
            if (window != PixelEngine.glfwWindow) return;
            System.out.println(Character.toString(codePoint));
            Keyboard.charCallback(codePoint);
        });
        
        glfwShowWindow(PixelEngine.glfwWindow);
        
        PixelEngine.LOGGER.trace("GLFW: Init Completed");
        
        PixelEngine.LOGGER.debug("Window Creation Finished");
    }
    
    private static void setupOpenGL()
    {
        PixelEngine.LOGGER.debug("Creating OpenGL Context");
        
        GL.createCapabilities();
        
        PixelEngine.LOGGER.trace("OpenGL: Shader setup");
        {
            int program, shader, result;
            program = glCreateProgram();
            
            PixelEngine.LOGGER.trace("OpenGL: Vertex Shader");
            
            shader = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(shader,
                           "#version 430 core\n" +
                           "layout(location = 0) in vec2 pos;\n" +
                           "out vec2 cord;\n" +
                           "void main(void)\n" +
                           "{\n" +
                           "    cord = vec2(pos.x < 0 ? 0.0 : 1.0, pos.y < 0 ? 1.0 : 0.0);\n" +
                           "    gl_Position = vec4(pos, 0.0, 1.0);\n" +
                           "}\n");
            glCompileShader(shader);
            
            result = glGetShaderi(shader, GL_COMPILE_STATUS);
            if (result != GL_TRUE)
            {
                String log = glGetShaderInfoLog(shader);
                throw new RuntimeException(String.format("Vertex Shader compile failure: %s", log));
            }
            glAttachShader(program, shader);
            glDeleteShader(shader);
            
            PixelEngine.LOGGER.trace("OpenGL: Fragment Shader");
            
            shader = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(shader,
                           "#version 430 core\n" +
                           "uniform sampler2D text;\n" +
                           "in vec2 cord;\n" +
                           "out vec4 color;\n" +
                           "void main(void)\n" +
                           "{\n" +
                           "    color = texture(text, cord);\n" +
                           "}\n");
            glCompileShader(shader);
            
            result = glGetShaderi(shader, GL_COMPILE_STATUS);
            if (result != GL_TRUE)
            {
                String log = glGetShaderInfoLog(shader);
                throw new RuntimeException(String.format("Fragment Shader compile failure: %s", log));
            }
            glAttachShader(program, shader);
            glDeleteShader(shader);
            
            PixelEngine.LOGGER.trace("OpenGL: Linking Program");
            
            glLinkProgram(program);
            result = glGetProgrami(program, GL_LINK_STATUS);
            if (result != GL_TRUE)
            {
                String log = glGetProgramInfoLog(program);
                throw new RuntimeException(String.format("Link failure: %s", log));
            }
            
            glValidateProgram(program);
            result = glGetProgrami(program, GL_VALIDATE_STATUS);
            if (result != GL_TRUE)
            {
                String log = glGetProgramInfoLog(program);
                throw new RuntimeException(String.format("Validation failure: %s", log));
            }
            glUseProgram(program);
        }
        PixelEngine.LOGGER.trace("OpenGL: Shader Validated");
        
        PixelEngine.LOGGER.trace("OpenGL: Setting Viewport");
        
        glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        glViewport(PixelEngine.viewX, PixelEngine.viewY, PixelEngine.viewW, PixelEngine.viewH);
        
        PixelEngine.LOGGER.trace("OpenGL: Building Vertex Array");
        
        glBindVertexArray(glGenVertexArrays());
        glBindBuffer(GL_ARRAY_BUFFER, glGenBuffers());
        glBufferData(GL_ARRAY_BUFFER, new float[] {-1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F, 1.0F}, GL_STATIC_DRAW);
        
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        
        PixelEngine.LOGGER.trace("OpenGL: Building Texture");
        
        glEnable(GL_TEXTURE_2D);
        
        glBindTexture(GL_TEXTURE_2D, glGenTextures());
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, PixelEngine.screenW, PixelEngine.screenH, 0, GL_RGBA, GL_UNSIGNED_BYTE, PixelEngine.window.getData());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        
        glfwSwapBuffers(PixelEngine.glfwWindow);
        glFinish();
        
        PixelEngine.LOGGER.debug("OpenGL Context Initialized");
    }
    
    private static void updateWindow()
    {
        PixelEngine.LOGGER.debug("Updating Window");
        
        PixelEngine.profiler.startSection("Setting vSync");
        {
            glfwSwapInterval(PixelEngine.vsync ? 1 : 0);
        }
        PixelEngine.profiler.endSection();
        
        PixelEngine.profiler.startSection("Updating Window Size/Position");
        {
            GLFWVidMode videoMode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));
            
            PixelEngine.monitorW = videoMode.width();
            PixelEngine.monitorH = videoMode.height();
            
            if (PixelEngine.fullscreen)
            {
                glfwSetWindowPos(PixelEngine.glfwWindow, 0, 0);
                glfwSetWindowSize(PixelEngine.glfwWindow, PixelEngine.monitorW, PixelEngine.monitorH);
            }
            else
            {
                glfwSetWindowPos(PixelEngine.glfwWindow, PixelEngine.windowX, PixelEngine.windowY);
                glfwSetWindowSize(PixelEngine.glfwWindow, PixelEngine.windowW, PixelEngine.windowH);
            }
        }
        PixelEngine.profiler.endSection();
        
        PixelEngine.profiler.startSection("Setting vSync");
        {
            int    ww     = PixelEngine.screenW * PixelEngine.pixelW;
            int    wh     = PixelEngine.screenH * PixelEngine.pixelH;
            double aspect = (double) ww / (double) wh;
            
            
            int actual_w = PixelEngine.fullscreen ? PixelEngine.monitorW : PixelEngine.windowW;
            int actual_h = PixelEngine.fullscreen ? PixelEngine.monitorH : PixelEngine.windowH;
            
            PixelEngine.viewW = actual_w;
            PixelEngine.viewH = (int) (PixelEngine.viewW / aspect);
            
            if (PixelEngine.viewH > actual_h)
            {
                PixelEngine.viewH = actual_h;
                PixelEngine.viewW = (int) (PixelEngine.viewH * aspect);
            }
            
            PixelEngine.viewX = (actual_w - PixelEngine.viewW) >> 1;
            PixelEngine.viewY = (actual_h - PixelEngine.viewH) >> 1;
            
            PixelEngine.LOGGER.debug("Viewport Pos(%s, %s) Size(%s, %s)", PixelEngine.viewX, PixelEngine.viewY, PixelEngine.viewW, PixelEngine.viewH);
        }
        PixelEngine.profiler.endSection();
    }
    
    private static void renderLoop()
    {
        glfwMakeContextCurrent(PixelEngine.glfwWindow);
        
        setupOpenGL();
        
        long t, dt;
        long lastFrame  = System.nanoTime();
        long lastSecond = 0;
        
        long frameTime;
        long minTime   = Long.MAX_VALUE;
        long maxTime   = Long.MIN_VALUE;
        long totalTime = 0;
        
        int totalFrames = 0;
        
        try
        {
            while (PixelEngine.engineRunning)
            {
                PixelEngine.LOGGER.trace("Frame Started");
                
                t = System.nanoTime();
                dt = t - lastFrame;
                lastFrame = t;
                
                PixelEngine.profiler.startTick();
                {
                    PixelEngine.profiler.startSection("Events");
                    {
                        PixelEngine.LOGGER.trace("Updating Mouse States");
                        {
                            Mouse.handleEvents(t, dt);
                        }
                        PixelEngine.profiler.endSection();
                        
                        PixelEngine.profiler.startSection("Key States");
                        {
                            Keyboard.handleEvents(t, dt);
                        }
                        PixelEngine.profiler.endSection();
                    }
                    PixelEngine.profiler.endSection();
                    
                    PixelEngine.overdraw = 0;
    
                    PixelEngine.profiler.startSection("PEX Pre");
                    {
                        for (String name : PixelEngine.extensions.keySet())
                        {
                            PixelEngine.profiler.startSection(name);
                            {
                                PixelEngine.extensions.get(name).beforeUserUpdate(dt / 1_000_000_000D);
                            }
                            PixelEngine.profiler.endSection();
                        }
                    }
                    PixelEngine.profiler.endSection();
                    
                    PixelEngine.profiler.startSection("User Update");
                    {
                        if (!PixelEngine.logic.onUserUpdate(dt / 1_000_000_000D))
                        {
                            PixelEngine.LOGGER.trace("onUserUpdate return false so engine will stop");
                            PixelEngine.engineRunning = false;
                        }
                    }
                    PixelEngine.profiler.endSection();
    
                    PixelEngine.profiler.startSection("PEX Post");
                    {
                        for (String name : PixelEngine.extensions.keySet())
                        {
                            PixelEngine.profiler.startSection(name);
                            {
                                PixelEngine.extensions.get(name).afterUserUpdate(dt / 1_000_000_000D);
                            }
                            PixelEngine.profiler.endSection();
                        }
                    }
                    PixelEngine.profiler.endSection();
                    
                    PixelEngine.LOGGER.trace("Overdraw Count: %s", PixelEngine.overdraw);
                    
                    PixelEngine.profiler.startSection("Window Update");
                    {
                        if (PixelEngine.updateWindow) updateWindow();
                    }
                    PixelEngine.profiler.endSection();
                    
                    PixelEngine.profiler.startSection("Render");
                    {
                        for (int i = 0; i < PixelEngine.screenW * PixelEngine.screenH; i++)
                        {
                            ByteBuffer currData = PixelEngine.window.getData();
                            ByteBuffer prevData = PixelEngine.prev.getData();
                            if (PixelEngine.updateWindow || currData.getInt(4 * i) != prevData.getInt(4 * i))
                            {
                                PixelEngine.LOGGER.trace("Rendering Frame");
                                
                                PixelEngine.profiler.startSection("Update Viewport");
                                {
                                    glClear(GL_COLOR_BUFFER_BIT);
                                    glViewport(PixelEngine.viewX, PixelEngine.viewY, PixelEngine.viewW, PixelEngine.viewH);
                                }
                                PixelEngine.profiler.endSection();
                                
                                PixelEngine.profiler.startSection("Update Texture");
                                {
                                    glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, PixelEngine.screenW, PixelEngine.screenH, GL_RGBA, GL_UNSIGNED_BYTE, PixelEngine.window.getData());
                                }
                                PixelEngine.profiler.endSection();
                                
                                PixelEngine.profiler.startSection("Draw Array");
                                {
                                    glDrawArrays(GL_TRIANGLES, 0, 6);
                                }
                                PixelEngine.profiler.endSection();
                                
                                PixelEngine.profiler.startSection("Swap");
                                {
                                    glfwSwapBuffers(PixelEngine.glfwWindow);
                                }
                                PixelEngine.profiler.endSection();
                                
                                PixelEngine.profiler.startSection("Swap Sprites");
                                {
                                    PixelEngine.window.copy(PixelEngine.prev);
                                }
                                PixelEngine.profiler.endSection();
                                
                                PixelEngine.updateWindow = false;
                                
                                break;
                            }
                        }
                    }
                    PixelEngine.profiler.endSection();
                    
                    PixelEngine.profiler.startSection("Stats");
                    {
                        PixelEngine.profiler.startSection("Update");
                        {
                            frameTime = System.nanoTime() - t;
                            minTime = Math.min(minTime, frameTime);
                            maxTime = Math.max(maxTime, frameTime);
                            totalTime += frameTime;
                            totalFrames++;
                        }
                        PixelEngine.profiler.endSection();
                        
                        dt = t - lastSecond;
                        if (dt > 1_000_000_000L)
                        {
                            PixelEngine.profiler.startSection("Update Title");
                            {
                                lastSecond = t;
                                
                                double s = 1000D;
                                
                                totalTime /= totalFrames;
                                
                                glfwSetWindowTitle(PixelEngine.glfwWindow,
                                                   String.format(PixelEngine.TITLE, PixelEngine.logic.name, totalFrames, totalTime / s, minTime / s, maxTime / s));
                                
                                totalTime = 0;
                                minTime = Long.MAX_VALUE;
                                maxTime = Long.MIN_VALUE;
                                totalFrames = 0;
                            }
                            PixelEngine.profiler.endSection();
                        }
                    }
                    PixelEngine.profiler.endSection();
                }
                PixelEngine.profiler.endTick();
                
                if (PixelEngine.profiler.enabled && PixelEngine.printFrame != null)
                {
                    String parent = PixelEngine.printFrame.equals("") ? null : PixelEngine.printFrame;
                    print(PixelEngine.profiler.getFormattedData(parent));
                    PixelEngine.printFrame = null;
                }
            }
        }
        finally
        {
            PixelEngine.engineRunning = false;
        }
    }
}
