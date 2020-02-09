package pe.gui;

import pe.*;

import static pe.PixelEngine.*;

public class PEX_GUI extends PEX
{
    // TODO - https://github.com/MyreMylar/pygame_gui/tree/master/pygame_gui
    private static final Logger LOGGER = Logger.getLogger();
    
    public static final Window ROOT  = new Window(null);
    public static final Window MODAL = new Window(null);
    
    private static Window top     = null;
    private static Window focused = null;
    private static Label  tooltip = null;
    
    private static String prevTooltip = "";
    
    private static Mouse.Button clickMouse = null;
    private static int          clickX     = 0;
    private static int          clickY     = 0;
    
    private static Mouse.Button dClickMouse = null;
    private static long         dClickTime  = 0;
    private static int          dClickX     = 0;
    private static int          dClickY     = 0;
    
    private static Window       drag      = null;
    private static Mouse.Button dragMouse = null;
    private static int          dragX     = 0;
    private static int          dragY     = 0;
    
    public PEX_GUI(Profiler profiler)
    {
        super(profiler);
    }
    
    public static boolean isFocused(Window window)
    {
        return window == PEX_GUI.focused;
    }
    
    public static void setFocused(Window window)
    {
        if (PEX_GUI.focused != window)
        {
            if (PEX_GUI.focused != null) PEX_GUI.focused.onUnfocused();
            PEX_GUI.focused = window;
            if (PEX_GUI.focused != null) PEX_GUI.focused.onFocused();
        }
    }
    
    public static boolean isTop(Window window)
    {
        return window == PEX_GUI.top;
    }
    
    @Override
    public void initialize()
    {
        PEX_GUI.tooltip = new Label(null);
        PEX_GUI.tooltip.setVisible(false);
    }
    
    @Override
    public void beforeUserUpdate(double elapsedTime)
    {
        this.profiler.startSection("Window Events");
        
        int mouseX = Mouse.x(), mouseY = Mouse.y();
        
        this.profiler.startSection("Mouse Over");
        
        PEX_GUI.ROOT.getChildren().forEach(window -> window.mouseOver(mouseX, mouseY, PEX_GUI.MODAL.getChildren().isEmpty()));
        for (int i = 0; i < PEX_GUI.MODAL.getChildren().size(); i++) PEX_GUI.MODAL.getChildren().get(i).mouseOver(mouseX, mouseY, i + 1 == PEX_GUI.MODAL.getChildren().size());
        
        this.profiler.endSection();
        
        this.profiler.startSection("Top Window");
        
        Window prevTop = PEX_GUI.top;
        PEX_GUI.top = null;
        if (PEX_GUI.drag != null)
        {
            PEX_GUI.top = PEX_GUI.drag;
        }
        else if (!PEX_GUI.MODAL.getChildren().isEmpty())
        {
            PEX_GUI.top = PEX_GUI.MODAL.getChildren().get(PEX_GUI.MODAL.getChildren().size() - 1).getTop();
        }
        
        if (PEX_GUI.top == null && PEX_GUI.MODAL.getChildren().isEmpty())
        {
            for (Window window : PEX_GUI.ROOT.getChildren())
            {
                PEX_GUI.top = window.getTop();
                if (PEX_GUI.top != null) break;
            }
        }
        
        if (prevTop != PEX_GUI.top)
        {
            if (prevTop != null) prevTop.onMouseExited();
            if (PEX_GUI.top != null) PEX_GUI.top.onMouseEntered();
        }
        
        this.profiler.endSection();
        
        this.profiler.startSection("ToolTip");
        
        if (PEX_GUI.top != null && !PEX_GUI.top.getTooltipText().equals(""))
        {
            String tooltip = PEX_GUI.top.getTooltipText();
            PEX_GUI.tooltip.setVisible(true);
            int posX = mouseX + 12 / pixelWidth();
            if (!PEX_GUI.prevTooltip.equals(tooltip))
            {
                PEX_GUI.tooltip.setText(tooltip);
    
                int maxWidth = screenWidth() - posX - (PEX_GUI.tooltip.getBorderSize() + PEX_GUI.tooltip.getMarginSize()) * 2;
                
                String linesString = join(clipTextWidth(tooltip, PEX_GUI.tooltip.getScale(), maxWidth), "\n");
    
                PEX_GUI.tooltip.setForegroundSize(textWidth(linesString, PEX_GUI.tooltip.getScale()), textHeight(linesString, PEX_GUI.tooltip.getScale()));
            }
            PEX_GUI.tooltip.setPosition(posX, mouseY);
        }
        else
        {
            PEX_GUI.tooltip.setVisible(false);
            PEX_GUI.tooltip.setPosition(screenWidth() + 10, screenHeight() + 10);
        }
        
        this.profiler.endSection();
        
        this.profiler.startSection("Event Handlers");
        
        this.profiler.startSection("Mouse Wheel Events");
    
        if (Mouse.scrollY() != 0)
        {
            Window window = PEX_GUI.top;
            while (window != null)
            {
                if (window.onMouseWheel(Mouse.scrollY())) break;
                window = window.getParent();
            }
        }
        
        this.profiler.endSection();
        
        this.profiler.startSection("Mouse Events");
    
        for (Mouse.Button button : Mouse.inputs())
        {
            if (button.down())
            {
                if (PEX_GUI.top == null) setFocused(null);
    
                PEX_GUI.clickMouse = button;
                PEX_GUI.clickX = mouseX;
                PEX_GUI.clickY = mouseY;
    
                PEX_GUI.dragMouse = button;
                PEX_GUI.dragX = mouseX;
                PEX_GUI.dragY = mouseY;
                Window window = PEX_GUI.top;
                while (window != null)
                {
                    if (PEX_GUI.focused != PEX_GUI.top) setFocused(window);
                    if (window.onMousePressed(button, mouseX - window.getAbsX(), mouseY - window.getAbsY())) break;
                    window = window.getParent();
                }
            }
            if (button.up())
            {
                PEX_GUI.drag = null;
                Window window = PEX_GUI.top;
                while (window != null)
                {
                    // TODO - Right Click Menu
                    boolean result = window.onMouseReleased(button, mouseX - window.getAbsX(), mouseY - window.getAbsY());
            
                    boolean inClickRange       = Math.abs(mouseX - PEX_GUI.clickX) < 2 && Math.abs(mouseY - PEX_GUI.clickY) < 2;
                    boolean inDoubleClickRange = Math.abs(mouseX - PEX_GUI.dClickX) < 2 && Math.abs(mouseY - PEX_GUI.dClickY) < 2;
                    if (inDoubleClickRange && PEX_GUI.dClickMouse == button && getTime() - PEX_GUI.dClickTime < 500_000_000)
                    {
                        PEX_GUI.dClickMouse = null;
                        result = window.onMouseClicked(button, mouseX - window.getAbsX(), mouseY - window.getAbsY(), true) || result;
                    }
                    else if (inClickRange && PEX_GUI.clickMouse == button)
                    {
                        result = window.onMouseClicked(button, mouseX - window.getAbsX(), mouseY - window.getAbsY(), false) || result;
                        PEX_GUI.dClickMouse = button;
                        PEX_GUI.dClickTime = getTime();
                        PEX_GUI.dClickX = mouseX;
                        PEX_GUI.dClickY = mouseY;
                    }
                    if (result) break;
                    window = window.getParent();
                }
                PEX_GUI.clickMouse = null;
            }
            if (button.held())
            {
                Window window = PEX_GUI.top;
                while (window != null)
                {
                    boolean result = window.onMouseHeld(button, mouseX - window.getAbsX(), mouseY - window.getAbsY());
                    if (PEX_GUI.dragMouse == button && (PEX_GUI.dragX != mouseX || PEX_GUI.dragY != mouseY))
                    {
                        if (window.onMouseDragged(button, mouseX - PEX_GUI.dragX, mouseY - PEX_GUI.dragY))
                        {
                            result = true;
                            PEX_GUI.drag = window;
                            PEX_GUI.dragX = mouseX;
                            PEX_GUI.dragY = mouseY;
                        }
                    }
                    if (result) break;
                    window = window.getParent();
                }
            }
            if (button.repeat())
            {
                Window window = PEX_GUI.top;
                while (window != null)
                {
                    if (window.onMouseRepeated(button, mouseX - window.getAbsX(), mouseY - window.getAbsY())) break;
                    window = window.getParent();
                }
            }
        }
        
        this.profiler.endSection();
        
        this.profiler.startSection("Key Events");
        
        if (PEX_GUI.focused != null)
        {
            for (Keyboard.Key key : Keyboard.inputs())
            {
                if (key.down())
                {
                    Window window = PEX_GUI.focused;
                    while (window != null)
                    {
                        if (window.onKeyPressed(key)) break;
                        window = window.getParent();
                    }
                }
                if (key.up())
                {
                    Window window = PEX_GUI.focused;
                    while (window != null)
                    {
                        if (window.onKeyReleased(key)) break;
                        window = window.getParent();
                    }
                }
                if (key.held())
                {
                    Window window = PEX_GUI.focused;
                    while (window != null)
                    {
                        if (window.onKeyHeld(key)) break;
                        window = window.getParent();
                    }
                }
                if (key.up())
                {
                    Window window = PEX_GUI.focused;
                    while (window != null)
                    {
                        if (window.onKeyRepeated(key)) break;
                        window = window.getParent();
                    }
                }
            }
        }
        
        this.profiler.endSection();
        
        this.profiler.endSection();
        
        this.profiler.endSection();
        
        this.profiler.startSection("Window Update");
        
        this.profiler.startSection("Windows");
        
        for (Window window : PEX_GUI.ROOT.getChildren()) window.update(elapsedTime);
        
        this.profiler.endSection();
        
        this.profiler.startSection("Modals");
        
        for (Window window : PEX_GUI.MODAL.getChildren()) window.update(elapsedTime);
        
        this.profiler.endSection();
        
        this.profiler.startSection("Tooltip");
        
        if (PEX_GUI.top != null && PEX_GUI.tooltip.isVisible()) PEX_GUI.tooltip.update(elapsedTime);
        
        this.profiler.endSection();
        
        this.profiler.endSection();
    }
    
    @Override
    public void afterUserUpdate(double elapsedTime)
    {
        this.profiler.startSection("Window Draw");
        
        this.profiler.startSection("Windows");
        
        for (Window window : PEX_GUI.ROOT.getChildren())
        {
            window.draw(elapsedTime);
            
            if (window.isVisible())
            {
                PixelEngine.drawMode(DrawMode.NORMAL);
                PixelEngine.renderTarget(null);
                drawSprite(window.getX(), window.getY(), window.getSprite(), 1);
            }
        }
        
        this.profiler.endSection();
        
        this.profiler.startSection("Modals");
        
        for (Window window : PEX_GUI.MODAL.getChildren())
        {
            window.draw(elapsedTime);
            
            if (window.isVisible())
            {
                PixelEngine.drawMode(DrawMode.NORMAL);
                PixelEngine.renderTarget(null);
                drawSprite(window.getX(), window.getY(), window.getSprite(), 1);
            }
        }
        
        this.profiler.endSection();
        
        this.profiler.startSection("Tooltip");
        
        if (PEX_GUI.top != null && PEX_GUI.tooltip.isVisible())
        {
            PEX_GUI.tooltip.draw(elapsedTime);
            
            if (PEX_GUI.tooltip.isVisible())
            {
                PixelEngine.drawMode(DrawMode.NORMAL);
                PixelEngine.renderTarget(null);
                drawSprite(PEX_GUI.tooltip.getX(), PEX_GUI.tooltip.getY(), PEX_GUI.tooltip.getSprite(), 1);
            }
        }
        
        this.profiler.endSection();
        
        this.profiler.endSection();
    }
    
    @Override
    public void destroy()
    {
        
    }
}
