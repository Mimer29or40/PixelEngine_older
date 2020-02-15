package pe.gui;

import pe.*;
import pe.draw.DrawMode;
import pe.event.*;

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
    
    private static Window drag = null;
    
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
    
        // Events.subscribe(EventMouseScrolled.class, this::scrollEvent);
    }
    
    @Override
    public void beforeUserUpdate(double elapsedTime)
    {
        this.profiler.startSection("Window Events");
        {
            int mouseX = Mouse.x(), mouseY = Mouse.y();
    
            this.profiler.startSection("Mouse Over");
            {
                PEX_GUI.ROOT.getChildren().forEach(window -> window.mouseOver(mouseX, mouseY, PEX_GUI.MODAL.getChildren().isEmpty()));
                for (int i = 0; i < PEX_GUI.MODAL.getChildren().size(); i++)
                {
                    PEX_GUI.MODAL.getChildren().get(i).mouseOver(mouseX, mouseY, i + 1 == PEX_GUI.MODAL.getChildren().size());
                }
            }
            this.profiler.endSection();
    
            this.profiler.startSection("Top Window");
            {
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
            }
            this.profiler.endSection();
    
            this.profiler.startSection("ToolTip");
            {
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
            }
            this.profiler.endSection();
    
            this.profiler.startSection("Event Handlers");
            {
                this.profiler.startSection("Mouse Events");
                {
                    for (Event e : Events.get(Events.MOUSE_EVENTS))
                    {
                        if (e instanceof EventMouseButtonDown)
                        {
                            EventMouseButtonDown event = (EventMouseButtonDown) e;
        
                            Window window = PEX_GUI.top;
                            while (window != null)
                            {
                                if (PEX_GUI.focused != PEX_GUI.top) setFocused(window);
                                if (window.onMouseButtonDown(event.button(), event.x() - window.getAbsX(), event.y() - window.getAbsY())) break;
                                window = window.getParent();
                            }
                        }
                        else if (e instanceof EventMouseButtonUp)
                        {
                            PEX_GUI.drag = null;
        
                            // TODO - Right Click Menu
                            EventMouseButtonUp event = (EventMouseButtonUp) e;
        
                            Window window = PEX_GUI.top;
                            while (window != null)
                            {
                                if (PEX_GUI.focused != PEX_GUI.top) setFocused(window);
                                if (window.onMouseButtonUp(event.button(), event.x() - window.getAbsX(), event.y() - window.getAbsY())) break;
                                window = window.getParent();
                            }
                        }
                        else if (e instanceof EventMouseButtonClicked)
                        {
                            EventMouseButtonClicked event = (EventMouseButtonClicked) e;
        
                            Window window = PEX_GUI.top;
                            while (window != null)
                            {
                                if (window.onMouseButtonClicked(event.button(), event.x() - window.getAbsX(), event.y() - window.getAbsY(), event.doubleClicked())) break;
                                window = window.getParent();
                            }
                        }
                        else if (e instanceof EventMouseButtonHeld)
                        {
                            EventMouseButtonHeld event = (EventMouseButtonHeld) e;
        
                            Window window = PEX_GUI.top;
                            while (window != null)
                            {
                                if (window.onMouseButtonHeld(event.button(), event.x() - window.getAbsX(), event.y() - window.getAbsY())) break;
                                window = window.getParent();
                            }
                        }
                        else if (e instanceof EventMouseButtonRepeat)
                        {
                            EventMouseButtonRepeat event = (EventMouseButtonRepeat) e;
        
                            Window window = PEX_GUI.top;
                            while (window != null)
                            {
                                if (window.onMouseButtonRepeated(event.button(), event.x() - window.getAbsX(), event.y() - window.getAbsY())) break;
                                window = window.getParent();
                            }
                        }
                        else if (e instanceof EventMouseButtonDragged)
                        {
                            EventMouseButtonDragged event = (EventMouseButtonDragged) e;
        
                            Window window = PEX_GUI.top;
                            while (window != null)
                            {
                                if (window.onMouseButtonDragged(event.button(),
                                                                event.x() - window.getAbsX(),
                                                                event.y() - window.getAbsY(),
                                                                event.dragX(),
                                                                event.dragY(),
                                                                event.relX(),
                                                                event.relY()))
                                {
                                    PEX_GUI.drag = window;
                                    break;
                                }
                                window = window.getParent();
                            }
                        }
                        else if (e instanceof EventMouseScrolled)
                        {
                            EventMouseScrolled event = (EventMouseScrolled) e;
    
                            Window window = PEX_GUI.top;
                            while (window != null)
                            {
                                if (window.onMouseScrolled(event.x(), event.y())) break;
                                window = window.getParent();
                            }
                        }
                    }
                }
                this.profiler.endSection();
    
                this.profiler.startSection("Keyboard Events");
                {
                    if (PEX_GUI.focused != null)
                    {
                        for (Event e : Events.get(Events.KEYBOARD_EVENTS))
                        {
                            if (e instanceof EventKeyboardKeyDown)
                            {
                                EventKeyboardKeyDown event = (EventKeyboardKeyDown) e;
                    
                                Window window = PEX_GUI.focused;
                                while (window != null)
                                {
                                    if (window.onKeyboardKeyDown(event.key())) break;
                                    window = window.getParent();
                                }
                            }
                            else if (e instanceof EventKeyboardKeyUp)
                            {
                                EventKeyboardKeyUp event = (EventKeyboardKeyUp) e;
                    
                                Window window = PEX_GUI.focused;
                                while (window != null)
                                {
                                    if (window.onKeyboardKeyUp(event.key())) break;
                                    window = window.getParent();
                                }
                            }
                            else if (e instanceof EventKeyboardKeyHeld)
                            {
                                EventKeyboardKeyHeld event = (EventKeyboardKeyHeld) e;
                    
                                Window window = PEX_GUI.focused;
                                while (window != null)
                                {
                                    if (window.onKeyboardKeyHeld(event.key())) break;
                                    window = window.getParent();
                                }
                            }
                            else if (e instanceof EventKeyboardKeyRepeat)
                            {
                                EventKeyboardKeyRepeat event = (EventKeyboardKeyRepeat) e;
                    
                                Window window = PEX_GUI.focused;
                                while (window != null)
                                {
                                    if (window.onKeyboardKeyRepeated(event.key())) break;
                                    window = window.getParent();
                                }
                            }
                            else if (e instanceof EventKeyboardKeyPressed)
                            {
                                EventKeyboardKeyPressed event = (EventKeyboardKeyPressed) e;
                    
                                Window window = PEX_GUI.focused;
                                while (window != null)
                                {
                                    if (window.onKeyboardKeyPressed(event.key(), event.doublePressed())) break;
                                    window = window.getParent();
                                }
                            }
                            else if (e instanceof EventKeyboardKeyTyped)
                            {
                                EventKeyboardKeyTyped event = (EventKeyboardKeyTyped) e;
                    
                                Window window = PEX_GUI.focused;
                                while (window != null)
                                {
                                    if (window.onKeyboardKeyTyped(event.charTyped())) break;
                                    window = window.getParent();
                                }
                            }
                        }
                    }
                }
                this.profiler.endSection();
            }
            this.profiler.endSection();
        }
        this.profiler.endSection();
    
        this.profiler.startSection("Window Update");
        {
            this.profiler.startSection("Windows");
            {
                for (Window window : PEX_GUI.ROOT.getChildren()) window.update(elapsedTime);
            }
            this.profiler.endSection();
    
            this.profiler.startSection("Modals");
            {
                for (Window window : PEX_GUI.MODAL.getChildren()) window.update(elapsedTime);
            }
            this.profiler.endSection();
    
            this.profiler.startSection("Tooltip");
            {
                if (PEX_GUI.top != null && PEX_GUI.tooltip.isVisible()) PEX_GUI.tooltip.update(elapsedTime);
            }
            this.profiler.endSection();
        }
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
                PixelEngine.drawTarget(null);
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
                PixelEngine.drawTarget(null);
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
                PixelEngine.drawTarget(null);
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
