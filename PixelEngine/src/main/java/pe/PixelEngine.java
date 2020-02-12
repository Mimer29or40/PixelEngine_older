package pe;

import org.reflections.Reflections;
import pe.util.Pair;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "SameParameterValue"})
public class PixelEngine
{
    private static final Logger   LOGGER       = Logger.getLogger();
    private static final Profiler PROFILER     = new Profiler("Engine");
    private static final Color    COLOR        = Color.WHITE.copy();
    private static final String   TITLE        = "Pixel Game Engine - %s - FPS(%s) SPF(Avg: %s us, Min: %s us, Max: %s us)";
    private static final Pattern  BOTTOM_CHARS = Pattern.compile(".*[gjpqy,]+.*");
    private static final Random   RANDOM       = new Random();
    
    private static PixelEngine logic;
    
    private static final Map<String, PEX> extensions = new HashMap<>();
    
    private static String  printFrame;
    private static boolean running;
    private static long    startTime;
    
    private static int screenW, screenH;
    private static int pixelW, pixelH;
    
    private static DrawMode  drawMode = DrawMode.NORMAL;
    private static IBlendPos blendFunc;
    
    private static Sprite font, prev, window, target;
    
    private String name;
    
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
    protected boolean onUserCreate()
    {
        return false;
    }
    
    /**
     * Called every frame.
     *
     * @param elapsedTime time in seconds since that last frame. Must be overridden for engine to run
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
        
        Window.fullscreen(fullscreen);
        PixelEngine.LOGGER.trace("Fullscreen: %s)", fullscreen);
        
        Window.vsync(vsync);
        PixelEngine.LOGGER.trace("VSync: %s)", vsync);
    
        if (PixelEngine.screenW == 0 || PixelEngine.screenH == 0) throw new RuntimeException("Screen dimension must be > 0");
        if (PixelEngine.pixelW == 0 || PixelEngine.pixelH == 0) throw new RuntimeException("Color dimension must be > 0");
        PixelEngine.LOGGER.trace("Screen Size and Color Dimensions pass initial test");
    
        createFontSheet();
    
        loadExtensions();
    
        PixelEngine.window = PixelEngine.target = new Sprite(screenW, screenH);
        PixelEngine.prev   = new Sprite(screenW, screenH);
    
        PixelEngine.running   = true;
        PixelEngine.startTime = System.nanoTime();
    
        try
        {
            PixelEngine.LOGGER.debug("Initializing Extensions");
            PixelEngine.extensions.values().forEach(PEX::initialize);
    
            PixelEngine.LOGGER.debug("User Initialization");
            if (PixelEngine.logic.onUserCreate())
            {
                Window.setup();
                
                new Thread(PixelEngine::renderLoop, "Render Loop").start();
                
                while (PixelEngine.running) Window.pollEvents();
            }
        }
        finally
        {
            PixelEngine.LOGGER.debug("User Initialization");
            PixelEngine.logic.onUserDestroy();
            
            PixelEngine.LOGGER.trace("Extension Destruction");
            PixelEngine.extensions.values().forEach(PEX::destroy);
            
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
    
    public static int pixelWidth()
    {
        return PixelEngine.pixelW;
    }
    
    public static int pixelHeight()
    {
        return PixelEngine.pixelH;
    }
    
    public static DrawMode drawMode()
    {
        return PixelEngine.drawMode;
    }
    
    public static void drawMode(DrawMode mode)
    {
        PixelEngine.drawMode = mode;
    }
    
    public static void colorMode(IBlendPos pixelFunc)
    {
        PixelEngine.drawMode  = DrawMode.CUSTOM;
        PixelEngine.blendFunc = pixelFunc;
    }
    
    public static Sprite renderTarget()
    {
        return PixelEngine.target;
    }
    
    public static void renderTarget(Sprite target)
    {
        PixelEngine.target = target != null ? target : PixelEngine.window;
    }
    
    public static int renderTargetWidth()
    {
        return PixelEngine.target != null ? PixelEngine.target.width : 0;
    }
    
    public static int renderTargetHeight()
    {
        return PixelEngine.target != null ? PixelEngine.target.height : 0;
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
        System.out.println(object);
    }
    
    public static void print(String format, Object... objects)
    {
        System.out.println(String.format(format, objects));
    }
    
    public static void print(Object... objects)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < objects.length; i++)
        {
            builder.append(objects[i]);
            if (i + 1 < objects.length) builder.append(" ");
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
        PixelEngine.PROFILER.enabled = true;
    }
    
    public static void printFrameData(String parent)
    {
        if (PixelEngine.PROFILER.enabled) PixelEngine.printFrame = parent;
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
    
    public static void drawLine(int x1, int y1, int x2, int y2, int width, Color p, int pattern)
    {
        // Bresenham's Algorithm
        
        if (width <= 1)
        {
            int dx, dy, x, y, xi, yi, d, tmp;
            
            dx = x2 - x1;
            dy = y2 - y1;
            
            if (dx == 0)
            {
                if (y2 < y1)
                {
                    tmp = y1;
                    y1  = y2;
                    y2  = tmp;
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
                    x1  = x2;
                    x2  = tmp;
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
                    x1  = x2;
                    x2  = tmp;
                    
                    tmp = y1;
                    y1  = y2;
                    y2  = tmp;
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
                    x1  = x2;
                    x2  = tmp;
                    
                    tmp = y1;
                    y1  = y2;
                    y2  = tmp;
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
        else
        {
            int    dx  = Math.abs(x2 - x1), sx = x1 < x2 ? 1 : -1;
            int    dy  = Math.abs(y2 - y1), sy = y1 < y2 ? 1 : -1;
            int    err = dx - dy, e2, x3, y3;                          /* error value e_xy */
            double ed  = dx + dy == 0 ? 1 : Math.sqrt((double) dx * dx + (double) dy * dy);
            
            for (width = (width + 1) / 2; ; )
            {                                   /* pixel loop */
                draw(x1, y1, p);
                e2 = err;
                x3 = x1;
                if (2 * e2 >= -dx)
                {                                           /* x step */
                    for (e2 += dy, y3 = y1; e2 < ed * width && (y2 != y3 || dx > dy); e2 += dx)
                    { draw(x1, y3 += sy, p); }
                    if (x1 == x2) break;
                    e2 = err;
                    err -= dy;
                    x1 += sx;
                }
                if (2 * e2 <= dy)
                {                                            /* y step */
                    for (e2 = dx - e2; e2 < ed * width && (x2 != x3 || dx < dy); e2 += dy)
                    { draw(x3 += sx, y1, p); }
                    if (y1 == y2) break;
                    err += dx;
                    y1 += sy;
                }
            }
        }
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2, int width, Color p)
    {
        drawLine(x1, y1, x2, y2, width, p, 0xFFFFFFFF);
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2, int width, int pattern)
    {
        drawLine(x1, y1, x2, y2, width, Color.WHITE, pattern);
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2, int width)
    {
        drawLine(x1, y1, x2, y2, width, Color.WHITE, 0xFFFFFFFF);
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2, Color p)
    {
        drawLine(x1, y1, x2, y2, 1, p, 0xFFFFFFFF);
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2)
    {
        drawLine(x1, y1, x2, y2, 1, Color.WHITE, 0xFFFFFFFF);
    }
    
    public static void drawBezier(int x1, int y1, int x2, int y2, int x3, int y3, Color p)
    {
        // TODO - http://members.chello.at/~easyfilter/bresenham.html
        int    sx               = x3 - x2, sy = y3 - y2;
        long   xx               = x1 - x2, yy = y1 - y2, xy;         /* relative values for checks */
        double dx, dy, err, cur = xx * sy - yy * sx;                    /* curvature */
        
        assert (xx * sx <= 0 && yy * sy <= 0);  /* sign of gradient must not change */
        
        if (sx * (long) sx + sy * (long) sy > xx * xx + yy * yy)
        { /* begin with longer part */
            x3  = x1;
            x1  = sx + x2;
            y3  = y1;
            y1  = sy + y2;
            cur = -cur;  /* swap P0 P2 */
        }
        if (cur != 0)
        {                                    /* no straight line */
            xx += sx;
            xx *= sx = x1 < x3 ? 1 : -1;           /* x step direction */
            yy += sy;
            yy *= sy = y1 < y3 ? 1 : -1;           /* y step direction */
            xy       = 2 * xx * yy;
            xx *= xx;
            yy *= yy;          /* differences 2nd degree */
            if (cur * sx * sy < 0)
            {                           /* negated curvature? */
                xx  = -xx;
                yy  = -yy;
                xy  = -xy;
                cur = -cur;
            }
            dx  = 4.0 * sy * cur * (x2 - x1) + xx - xy;             /* differences 1st degree */
            dy  = 4.0 * sx * cur * (y1 - y2) + yy - xy;
            xx += xx;
            yy += yy;
            err = dx + dy + xy;                /* error 1st step */
            do
            {
                draw(x1, y1, p);                  /* plot curve */
                if (x1 == x3 && y1 == y3) return;  /* last pixel -> curve finished */
                boolean yStep = 2 * err < dx;      /* save value for test of y step */
                if (2 * err > dy)
                {
                    x1 += sx;
                    dx -= xy;
                    err += dy += yy;
                } /* x step */
                if (yStep)
                {
                    y1 += sy;
                    dy -= xy;
                    err += dx += xx;
                } /* y step */
            } while (dy < dx);           /* gradient negates -> algorithm fails */
        }
        drawLine(x1, y1, x3, y3, p);
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
    
    public static void drawEllipse(int x, int y, int w, int h, Color p)
    {
        // TODO - http://members.chello.at/~easyfilter/bresenham.html
        int  x0  = x - w / 2, y0 = y - h / 2;
        int  x1  = x + w / 2, y1 = y + h / 2;
        int  b1  = h & 1; /* values of diameter */
        long dx  = 4 * (1 - w) * h * h, dy = 4 * (b1 + 1) * w * w; /* error increment */
        long err = dx + dy + b1 * w * w, e2; /* error of 1.step */
        
        if (x0 > x1)
        {
            x0 = x1;
            x1 += w;
        } /* if called with swapped points */
        if (y0 > y1) y0 = y1; /* .. exchange them */
        y0 += (h + 1) / 2;
        y1 = y0 - b1;   /* starting pixel */
        w *= 8 * w;
        b1 = 8 * h * h;
        
        do
        {
            draw(x1, y0, p); /*   I. Quadrant */
            draw(x0, y0, p); /*  II. Quadrant */
            draw(x0, y1, p); /* III. Quadrant */
            draw(x1, y1, p); /*  IV. Quadrant */
            e2 = 2 * err;
            if (e2 <= dy)
            {
                y0++;
                y1--;
                err += dy += w;
            }  /* y step */
            if (e2 >= dx || 2 * err > dy)
            {
                x0++;
                x1--;
                err += dx += b1;
            } /* x step */
        } while (x0 <= x1);
        
        while (y0 - y1 < h)
        {  /* too early stop of flat ellipses w=1 */
            draw(x0 - 1, y0, p); /* -> finish tip of ellipse */
            draw(x1 + 1, y0++, p);
            draw(x0 - 1, y1, p);
            draw(x1 + 1, y1--, p);
        }
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
        
        x  = Math.max(0, Math.min(x, PixelEngine.screenW));
        y  = Math.max(0, Math.min(y, PixelEngine.screenH));
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
            drawMode(color.a() == 255 ? DrawMode.MASK : DrawMode.ALPHA);
            
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
            drawMode(DrawMode.ALPHA);
            
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
        
        drawMode(DrawMode.NORMAL);
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
                PixelEngine.extensions.put(name, ext.getConstructor(Profiler.class).newInstance(PixelEngine.PROFILER));
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
        Color p = new Color();
        for (int b = 0, px = 0, py = 0; b < 1024; b += 4)
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
    
    private static void renderLoop()
    {
        Window.setupContext();
        
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
                                PixelEngine.extensions.get(name).beforeUserUpdate(dt / 1_000_000_000D);
                            }
                            PixelEngine.PROFILER.endSection();
                        }
                    }
                    PixelEngine.PROFILER.endSection();
                    
                    PixelEngine.PROFILER.startSection("User Update");
                    {
                        if (!PixelEngine.logic.onUserUpdate(dt / 1_000_000_000D))
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
                                PixelEngine.extensions.get(name).afterUserUpdate(dt / 1_000_000_000D);
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
                        for (int i = 0; i < PixelEngine.screenW * PixelEngine.screenH; i++)
                        {
                            ByteBuffer currData = PixelEngine.window.getData();
                            ByteBuffer prevData = PixelEngine.prev.getData();
                            if (update || currData.getInt(4 * i) != prevData.getInt(4 * i))
                            {
                                PixelEngine.LOGGER.trace("Rendering Frame");
                                
                                PixelEngine.PROFILER.startSection("Update/Draw Texture");
                                {
                                    Window.drawSprite(PixelEngine.window);
                                }
                                PixelEngine.PROFILER.endSection();
                                
                                PixelEngine.PROFILER.startSection("Swap");
                                {
                                    Window.swap();
                                }
                                PixelEngine.PROFILER.endSection();
                                
                                PixelEngine.PROFILER.startSection("Swap Sprites");
                                {
                                    PixelEngine.window.copy(PixelEngine.prev);
                                }
                                PixelEngine.PROFILER.endSection();
                                
                                break;
                            }
                        }
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
                    print(PixelEngine.PROFILER.getFormattedData(parent));
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
