package pe;

import org.joml.*;
import org.reflections.Reflections;
import pe.color.Blend;
import pe.color.Color;
import pe.color.Colorc;
import pe.color.IBlendPos;
import pe.render.DrawMode;
import pe.render.OpenGLRenderer;
import pe.render.Renderer;
import pe.util.PairI;

import java.lang.Math;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "SameParameterValue"})
public class PixelEngine
{
    private static final Logger   LOGGER   = Logger.getLogger();
    private static final Profiler PROFILER = new Profiler("Engine");
    
    private static final Color COLOR = new Color(Color.WHITE);
    
    private static final String         TITLE        = "Pixel Game Engine - %s - FPS(%s) SPF(Avg: %s us, Min: %s us, Max: %s us)";
    private static final Pattern        BOTTOM_CHARS = Pattern.compile(".*[gjpqy,]+.*");
    private static final HashSet<PairI> DRAW_CORDS   = new HashSet<>();
    
    private static PixelEngine logic;
    
    private static final Map<String, PEX> extensions = new HashMap<>();
    
    private static String  printFrame;
    private static boolean running;
    private static long    startTime;
    
    private static final Vector2i screenSize = new Vector2i();
    private static final Vector2i pixelSize  = new Vector2i();
    
    private static final Random random = new Random();
    
    private static       Renderer renderer;
    private static final Blend    blend = new Blend();
    
    private final String name;
    
    protected PixelEngine()
    {
        PixelEngine.LOGGER.trace("PixelEngine instance created");
        String        clazz = getClass().getSimpleName();
        StringBuilder name  = new StringBuilder();
        for (int i = 0; i < clazz.length(); i++)
        {
            char ch = clazz.charAt(i);
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
    protected boolean setup()
    {
        return false;
    }
    
    /**
     * Called every frame.
     *
     * @param elapsedTime time in seconds since that last frame. Must be overridden for engine to run
     * @return True if engine can continue to run
     */
    protected boolean draw(double elapsedTime)
    {
        return false;
    }
    
    /**
     * Called once after engine is put into a stopped state.
     */
    protected void destroy()
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
        
        PixelEngine.screenSize.set(screenW, screenH);
        PixelEngine.LOGGER.trace("Screen Size (%s, %s)", screenW, screenH);
        
        PixelEngine.pixelSize.x = pixelW;
        PixelEngine.pixelSize.y = pixelH;
        PixelEngine.LOGGER.trace("Color Dimensions (%s, %s)", pixelW, pixelH);
        
        Window.fullscreen(fullscreen);
        PixelEngine.LOGGER.trace("Fullscreen: %s)", fullscreen);
        
        Window.vsync(vsync);
        PixelEngine.LOGGER.trace("VSync: %s)", vsync);
        
        if (PixelEngine.screenSize.x == 0 || PixelEngine.screenSize.y == 0) throw new RuntimeException("Screen dimension must be > 0");
        if (PixelEngine.pixelSize.x == 0 || PixelEngine.pixelSize.y == 0) throw new RuntimeException("Pixel dimension must be > 0");
        PixelEngine.LOGGER.trace("Screen Size and Color Dimensions pass initial test");
    
        // PixelEngine.renderer = new SoftwareRenderer();
        PixelEngine.renderer = new OpenGLRenderer();
    
        loadExtensions();
    
        PixelEngine.running   = true;
        PixelEngine.startTime = System.nanoTime();
    
        try
        {
            PixelEngine.LOGGER.debug("Extension Pre Setup");
            PixelEngine.extensions.values().forEach(PEX::beforeSetup);
            
            PixelEngine.LOGGER.debug("User Initialization");
            if (PixelEngine.logic.setup())
            {
                PixelEngine.LOGGER.debug("Extension Post Setup");
                PixelEngine.extensions.values().forEach(PEX::afterSetup);
    
                Window.setup();
    
                new Thread(PixelEngine::renderLoop, "Render Loop").start();
    
                while (PixelEngine.running) Window.pollEvents();
            }
        }
        finally
        {
            PixelEngine.LOGGER.trace("Extension Pre Destruction");
            PixelEngine.extensions.values().forEach(PEX::beforeDestroy);
    
            PixelEngine.LOGGER.debug("User Initialization");
            PixelEngine.logic.destroy();
    
            PixelEngine.LOGGER.trace("Extension Post Destruction");
            PixelEngine.extensions.values().forEach(PEX::afterDestroy);
    
            Window.destroy();
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
        PixelEngine.running = false;
    }
    
    public static long getTime()
    {
        return System.nanoTime() - PixelEngine.startTime;
    }
    
    // --------------
    // - Properties -
    // --------------
    
    public static Vector2ic screenSize()
    {
        return PixelEngine.screenSize;
    }
    
    public static int screenWidth()
    {
        return PixelEngine.screenSize.x;
    }
    
    public static int screenHeight()
    {
        return PixelEngine.screenSize.y;
    }
    
    public static Vector2ic pixelSize()
    {
        return PixelEngine.screenSize;
    }
    
    // TODO - set pixel dims on window resize
    public static int pixelWidth()
    {
        return PixelEngine.pixelSize.x;
    }
    
    public static int pixelHeight()
    {
        return PixelEngine.pixelSize.y;
    }
    
    public static Renderer renderer()
    {
        return PixelEngine.renderer;
    }
    
    public static Blend blend()
    {
        return PixelEngine.blend;
    }
    
    // -------------
    // - Functions -
    // -------------
    
    public static int scaleToPixels(double scale)
    {
        return (int) round(8 * scale, 0);
    }
    
    public static int textWidth(String text, double scale)
    {
        if (text.contains("\n"))
        {
            int max = 0;
            for (String s : text.split("\n")) max = Math.max(max, textWidth(s, scale));
            return max;
        }
        return scaleToPixels((text.length() * 8 - 1) / 8.0 * scale);
    }
    
    public static int textWidth(String text)
    {
        return textWidth(text, 1);
    }
    
    public static int textHeight(String text, double scale)
    {
        int size = Math.max(scaleToPixels(scale), 1);
        
        if (text.contains("\n"))
        {
            String[] lines  = text.split("\n");
            int      bottom = BOTTOM_CHARS.matcher(lines[lines.length - 1]).matches() ? 0 : 1;
            return scaleToPixels((lines.length * 8 - bottom) / 8.0 * scale);
        }
        int bottom = BOTTOM_CHARS.matcher(text).matches() ? 0 : 1;
        return scaleToPixels((8.0 - bottom) / 8.0 * scale);
    }
    
    public static int textHeight(String text)
    {
        return textHeight(text, 1);
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
            
            if (textWidth(line, scale) > maxWidth)
            {
                String[]      subLines = line.split(" ");
                StringBuilder newLine  = new StringBuilder(subLines[0]);
                for (int j = 1; j < subLines.length; j++)
                {
                    if (textWidth(newLine.toString() + " " + subLines[j], scale) > maxWidth)
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
        System.out.print(object);
    }
    
    public static void print(String format, Object... objects)
    {
        System.out.print(String.format(format, objects));
    }
    
    public static void print(Object... objects)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, n = objects.length; i < n; i++)
        {
            builder.append(objects[i]);
            if (i + 1 < n) builder.append(" ");
        }
        System.out.print(builder.toString());
    }
    
    public static void println(Object object)
    {
        System.out.println(object);
    }
    
    public static void println(String format, Object... objects)
    {
        System.out.println(String.format(format, objects));
    }
    
    public static void println(Object... objects)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, n = objects.length; i < n; i++)
        {
            builder.append(objects[i]);
            if (i + 1 < n) builder.append(" ");
        }
        System.out.println(builder.toString());
    }
    
    public static double map(double x, double xMin, double xMax, double yMin, double yMax)
    {
        return (x - xMin) * (yMax - yMin) / (xMax - xMin) + yMin;
    }
    
    public static PairI getFormatNumbers(double[] values)
    {
        int numI = 1, numD = 0;
        for (double val : values)
        {
            String[] num = String.valueOf(val).split("\\.");
            numI = Math.max(numI, num[0].length());
            if (val != (int) val) numD = Math.max(numD, num[1].length());
        }
        return new PairI(numI, numD);
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
    
    public static String format(double x, PairI numbers)
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
        PixelEngine.PROFILER.enabled = true;
    }
    
    public static void printFrameData(String parent)
    {
        if (PixelEngine.PROFILER.enabled) PixelEngine.printFrame = parent;
    }
    
    public static void enableExtension(String extension)
    {
        if (PixelEngine.extensions.containsKey(extension)) PixelEngine.extensions.get(extension).enable();
    }
    
    public static void disableExtension(String extension)
    {
        if (PixelEngine.extensions.containsKey(extension)) PixelEngine.extensions.get(extension).disable();
    }
    
    // ----------
    // - Random -
    // ----------
    
    public static Random random()
    {
        return PixelEngine.random;
    }
    
    public static void setSeed(long seed)
    {
        PixelEngine.random.setSeed(seed);
    }
    
    public static boolean nextBoolean()
    {
        return PixelEngine.random.nextBoolean();
    }
    
    public static int nextInt()
    {
        return PixelEngine.random.nextInt();
    }
    
    public static int nextInt(int limit)
    {
        return PixelEngine.random.nextInt(limit);
    }
    
    public static int nextInt(int origin, int limit)
    {
        return PixelEngine.random.nextInt(origin, limit);
    }
    
    public static long nextLong()
    {
        return PixelEngine.random.nextLong();
    }
    
    public static long nextLong(long limit)
    {
        return PixelEngine.random.nextLong(limit);
    }
    
    public static long nextLong(long origin, long limit)
    {
        return PixelEngine.random.nextLong(origin, limit);
    }
    
    public static float nextFloat()
    {
        return PixelEngine.random.nextFloat();
    }
    
    public static float nextFloat(float limit)
    {
        return PixelEngine.random.nextFloat(limit);
    }
    
    public static float nextFloat(float origin, float limit)
    {
        return PixelEngine.random.nextFloat(origin, limit);
    }
    
    public static double nextDouble()
    {
        return PixelEngine.random.nextDouble();
    }
    
    public static double nextDouble(double limit)
    {
        return PixelEngine.random.nextDouble(limit);
    }
    
    public static double nextDouble(double origin, double limit)
    {
        return PixelEngine.random.nextDouble(origin, limit);
    }
    
    public static double nextGaussian()
    {
        return PixelEngine.random.nextGaussian();
    }
    
    public static int nextIndex(int[] array)
    {
        return PixelEngine.random.nextIndex(array);
    }
    
    public static long nextIndex(long[] array)
    {
        return PixelEngine.random.nextIndex(array);
    }
    
    public static float nextIndex(float[] array)
    {
        return PixelEngine.random.nextIndex(array);
    }
    
    public static double nextIndex(double[] array)
    {
        return PixelEngine.random.nextIndex(array);
    }
    
    public static <T> T nextIndex(T[] array)
    {
        return PixelEngine.random.nextIndex(array);
    }
    
    public static <T> T nextIndex(Collection<T> collection)
    {
        return PixelEngine.random.nextIndex(collection);
    }
    
    public static int choose(int... options)
    {
        return PixelEngine.random.nextIndex(options);
    }
    
    public static long choose(long... options)
    {
        return PixelEngine.random.nextIndex(options);
    }
    
    public static float choose(float... options)
    {
        return PixelEngine.random.nextIndex(options);
    }
    
    public static double choose(double... options)
    {
        return PixelEngine.random.nextIndex(options);
    }
    
    public static Vector2i nextVector2i()
    {
        return PixelEngine.random.nextVector2i();
    }
    
    public static Vector2i nextVector2i(int bound)
    {
        return PixelEngine.random.nextVector2i(bound);
    }
    
    public static Vector2i nextVector2i(int origin, int bound)
    {
        return PixelEngine.random.nextVector2i(origin, bound);
    }
    
    public static Vector3i nextVector3i()
    {
        return PixelEngine.random.nextVector3i();
    }
    
    public static Vector3i nextVector3i(int bound)
    {
        return PixelEngine.random.nextVector3i(bound);
    }
    
    public static Vector3i nextVector3i(int origin, int bound)
    {
        return PixelEngine.random.nextVector3i(origin, bound);
    }
    
    public static Vector4i nextVector4i()
    {
        return PixelEngine.random.nextVector4i();
    }
    
    public static Vector4i nextVector4i(int bound)
    {
        return PixelEngine.random.nextVector4i(bound);
    }
    
    public static Vector4i nextVector4i(int origin, int bound)
    {
        return PixelEngine.random.nextVector4i(origin, bound);
    }
    
    public static Vector2f nextVector2f()
    {
        return PixelEngine.random.nextVector2f();
    }
    
    public static Vector2f nextVector2f(float bound)
    {
        return PixelEngine.random.nextVector2f(bound);
    }
    
    public static Vector2f nextVector2f(float origin, float bound)
    {
        return PixelEngine.random.nextVector2f(origin, bound);
    }
    
    public static Vector3f nextVector3f()
    {
        return PixelEngine.random.nextVector3f();
    }
    
    public static Vector3f nextVector3f(float bound)
    {
        return PixelEngine.random.nextVector3f(bound);
    }
    
    public static Vector3f nextVector3f(float origin, float bound)
    {
        return PixelEngine.random.nextVector3f(origin, bound);
    }
    
    public static Vector4f nextVector4f()
    {
        return PixelEngine.random.nextVector4f();
    }
    
    public static Vector4f nextVector4f(float bound)
    {
        return PixelEngine.random.nextVector4f(bound);
    }
    
    public static Vector4f nextVector4f(float origin, float bound)
    {
        return PixelEngine.random.nextVector4f(origin, bound);
    }
    
    public static Vector2d nextVector2d()
    {
        return PixelEngine.random.nextVector2d();
    }
    
    public static Vector2d nextVector2d(float bound)
    {
        return PixelEngine.random.nextVector2d(bound);
    }
    
    public static Vector2d nextVector2d(float origin, float bound)
    {
        return PixelEngine.random.nextVector2d(origin, bound);
    }
    
    public static Vector3d nextVector3d()
    {
        return PixelEngine.random.nextVector3d();
    }
    
    public static Vector3d nextVector3d(float bound)
    {
        return PixelEngine.random.nextVector3d(bound);
    }
    
    public static Vector3d nextVector3d(float origin, float bound)
    {
        return PixelEngine.random.nextVector3d(origin, bound);
    }
    
    public static Vector4d nextVector4d()
    {
        return PixelEngine.random.nextVector4d();
    }
    
    public static Vector4d nextVector4d(float bound)
    {
        return PixelEngine.random.nextVector4d(bound);
    }
    
    public static Vector4d nextVector4d(float origin, float bound)
    {
        return PixelEngine.random.nextVector4d(origin, bound);
    }
    
    public static Vector2f nextUnit2f()
    {
        return PixelEngine.random.nextUnit2f();
    }
    
    public static Vector3f nextUnit3f()
    {
        return PixelEngine.random.nextUnit3f();
    }
    
    public static Vector4f nextUnit4f()
    {
        return PixelEngine.random.nextUnit4f();
    }
    
    public static Vector2d nextUnit2d()
    {
        return PixelEngine.random.nextUnit2d();
    }
    
    public static Vector3d nextUnit3d()
    {
        return PixelEngine.random.nextUnit3d();
    }
    
    public static Vector4d nextUnit4d()
    {
        return PixelEngine.random.nextUnit4d();
    }
    
    // ------------
    // - Renderer -
    // ------------
    
    public static Sprite drawTarget()
    {
        return PixelEngine.renderer.drawTarget();
    }
    
    public static void drawTarget(Sprite target)
    {
        PixelEngine.renderer.drawTarget(target);
    }
    
    public static DrawMode drawMode()
    {
        return PixelEngine.renderer.drawMode();
    }
    
    public static void drawMode(DrawMode mode)
    {
        PixelEngine.renderer.drawMode(mode);
    }
    
    public static void drawMode(IBlendPos pixelFunc)
    {
        PixelEngine.renderer.drawMode(pixelFunc);
    }
    
    public static void stroke(Number r, Number g, Number b, Number a)
    {
        PixelEngine.renderer.stroke(r, g, b, a);
    }
    
    public static void stroke(Number r, Number g, Number b)
    {
        PixelEngine.renderer.stroke(r, g, b);
    }
    
    public static void stroke(Number grey, Number a)
    {
        PixelEngine.renderer.stroke(grey, a);
    }
    
    public static void stroke(Number grey)
    {
        PixelEngine.renderer.stroke(grey);
    }
    
    public static void stroke(Colorc color)
    {
        PixelEngine.renderer.stroke(color);
    }
    
    public static void noStroke()
    {
        PixelEngine.renderer.noStroke();
    }
    
    public static void fill(Number r, Number g, Number b, Number a)
    {
        PixelEngine.renderer.fill(r, g, b, a);
    }
    
    public static void fill(Number r, Number g, Number b)
    {
        PixelEngine.renderer.fill(r, g, b);
    }
    
    public static void fill(Number grey, Number a)
    {
        PixelEngine.renderer.fill(grey, a);
    }
    
    public static void fill(Number grey)
    {
        PixelEngine.renderer.fill(grey);
    }
    
    public static void fill(Colorc color)
    {
        PixelEngine.renderer.fill(color);
    }
    
    public static void noFill()
    {
        PixelEngine.renderer.noFill();
    }
    
    public static void strokeWeight(int strokeWeight)
    {
        PixelEngine.renderer.strokeWeight(strokeWeight);
    }
    
    public static void clear(Number r, Number g, Number b, Number a)
    {
        PixelEngine.renderer.clear(r, g, b, a);
    }
    
    public static void clear(Number r, Number g, Number b)
    {
        PixelEngine.renderer.clear(r, g, b);
    }
    
    public static void clear(Number grey, Number a)
    {
        PixelEngine.renderer.clear(grey, a);
    }
    
    public static void clear(Number grey)
    {
        PixelEngine.renderer.clear(grey);
    }
    
    public static void clear()
    {
        PixelEngine.renderer.clear();
    }
    
    public static void clear(Colorc color)
    {
        PixelEngine.renderer.clear(color);
    }
    
    public static void point(int x, int y)
    {
        PixelEngine.renderer.point(x, y);
    }
    
    public static void line(int x1, int y1, int x2, int y2)
    {
        PixelEngine.renderer.line(x1, y1, x2, y2);
    }
    
    public static void bezier(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        PixelEngine.renderer.bezier(x1, y1, x2, y2, x3, y3);
    }
    
    public static void circle(int x, int y, int radius)
    {
        PixelEngine.renderer.circle(x, y, radius);
    }
    
    public static void ellipse(int x, int y, int w, int h)
    {
        PixelEngine.renderer.ellipse(x, y, w, h);
    }
    
    public static void rect(int x, int y, int w, int h)
    {
        PixelEngine.renderer.rect(x, y, w, h);
    }
    
    public static void triangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        PixelEngine.renderer.triangle(x1, y1, x2, y2, x3, y3);
    }
    
    public static void partialSprite(int x, int y, Sprite sprite, int ox, int oy, int w, int h, double scale)
    {
        PixelEngine.renderer.partialSprite(x, y, sprite, ox, oy, w, h, scale);
    }
    
    public static void partialSprite(int x, int y, Sprite sprite, int ox, int oy, int w, int h)
    {
        PixelEngine.renderer.partialSprite(x, y, sprite, ox, oy, w, h);
    }
    
    public static void sprite(int x, int y, Sprite sprite, double scale)
    {
        PixelEngine.renderer.sprite(x, y, sprite, scale);
    }
    
    public static void sprite(int x, int y, Sprite sprite)
    {
        PixelEngine.renderer.sprite(x, y, sprite);
    }
    
    public static void string(int x, int y, String text, double scale)
    {
        PixelEngine.renderer.text(x, y, text, scale);
    }
    
    public static void string(int x, int y, String text)
    {
        PixelEngine.renderer.text(x, y, text);
    }
    
    private static void loadExtensions()
    {
        PixelEngine.LOGGER.debug("Looking for Extensions");
        
        Reflections reflections = new Reflections("pe");
        for (Class<? extends PEX> ext : reflections.getSubTypesOf(PEX.class))
        {
            try
            {
                String name = ext.getSimpleName();
                PixelEngine.extensions.put(name, ext.getConstructor(Profiler.class).newInstance(PixelEngine.PROFILER));
                PixelEngine.LOGGER.info("Loading: %s", name);
            }
            catch (ReflectiveOperationException ignored)
            {
            
            }
        }
    }
    
    private static void renderLoop()
    {
        PixelEngine.renderer.init();
    
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
            while (PixelEngine.running)
            {
                PixelEngine.LOGGER.trace("Frame Started");
    
                t         = System.nanoTime();
                dt        = t - lastFrame;
                lastFrame = t;
    
                PixelEngine.PROFILER.startTick();
                {
                    PixelEngine.PROFILER.startSection("Events");
                    {
                        Events.clear(); // TODO - Have a way to have events persist and be consumable.
                        
                        PixelEngine.PROFILER.startSection("Mouse Events");
                        {
                            Mouse.handleEvents(t, dt);
                        }
                        PixelEngine.PROFILER.endSection();
                        
                        PixelEngine.PROFILER.startSection("Key Events");
                        {
                            Keyboard.handleEvents(t, dt);
                        }
                        PixelEngine.PROFILER.endSection();
                        
                        PixelEngine.PROFILER.startSection("Window Events");
                        {
                            Window.handleEvents(t, dt);
                        }
                        PixelEngine.PROFILER.endSection();
                    }
                    PixelEngine.PROFILER.endSection();
                    
                    PixelEngine.PROFILER.startSection("PEX Pre");
                    {
                        for (String name : PixelEngine.extensions.keySet())
                        {
                            if (PixelEngine.extensions.get(name).isEnabled())
                            {
                                PixelEngine.PROFILER.startSection(name);
                                {
                                    PixelEngine.extensions.get(name).beforeDraw(dt / 1_000_000_000D);
                                }
                                PixelEngine.PROFILER.endSection();
                            }
                        }
                    }
                    PixelEngine.PROFILER.endSection();
                    
                    PixelEngine.PROFILER.startSection("User Update");
                    {
                        if (!PixelEngine.logic.draw(dt / 1_000_000_000D))
                        {
                            PixelEngine.LOGGER.trace("onUserUpdate return false so engine will stop");
                            PixelEngine.running = false;
                        }
                    }
                    PixelEngine.PROFILER.endSection();
                    
                    PixelEngine.PROFILER.startSection("PEX Post");
                    {
                        for (String name : PixelEngine.extensions.keySet())
                        {
                            if (PixelEngine.extensions.get(name).isEnabled())
                            {
                                PixelEngine.PROFILER.startSection(name);
                                {
                                    PixelEngine.extensions.get(name).afterDraw(dt / 1_000_000_000D);
                                }
                                PixelEngine.PROFILER.endSection();
                            }
                        }
                    }
                    PixelEngine.PROFILER.endSection();
                    
                    boolean update;
                    PixelEngine.PROFILER.startSection("Window Update");
                    {
                        update = Window.update();
                    }
                    PixelEngine.PROFILER.endSection();
                    
                    PixelEngine.PROFILER.startSection("Render");
                    {
                        PixelEngine.renderer.render(update, PixelEngine.PROFILER);
                    }
                    PixelEngine.PROFILER.endSection();
                    
                    PixelEngine.PROFILER.startSection("Stats");
                    {
                        PixelEngine.PROFILER.startSection("Update");
                        {
                            frameTime = System.nanoTime() - t;
                            minTime   = Math.min(minTime, frameTime);
                            maxTime   = Math.max(maxTime, frameTime);
                            totalTime += frameTime;
                            totalFrames++;
                        }
                        PixelEngine.PROFILER.endSection();
                        
                        dt = t - lastSecond;
                        if (dt > 1_000_000_000L)
                        {
                            PixelEngine.PROFILER.startSection("Update Title");
                            {
                                lastSecond = t;
                                
                                double s = 1000D;
                                
                                totalTime /= totalFrames;
                                
                                Window.title(String.format(PixelEngine.TITLE, PixelEngine.logic.name, totalFrames, totalTime / s, minTime / s, maxTime / s));
                                
                                totalTime   = 0;
                                minTime     = Long.MAX_VALUE;
                                maxTime     = Long.MIN_VALUE;
                                totalFrames = 0;
                            }
                            PixelEngine.PROFILER.endSection();
                        }
                    }
                    PixelEngine.PROFILER.endSection();
                }
                PixelEngine.PROFILER.endTick();
                
                if (PixelEngine.PROFILER.enabled && PixelEngine.printFrame != null)
                {
                    String parent = PixelEngine.printFrame.equals("") ? null : PixelEngine.printFrame;
                    println(PixelEngine.PROFILER.getFormattedData(parent));
                    PixelEngine.printFrame = null;
                }
            }
        }
        finally
        {
            PixelEngine.running = false;
        }
    }
}
