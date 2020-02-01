package pe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@SuppressWarnings("unused")
public class Logger
{
    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
    
    private static Level  level = Level.INFO;
    private final  String className;
    
    private Logger(String className)
    {
        this.className = className;
    }
    
    public static Level getLevel()
    {
        return Logger.level;
    }
    
    public static void setLevel(Level level)
    {
        Logger.level = level;
    }
    
    public static Logger getLogger()
    {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        
        return new Logger(elements.length > 2 ? elements[2].getClassName() : "");
    }
    
    public void log(Level level, String message)
    {
        if (level.compareTo(Logger.level) <= 0)
        {
            String date   = Logger.dateFormat.format(Date.from(Instant.now()));
            String thread = Thread.currentThread().getName();
            String line;
            if (this.className.equals(""))
            {
                line = String.format("[%s] [%s/%s]: %s", date, thread, level, message);
            }
            else
            {
                line = String.format("[%s] [%s/%s] [%s]: %s", date, thread, level, this.className, message);
            }
            System.out.println(line);
        }
    }
    
    public void log(Level level, String format, Object... arguments)
    {
        log(level, String.format(format, arguments));
    }
    
    public void fatal(String message)
    {
        log(Level.FATAL, message);
    }
    
    public void fatal(String format, Object... arguments)
    {
        log(Level.FATAL, format, arguments);
    }
    
    public void error(String message)
    {
        log(Level.ERROR, message);
    }
    
    public void error(String format, Object... arguments)
    {
        log(Level.ERROR, format, arguments);
    }
    
    public void warn(String message)
    {
        log(Level.WARN, message);
    }
    
    public void warn(String format, Object... arguments)
    {
        log(Level.WARN, format, arguments);
    }
    
    public void info(String message)
    {
        log(Level.INFO, message);
    }
    
    public void info(String format, Object... arguments)
    {
        log(Level.INFO, format, arguments);
    }
    
    public void debug(String message)
    {
        log(Level.DEBUG, message);
    }
    
    public void debug(String format, Object... arguments)
    {
        log(Level.DEBUG, format, arguments);
    }
    
    public void trace(String message)
    {
        log(Level.TRACE, message);
    }
    
    public void trace(String format, Object... arguments)
    {
        log(Level.TRACE, format, arguments);
    }
    
    public void all(String message)
    {
        log(Level.ALL, message);
    }
    
    public void all(String format, Object... arguments)
    {
        log(Level.ALL, format, arguments);
    }
    
    public enum Level
    {
        OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL
    }
}
