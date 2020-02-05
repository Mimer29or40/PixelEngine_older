package pe;

import org.joml.Matrix3d;
import org.joml.Vector3d;

public class MatrixTest
{
    public static void main(String[] args)
    {
        Matrix3d matrix = new Matrix3d();
        matrix.transform(new Vector3d(1, 0, 0));
        System.out.println(matrix);
        matrix.invert();
        System.out.println(matrix);
    }
}
