package pe.vector;

import org.joml.*;

import java.lang.Math;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Vector3
{
    public static final Vector3ic Xi    = new Vector3i(1, 0, 0);
    public static final Vector3ic Yi    = new Vector3i(0, 1, 0);
    public static final Vector3ic Zi    = new Vector3i(0, 0, 1);
    public static final Vector3ic ZEROi = new Vector3i(0, 0, 0);
    public static final Vector3ic ONEi  = new Vector3i(1, 1, 1);
    
    public static final Vector3fc Xf    = new Vector3f(1, 0, 0);
    public static final Vector3fc Yf    = new Vector3f(0, 1, 0);
    public static final Vector3fc Zf    = new Vector3f(0, 0, 1);
    public static final Vector3fc ZEROf = new Vector3f(0, 0, 0);
    public static final Vector3fc ONEf  = new Vector3f(1, 1, 1);
    
    public static final Vector3dc Xd    = new Vector3d(1, 0, 0);
    public static final Vector3dc Yd    = new Vector3d(0, 1, 0);
    public static final Vector3dc Zd    = new Vector3d(0, 0, 1);
    public static final Vector3dc ZEROd = new Vector3d(0, 0, 0);
    public static final Vector3dc ONEd  = new Vector3d(1, 1, 1);
    
    public static Vector3f fromAngle(float angle, float pitch, Vector3f out)
    {
        double xy = Math.cos(pitch);
        return out.set(xy * Math.cos(angle), xy * Math.sin(angle), Math.sin(pitch));
    }
    
    public static Vector3f fromAngle(float angle, float pitch)
    {
        return fromAngle(angle, pitch, new Vector3f());
    }
    
    public static Vector3d fromAngle(double angle, double pitch, Vector3d out)
    {
        double xy = Math.cos(pitch);
        return out.set(xy * Math.cos(angle), xy * Math.sin(angle), Math.sin(pitch));
    }
    
    public static Vector3d fromAngle(double angle, double pitch)
    {
        return fromAngle(angle, pitch, new Vector3d());
    }
}
