package pe.vector;

import org.joml.*;

import java.lang.Math;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Vector2
{
    public static final Vector2ic Xi    = new Vector2i(1, 0);
    public static final Vector2ic Yi    = new Vector2i(0, 1);
    public static final Vector2ic ZEROi = new Vector2i(0, 0);
    public static final Vector2ic ONEi  = new Vector2i(1, 1);
    
    public static final Vector2fc Xf    = new Vector2f(1, 0);
    public static final Vector2fc Yf    = new Vector2f(0, 1);
    public static final Vector2fc ZEROf = new Vector2f(0, 0);
    public static final Vector2fc ONEf  = new Vector2f(1, 1);
    
    public static final Vector2dc Xd    = new Vector2d(1, 0);
    public static final Vector2dc Yd    = new Vector2d(0, 1);
    public static final Vector2dc ZEROd = new Vector2d(0, 0);
    public static final Vector2dc ONEd  = new Vector2d(1, 1);
    
    public static Vector2f fromAngle(float angle, Vector2f out)
    {
        return out.set(Math.cos(angle), Math.sin(angle));
    }
    
    public static Vector2f fromAngle(float angle)
    {
        return fromAngle(angle, new Vector2f());
    }
    
    public static Vector2d fromAngle(double angle, Vector2d out)
    {
        return out.set(Math.cos(angle), Math.sin(angle));
    }
    
    public static Vector2d fromAngle(double angle)
    {
        return fromAngle(angle, new Vector2d());
    }
}
