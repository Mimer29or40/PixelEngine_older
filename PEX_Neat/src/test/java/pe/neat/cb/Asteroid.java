package pe.neat.cb;

import org.joml.Vector2d;
import pe.color.Color;

import java.util.ArrayList;

import static pe.PixelEngine.*;

public class Asteroid
{
    public Vector2d            pos;
    public Vector2d            vel;
    public int                 size   = 3;//3 = large 2 = medium and 1 = small
    public double              radius;
    public ArrayList<Asteroid> chunks = new ArrayList<>();//each asteroid contains 2 smaller asteroids which are released when shot
    public boolean             split  = false;//whether the asteroid has been hit and split into to 2
    public int                 sizeHit;
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //constructor
    public Asteroid(double posX, double posY, double velX, double velY, int sizeNo)
    {
        pos  = new Vector2d(posX, posY);
        size = sizeNo;
        vel  = new Vector2d(velX, velY);
        
        switch (sizeNo)
        {//set the velocity and radius depending on size
            case 1:
                radius = 15;
                vel.normalize();
                vel.mul(1.25);
                break;
            case 2:
                radius = 30;
                vel.normalize();
                vel.mul(1);
                break;
            case 3:
                radius = 60;
                vel.normalize();
                vel.mul(0.75);
                break;
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //draw the asteroid
    public void show()
    {
        if (split)
        {//if split show the 2 chunks
            for (Asteroid a : chunks)
            {
                a.show();
            }
        }
        else
        {// if still whole
            // noFill();
            // stroke(255);
            // polygon(pos.x, pos.y, radius, 12);//draw the dodecahedrons
            stroke(Color.WHITE);
            noFill();
            circle((int) pos.x, (int) pos.y, (int) radius);
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------------
    //draws a polygon
    //not gonna lie, I copied this from https://processing.org/examples/regularpolygon.html
    // public void polygon(double x, double y, double radius, int npoints)
    // {
    //     double angle = Math.PI * 2.0 / npoints;//set the angle between vertexes
    //     beginShape();
    //     for (double a = 0; a < Math.PI * 2.0; a += angle)
    //     {//draw each vertex of the polygon
    //         double sx = x + Math.cos(a) * radius;//math
    //         double sy = y + Math.sin(a) * radius;//math
    //         vertex(sx, sy);
    //     }
    //     endShape(CLOSE);
    // }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //adds the velocity to the position
    public void move()
    {
        if (split)
        {//if split move the chunks
            for (Asteroid a : chunks)
            {
                a.move();
            }
        }
        else
        {//if not split
            pos.add(vel);//move it
            if (PEX_CBNeat.isOut(pos))
            {//if out of the playing area wrap (loop) it to the other side
                loopy();
            }
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
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //checks if a bullet hit the asteroid
    public boolean checkIfHit(Vector2d bulletPos)
    {
        if (split)
        {//if split check if the bullet hit one of the chunks
            for (Asteroid a : chunks)
            {
                if (a.checkIfHit(bulletPos))
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            if (pos.distance(bulletPos) < radius)
            {//if it did hit
                isHit();//boom
                return true;
            }
            if (pos.x < -50 + radius || pos.x > screenWidth() + 50 - radius || pos.y < -50 + radius || pos.y > screenHeight() + 50 - radius)
            {//if ateroid is overlapping edge
                if (pos.x < -50 + radius || pos.x > screenWidth() + 50 - radius || pos.y < -50 + radius || pos.y > screenHeight() + 50 - radius)
                {//if bullet is near the edge
                    Vector2d overlapPos = new Vector2d(pos.x, pos.y);
                    if (pos.x < -50 + radius)
                    {
                        overlapPos.x += screenWidth() + 100;
                    }
                    if (pos.x > screenWidth() + 50 - radius)
                    {
                        overlapPos.x -= screenWidth() + 100;
                    }
                    
                    if (pos.y < -50 + radius)
                    {
                        overlapPos.y += screenHeight() + 100;
                    }
                    
                    if (pos.y > screenHeight() + 50 - radius)
                    {
                        
                        overlapPos.y -= screenHeight() + 100;
                    }
                    if (overlapPos.distance(bulletPos) < radius)
                    {
                        isHit();//boom
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //probs could have made these 3 functions into 1 but whatever
    //this one checks if the player hit the asteroid
    public boolean checkIfHitPlayer(Vector2d playerPos)
    {
        if (split)
        {//if split check if the player hit one of the chunks
            for (Asteroid a : chunks)
            {
                if (a.checkIfHitPlayer(playerPos))
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            if (pos.distance(playerPos) < radius + 15)
            {//if hit player
                isHit();//boom
                
                return true;
            }
            
            if (pos.x < -50 + radius || pos.x > screenWidth() + 50 - radius || pos.y < -50 + radius || pos.y > screenHeight() + 50 - radius)
            {//if ateroid is overlapping edge
                if (pos.x < -50 + radius || pos.x > screenWidth() + 50 - radius || pos.y < -50 + radius || pos.y > screenHeight() + 50 - radius)
                {//if bullet is near the edge
                    Vector2d overlapPos = new Vector2d(pos.x, pos.y);
                    if (pos.x < -50 + radius)
                    {
                        overlapPos.x += screenWidth() + 100;
                    }
                    if (pos.x > screenWidth() + 50 - radius)
                    {
                        overlapPos.x -= screenWidth() + 100;
                    }
                    
                    if (pos.y < -50 + radius)
                    {
                        overlapPos.y += screenHeight() + 100;
                    }
                    
                    if (pos.y > screenHeight() + 50 - radius)
                    {
                        
                        overlapPos.y -= screenHeight() + 100;
                    }
                    if (overlapPos.distance(playerPos) < radius)
                    {
                        isHit();//boom
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //same as checkIfHit but it doesnt destroy the asteroid used by the look function
    public boolean lookForHit(Vector2d bulletPos)
    {
        if (split)
        {
            for (Asteroid a : chunks)
            {
                if (a.lookForHit(bulletPos))
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            if (pos.distance(bulletPos) < radius)
            {
                sizeHit = size;
                
                return true;
            }
            if (pos.x < -50 + radius || pos.x > screenWidth() + 50 - radius || pos.y < -50 + radius || pos.y > screenHeight() + 50 - radius)
            {//if ateroid is overlapping edge
                if (pos.x < -50 + radius || pos.x > screenWidth() + 50 - radius || pos.y < -50 + radius || pos.y > screenHeight() + 50 - radius)
                {//if bullet is near the edge
                    Vector2d overlapPos = new Vector2d(pos.x, pos.y);
                    if (pos.x < -50 + radius)
                    {
                        overlapPos.x += screenWidth() + 100;
                    }
                    if (pos.x > screenWidth() + 50 - radius)
                    {
                        overlapPos.x -= screenWidth() + 100;
                    }
                    
                    if (pos.y < -50 + radius)
                    {
                        overlapPos.y += screenHeight() + 100;
                    }
                    
                    if (pos.y > screenHeight() + 50 - radius)
                    {
                        
                        overlapPos.y -= screenHeight() + 100;
                    }
                    if (overlapPos.distance(bulletPos) < radius)
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------
    
    //destroys/splits asteroid
    public void isHit()
    {
        split = true;
        if (size == 1)
        {//can't split the smallest asteroids
            return;
        }
        else
        {
            //add 2 smaller asteroids to the chunks array with slightly different velocities
            Vector2d velocity = new Vector2d(vel.x, vel.y);
            velocity.add(nextGaussian(), nextGaussian());
            chunks.add(new Asteroid(pos.x, pos.y, velocity.x, velocity.y, size - 1));
            velocity.add(nextGaussian(), nextGaussian());
            chunks.add(new Asteroid(pos.x, pos.y, velocity.x, velocity.y, size - 1));
        }
    }
    
    public Asteroid getAsteroid(Vector2d bulletPos)
    {
        
        if (split)
        {
            for (Asteroid a : chunks)
            {
                if (a.getAsteroid(bulletPos) != null)
                {
                    return a.getAsteroid(bulletPos);
                }
            }
            return null;
        }
        else
        {
            
            if (pos.distance(bulletPos) < radius)
            {
                return this;
            }
            if (pos.x < -50 + radius || pos.x > screenWidth() + 50 - radius || pos.y < -50 + radius || pos.y > screenHeight() + 50 - radius)
            {//if ateroid is overlapping edge
                if (pos.x < -50 + radius || pos.x > screenWidth() + 50 - radius || pos.y < -50 + radius || pos.y > screenHeight() + 50 - radius)
                {//if bullet is near the edge
                    Vector2d overlapPos = new Vector2d(pos.x, pos.y);
                    if (pos.x < -50 + radius)
                    {
                        overlapPos.x += screenWidth() + 100;
                    }
                    if (pos.x > screenWidth() + 50 - radius)
                    {
                        overlapPos.x -= screenWidth() + 100;
                    }
                    
                    if (pos.y < -50 + radius)
                    {
                        overlapPos.y += screenHeight() + 100;
                    }
                    
                    if (pos.y > screenHeight() + 50 - radius)
                    {
                        
                        overlapPos.y -= screenHeight() + 100;
                    }
                    if (overlapPos.distance(bulletPos) < radius)
                    {
                        return this;
                    }
                }
            }
            return null;
        }
    }
}
