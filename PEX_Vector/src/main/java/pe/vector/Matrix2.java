package pe.vector;

import pe.util.Pair;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static pe.PixelEngine.format;
import static pe.PixelEngine.getFormatNumbers;

@SuppressWarnings({"unused", "CopyConstructorMissesField", "UnusedReturnValue"})
public class Matrix2
{
    public static final Matrix2 IDENTITY = new Matrix2().markImmutable();
    
    public static final Matrix2 ONE  = new Matrix2(0, 0, 0, 0).markImmutable();
    public static final Matrix2 ZERO = new Matrix2(1, 1, 1, 1).markImmutable();
    
    private double m00, m01;
    private double m10, m11;
    
    private boolean immutable;
    
    public final Slice2 row0 = new Slice2(this::m00, this::m01, this::m00, this::m01);
    public final Slice2 row1 = new Slice2(this::m10, this::m11, this::m10, this::m11);
    
    public final Slice2 col0 = new Slice2(this::m00, this::m10, this::m00, this::m10);
    public final Slice2 col1 = new Slice2(this::m01, this::m11, this::m01, this::m11);
    
    public Matrix2(double m00, double m01, double m10, double m11)
    {
        m00(m00).m01(m01).m10(m10).m11(m11);
    }
    
    public Matrix2()
    {
        this(1, 0, 0, 1);
    }
    
    public Matrix2(Matrix2 o)
    {
        this(o.m00(), o.m01(), o.m10(), o.m11());
    }
    
    public Matrix2(double[][] array)
    {
        this(array[0][0], array[0][1], array[1][0], array[1][1]);
    }
    
    @Override
    public String toString()
    {
        Pair<Integer, Integer> numbers = getFormatNumbers(new double[] {
                m00(), m01(), m10(), m11()
        });
        
        return String.format("[[%s, %s], \n [%s, %s]]", format(m00(), numbers), format(m01(), numbers), format(m10(), numbers), format(m11(), numbers));
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Matrix2)) return false;
        Matrix2 m = (Matrix2) o;
        return Double.compare(m.m00(), m00()) == 0 && Double.compare(m.m01(), m01()) == 0 && Double.compare(m.m10(), m10()) == 0 && Double.compare(m.m11(), m11()) == 0;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(m00(), m01(), m10(), m11());
    }
    
    public boolean isImmutable()
    {
        return this.immutable;
    }
    
    public Matrix2 markImmutable()
    {
        this.immutable = true;
        return this;
    }
    
    public double m00()
    {
        return this.m00;
    }
    
    public Matrix2 m00(double m00)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m00 = m00;
        return this;
    }
    
    public double m01()
    {
        return this.m01;
    }
    
    public Matrix2 m01(double m01)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m01 = m01;
        return this;
    }
    
    public double m10()
    {
        return this.m10;
    }
    
    public Matrix2 m10(double m10)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m10 = m10;
        return this;
    }
    
    public double m11()
    {
        return this.m11;
    }
    
    public Matrix2 m11(double m11)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Matrix is Immutable");
        this.m11 = m11;
        return this;
    }
    
    public Matrix2 set(Matrix2 o)
    {
        m00(o.m00());
        m01(o.m01());
        m10(o.m10());
        m11(o.m11());
        return this;
    }
    
    public Matrix2 set(double[][] array)
    {
        m00(array[0][0]);
        m01(array[0][1]);
        m10(array[1][0]);
        m11(array[1][1]);
        return this;
    }
    
    public double[][] toArray()
    {
        return new double[][] {{m00(), m01()}, {m10(), m11()}};
    }
    
    public Matrix2 copy()
    {
        return new Matrix2(this);
    }
    
    protected Matrix2 defaultOut()
    {
        return isImmutable() ? new Matrix2() : this;
    }
    
    public Matrix2 add(double amount, Matrix2 out)
    {
        if (out == null) out = new Matrix2();
        
        out.m00(m00() + amount);
        out.m01(m01() + amount);
        out.m10(m10() + amount);
        out.m11(m11() + amount);
        
        return out;
    }
    
    public Matrix2 add(double amount)
    {
        return add(amount, defaultOut());
    }
    
    public Matrix2 add(Matrix2 o, Matrix2 out)
    {
        if (out == null) out = new Matrix2();
        
        out.m00(m00() + o.m00());
        out.m01(m01() + o.m01());
        out.m10(m10() + o.m10());
        out.m11(m11() + o.m11());
        
        return out;
    }
    
    public Matrix2 add(Matrix2 o)
    {
        return add(o, defaultOut());
    }
    
    public Matrix2 sub(double amount, Matrix2 out)
    {
        if (out == null) out = new Matrix2();
        
        out.m00(m00() - amount);
        out.m01(m01() - amount);
        out.m10(m10() - amount);
        out.m11(m11() - amount);
        
        return out;
    }
    
    public Matrix2 sub(double amount)
    {
        return sub(amount, defaultOut());
    }
    
    public Matrix2 sub(Matrix2 o, Matrix2 out)
    {
        if (out == null) out = new Matrix2();
        
        out.m00(m00() - o.m00());
        out.m01(m01() - o.m01());
        out.m10(m10() - o.m10());
        out.m11(m11() - o.m11());
        
        return out;
    }
    
    public Matrix2 sub(Matrix2 o)
    {
        return sub(o, defaultOut());
    }
    
    public Matrix2 mul(double amount, Matrix2 out)
    {
        if (out == null) out = new Matrix2();
        
        out.m00(m00() * amount);
        out.m01(m01() * amount);
        out.m10(m10() * amount);
        out.m11(m11() * amount);
        
        return out;
    }
    
    public Matrix2 mul(double amount)
    {
        return mul(amount, defaultOut());
    }
    
    public Matrix2 mul(Matrix2 o, Matrix2 out)
    {
        if (out == null) out = new Matrix2();
        
        out.m00(m00() * o.m00());
        out.m01(m01() * o.m01());
        out.m10(m10() * o.m10());
        out.m11(m11() * o.m11());
        
        return out;
    }
    
    public Matrix2 mul(Matrix2 o)
    {
        return mul(o, defaultOut());
    }
    
    public Matrix2 div(double amount, Matrix2 out)
    {
        if (out == null) out = new Matrix2();
        
        out.m00(m00() / amount);
        out.m01(m01() / amount);
        out.m10(m10() / amount);
        out.m11(m11() / amount);
        
        return out;
    }
    
    public Matrix2 div(double amount)
    {
        return div(amount, defaultOut());
    }
    
    public Matrix2 div(Matrix2 o, Matrix2 out)
    {
        if (out == null) out = new Matrix2();
        
        out.m00(m00() / o.m00());
        out.m01(m01() / o.m01());
        out.m10(m10() / o.m10());
        out.m11(m11() / o.m11());
        
        return out;
    }
    
    public Matrix2 div(Matrix2 o)
    {
        return div(o, defaultOut());
    }
    
    public Matrix2 matmul(Matrix2 o, Matrix2 out)
    {
        if (out == null) out = new Matrix2();
        
        out.m00(m00() * o.m00() + m01() * o.m10());
        out.m01(m00() * o.m01() + m01() * o.m11());
        
        out.m10(m10() * o.m00() + m11() * o.m10());
        out.m11(m10() * o.m01() + m11() * o.m11());
        
        return out;
    }
    
    public Matrix2 matmul(Matrix2 o)
    {
        return matmul(o, defaultOut());
    }
    
    public Matrix2 inverse(Matrix2 out)
    {
        if (out == null) out = new Matrix2();
        
        int n = 2;
        
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
    
    public Matrix2 inverse()
    {
        return inverse(defaultOut());
    }
    
    public static class Slice2 extends Vector2
    {
        private final Supplier<Double> getX;
        private final Supplier<Double> getY;
        
        private final Consumer<Double> setX;
        private final Consumer<Double> setY;
        
        private Slice2(Supplier<Double> getX, Supplier<Double> getY, Consumer<Double> setX, Consumer<Double> setY)
        {
            this.getX = getX;
            this.getY = getY;
            
            this.setX = setX;
            this.setY = setY;
        }
        
        public double x()
        {
            return this.getX != null ? this.getX.get() : 0;
        }
        
        public Slice2 x(double x)
        {
            if (this.setX != null) this.setX.accept(x);
            return this;
        }
        
        public double y()
        {
            return this.getY != null ? this.getY.get() : 0;
        }
        
        public Slice2 y(double y)
        {
            if (this.setY != null) this.setY.accept(y);
            return this;
        }
    }
}
