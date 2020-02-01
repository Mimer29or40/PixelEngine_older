package pe;

import pe.vector.Matrix3;
import pe.vector.Vector2;

public class MatrixTest
{
    public static void main(String[] args)
    {
        Matrix3 matrix = new Matrix3();
        matrix.translate(new Vector2(1, 0));
        System.out.println(matrix);
        matrix.inverse();
        System.out.println(matrix);
    }
}
