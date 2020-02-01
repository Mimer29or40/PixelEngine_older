package pe.vector;

import pe.util.Pair;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static pe.PixelEngine.format;
import static pe.PixelEngine.getFormatNumbers;

@SuppressWarnings({"unused", "CopyConstructorMissesField", "UnusedReturnValue"})
public class Matrix3
{
    public static final Matrix3 IDENTITY = new Matrix3().markImmutable();
    
    public static final Matrix3 ONE  = new Matrix3(0, 0, 0, 0, 0, 0, 0, 0, 0).markImmutable();
    public static final Matrix3 ZERO = new Matrix3(1, 1, 1, 1, 1, 1, 1, 1, 1).markImmutable();
    
    private static final Vector2 TEMP = new Vector2();
    
    private double m00, m01, m02;
    private double m10, m11, m12;
    private double m20, m21, m22;
    
    private boolean immutable;
    
    public final Slice3 row0 = new Slice3(this::m00, this::m01, this::m02, this::m00, this::m01, this::m02);
    public final Slice3 row1 = new Slice3(this::m10, this::m11, this::m12, this::m10, this::m11, this::m12);
    public final Slice3 row2 = new Slice3(this::m20, this::m21, this::m22, this::m20, this::m21, this::m22);
    
    public final Slice3 col0 = new Slice3(this::m00, this::m10, this::m20, this::m00, this::m10, this::m20);
    public final Slice3 col1 = new Slice3(this::m01, this::m11, this::m21, this::m01, this::m11, this::m21);
    public final Slice3 col2 = new Slice3(this::m02, this::m12, this::m22, this::m02, this::m12, this::m22);
    
    public Matrix3(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22)
    {
        m00(m00).m01(m01).m02(m02).m10(m10).m11(m11).m12(m12).m20(m20).m21(m21).m22(m22);
    }
    
    public Matrix3()
    {
        this(1, 0, 0, 0, 1, 0, 0, 0, 1);
    }
    
    public Matrix3(Matrix3 o)
    {
        this(o.m00(), o.m01(), o.m02(), o.m10(), o.m11(), o.m12(), o.m20(), o.m21(), o.m22());
    }
    
    public Matrix3(double[][] array)
    {
        this(array[0][0], array[0][1], array[0][2], array[1][0], array[1][1], array[1][2], array[2][0], array[2][1], array[2][2]);
    }
    
    @Override
    public String toString()
    {
        Pair<Integer, Integer> numbers = getFormatNumbers(new double[] {
                m00(), m01(), m02(), m10(), m11(), m12(), m20(), m21(), m22()
        });
        
        return String.format("[[%s, %s, %s], \n [%s, %s, %s], \n [%s, %s, %s]]",
                             format(m00(), numbers),
                             format(m01(), numbers),
                             format(m02(), numbers),
                             format(m10(), numbers),
                             format(m11(), numbers),
                             format(m12(), numbers),
                             format(m20(), numbers),
                             format(m21(), numbers),
                             format(m22(), numbers));
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Matrix3)) return false;
        Matrix3 m = (Matrix3) o;
        return Double.compare(m.m00(), m00()) == 0 &&
               Double.compare(m.m01(), m01()) == 0 &&
               Double.compare(m.m02(), m02()) == 0 &&
               Double.compare(m.m10(), m10()) == 0 &&
               Double.compare(m.m11(), m11()) == 0 &&
               Double.compare(m.m12(), m12()) == 0 &&
               Double.compare(m.m20(), m20()) == 0 &&
               Double.compare(m.m21(), m21()) == 0 &&
               Double.compare(m.m22(), m22()) == 0;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(m00(), m01(), m02(), m10(), m11(), m12(), m20(), m21(), m22());
    }
    
    public boolean isImmutable()
    {
        return this.immutable;
    }
    
    public Matrix3 markImmutable()
    {
        this.immutable = true;
        return this;
    }
    
    public double m00()
    {
        return this.m00;
    }
    
    public Matrix3 m00(double m00)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m00 = m00;
        return this;
    }
    
    public double m01()
    {
        return this.m01;
    }
    
    public Matrix3 m01(double m01)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m01 = m01;
        return this;
    }
    
    public double m02()
    {
        return this.m02;
    }
    
    public Matrix3 m02(double m02)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m02 = m02;
        return this;
    }
    
    public double m10()
    {
        return this.m10;
    }
    
    public Matrix3 m10(double m10)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m10 = m10;
        return this;
    }
    
    public double m11()
    {
        return this.m11;
    }
    
    public Matrix3 m11(double m11)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m11 = m11;
        return this;
    }
    
    public double m12()
    {
        return this.m12;
    }
    
    public Matrix3 m12(double m12)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m12 = m12;
        return this;
    }
    
    public double m20()
    {
        return this.m20;
    }
    
    public Matrix3 m20(double m20)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m20 = m20;
        return this;
    }
    
    public double m21()
    {
        return this.m21;
    }
    
    public Matrix3 m21(double m21)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m21 = m21;
        return this;
    }
    
    public double m22()
    {
        return this.m22;
    }
    
    public Matrix3 m22(double m22)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m22 = m22;
        return this;
    }
    
    public Matrix3 set(Matrix3 o)
    {
        m00(o.m00());
        m01(o.m01());
        m02(o.m02());
        m10(o.m10());
        m11(o.m11());
        m12(o.m12());
        m20(o.m20());
        m21(o.m21());
        m22(o.m22());
        return this;
    }
    
    public Matrix3 set(double[][] array)
    {
        m00(array[0][0]);
        m01(array[0][1]);
        m02(array[0][2]);
        m10(array[1][0]);
        m11(array[1][1]);
        m12(array[1][2]);
        m20(array[2][0]);
        m21(array[2][1]);
        m22(array[2][2]);
        return this;
    }
    
    public double[][] toArray()
    {
        return new double[][] {{m00(), m01(), m02()}, {m10(), m11(), m12()}, {m20(), m21(), m22()}};
    }
    
    public Matrix3 copy()
    {
        return new Matrix3(this);
    }
    
    protected Matrix3 defaultOut()
    {
        return isImmutable() ? new Matrix3() : this;
    }
    
    public Matrix3 add(double amount, Matrix3 out)
    {
        if (out == null) out = new Matrix3();
        
        out.m00(m00() + amount);
        out.m01(m01() + amount);
        out.m02(m02() + amount);
        out.m10(m10() + amount);
        out.m11(m11() + amount);
        out.m12(m12() + amount);
        out.m20(m20() + amount);
        out.m21(m21() + amount);
        out.m22(m22() + amount);
        
        return out;
    }
    
    public Matrix3 add(double amount)
    {
        return add(amount, defaultOut());
    }
    
    public Matrix3 add(Matrix3 o, Matrix3 out)
    {
        if (out == null) out = new Matrix3();
        
        out.m00(m00() + o.m00());
        out.m01(m01() + o.m01());
        out.m02(m02() + o.m02());
        out.m10(m10() + o.m10());
        out.m11(m11() + o.m11());
        out.m12(m12() + o.m12());
        out.m20(m20() + o.m20());
        out.m21(m21() + o.m21());
        out.m22(m22() + o.m22());
        
        return out;
    }
    
    public Matrix3 add(Matrix3 o)
    {
        return add(o, defaultOut());
    }
    
    public Matrix3 sub(double amount, Matrix3 out)
    {
        if (out == null) out = new Matrix3();
        
        out.m00(m00() - amount);
        out.m01(m01() - amount);
        out.m02(m02() - amount);
        out.m10(m10() - amount);
        out.m11(m11() - amount);
        out.m12(m12() - amount);
        out.m20(m20() - amount);
        out.m21(m21() - amount);
        out.m22(m22() - amount);
        
        return out;
    }
    
    public Matrix3 sub(double amount)
    {
        return sub(amount, defaultOut());
    }
    
    public Matrix3 sub(Matrix3 o, Matrix3 out)
    {
        if (out == null) out = new Matrix3();
        
        out.m00(m00() - o.m00());
        out.m01(m01() - o.m01());
        out.m02(m02() - o.m02());
        out.m10(m10() - o.m10());
        out.m11(m11() - o.m11());
        out.m12(m12() - o.m12());
        out.m20(m20() - o.m20());
        out.m21(m21() - o.m21());
        out.m22(m22() - o.m22());
        
        return out;
    }
    
    public Matrix3 sub(Matrix3 o)
    {
        return sub(o, defaultOut());
    }
    
    public Matrix3 mul(double amount, Matrix3 out)
    {
        if (out == null) out = new Matrix3();
        
        out.m00(m00() * amount);
        out.m01(m01() * amount);
        out.m02(m02() * amount);
        out.m10(m10() * amount);
        out.m11(m11() * amount);
        out.m12(m12() * amount);
        out.m20(m20() * amount);
        out.m21(m21() * amount);
        out.m22(m22() * amount);
        
        return out;
    }
    
    public Matrix3 mul(double amount)
    {
        return mul(amount, defaultOut());
    }
    
    public Matrix3 mul(Matrix3 o, Matrix3 out)
    {
        if (out == null) out = new Matrix3();
        
        out.m00(m00() * o.m00());
        out.m01(m01() * o.m01());
        out.m02(m02() * o.m02());
        out.m10(m10() * o.m10());
        out.m11(m11() * o.m11());
        out.m12(m12() * o.m12());
        out.m20(m20() * o.m20());
        out.m21(m21() * o.m21());
        out.m22(m22() * o.m22());
        
        return out;
    }
    
    public Matrix3 mul(Matrix3 o)
    {
        return mul(o, defaultOut());
    }
    
    public Matrix3 div(double amount, Matrix3 out)
    {
        if (out == null) out = new Matrix3();
        
        out.m00(m00() / amount);
        out.m01(m01() / amount);
        out.m02(m02() / amount);
        out.m10(m10() / amount);
        out.m11(m11() / amount);
        out.m12(m12() / amount);
        out.m20(m20() / amount);
        out.m21(m21() / amount);
        out.m22(m22() / amount);
        
        return out;
    }
    
    public Matrix3 div(double amount)
    {
        return div(amount, defaultOut());
    }
    
    public Matrix3 div(Matrix3 o, Matrix3 out)
    {
        if (out == null) out = new Matrix3();
        
        out.m00(m00() / o.m00());
        out.m01(m01() / o.m01());
        out.m02(m02() / o.m02());
        out.m10(m10() / o.m10());
        out.m11(m11() / o.m11());
        out.m12(m12() / o.m12());
        out.m20(m20() / o.m20());
        out.m21(m21() / o.m21());
        out.m22(m22() / o.m22());
        
        return out;
    }
    
    public Matrix3 div(Matrix3 o)
    {
        return div(o, defaultOut());
    }
    
    public Matrix3 matmul(Matrix3 o, Matrix3 out)
    {
        if (out == null) out = new Matrix3();
        
        out.m00(m00() * o.m00() + m01() * o.m10() + m02() * o.m20());
        out.m01(m00() * o.m01() + m01() * o.m11() + m02() * o.m21());
        out.m02(m00() * o.m02() + m01() * o.m12() + m02() * o.m22());
        
        out.m10(m10() * o.m00() + m11() * o.m10() + m12() * o.m20());
        out.m11(m10() * o.m01() + m11() * o.m11() + m12() * o.m21());
        out.m12(m10() * o.m02() + m11() * o.m12() + m12() * o.m22());
        
        out.m20(m20() * o.m00() + m21() * o.m10() + m22() * o.m20());
        out.m21(m20() * o.m01() + m21() * o.m11() + m22() * o.m21());
        out.m22(m20() * o.m02() + m21() * o.m12() + m22() * o.m22());
        
        return out;
    }
    
    public Matrix3 matmul(Matrix3 o)
    {
        return matmul(o, defaultOut());
    }
    
    public Matrix3 inverse(Matrix3 out)
    {
        if (out == null) out = new Matrix3();
        
        int n = 3;
        
        double[][] x = new double[n][n];
        double[][] a = toArray();
        double[][] b = new double[n][n];
        double[]   c = new double[n];
        
        int[] index = new int[n];
        
        int i, j, k;
        
        for (i = 0; i < n; ++i) b[i][i] = 1;
        for (i = 0; i < n; ++i) index[i] = i;
        
        for (i = 0; i < n; ++i)
        {
            double c1 = 0;
            for (j = 0; j < n; ++j)
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }
        
        k = 0;
        for (j = 0; j < n - 1; ++j)
        {
            double pi1 = 0;
            for (i = j; i < n; ++i)
            {
                double pi0 = Math.abs(a[index[i]][j]) / c[index[i]];
                if (pi0 > pi1)
                {
                    pi1 = pi0;
                    k = i;
                }
            }
            
            int tmp = index[j];
            index[j] = index[k];
            index[k] = tmp;
            for (i = j + 1; i < n; ++i)
            {
                double pj = a[index[i]][j] / a[index[j]][j];
                
                a[index[i]][j] = pj;
                
                for (int l = j + 1; l < n; ++l)
                {
                    a[index[i]][l] -= pj * a[index[j]][l];
                }
            }
        }
        
        for (i = 0; i < n - 1; ++i)
        {
            for (j = i + 1; j < n; ++j)
            {
                for (k = 0; k < n; ++k)
                {
                    b[index[j]][k] -= a[index[j]][i] * b[index[i]][k];
                }
            }
        }
        
        for (i = 0; i < n; ++i)
        {
            x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
            for (j = n - 2; j >= 0; --j)
            {
                x[j][i] = b[index[j]][i];
                for (k = j + 1; k < n; ++k)
                {
                    x[j][i] -= a[index[j]][k] * x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return out.set(x);
    }
    
    public Matrix3 inverse()
    {
        return inverse(defaultOut());
    }
    
    public Matrix3 translate(Vector2 axis, double amount, boolean normalize, Matrix3 out)
    {
        if (out == null) out = new Matrix3(defaultOut());
        
        TEMP.set(axis);
        
        if (normalize) TEMP.normalize();
        
        double x = TEMP.x();
        double y = TEMP.y();
        
        out.m20(m20() + x * amount);
        out.m21(m21() + y * amount);
        
        return out;
    }
    
    public Matrix3 translate(Vector2 axis, double amount, boolean normalize)
    {
        return translate(axis, amount, normalize, defaultOut());
    }
    
    public Matrix3 translate(Vector2 axis, double amount, Matrix3 out)
    {
        return translate(axis, amount, false, out);
    }
    
    public Matrix3 translate(Vector2 axis, double amount)
    {
        return translate(axis, amount, false, defaultOut());
    }
    
    public Matrix3 translate(Vector2 axis, boolean normalize, Matrix3 out)
    {
        return translate(axis, 1D, normalize, out);
    }
    
    public Matrix3 translate(Vector2 axis, boolean normalize)
    {
        return translate(axis, 1D, normalize, defaultOut());
    }
    
    public Matrix3 translate(Vector2 axis, Matrix3 out)
    {
        return translate(axis, 1D, false, out);
    }
    
    public Matrix3 translate(Vector2 axis)
    {
        return translate(axis, 1D, false, defaultOut());
    }
    
    public Matrix3 translateLocal(Vector2 axis, double amount, boolean normalize, Matrix3 out)
    {
        if (out == null) out = new Matrix3(defaultOut());
        
        TEMP.set(axis);
        
        if (normalize) TEMP.normalize();
        
        double x = m00() * TEMP.x() + m01() * TEMP.y();
        double y = m10() * TEMP.x() + m11() * TEMP.y();
        
        out.m20(m20() + x * amount);
        out.m21(m21() + y * amount);
        
        return out;
    }
    
    public Matrix3 translateLocal(Vector2 axis, double amount, boolean normalize)
    {
        return translateLocal(axis, amount, normalize, defaultOut());
    }
    
    public Matrix3 translateLocal(Vector2 axis, double amount, Matrix3 out)
    {
        return translateLocal(axis, amount, false, out);
    }
    
    public Matrix3 translateLocal(Vector2 axis, double amount)
    {
        return translateLocal(axis, amount, false, defaultOut());
    }
    
    public Matrix3 translateLocal(Vector2 axis, boolean normalize, Matrix3 out)
    {
        return translateLocal(axis, 1D, normalize, out);
    }
    
    public Matrix3 translateLocal(Vector2 axis, boolean normalize)
    {
        return translateLocal(axis, 1D, normalize, defaultOut());
    }
    
    public Matrix3 translateLocal(Vector2 axis, Matrix3 out)
    {
        return translateLocal(axis, 1D, false, out);
    }
    
    public Matrix3 translateLocal(Vector2 axis)
    {
        return translateLocal(axis, 1D, false, defaultOut());
    }
    
    public Matrix3 rotate(double angle, Matrix3 out)
    {
        if (out == null) out = new Matrix3(defaultOut());
        
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        
        double m00, m01;
        double m10, m11;
        
        m00 = m00() * c + m01() * s;
        m01 = m10() * c + m11() * s;
        m10 = m00() * -s + m01() * c;
        m11 = m10() * -s + m11() * c;
        
        out.m00(m00);
        out.m01(m01);
        out.m10(m10);
        out.m11(m11);
        
        return out;
    }
    
    public Matrix3 rotate(double angle)
    {
        return rotate(angle, defaultOut());
    }
    
    public Matrix3 scale(Vector2 axis, double amount, boolean normalize, Matrix3 out)
    {
        if (out == null) out = new Matrix3(defaultOut());
        
        TEMP.set(axis);
        
        if (normalize) TEMP.normalize();
        
        double x = TEMP.x() * amount;
        double y = TEMP.y() * amount;
        
        out.m00(m00() * x);
        out.m01(m01() * x);
        out.m10(m10() * y);
        out.m11(m11() * y);
        
        return out;
    }
    
    public Matrix3 scale(Vector2 axis, double amount, boolean normalize)
    {
        return scale(axis, amount, normalize, defaultOut());
    }
    
    public Matrix3 scale(Vector2 axis, double amount, Matrix3 out)
    {
        return scale(axis, amount, false, out);
    }
    
    public Matrix3 scale(Vector2 axis, double amount)
    {
        return scale(axis, amount, false, defaultOut());
    }
    
    public Matrix3 scale(Vector2 axis, boolean normalize, Matrix3 out)
    {
        return scale(axis, 1D, normalize, out);
    }
    
    public Matrix3 scale(Vector2 axis, boolean normalize)
    {
        return scale(axis, 1D, normalize, defaultOut());
    }
    
    public Matrix3 scale(Vector2 axis, Matrix3 out)
    {
        return scale(axis, 1D, false, out);
    }
    
    public Matrix3 scale(Vector2 axis)
    {
        return scale(axis, 1D, false, defaultOut());
    }
    
    public Vector2 transform(Vector2 v)
    {
        double x = m00() * v.x() + m10() * v.y() + m20();
        double y = m01() * v.x() + m11() * v.y() + m21();
        
        return v.set(x, y);
    }
    
    public static class Slice3 extends Vector3
    {
        private final Supplier<Double> getX;
        private final Supplier<Double> getY;
        private final Supplier<Double> getZ;
        
        private final Consumer<Double> setX;
        private final Consumer<Double> setY;
        private final Consumer<Double> setZ;
        
        private Slice3(Supplier<Double> getX, Supplier<Double> getY, Supplier<Double> getZ, Consumer<Double> setX, Consumer<Double> setY, Consumer<Double> setZ)
        {
            this.getX = getX;
            this.getY = getY;
            this.getZ = getZ;
            
            this.setX = setX;
            this.setY = setY;
            this.setZ = setZ;
        }
        
        public double x()
        {
            return this.getX != null ? this.getX.get() : 0;
        }
        
        public Slice3 x(double x)
        {
            if (this.setX != null) this.setX.accept(x);
            return this;
        }
        
        public double y()
        {
            return this.getY != null ? this.getY.get() : 0;
        }
        
        public Slice3 y(double y)
        {
            if (this.setY != null) this.setY.accept(y);
            return this;
        }
        
        public double z()
        {
            return this.getZ != null ? this.getZ.get() : 0;
        }
        
        public Slice3 z(double z)
        {
            if (this.setZ != null) this.setZ.accept(z);
            return this;
        }
    }
}
