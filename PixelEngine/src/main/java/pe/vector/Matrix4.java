package pe.vector;

import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Matrix4
{
    public static final Matrix4fc IDENTITYf = new Matrix4f();
    public static final Matrix4fc ZEROf     = new Matrix4f(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    public static final Matrix4fc ONEf      = new Matrix4f(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
    
    public static final Matrix4dc IDENTITYd = new Matrix4d();
    public static final Matrix4dc ZEROd     = new Matrix4d(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    public static final Matrix4dc ONEd      = new Matrix4d(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
}
