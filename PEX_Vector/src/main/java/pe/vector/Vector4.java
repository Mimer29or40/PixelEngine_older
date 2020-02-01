package pe.vector;

import pe.PixelEngine;
import pe.util.Pair;

import java.util.Objects;

import static pe.PixelEngine.format;
import static pe.PixelEngine.getFormatNumbers;

@SuppressWarnings({"unused", "CopyConstructorMissesField", "UnusedReturnValue"})
public class Vector4
{
    public static final Vector4 X = new Vector4(1, 0, 0, 0).markImmutable();
    public static final Vector4 Y = new Vector4(0, 1, 0, 0).markImmutable();
    public static final Vector4 Z = new Vector4(0, 0, 1, 0).markImmutable();
    public static final Vector4 W = new Vector4(0, 0, 0, 1).markImmutable();
    
    public static final Vector4 ZERO = new Vector4(0, 0, 0, 0).markImmutable();
    public static final Vector4 ONE  = new Vector4(1, 1, 1, 1).markImmutable();
    
    protected double x, y, z, w;
    
    private boolean immutable;
    
    public Vector4(double x, double y, double z, double w)
    {
        x(x).y(y).z(z).w(w);
    }
    
    public Vector4()
    {
        this(0, 0, 0, 0);
    }
    
    public Vector4(Vector4 o)
    {
        this(o.x(), o.y(), o.z(), o.w());
    }
    
    public Vector4(double[] array)
    {
        this(array[0], array[1], array[2], array[3]);
    }
    
    @Override
    public String toString()
    {
        Pair<Integer, Integer> numbers = getFormatNumbers(new double[] {x(), y(), z(), w()});
        
        return String.format("[%s, %s, %s, %s]", format(x(), numbers), format(y(), numbers), format(z(), numbers), format(w(), numbers));
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Vector4)) return false;
        Vector4 v = (Vector4) o;
        return equals(v.x(), v.y(), v.z(), v.w());
    }
    
    public boolean equals(double x, double y, double z, double w)
    {
        return Double.compare(x, x()) == 0 && Double.compare(y, y()) == 0 && Double.compare(z, z()) == 0 && Double.compare(w, w()) == 0;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(x(), y(), z(), w());
    }
    
    public boolean isImmutable()
    {
        return this.immutable;
    }
    
    public Vector4 markImmutable()
    {
        this.immutable = true;
        return this;
    }
    
    public double x()
    {
        return this.x;
    }
    
    public float xf()
    {
        return (float) x();
    }
    
    public int xi()
    {
        return (int) x();
    }
    
    public long xl()
    {
        return (long) x();
    }
    
    public Vector4 x(double x)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Vector is Immutable");
        this.x = x;
        return this;
    }
    
    public double y()
    {
        return this.y;
    }
    
    public float yf()
    {
        return (float) y();
    }
    
    public int yi()
    {
        return (int) y();
    }
    
    public long yl()
    {
        return (long) y();
    }
    
    public Vector4 y(double y)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Vector is Immutable");
        this.y = y;
        return this;
    }
    
    public double z()
    {
        return this.z;
    }
    
    public float zf()
    {
        return (float) z();
    }
    
    public int zi()
    {
        return (int) z();
    }
    
    public long zl()
    {
        return (long) z();
    }
    
    public Vector4 z(double z)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Vector is Immutable");
        this.z = z;
        return this;
    }
    
    public double w()
    {
        return this.w;
    }
    
    public float wf()
    {
        return (float) w();
    }
    
    public int wi()
    {
        return (int) w();
    }
    
    public long wl()
    {
        return (long) w();
    }
    
    public Vector4 w(double w)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Vector is Immutable");
        this.w = w;
        return this;
    }
    
    public Vector4 set(double x, double y, double z, double w)
    {
        return x(x).y(y).z(z).w(w);
    }
    
    public Vector4 set(Vector4 o)
    {
        return set(o.x(), o.y(), o.z(), o.w());
    }
    
    public Vector4 set(double[] array)
    {
        return set(array[0], array[1], array[2], array[3]);
    }
    
    public double[] toArray()
    {
        return new double[] {x(), y(), z(), w()};
    }
    
    public Vector4 copy(Vector4 out)
    {
        return new Vector4(this);
    }
    
    protected Vector4 defaultOut()
    {
        return isImmutable() ? new Vector4() : this;
    }
    
    public Vector4 add(double x, double y, double z, double w, Vector4 out)
    {
        if (out == null) out = new Vector4();
        out.x(x() + x);
        out.y(y() + y);
        out.z(z() + z);
        out.w(w() + w);
        return out;
    }
    
    public Vector4 add(double x, double y, double z, double w)
    {
        return add(x, y, z, w, defaultOut());
    }
    
    public Vector4 add(double amount, Vector4 out)
    {
        return add(amount, amount, amount, amount, out);
    }
    
    public Vector4 add(double amount)
    {
        return add(amount, amount, amount, amount, defaultOut());
    }
    
    public Vector4 add(Vector4 o, Vector4 out)
    {
        return add(o.x(), o.y(), o.z(), o.w(), out);
    }
    
    public Vector4 add(Vector4 o)
    {
        return add(o.x(), o.y(), o.z(), o.w(), defaultOut());
    }
    
    public Vector4 sub(double x, double y, double z, double w, Vector4 out)
    {
        if (out == null) out = new Vector4();
        out.x(x() - x);
        out.y(y() - y);
        out.z(z() - z);
        out.w(w() - w);
        return out;
    }
    
    public Vector4 sub(double x, double y, double z, double w)
    {
        return sub(x, y, z, w, defaultOut());
    }
    
    public Vector4 sub(double amount, Vector4 out)
    {
        return sub(amount, amount, amount, amount, out);
    }
    
    public Vector4 sub(double amount)
    {
        return sub(amount, amount, amount, amount, defaultOut());
    }
    
    public Vector4 sub(Vector4 o, Vector4 out)
    {
        return sub(o.x(), o.y(), o.z(), o.w(), out);
    }
    
    public Vector4 sub(Vector4 o)
    {
        return sub(o.x(), o.y(), o.z(), o.w(), defaultOut());
    }
    
    public Vector4 mul(double x, double y, double z, double w, Vector4 out)
    {
        if (out == null) out = new Vector4();
        out.x(x() * x);
        out.y(y() * y);
        out.z(z() * z);
        out.w(w() * w);
        return out;
    }
    
    public Vector4 mul(double x, double y, double z, double w)
    {
        return mul(x, y, z, w, defaultOut());
    }
    
    public Vector4 mul(double amount, Vector4 out)
    {
        return mul(amount, amount, amount, amount, out);
    }
    
    public Vector4 mul(double amount)
    {
        return mul(amount, amount, amount, amount, defaultOut());
    }
    
    public Vector4 mul(Vector4 o, Vector4 out)
    {
        return mul(o.x(), o.y(), o.z(), o.w(), out);
    }
    
    public Vector4 mul(Vector4 o)
    {
        return mul(o.x(), o.y(), o.z(), o.w(), defaultOut());
    }
    
    public Vector4 div(double x, double y, double z, double w, Vector4 out)
    {
        if (out == null) out = new Vector4();
        out.x(x() / x);
        out.y(y() / y);
        out.z(z() / z);
        out.w(w() / w);
        return out;
    }
    
    public Vector4 div(double x, double y, double z, double w)
    {
        return div(x, y, z, w, defaultOut());
    }
    
    public Vector4 div(double amount, Vector4 out)
    {
        return div(amount, amount, amount, amount, out);
    }
    
    public Vector4 div(double amount)
    {
        return div(amount, amount, amount, amount, defaultOut());
    }
    
    public Vector4 div(Vector4 o, Vector4 out)
    {
        return div(o.x(), o.y(), o.z(), o.w(), out);
    }
    
    public Vector4 div(Vector4 o)
    {
        return div(o.x(), o.y(), o.z(), o.w(), defaultOut());
    }
    
    public double mag()
    {
        return Math.sqrt(magSq());
    }
    
    public Vector4 mag(double mag, Vector4 out)
    {
        double newMag = mag / mag();
        out.x(x() * newMag);
        out.y(y() * newMag);
        out.z(z() * newMag);
        out.w(w() * newMag);
        return out;
    }
    
    public Vector4 mag(double mag)
    {
        return mag(mag, defaultOut());
    }
    
    public double magSq()
    {
        return x() * x() + y() * y() + z() * z() + w() * w();
    }
    
    public double dot(double x, double y, double z, double w)
    {
        return x() * x + y() * y + z() * z + w() * w;
    }
    
    public double dot(Vector4 o)
    {
        return dot(o.x(), o.y(), o.z(), o.w());
    }
    
    public double distSq(double x, double y, double z, double w)
    {
        return (x - x()) * (x - x()) + (y - y()) * (y - y()) + (z - z()) * (z - z()) + (w - w()) * (w - w());
    }
    
    public double distSq(Vector4 o)
    {
        return distSq(o.x(), o.y(), o.z(), o.w());
    }
    
    public double dist(double x, double y, double z, double w)
    {
        return Math.sqrt(distSq(x, y, z, w));
    }
    
    public double dist(Vector4 o)
    {
        return dist(o.x(), o.y(), o.z(), o.w());
    }
    
    public double angle(Vector4 o)
    {
        return Math.acos(dot(o) / (mag() * o.mag()));
    }
    
    public Vector4 normalize(Vector4 out)
    {
        if (out == null) out = new Vector4();
        double mag = mag();
        out.x(x() / mag);
        out.y(y() / mag);
        out.z(z() / mag);
        return out;
    }
    
    public Vector4 normalize()
    {
        return normalize(defaultOut());
    }
    
    public Vector4 limit(double amount, Vector4 out)
    {
        if (out == null) out = new Vector4();
        if (magSq() > amount * amount) mag(amount, out);
        return out;
    }
    
    public Vector4 limit(double amount)
    {
        return limit(amount, defaultOut());
    }
    
    public Vector4 lerp(double x, double y, double z, double w, double amount, Vector4 out)
    {
        if (out == null) out = new Vector4();
        x = (x - x()) * amount;
        y = (y - y()) * amount;
        z = (z - z()) * amount;
        w = (w - w()) * amount;
        return add(x, y, z, w, out);
    }
    
    public Vector4 lerp(double x, double y, double z, double w, double amount)
    {
        return lerp(x, y, z, w, amount, defaultOut());
    }
    
    public Vector4 lerp(Vector4 o, double amount, Vector4 out)
    {
        return lerp(o.x(), o.y(), o.z(), o.w(), amount, out);
    }
    
    public Vector4 lerp(Vector4 o, double amount)
    {
        return lerp(o.x(), o.y(), o.z(), o.w(), amount, defaultOut());
    }
    
    public Vector4 floor(Vector4 out)
    {
        if (out == null) out = new Vector4();
        out.x(Math.floor(x()));
        out.y(Math.floor(y()));
        out.z(Math.floor(z()));
        out.w(Math.floor(w()));
        return out;
    }
    
    public Vector4 floor()
    {
        return floor(defaultOut());
    }
    
    public Vector4 ceil(Vector4 out)
    {
        if (out == null) out = new Vector4();
        out.x(Math.ceil(x()));
        out.y(Math.ceil(y()));
        out.z(Math.ceil(z()));
        out.w(Math.ceil(w()));
        return out;
    }
    
    public Vector4 ceil()
    {
        return ceil(defaultOut());
    }
    
    public Vector4 round(int places, Vector4 out)
    {
        if (out == null) out = new Vector4();
        out.x(PixelEngine.round(x(), places));
        out.y(PixelEngine.round(y(), places));
        out.z(PixelEngine.round(z(), places));
        out.w(PixelEngine.round(w(), places));
        return out;
    }
    
    public Vector4 round(int places)
    {
        return round(places, defaultOut());
    }
    
    public static Vector4 random()
    {
        double x = PixelEngine.random(-1.0, 1.0);
        double y = PixelEngine.random(-1.0, 1.0);
        double z = PixelEngine.random(-1.0, 1.0);
        double w = PixelEngine.random(-1.0, 1.0);
        return new Vector4(x, y, z, w).normalize();
    }
}
