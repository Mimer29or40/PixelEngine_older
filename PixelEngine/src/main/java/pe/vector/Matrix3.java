package pe.vector;

import org.joml.Matrix3d;
import org.joml.Matrix3dc;
import org.joml.Matrix3f;
import org.joml.Matrix3fc;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Matrix3
{
    public static final Matrix3fc IDENTITYf = new Matrix3f();
    public static final Matrix3fc ZEROf     = new Matrix3f(0, 0, 0, 0, 0, 0, 0, 0, 0);
    public static final Matrix3fc ONEf      = new Matrix3f(1, 1, 1, 1, 1, 1, 1, 1, 1);
    
    public static final Matrix3dc IDENTITYd = new Matrix3d();
    public static final Matrix3dc ZEROd     = new Matrix3d(0, 0, 0, 0, 0, 0, 0, 0, 0);
    public static final Matrix3dc ONEd      = new Matrix3d(1, 1, 1, 1, 1, 1, 1, 1, 1);
}
