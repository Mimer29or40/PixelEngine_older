package pe;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import pe.event.*;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static pe.PixelEngine.*;

@SuppressWarnings("unused")
public class Window
{
    private static final Logger LOGGER = Logger.getLogger();
    
    private static long glfwWindow;
    
    private static int viewX, viewY;
    private static int viewW, viewH;
    
    private static boolean update = true;
    
    private static int monitorW, monitorH;
    
    private static int windowX, windowY, newWindowX, newWindowY, fullX, fullY;
    private static int windowW, windowH, newWindowW, newWindowH, fullW, fullH;
    
    private static boolean focused, newFocused;
    
    private static boolean fullscreen, newFullscreen;
    private static boolean vsync, newVsync;
    
    public static int monitorWidth()
    {
        return Window.monitorW;
    }
    
    public static int monitorHeight()
    {
        return Window.monitorH;
    }
    
    public static int windowX()
    {
        return Window.windowX;
    }
    
    public static int windowY()
    {
        return Window.windowY;
    }
    
    public static int windowWidth()
    {
        return Window.windowW;
    }
    
    public static int windowHeight()
    {
        return Window.windowH;
    }
    
    public static boolean focused()
    {
        return Window.focused;
    }
    
    public static boolean fullscreen()
    {
        return Window.fullscreen;
    }
    
    public static void fullscreen(boolean fullscreen)
    {
        Window.newFullscreen = fullscreen;
    }
    
    public static boolean vsync()
    {
        return Window.vsync;
    }
    
    public static void vsync(boolean vsync)
    {
        Window.newVsync = vsync;
    }
    
    public static void title(String title)
    {
        glfwSetWindowTitle(Window.glfwWindow, title);
    }
    
    protected static void setup()
    {
        Window.LOGGER.debug("Window Creation Started");
        
        Window.windowW = screenWidth() * pixelWidth();
        Window.windowH = screenHeight() * pixelWidth();
        
        Window.LOGGER.trace("Window Size: (%s, %s)", Window.windowW, Window.windowH);
        
        Window.LOGGER.trace("GLFW: Init");
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        
        Window.LOGGER.trace("GLFW: Hints");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        
        Window.LOGGER.trace("GLFW: Checking Window Size");
        GLFWVidMode videoMode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));
        Window.monitorW = videoMode.width();
        Window.monitorH = videoMode.height();
        
        if (Window.fullscreen)
        {
            Window.windowW = videoMode.width();
            Window.windowH = videoMode.height();
        }
        
        if (Window.windowW > Window.monitorW)
        {
            throw new RuntimeException(String.format("Window width (%s) is greater than monitor width", Window.windowW));
        }
        if (Window.windowH > Window.monitorH)
        {
            throw new RuntimeException(String.format("Window height (%s) is greater than monitor height", Window.windowH));
        }
        
        Window.LOGGER.trace("GLFW: Creating Window");
        Window.glfwWindow = glfwCreateWindow(Window.windowW, Window.windowH, "", NULL, NULL);
        
        if (Window.glfwWindow == NULL) throw new RuntimeException("Failed to create the GLFW window");
        
        Window.windowX = (Window.monitorW - Window.windowW) >> 1;
        Window.windowY = (Window.monitorH - Window.windowH) >> 1;
        glfwSetWindowPos(Window.glfwWindow, Window.windowX, Window.windowY);
        
        Window.LOGGER.trace("GLFW: Event Handling");
        
        glfwSetWindowCloseCallback(Window.glfwWindow, window -> {
            if (window != Window.glfwWindow) return;
            PixelEngine.stop();
        });
        
        glfwSetWindowPosCallback(Window.glfwWindow, (window, x, y) -> {
            if (window != Window.glfwWindow) return;
            Window.newWindowX = x;
            Window.newWindowY = y;
        });
        
        glfwSetWindowSizeCallback(Window.glfwWindow, (window, w, h) -> {
            if (window != Window.glfwWindow) return;
            Window.newWindowW = w;
            Window.newWindowH = h;
        });
        
        glfwSetWindowFocusCallback(Window.glfwWindow, (window, focused) -> {
            if (window != Window.glfwWindow) return;
            Window.newFocused = focused;
        });
        
        glfwSetCursorEnterCallback(Window.glfwWindow, (window, entered) -> {
            if (window != Window.glfwWindow) return;
            Mouse.enteredCallback(entered);
        });
        
        glfwSetCursorPosCallback(Window.glfwWindow, (window, x, y) -> {
            if (window != Window.glfwWindow) return;
            x = (x - Window.viewX) * (double) screenWidth() / (double) Window.viewW;
            y = (y - Window.viewY) * (double) screenHeight() / (double) Window.viewH;
            Mouse.positionCallback(x, y);
        });
        
        glfwSetScrollCallback(Window.glfwWindow, (window, x, y) -> {
            if (window != Window.glfwWindow) return;
            Mouse.scrollCallback(x, y);
        });
        
        glfwSetMouseButtonCallback(Window.glfwWindow, (window, mouse, action, mods) -> {
            if (window != Window.glfwWindow) return;
            Mouse.stateCallback(mouse, action);
        });
        
        glfwSetKeyCallback(Window.glfwWindow, (window, key, scancode, action, mods) -> {
            if (window != Window.glfwWindow) return;
            Keyboard.stateCallback(key, action);
        });
        
        glfwSetCharCallback(Window.glfwWindow, (window, codePoint) -> {
            if (window != Window.glfwWindow) return;
            Keyboard.charCallback(codePoint);
        });
        
        glfwShowWindow(Window.glfwWindow);
        
        Window.LOGGER.trace("GLFW: Init Completed");
        
        Window.LOGGER.debug("Window Creation Finished");
    }
    
    protected static void setupContext()
    {
        Window.LOGGER.debug("Creating OpenGL Context");
        
        glfwMakeContextCurrent(Window.glfwWindow);
        
        GL.createCapabilities();
        
        Window.LOGGER.trace("OpenGL: Shader setup");
        {
            int program, shader, result;
            program = glCreateProgram();
            
            Window.LOGGER.trace("OpenGL: Vertex Shader");
            
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
            
            Window.LOGGER.trace("OpenGL: Fragment Shader");
            
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
            
            Window.LOGGER.trace("OpenGL: Linking Program");
            
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
        Window.LOGGER.trace("OpenGL: Shader Validated");
        
        Window.LOGGER.trace("OpenGL: Setting Viewport");
        
        glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        glViewport(Window.viewX, Window.viewY, Window.viewW, Window.viewH);
        
        Window.LOGGER.trace("OpenGL: Building Vertex Array");
        
        glBindVertexArray(glGenVertexArrays());
        glBindBuffer(GL_ARRAY_BUFFER, glGenBuffers());
        glBufferData(GL_ARRAY_BUFFER, new float[] {-1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F, 1.0F}, GL_STATIC_DRAW);
        
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        
        Window.LOGGER.trace("OpenGL: Building Texture");
        
        glEnable(GL_TEXTURE_2D);
        
        glBindTexture(GL_TEXTURE_2D, glGenTextures());
        // glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, screenWidth(), screenHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, Window.window.getData());
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, screenWidth(), screenHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        
        glfwSwapBuffers(Window.glfwWindow);
        glFinish();
        
        Window.LOGGER.debug("OpenGL Context Initialized");
    }
    
    protected static void pollEvents()
    {
        glfwPollEvents();
    }
    
    protected static void handleEvents(long time, long delta)
    {
        Window.LOGGER.trace("Handling Window Events");
        
        if (Window.focused != Window.newFocused)
        {
            Events.post(EventWindowFocused.class, Window.newFocused);
            Window.focused = Window.newFocused;
        }
        
        if (Window.fullscreen != Window.newFullscreen)
        {
            Events.post(EventWindowFullscreen.class, Window.newFullscreen);
            Window.fullscreen = Window.newFullscreen;
            Window.update = true;
            if (Window.fullscreen)
            {
                Window.fullX = Window.windowX;
                Window.fullY = Window.windowY;
                Window.fullW = Window.windowW;
                Window.fullH = Window.windowH;
            }
            else
            {
                Window.newWindowX = Window.fullX;
                Window.newWindowY = Window.fullY;
                Window.newWindowW = Window.fullW;
                Window.newWindowH = Window.fullH;
            }
        }
        
        if (Window.vsync != Window.newVsync)
        {
            Events.post(EventWindowVSync.class, Window.newVsync);
            Window.vsync = Window.newVsync;
            Window.update = true;
        }
        
        if (Window.windowX != Window.newWindowX || Window.windowY != Window.newWindowY)
        {
            Events.post(EventWindowMoved.class, Window.newWindowX, Window.newWindowY);
            Window.windowX = Window.newWindowX;
            Window.windowY = Window.newWindowY;
            Window.update = true;
        }
        
        if (Window.windowW != Window.newWindowW || Window.windowH != Window.newWindowH)
        {
            Events.post(EventWindowResized.class, Window.newWindowW, Window.newWindowH);
            Window.windowW = Window.newWindowW;
            Window.windowH = Window.newWindowH;
            Window.update = true;
        }
    }
    
    protected static boolean update()
    {
        if (Window.update)
        {
            Window.LOGGER.debug("Updating Window");
            
            glfwSwapInterval(Window.vsync ? 1 : 0);
            
            GLFWVidMode videoMode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));
            
            Window.monitorW = videoMode.width();
            Window.monitorH = videoMode.height();
            
            if (Window.fullscreen)
            {
                glfwSetWindowPos(Window.glfwWindow, 0, 0);
                glfwSetWindowSize(Window.glfwWindow, Window.monitorW, Window.monitorH);
            }
            else
            {
                glfwSetWindowPos(Window.glfwWindow, Window.windowX, Window.windowY);
                glfwSetWindowSize(Window.glfwWindow, Window.windowW, Window.windowH);
            }
            
            double aspect = (double) (screenWidth() * pixelWidth()) / (double) (screenHeight() * pixelHeight());
            
            int actual_w = Window.fullscreen ? Window.monitorW : Window.windowW;
            int actual_h = Window.fullscreen ? Window.monitorH : Window.windowH;
            
            Window.viewW = actual_w;
            Window.viewH = (int) (Window.viewW / aspect);
            
            if (Window.viewH > actual_h)
            {
                Window.viewH = actual_h;
                Window.viewW = (int) (Window.viewH * aspect);
            }
            
            Window.viewX = (actual_w - Window.viewW) >> 1;
            Window.viewY = (actual_h - Window.viewH) >> 1;
            
            glClear(GL_COLOR_BUFFER_BIT);
            glViewport(Window.viewX, Window.viewY, Window.viewW, Window.viewH);
            
            Window.LOGGER.debug("Viewport Pos(%s, %s) Size(%s, %s)", Window.viewX, Window.viewY, Window.viewW, Window.viewH);
        }
        return Window.update;
    }
    
    protected static void drawSprite(Sprite sprite)
    {
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, screenWidth(), screenHeight(), GL_RGBA, GL_UNSIGNED_BYTE, sprite.getData());
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
    
    protected static void swap()
    {
        glfwSwapBuffers(Window.glfwWindow);
        Window.update = false;
    }
    
    protected static void destroy()
    {
        if (Window.glfwWindow > 0)
        {
            glfwFreeCallbacks(Window.glfwWindow);
            glfwDestroyWindow(Window.glfwWindow);
            
            glfwTerminate();
            glfwSetErrorCallback(null);
        }
    }
}
