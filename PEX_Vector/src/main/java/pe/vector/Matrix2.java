package pe.vector;

import org.joml.Matrix2d;
import org.joml.Matrix2dc;
import org.joml.Matrix2f;
import org.joml.Matrix2fc;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Matrix2
{
    public static final Matrix2fc IDENTITYf = new Matrix2f();
    public static final Matrix2fc ZEROf     = new Matrix2f(0, 0, 0, 0);
    public static final Matrix2fc ONEf      = new Matrix2f(1, 1, 1, 1);
    
    public static final Matrix2dc IDENTITYd = new Matrix2d();
    public static final Matrix2dc ZEROd     = new Matrix2d(0, 0, 0, 0);
    public static final Matrix2dc ONEd      = new Matrix2d(1, 1, 1, 1);
}
