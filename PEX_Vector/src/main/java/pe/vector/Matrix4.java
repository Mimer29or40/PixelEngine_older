package pe.vector;

import pe.util.Pair;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static pe.PixelEngine.format;
import static pe.PixelEngine.getFormatNumbers;

@SuppressWarnings({"unused", "CopyConstructorMissesField", "UnusedReturnValue"})
public class Matrix4
{
    public static final Matrix4 IDENTITY = new Matrix4().markImmutable();
    
    public static final Matrix4 ONE  = new Matrix4(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0).markImmutable();
    public static final Matrix4 ZERO = new Matrix4(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1).markImmutable();
    
    private static final Vector3 TEMP = new Vector3();
    
    protected double m00, m01, m02, m03;
    protected double m10, m11, m12, m13;
    protected double m20, m21, m22, m23;
    protected double m30, m31, m32, m33;
    
    private boolean immutable;
    
    public final Slice4 row0 = new Slice4(this::m00, this::m01, this::m02, this::m03, this::m00, this::m01, this::m02, this::m03);
    public final Slice4 row1 = new Slice4(this::m10, this::m11, this::m12, this::m13, this::m10, this::m11, this::m12, this::m13);
    public final Slice4 row2 = new Slice4(this::m20, this::m21, this::m22, this::m23, this::m20, this::m21, this::m22, this::m23);
    public final Slice4 row3 = new Slice4(this::m30, this::m31, this::m32, this::m33, this::m30, this::m31, this::m32, this::m33);
    
    public final Slice4 col0 = new Slice4(this::m00, this::m10, this::m20, this::m30, this::m00, this::m10, this::m20, this::m30);
    public final Slice4 col1 = new Slice4(this::m01, this::m11, this::m21, this::m31, this::m01, this::m11, this::m21, this::m31);
    public final Slice4 col2 = new Slice4(this::m02, this::m12, this::m22, this::m32, this::m02, this::m12, this::m22, this::m32);
    public final Slice4 col3 = new Slice4(this::m03, this::m13, this::m23, this::m33, this::m03, this::m13, this::m23, this::m33);
    
    public Matrix4(double m00,
                   double m01,
                   double m02,
                   double m03,
                   double m10,
                   double m11,
                   double m12,
                   double m13,
                   double m20,
                   double m21,
                   double m22,
                   double m23,
                   double m30,
                   double m31,
                   double m32,
                   double m33)
    {
        m00(m00).m01(m01).m02(m02).m03(m03).m10(m10).m11(m11).m12(m12).m13(m13).m20(m20).m21(m21).m22(m22).m23(m23).m30(m30).m31(m31).m32(m32).m33(m33);
    }
    
    public Matrix4()
    {
        this(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
    }
    
    public Matrix4(Matrix4 o)
    {
        this(o.m00(), o.m01(), o.m02(), o.m03(), o.m10(), o.m11(), o.m12(), o.m13(), o.m20(), o.m21(), o.m22(), o.m23(), o.m30(), o.m31(), o.m32(), o.m33());
    }
    
    public Matrix4(double[][] array)
    {
        this(array[0][0],
             array[0][1],
             array[0][2],
             array[0][3],
             array[1][0],
             array[1][1],
             array[1][2],
             array[1][3],
             array[2][0],
             array[2][1],
             array[2][2],
             array[2][3],
             array[3][0],
             array[3][1],
             array[3][2],
             array[3][3]);
    }
    
    @Override
    public String toString()
    {
        Pair<Integer, Integer> numbers = getFormatNumbers(new double[] {
                m00(), m01(), m02(), m03(), m10(), m11(), m12(), m13(), m20(), m21(), m22(), m23(), m30(), m31(), m32(), m33()
        });
        
        return String.format("[[%s, %s, %s, %s], \n [%s, %s, %s, %s], \n [%s, %s, %s, %s], \n [%s, %s, %s, %s]]",
                             format(m00(), numbers),
                             format(m01(), numbers),
                             format(m02(), numbers),
                             format(m03(), numbers),
                             format(m10(), numbers),
                             format(m11(), numbers),
                             format(m12(), numbers),
                             format(m13(), numbers),
                             format(m20(), numbers),
                             format(m21(), numbers),
                             format(m22(), numbers),
                             format(m23(), numbers),
                             format(m30(), numbers),
                             format(m31(), numbers),
                             format(m32(), numbers),
                             format(m33(), numbers));
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Matrix4)) return false;
        Matrix4 m = (Matrix4) o;
        return Double.compare(m.m00(), m00()) == 0 &&
               Double.compare(m.m01(), m01()) == 0 &&
               Double.compare(m.m02(), m02()) == 0 &&
               Double.compare(m.m03(), m03()) == 0 &&
               Double.compare(m.m10(), m10()) == 0 &&
               Double.compare(m.m11(), m11()) == 0 &&
               Double.compare(m.m12(), m12()) == 0 &&
               Double.compare(m.m13(), m13()) == 0 &&
               Double.compare(m.m20(), m20()) == 0 &&
               Double.compare(m.m21(), m21()) == 0 &&
               Double.compare(m.m22(), m22()) == 0 &&
               Double.compare(m.m23(), m23()) == 0 &&
               Double.compare(m.m30(), m30()) == 0 &&
               Double.compare(m.m31(), m31()) == 0 &&
               Double.compare(m.m32(), m32()) == 0 &&
               Double.compare(m.m33(), m33()) == 0;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(m00(), m01(), m02(), m03(), m10(), m11(), m12(), m13(), m20(), m21(), m22(), m23(), m30(), m31(), m32(), m33());
    }
    
    public boolean isImmutable()
    {
        return this.immutable;
    }
    
    public Matrix4 markImmutable()
    {
        this.immutable = true;
        return this;
    }
    
    public double m00()
    {
        return this.m00;
    }
    
    public Matrix4 m00(double m00)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m00 = m00;
        return this;
    }
    
    public double m01()
    {
        return this.m01;
    }
    
    public Matrix4 m01(double m01)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m01 = m01;
        return this;
    }
    
    public double m02()
    {
        return this.m02;
    }
    
    public Matrix4 m02(double m02)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m02 = m02;
        return this;
    }
    
    public double m03()
    {
        return this.m03;
    }
    
    public Matrix4 m03(double m03)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m03 = m03;
        return this;
    }
    
    public double m10()
    {
        return this.m10;
    }
    
    public Matrix4 m10(double m10)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m10 = m10;
        return this;
    }
    
    public double m11()
    {
        return this.m11;
    }
    
    public Matrix4 m11(double m11)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m11 = m11;
        return this;
    }
    
    public double m12()
    {
        return this.m12;
    }
    
    public Matrix4 m12(double m12)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m12 = m12;
        return this;
    }
    
    public double m13()
    {
        return this.m13;
    }
    
    public Matrix4 m13(double m13)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m13 = m13;
        return this;
    }
    
    public double m20()
    {
        return this.m20;
    }
    
    public Matrix4 m20(double m20)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m20 = m20;
        return this;
    }
    
    public double m21()
    {
        return this.m21;
    }
    
    public Matrix4 m21(double m21)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m21 = m21;
        return this;
    }
    
    public double m22()
    {
        return this.m22;
    }
    
    public Matrix4 m22(double m22)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m22 = m22;
        return this;
    }
    
    public double m23()
    {
        return this.m23;
    }
    
    public Matrix4 m23(double m23)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m23 = m23;
        return this;
    }
    
    public double m30()
    {
        return this.m30;
    }
    
    public Matrix4 m30(double m30)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m30 = m30;
        return this;
    }
    
    public double m31()
    {
        return this.m31;
    }
    
    public Matrix4 m31(double m31)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m31 = m31;
        return this;
    }
    
    public double m32()
    {
        return this.m32;
    }
    
    public Matrix4 m32(double m32)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m32 = m32;
        return this;
    }
    
    public double m33()
    {
        return this.m33;
    }
    
    public Matrix4 m33(double m33)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m33 = m33;
        return this;
    }
    
    public Matrix4 set(Matrix4 o)
    {
        m00(o.m00());
        m01(o.m01());
        m02(o.m02());
        m03(o.m03());
        m10(o.m10());
        m11(o.m11());
        m12(o.m12());
        m13(o.m13());
        m20(o.m20());
        m21(o.m21());
        m22(o.m22());
        m23(o.m23());
        m30(o.m30());
        m31(o.m31());
        m32(o.m32());
        m33(o.m33());
        return this;
    }
    
    public Matrix4 set(double[][] array)
    {
        m00(array[0][0]);
        m01(array[0][1]);
        m02(array[0][2]);
        m03(array[0][3]);
        m10(array[1][0]);
        m11(array[1][1]);
        m12(array[1][2]);
        m13(array[1][3]);
        m20(array[2][0]);
        m21(array[2][1]);
        m22(array[2][2]);
        m23(array[2][3]);
        m30(array[3][0]);
        m31(array[3][1]);
        m32(array[3][2]);
        m33(array[3][3]);
        return this;
    }
    
    public double[][] toArray()
    {
        return new double[][] {
                {m00(), m01(), m02(), m03()}, {m10(), m11(), m12(), m13()}, {m20(), m21(), m22(), m23()}, {m30(), m31(), m32(), m33()}
        };
    }
    
    public Matrix4 copy()
    {
        return new Matrix4(this);
    }
    
    protected Matrix4 defaultOut()
    {
        return isImmutable() ? new Matrix4() : this;
    }
    
    public Matrix4 add(double amount, Matrix4 out)
    {
        if (out == null) out = new Matrix4();
        
        out.m00(m00() + amount);
        out.m01(m01() + amount);
        out.m02(m02() + amount);
        out.m03(m03() + amount);
        out.m10(m10() + amount);
        out.m11(m11() + amount);
        out.m12(m12() + amount);
        out.m13(m13() + amount);
        out.m20(m20() + amount);
        out.m21(m21() + amount);
        out.m22(m22() + amount);
        out.m23(m23() + amount);
        out.m30(m30() + amount);
        out.m31(m31() + amount);
        out.m32(m32() + amount);
        out.m33(m33() + amount);
        
        return out;
    }
    
    public Matrix4 add(double amount)
    {
        return add(amount, defaultOut());
    }
    
    public Matrix4 add(Matrix4 o, Matrix4 out)
    {
        if (out == null) out = new Matrix4();
        
        out.m00(m00() + o.m00());
        out.m01(m01() + o.m01());
        out.m02(m02() + o.m02());
        out.m03(m03() + o.m03());
        out.m10(m10() + o.m10());
        out.m11(m11() + o.m11());
        out.m12(m12() + o.m12());
        out.m13(m13() + o.m13());
        out.m20(m20() + o.m20());
        out.m21(m21() + o.m21());
        out.m22(m22() + o.m22());
        out.m23(m23() + o.m23());
        out.m30(m30() + o.m30());
        out.m31(m31() + o.m31());
        out.m32(m32() + o.m32());
        out.m33(m33() + o.m33());
        
        return out;
    }
    
    public Matrix4 add(Matrix4 o)
    {
        return add(o, defaultOut());
    }
    
    public Matrix4 sub(double amount, Matrix4 out)
    {
        if (out == null) out = new Matrix4();
        
        out.m00(m00() - amount);
        out.m01(m01() - amount);
        out.m02(m02() - amount);
        out.m03(m03() - amount);
        out.m10(m10() - amount);
        out.m11(m11() - amount);
        out.m12(m12() - amount);
        out.m13(m13() - amount);
        out.m20(m20() - amount);
        out.m21(m21() - amount);
        out.m22(m22() - amount);
        out.m23(m23() - amount);
        out.m30(m30() - amount);
        out.m31(m31() - amount);
        out.m32(m32() - amount);
        out.m33(m33() - amount);
        
        return out;
    }
    
    public Matrix4 sub(double amount)
    {
        return sub(amount, defaultOut());
    }
    
    public Matrix4 sub(Matrix4 o, Matrix4 out)
    {
        if (out == null) out = new Matrix4();
        
        out.m00(m00() - o.m00());
        out.m01(m01() - o.m01());
        out.m02(m02() - o.m02());
        out.m03(m03() - o.m03());
        out.m10(m10() - o.m10());
        out.m11(m11() - o.m11());
        out.m12(m12() - o.m12());
        out.m13(m13() - o.m13());
        out.m20(m20() - o.m20());
        out.m21(m21() - o.m21());
        out.m22(m22() - o.m22());
        out.m23(m23() - o.m23());
        out.m30(m30() - o.m30());
        out.m31(m31() - o.m31());
        out.m32(m32() - o.m32());
        out.m33(m33() - o.m33());
        
        return out;
    }
    
    public Matrix4 sub(Matrix4 o)
    {
        return sub(o, defaultOut());
    }
    
    public Matrix4 mul(double amount, Matrix4 out)
    {
        if (out == null) out = new Matrix4();
        
        out.m00(m00() * amount);
        out.m01(m01() * amount);
        out.m02(m02() * amount);
        out.m03(m03() * amount);
        out.m10(m10() * amount);
        out.m11(m11() * amount);
        out.m12(m12() * amount);
        out.m13(m13() * amount);
        out.m20(m20() * amount);
        out.m21(m21() * amount);
        out.m22(m22() * amount);
        out.m23(m23() * amount);
        out.m30(m30() * amount);
        out.m31(m31() * amount);
        out.m32(m32() * amount);
        out.m33(m33() * amount);
        
        return out;
    }
    
    public Matrix4 mul(double amount)
    {
        return mul(amount, defaultOut());
    }
    
    public Matrix4 mul(Matrix4 o, Matrix4 out)
    {
        if (out == null) out = new Matrix4();
        
        out.m00(m00() * o.m00());
        out.m01(m01() * o.m01());
        out.m02(m02() * o.m02());
        out.m03(m03() * o.m03());
        out.m10(m10() * o.m10());
        out.m11(m11() * o.m11());
        out.m12(m12() * o.m12());
        out.m13(m13() * o.m13());
        out.m20(m20() * o.m20());
        out.m21(m21() * o.m21());
        out.m22(m22() * o.m22());
        out.m23(m23() * o.m23());
        out.m30(m30() * o.m30());
        out.m31(m31() * o.m31());
        out.m32(m32() * o.m32());
        out.m33(m33() * o.m33());
        
        return out;
    }
    
    public Matrix4 mul(Matrix4 o)
    {
        return mul(o, defaultOut());
    }
    
    public Matrix4 div(double amount, Matrix4 out)
    {
        if (out == null) out = new Matrix4();
        
        out.m00(m00() / amount);
        out.m01(m01() / amount);
        out.m02(m02() / amount);
        out.m03(m03() / amount);
        out.m10(m10() / amount);
        out.m11(m11() / amount);
        out.m12(m12() / amount);
        out.m13(m13() / amount);
        out.m20(m20() / amount);
        out.m21(m21() / amount);
        out.m22(m22() / amount);
        out.m23(m23() / amount);
        out.m30(m30() / amount);
        out.m31(m31() / amount);
        out.m32(m32() / amount);
        out.m33(m33() / amount);
        
        return out;
    }
    
    public Matrix4 div(double amount)
    {
        return div(amount, defaultOut());
    }
    
    public Matrix4 div(Matrix4 o, Matrix4 out)
    {
        if (out == null) out = new Matrix4();
        
        out.m00(m00() / o.m00());
        out.m01(m01() / o.m01());
        out.m02(m02() / o.m02());
        out.m03(m03() / o.m03());
        out.m10(m10() / o.m10());
        out.m11(m11() / o.m11());
        out.m12(m12() / o.m12());
        out.m13(m13() / o.m13());
        out.m20(m20() / o.m20());
        out.m21(m21() / o.m21());
        out.m22(m22() / o.m22());
        out.m23(m23() / o.m23());
        out.m30(m30() / o.m30());
        out.m31(m31() / o.m31());
        out.m32(m32() / o.m32());
        out.m33(m33() / o.m33());
        
        return out;
    }
    
    public Matrix4 div(Matrix4 o)
    {
        return div(o, defaultOut());
    }
    
    public Matrix4 matmul(Matrix4 o, Matrix4 out)
    {
        if (out == null) out = new Matrix4();
        
        out.m00(m00() * o.m00() + m01() * o.m10() + m02() * o.m20() + m03() * o.m30());
        out.m01(m00() * o.m01() + m01() * o.m11() + m02() * o.m21() + m03() * o.m31());
        out.m02(m00() * o.m02() + m01() * o.m12() + m02() * o.m22() + m03() * o.m32());
        out.m03(m00() * o.m03() + m01() * o.m13() + m02() * o.m23() + m03() * o.m33());
        
        out.m10(m10() * o.m00() + m11() * o.m10() + m12() * o.m20() + m13() * o.m30());
        out.m11(m10() * o.m01() + m11() * o.m11() + m12() * o.m21() + m13() * o.m31());
        out.m12(m10() * o.m02() + m11() * o.m12() + m12() * o.m22() + m13() * o.m32());
        out.m12(m10() * o.m03() + m11() * o.m13() + m12() * o.m23() + m13() * o.m33());
        
        out.m20(m20() * o.m00() + m21() * o.m10() + m22() * o.m20() + m23() * o.m30());
        out.m21(m20() * o.m01() + m21() * o.m11() + m22() * o.m21() + m23() * o.m31());
        out.m22(m20() * o.m02() + m21() * o.m12() + m22() * o.m22() + m23() * o.m32());
        out.m22(m20() * o.m03() + m21() * o.m13() + m22() * o.m23() + m23() * o.m33());
        
        out.m20(m30() * o.m00() + m31() * o.m10() + m32() * o.m20() + m33() * o.m30());
        out.m21(m30() * o.m01() + m31() * o.m11() + m32() * o.m21() + m33() * o.m31());
        out.m22(m30() * o.m02() + m31() * o.m12() + m32() * o.m22() + m33() * o.m32());
        out.m22(m30() * o.m03() + m31() * o.m13() + m32() * o.m23() + m33() * o.m33());
        
        return out;
    }
    
    public Matrix4 matmul(Matrix4 o)
    {
        return matmul(o, defaultOut());
    }
    
    public Matrix4 inverse(Matrix4 out)
    {
        if (out == null) out = new Matrix4();
        
        int n = 4;
        
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
    
    public Matrix4 inverse()
    {
        return inverse(defaultOut());
    }
    
    public Matrix4 translate(Vector3 axis, double amount, boolean normalize, Matrix4 out)
    {
        if (out == null) return new Matrix4(defaultOut());
        
        TEMP.set(axis);
        
        if (normalize) TEMP.normalize();
        
        double x = TEMP.x();
        double y = TEMP.y();
        double z = TEMP.z();
        
        out.m20(m20() + x * amount);
        out.m21(m21() + y * amount);
        out.m22(m22() + z * amount);
        
        return out;
    }
    
    public Matrix4 translate(Vector3 axis, double amount, boolean normalize)
    {
        return translate(axis, amount, normalize, defaultOut());
    }
    
    public Matrix4 translate(Vector3 axis, double amount, Matrix4 out)
    {
        return translate(axis, amount, false, out);
    }
    
    public Matrix4 translate(Vector3 axis, double amount)
    {
        return translate(axis, amount, false, defaultOut());
    }
    
    public Matrix4 translate(Vector3 axis, boolean normalize, Matrix4 out)
    {
        return translate(axis, 1D, normalize, out);
    }
    
    public Matrix4 translate(Vector3 axis, boolean normalize)
    {
        return translate(axis, 1D, normalize, defaultOut());
    }
    
    public Matrix4 translate(Vector3 axis, Matrix4 out)
    {
        return translate(axis, 1D, false, out);
    }
    
    public Matrix4 translate(Vector3 axis)
    {
        return translate(axis, 1D, false, defaultOut());
    }
    
    public Matrix4 translateLocal(Vector3 axis, double amount, boolean normalize, Matrix4 out)
    {
        if (out == null) return new Matrix4(defaultOut());
        
        TEMP.set(axis);
        
        if (normalize) TEMP.normalize();
        
        double x = m00() * TEMP.x() + m01() * TEMP.y() + m02() * TEMP.z();
        double y = m10() * TEMP.x() + m11() * TEMP.y() + m12() * TEMP.z();
        double z = m20() * TEMP.x() + m21() * TEMP.y() + m22() * TEMP.z();
        
        out.m20(m20() + x * amount);
        out.m21(m21() + y * amount);
        out.m22(m22() + z * amount);
        
        return out;
    }
    
    public Matrix4 translateLocal(Vector3 axis, double amount, boolean normalize)
    {
        return translateLocal(axis, amount, normalize, defaultOut());
    }
    
    public Matrix4 translateLocal(Vector3 axis, double amount, Matrix4 out)
    {
        return translateLocal(axis, amount, false, out);
    }
    
    public Matrix4 translateLocal(Vector3 axis, double amount)
    {
        return translateLocal(axis, amount, false, defaultOut());
    }
    
    public Matrix4 translateLocal(Vector3 axis, boolean normalize, Matrix4 out)
    {
        return translateLocal(axis, 1D, normalize, out);
    }
    
    public Matrix4 translateLocal(Vector3 axis, boolean normalize)
    {
        return translateLocal(axis, 1D, normalize, defaultOut());
    }
    
    public Matrix4 translateLocal(Vector3 axis, Matrix4 out)
    {
        return translateLocal(axis, 1D, false, out);
    }
    
    public Matrix4 translateLocal(Vector3 axis)
    {
        return translateLocal(axis, 1D, false, defaultOut());
    }
    
    public Matrix4 rotate(Vector3 axis, double angle, boolean normalize, Matrix4 out)
    {
        if (out == null) return new Matrix4(defaultOut());
        
        TEMP.set(axis);
        
        if (normalize) TEMP.normalize();
        
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        
        double x = (1D - c) * TEMP.x();
        double y = (1D - c) * TEMP.y();
        double z = (1D - c) * TEMP.z();
        
        double tx, ty, tz;
        double m00, m01, m02;
        double m10, m11, m12;
        double m20, m21, m22;
        
        tx = x * TEMP.x() + c;
        ty = x * TEMP.y() + (s * TEMP.z());
        tz = x * TEMP.z() + (-s * TEMP.y());
        m00 = m00() * tx + m01() * ty * m02() * tz;
        m01 = m10() * tx + m11() * ty * m12() * tz;
        m02 = m20() * tx + m21() * ty * m22() * tz;
        
        tx = y * TEMP.x() + (-s * TEMP.z());
        ty = y * TEMP.y() + c;
        tz = y * TEMP.z() + (s * TEMP.x());
        m10 = m00() * tx + m01() * ty * m02() * tz;
        m11 = m10() * tx + m11() * ty * m12() * tz;
        m12 = m20() * tx + m21() * ty * m22() * tz;
        
        tx = y * TEMP.x() + (s * TEMP.y());
        ty = y * TEMP.y() + (-s * TEMP.x());
        tz = y * TEMP.z() + c;
        m20 = m00() * tx + m01() * ty * m02() * tz;
        m21 = m10() * tx + m11() * ty * m12() * tz;
        m22 = m20() * tx + m21() * ty * m22() * tz;
        
        out.m00(m00);
        out.m01(m01);
        out.m02(m02);
        out.m10(m10);
        out.m11(m11);
        out.m12(m12);
        out.m20(m20);
        out.m21(m21);
        out.m22(m22);
        
        return out;
    }
    
    public Matrix4 rotate(Vector3 axis, double angle, boolean normalize)
    {
        return rotate(axis, angle, normalize, defaultOut());
    }
    
    public Matrix4 rotate(Vector3 axis, double angle, Matrix4 out)
    {
        return rotate(axis, angle, false, out);
    }
    
    public Matrix4 rotate(Vector3 axis, double angle)
    {
        return rotate(axis, angle, false, defaultOut());
    }
    
    public Matrix4 scale(Vector3 axis, double amount, boolean normalize, Matrix4 out)
    {
        if (out == null) return new Matrix4(defaultOut());
        
        TEMP.set(axis);
        
        if (normalize) TEMP.normalize();
        
        double x = TEMP.x() * amount;
        double y = TEMP.y() * amount;
        double z = TEMP.z() * amount;
        
        out.m00(m00() * x);
        out.m01(m01() * x);
        out.m02(m02() * x);
        out.m10(m10() * y);
        out.m11(m11() * y);
        out.m12(m12() * y);
        out.m20(m20() * z);
        out.m21(m21() * z);
        out.m22(m22() * z);
        
        return out;
    }
    
    public Matrix4 scale(Vector3 axis, double amount, boolean normalize)
    {
        return scale(axis, amount, normalize, defaultOut());
    }
    
    public Matrix4 scale(Vector3 axis, double amount, Matrix4 out)
    {
        return scale(axis, amount, false, out);
    }
    
    public Matrix4 scale(Vector3 axis, double amount)
    {
        return scale(axis, amount, false, defaultOut());
    }
    
    public Matrix4 scale(Vector3 axis, boolean normalize, Matrix4 out)
    {
        return scale(axis, 1D, normalize, out);
    }
    
    public Matrix4 scale(Vector3 axis, boolean normalize)
    {
        return scale(axis, 1D, normalize, defaultOut());
    }
    
    public Matrix4 scale(Vector3 axis, Matrix4 out)
    {
        return scale(axis, 1D, false, out);
    }
    
    public Matrix4 scale(Vector3 axis)
    {
        return scale(axis, 1D, false, defaultOut());
    }
    
    public Vector3 transform(Vector3 v)
    {
        double x = m00() * v.x() + m10() * v.y() + m20() * v.z() + m30();
        double y = m01() * v.x() + m11() * v.y() + m21() * v.z() + m31();
        double z = m02() * v.x() + m12() * v.y() + m22() * v.z() + m32();
        
        return v.set(x, y, z);
    }
    
    public static class Slice4 extends Vector4
    {
        private final Supplier<Double> getX;
        private final Supplier<Double> getY;
        private final Supplier<Double> getZ;
        private final Supplier<Double> getW;
        
        private final Consumer<Double> setX;
        private final Consumer<Double> setY;
        private final Consumer<Double> setZ;
        private final Consumer<Double> setW;
        
        private Slice4(Supplier<Double> getX,
                       Supplier<Double> getY,
                       Supplier<Double> getZ,
                       Supplier<Double> getW,
                       Consumer<Double> setX,
                       Consumer<Double> setY,
                       Consumer<Double> setZ,
                       Consumer<Double> setW)
        {
            this.getX = getX;
            this.getY = getY;
            this.getZ = getZ;
            this.getW = getW;
            
            this.setX = setX;
            this.setY = setY;
            this.setZ = setZ;
            this.setW = setW;
        }
        
        public double x()
        {
            return this.getX != null ? this.getX.get() : 0;
        }
        
        public Slice4 x(double x)
        {
            if (this.setX != null) this.setX.accept(x);
            return this;
        }
        
        public double y()
        {
            return this.getY != null ? this.getY.get() : 0;
        }
        
        public Slice4 y(double y)
        {
            if (this.setY != null) this.setY.accept(y);
            return this;
        }
        
        public double z()
        {
            return this.getZ != null ? this.getZ.get() : 0;
        }
        
        public Slice4 z(double z)
        {
            if (this.setZ != null) this.setZ.accept(z);
            return this;
        }
        
        public double w()
        {
            return this.getW != null ? this.getW.get() : 0;
        }
        
        public Slice4 w(double w)
        {
            if (this.setW != null) this.setW.accept(w);
            return this;
        }
    }
}
