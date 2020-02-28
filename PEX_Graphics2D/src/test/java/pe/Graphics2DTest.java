package pe;

import pe.color.Color;
import pe.gfx2d.Transform2D;
import pe.render.DrawMode;

import java.util.ArrayList;
import java.util.List;

public class Graphics2DTest extends PixelEngine
{
    List<String> events    = new ArrayList<>();
    float        totalTime = 0.0F;
    Sprite       spr;
    
    void addEvent(String event)
    {
        this.events.add(event);
        this.events.remove(0);
    }
    
    @Override
    public boolean setup()
    {
        for (int i = 0; i < 16; i++) events.add("");
    
        spr = Sprite.loadImage("zombie.png");
    
        return true;
    }
    
    @Override
    public boolean draw(double elapsedTime)
    {
        renderer().drawMode(DrawMode.NORMAL);
        renderer().clear(Color.BLUE);
    
        renderer().circle(32, 32, 30);
        renderer().circle(96, 32, 30);
    
        float mx = Mouse.x();
        float my = Mouse.y();
    
    
        float px1 = mx - 32, px2 = mx - 96;
        float py1 = my - 32, py2 = my - 32;
        float pr1 = 1.0f / (float) Math.sqrt(px1 * px1 + py1 * py1);
        float pr2 = 1.0f / (float) Math.sqrt(px2 * px2 + py2 * py2);
        px1 = 22.0f * (px1 * pr1) + 32.0f;
        py1 = 22.0f * (py1 * pr1) + 32.0f;
        px2 = 22.0f * (px2 * pr2) + 96.0f;
        py2 = 22.0f * (py2 * pr2) + 32.0f;
        renderer().noStroke();
        renderer().fill(Color.CYAN);
        renderer().circle((int) px1, (int) py1, 8);
        renderer().circle((int) px2, (int) py2, 8);
    
        renderer().line(10, 70, 54, 70);    // Lines
        renderer().line(54, 70, 70, 54);
    
        renderer().rect(10, 80, 54, 30);
    
        // Multiline Text
        renderer().text(10, 130, "Your Mouse Position is:\nX=" + mx + "\nY=" + my);
    
        if (Mouse.LEFT.down()) addEvent("Mouse Button LEFT Down");
        if (Mouse.LEFT.up()) addEvent("Mouse Button LEFT Up");
        if (Mouse.RIGHT.down()) addEvent("Mouse Button RIGHT Down");
        if (Mouse.RIGHT.up()) addEvent("Mouse Button RIGHT Up");
        if (Mouse.MIDDLE.down()) addEvent("Mouse Button MIDDLE Down");
        if (Mouse.MIDDLE.up()) addEvent("Mouse Button MIDDLE Up");
    
    
        // Draw Event Log
        int nLog = 0;
        for (String s : this.events)
        {
            renderer().stroke(nLog * 16, nLog * 16, nLog * 16);
            renderer().text(200, nLog * 8 + 20, s);
            nLog++;
        }
    
        // Test Text scaling and colours
        renderer().stroke(Color.WHITE);
        renderer().text(0, 360, "Text Scale = 1", 1);
        renderer().stroke(Color.BLUE);
        renderer().text(0, 368, "Text Scale = 2", 2);
        renderer().stroke(Color.RED);
        renderer().text(0, 384, "Text Scale = 3", 3);
        renderer().stroke(Color.YELLOW);
        renderer().text(0, 408, "Text Scale = 4", 4);
        renderer().stroke(Color.GREEN);
        renderer().text(0, 440, "Text Scale = 5", 5);
    
        totalTime += elapsedTime;
    
        float angle = totalTime;
    
        // Draw Sprite using extension, first create a transformation stack
        Transform2D t1 = new Transform2D();
    
        // Translate sprite so center of image is at 0,0
        t1.translate(-250, -35);
        // Scale the sprite
        t1.scale(1 * (float) Math.sin(angle) + 1, 1 * (float) Math.sin(angle) + 1);
        // Rotate it
        t1.rotate(angle * 2.0f);
        // Translate to 0,100
        t1.translate(0, 100);
        // Rotate different speed
        t1.rotate(angle / 3);
        // Translate to centre of screen
        t1.translate(320, 240);
    
        renderer().drawMode(DrawMode.BLEND);
    
        // Use extension to draw sprite with transform applied
        // PEX_GFX2D.drawSprite(spr, t1);
    
        renderer().sprite((int) mx, (int) my, spr, 4);
    
        return true;
    }
    
    public static void main(String[] args)
    {
        start(new Graphics2DTest(), 640, 480, 2, 2);
    }
}
