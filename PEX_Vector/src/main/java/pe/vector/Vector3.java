package pe.vector;

import pe.PixelEngine;
import pe.util.Pair;

import java.util.Objects;

import static pe.PixelEngine.format;
import static pe.PixelEngine.getFormatNumbers;

@SuppressWarnings({"unused", "CopyConstructorMissesField", "UnusedReturnValue"})
public class Vector3
{
    public static final Vector3 X = new Vector3(1, 0, 0).markImmutable();
    public static final Vector3 Y = new Vector3(0, 1, 0).markImmutable();
    public static final Vector3 Z = new Vector3(0, 0, 1).markImmutable();
    
    public static final Vector3 ZERO = new Vector3(0, 0, 0).markImmutable();
    public static final Vector3 ONE  = new Vector3(1, 1, 0).markImmutable();
    
    protected double x, y, z;
    
    private boolean immutable;
    
    public Vector3(double x, double y, double z)
    {
        x(x).y(y).z(z);
    }
    
    public Vector3()
    {
        this(0, 0, 0);
    }
    
    public Vector3(Vector3 o)
    {
        this(o.x(), o.y(), o.z());
    }
    
    public Vector3(double[] array)
    {
        this(array[0], array[1], array[2]);
    }
    
    @Override
    public String toString()
    {
        Pair<Integer, Integer> numbers = getFormatNumbers(new double[] {x(), y(), z()});
        
        return String.format("[%s, %s, %s]", format(x(), numbers), format(y(), numbers), format(z(), numbers));
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Vector3)) return false;
        Vector3 v = (Vector3) o;
        return equals(v.x(), v.y(), v.z());
    }
    
    public boolean equals(double x, double y, double z)
    {
        return Double.compare(x, x()) == 0 && Double.compare(y, y()) == 0 && Double.compare(z, z()) == 0;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(x(), y(), z());
    }
    
    public boolean isImmutable()
    {
        return this.immutable;
    }
    
    public Vector3 markImmutable()
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
    
    public Vector3 x(double x)
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
    
    public Vector3 y(double y)
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
    
    public Vector3 z(double z)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Vector is Immutable");
        this.z = z;
        return this;
    }
    
    public Vector3 set(double x, double y, double z)
    {
        return x(x).y(y).z(z);
    }
    
    public Vector3 set(Vector3 o)
    {
        return set(o.x(), o.y(), o.z());
    }
    
    public Vector3 set(double[] array)
    {
        return set(array[0], array[1], array[2]);
    }
    
    public double[] toArray()
    {
        return new double[] {x(), y(), z()};
    }
    
    public Vector3 copy(Vector3 out)
    {
        return new Vector3(this);
    }
    
    protected Vector3 defaultOut()
    {
        return isImmutable() ? new Vector3() : this;
    }
    
    public Vector3 add(double x, double y, double z, Vector3 out)
    {
        if (out == null) out = new Vector3();
        out.x(x() + x);
        out.y(y() + y);
        out.z(z() + z);
        return out;
    }
    
    public Vector3 add(double x, double y, double z)
    {
        return add(x, y, z, defaultOut());
    }
    
    public Vector3 add(double amount, Vector3 out)
    {
        return add(amount, amount, amount, out);
    }
    
    public Vector3 add(double amount)
    {
        return add(amount, amount, amount, defaultOut());
    }
    
    public Vector3 add(Vector3 o, Vector3 out)
    {
        return add(o.x(), o.y(), o.z(), out);
    }
    
    public Vector3 add(Vector3 o)
    {
        return add(o.x(), o.y(), o.z(), defaultOut());
    }
    
    public Vector3 sub(double x, double y, double z, Vector3 out)
    {
        if (out == null) out = new Vector3();
        out.x(x() - x);
        out.y(y() - y);
        out.z(z() - z);
        return out;
    }
    
    public Vector3 sub(double x, double y, double z)
    {
        return sub(x, y, z, defaultOut());
    }
    
    public Vector3 sub(double amount, Vector3 out)
    {
        return sub(amount, amount, amount, out);
    }
    
    public Vector3 sub(double amount)
    {
        return sub(amount, amount, amount, defaultOut());
    }
    
    public Vector3 sub(Vector3 o, Vector3 out)
    {
        return sub(o.x(), o.y(), o.z(), out);
    }
    
    public Vector3 sub(Vector3 o)
    {
        return sub(o.x(), o.y(), o.z(), defaultOut());
    }
    
    public Vector3 mul(double x, double y, double z, Vector3 out)
    {
        if (out == null) out = new Vector3();
        out.x(x() * x);
        out.y(y() * y);
        out.z(z() * z);
        return out;
    }
    
    public Vector3 mul(double x, double y, double z)
    {
        return mul(x, y, z, defaultOut());
    }
    
    public Vector3 mul(double amount, Vector3 out)
    {
        return mul(amount, amount, amount, out);
    }
    
    public Vector3 mul(double amount)
    {
        return mul(amount, amount, amount, defaultOut());
    }
    
    public Vector3 mul(Vector3 o, Vector3 out)
    {
        return mul(o.x(), o.y(), o.z(), out);
    }
    
    public Vector3 mul(Vector3 o)
    {
        return mul(o.x(), o.y(), o.z(), defaultOut());
    }
    
    public Vector3 div(double x, double y, double z, Vector3 out)
    {
        if (out == null) out = new Vector3();
        out.x(x() / x);
        out.y(y() / y);
        out.z(z() / z);
        return out;
    }
    
    public Vector3 div(double x, double y, double z)
    {
        return div(x, y, z, defaultOut());
    }
    
    public Vector3 div(double amount, Vector3 out)
    {
        return div(amount, amount, amount, out);
    }
    
    public Vector3 div(double amount)
    {
        return div(amount, amount, amount, defaultOut());
    }
    
    public Vector3 div(Vector3 o, Vector3 out)
    {
        return div(o.x(), o.y(), o.z(), out);
    }
    
    public Vector3 div(Vector3 o)
    {
        return div(o.x(), o.y(), o.z(), defaultOut());
    }
    
    public double mag()
    {
        return Math.sqrt(magSq());
    }
    
    public Vector3 mag(double mag, Vector3 out)
    {
        double newMag = mag / mag();
        out.x(x() * newMag);
        out.y(y() * newMag);
        out.z(z() * newMag);
        return out;
    }
    
    public Vector3 mag(double mag)
    {
        return mag(mag, defaultOut());
    }
    
    public double magSq()
    {
        return x() * x() + y() * y() + z() * z();
    }
    
    public double dot(double x, double y, double z)
    {
        return x() * x + y() * y + z() * z;
    }
    
    public double dot(Vector3 o)
    {
        return dot(o.x(), o.y(), o.z());
    }
    
    public Vector3 cross(double x, double y, double z, Vector3 out)
    {
        if (out == null) out = new Vector3();
        out.x(y() * z - z() * y);
        out.y(z() * x - x() * z);
        out.z(x() * y - y() * x);
        return out;
    }
    
    public Vector3 cross(double x, double y, double z)
    {
        return cross(x, y, z, defaultOut());
    }
    
    public Vector3 cross(Vector3 o, Vector3 out)
    {
        return cross(o.x(), o.y(), o.z(), out);
    }
    
    public Vector3 cross(Vector3 o)
    {
        return cross(o.x(), o.y(), o.z(), defaultOut());
    }
    
    public double distSq(double x, double y, double z)
    {
        return (x - x()) * (x - x()) + (y - y()) * (y - y()) + (z - z()) * (z - z());
    }
    
    public double distSq(Vector3 o)
    {
        return distSq(o.x(), o.y(), o.z());
    }
    
    public double dist(double x, double y, double z)
    {
        return Math.sqrt(distSq(x, y, z));
    }
    
    public double dist(Vector3 o)
    {
        return dist(o.x(), o.y(), o.z());
    }
    
    public double heading()
    {
        return Math.atan2(y(), x());
    }
    
    public double pitch()
    {
        return Math.atan2(z(), Math.sqrt(x() * x() + y() * y()));
    }
    
    public double angle(Vector3 o)
    {
        return Math.acos(dot(o) / (mag() * o.mag()));
    }
    
    public Vector3 normalize(Vector3 out)
    {
        if (out == null) out = new Vector3();
        double mag = mag();
        out.x(x() / mag);
        out.y(y() / mag);
        out.z(z() / mag);
        return out;
    }
    
    public Vector3 normalize()
    {
        return normalize(defaultOut());
    }
    
    public Vector3 limit(double amount, Vector3 out)
    {
        if (out == null) out = new Vector3();
        if (magSq() > amount * amount) mag(amount, out);
        return out;
    }
    
    public Vector3 limit(double amount)
    {
        return limit(amount, defaultOut());
    }
    
    public Vector3 lerp(double x, double y, double z, double amount, Vector3 out)
    {
        if (out == null) out = new Vector3();
        x = (x - x()) * amount;
        y = (y - y()) * amount;
        z = (z - z()) * amount;
        return add(x, y, z, out);
    }
    
    public Vector3 lerp(double x, double y, double z, double amount)
    {
        return lerp(x, y, z, amount, defaultOut());
    }
    
    public Vector3 lerp(Vector3 o, double amount, Vector3 out)
    {
        return lerp(o.x(), o.y(), o.z(), amount, out);
    }
    
    public Vector3 lerp(Vector3 o, double amount)
    {
        return lerp(o.x(), o.y(), o.z(), amount, defaultOut());
    }
    
    public Vector3 floor(Vector3 out)
    {
        if (out == null) out = new Vector3();
        out.x(Math.floor(x()));
        out.y(Math.floor(y()));
        out.z(Math.floor(z()));
        return out;
    }
    
    public Vector3 floor()
    {
        return floor(defaultOut());
    }
    
    public Vector3 ceil(Vector3 out)
    {
        if (out == null) out = new Vector3();
        out.x(Math.ceil(x()));
        out.y(Math.ceil(y()));
        out.z(Math.ceil(z()));
        return out;
    }
    
    public Vector3 ceil()
    {
        return ceil(defaultOut());
    }
    
    public Vector3 round(int places, Vector3 out)
    {
        if (out == null) out = new Vector3();
        out.x(PixelEngine.round(x(), places));
        out.y(PixelEngine.round(y(), places));
        out.z(PixelEngine.round(z(), places));
        return out;
    }
    
    public Vector3 round(int places)
    {
        return round(places, defaultOut());
    }
    
    public static Vector3 fromAngle(double angle, double pitch, Vector3 out)
    {
        double xy = Math.cos(pitch);
        if (out == null) out = new Vector3();
        return out.set(xy * Math.cos(angle), xy * Math.sin(angle), Math.sin(pitch));
    }
    
    public static Vector3 fromAngle(double angle, double pitch)
    {
        return fromAngle(angle, pitch, null);
    }
    
    public static Vector3 random()
    {
        double x = PixelEngine.random(-1.0, 1.0);
        double y = PixelEngine.random(-1.0, 1.0);
        double z = PixelEngine.random(-1.0, 1.0);
        return new Vector3(x, y, z).normalize();
    }
}
