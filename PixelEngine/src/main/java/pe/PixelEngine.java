package pe;

import org.reflections.Reflections;
import pe.color.Blend;
import pe.color.Color;
import pe.render.Renderer;
import pe.render.SoftwareRenderer;
import pe.util.PairI;

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
    private static final Random   RANDOM   = new Random();
    
    private static final Color COLOR = new Color(Color.WHITE);
    
    private static final String         TITLE        = "Pixel Game Engine - %s - FPS(%s) SPF(Avg: %s us, Min: %s us, Max: %s us)";
    private static final Pattern        BOTTOM_CHARS = Pattern.compile(".*[gjpqy,]+.*");
    private static final HashSet<PairI> DRAW_CORDS   = new HashSet<>();
    
    private static PixelEngine logic;
    
    private static final Map<String, PEX> extensions = new HashMap<>();
    
    private static String  printFrame;
    private static boolean running;
    private static long    startTime;
    
    private static int screenW, screenH;
    private static int pixelW, pixelH;
    
    private static       Renderer renderer;
    // TODO - Push/Pop
    // private static       DrawMode  drawMode = DrawMode.NORMAL;
    private static final Blend    blend = new Blend();
    // private static       IBlendPos blendFunc;
    
    // private static Sprite font, prev, window, target;
    
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
        
        PixelEngine.screenW = screenW;
        PixelEngine.screenH = screenH;
        PixelEngine.LOGGER.trace("Screen Size (%s, %s)", screenW, screenH);
        
        PixelEngine.pixelW = pixelW;
        PixelEngine.pixelH = pixelH;
        PixelEngine.LOGGER.trace("Color Dimensions (%s, %s)", pixelW, pixelH);
    
        Window.fullscreen(fullscreen);
        PixelEngine.LOGGER.trace("Fullscreen: %s)", fullscreen);
    
        Window.vsync(vsync);
        PixelEngine.LOGGER.trace("VSync: %s)", vsync);
    
        if (PixelEngine.screenW == 0 || PixelEngine.screenH == 0) throw new RuntimeException("Screen dimension must be > 0");
        if (PixelEngine.pixelW == 0 || PixelEngine.pixelH == 0) throw new RuntimeException("Color dimension must be > 0");
        PixelEngine.LOGGER.trace("Screen Size and Color Dimensions pass initial test");
    
        PixelEngine.renderer = new SoftwareRenderer();
        // PixelEngine.renderer.init();
    
        // createFontSheet();
    
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
    
    public static int screenWidth()
    {
        return PixelEngine.screenW;
    }
    
    public static int screenHeight()
    {
        return PixelEngine.screenH;
    }
    
    // TODO - set pixel dims on window resize
    public static int pixelWidth()
    {
        return PixelEngine.pixelW;
    }
    
    public static int pixelHeight()
    {
        return PixelEngine.pixelH;
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
    
    public static void seed(long seed)
    {
        PixelEngine.RANDOM.setSeed(seed);
    }
    
    public static Random random()
    {
        return PixelEngine.RANDOM;
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
    
    private static void loadExtensions()
    {
        PixelEngine.LOGGER.info("Looking for Extensions");
        
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
        // Window.setupContext();
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
                            PixelEngine.PROFILER.startSection(name);
                            {
                                PixelEngine.extensions.get(name).beforeDraw(dt / 1_000_000_000D);
                            }
                            PixelEngine.PROFILER.endSection();
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
                            PixelEngine.PROFILER.startSection(name);
                            {
                                PixelEngine.extensions.get(name).afterDraw(dt / 1_000_000_000D);
                            }
                            PixelEngine.PROFILER.endSection();
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
