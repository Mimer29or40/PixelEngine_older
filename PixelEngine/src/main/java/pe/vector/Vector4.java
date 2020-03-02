package pe.vector;

import org.joml.*;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Vector4
{
    public static final Vector4ic Xi    = new Vector4i(1, 0, 0, 0);
    public static final Vector4ic Yi    = new Vector4i(0, 1, 0, 0);
    public static final Vector4ic Zi    = new Vector4i(0, 0, 1, 0);
    public static final Vector4ic Wi    = new Vector4i(0, 0, 0, 1);
    public static final Vector4ic ZEROi = new Vector4i(0, 0, 0, 0);
    public static final Vector4ic ONEi  = new Vector4i(1, 1, 1, 1);
    
    public static final Vector4fc Xf    = new Vector4f(1, 0, 0, 0);
    public static final Vector4fc Yf    = new Vector4f(0, 1, 0, 0);
    public static final Vector4fc Zf    = new Vector4f(0, 0, 1, 0);
    public static final Vector4fc Wf    = new Vector4f(0, 0, 0, 1);
    public static final Vector4fc ZEROf = new Vector4f(0, 0, 0, 0);
    public static final Vector4fc ONEf  = new Vector4f(1, 1, 1, 1);
    
    public static final Vector4dc Xd    = new Vector4d(1, 0, 0, 0);
    public static final Vector4dc Yd    = new Vector4d(0, 1, 0, 0);
    public static final Vector4dc Zd    = new Vector4d(0, 0, 1, 0);
    public static final Vector4dc Wd    = new Vector4d(0, 0, 0, 1);
    public static final Vector4dc ZEROd = new Vector4d(0, 0, 0, 0);
    public static final Vector4dc ONEd  = new Vector4d(1, 1, 1, 1);
}
