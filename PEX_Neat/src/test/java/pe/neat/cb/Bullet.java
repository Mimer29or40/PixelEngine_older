package pe.neat.cb;

import org.joml.Vector2d;
import pe.color.Color;

import static pe.PixelEngine.*;

public class Bullet
{
    public Vector2d pos;
    public Vector2d vel;
    public double   speed    = 10;
    public boolean  off      = false;
    public int      lifespan = 60;
    //------------------------------------------------------------------------------------------------------------------------------------------
    
    public Bullet(double x, double y, double r, double playerSpeed)
    {
        
        pos = new Vector2d(x, y);
        vel = new Vector2d(Math.cos(r), Math.sin(r));
        vel.mul(speed + playerSpeed);//bullet speed = 10 + the speed of the player
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //move the bullet 
    public void move()
    {
        lifespan--;
        if (lifespan < 0)
        {//if lifespan is up then destroy the bullet
            off = true;
        }
        else
        {
            pos.add(vel);
            if (PEX_CBNeat.isOut(pos))
            {//wrap bullet
                loopy();
            }
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //show a dot representing the bullet
    public void show()
    {
        if (!off)
        {
            // fill(255);
            // ellipse(pos.x, pos.y, 3, 3);
            drawCircle((int) pos.x, (int) pos.y, 3, Color.WHITE);
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //if out moves it to the other side of the screen
    
    public void loopy()
    {
        if (pos.y < -50)
        {
            pos.y = screenHeight() + 50;
        }
        else if (pos.y > screenHeight() + 50)
        {
            pos.y = -50;
        }
        if (pos.x < -50)
        {
            pos.x = screenWidth() + 50;
        }
        else if (pos.x > screenWidth() + 50)
        {
            pos.x = -50;
        }
    }
}
