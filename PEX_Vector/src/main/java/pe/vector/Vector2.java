package pe.vector;

import pe.PixelEngine;
import pe.util.Pair;

import java.util.Objects;

import static pe.PixelEngine.format;
import static pe.PixelEngine.getFormatNumbers;

@SuppressWarnings({"unused", "CopyConstructorMissesField", "UnusedReturnValue"})
public class Vector2
{
    public static final Vector2 X = new Vector2(1, 0).markImmutable();
    public static final Vector2 Y = new Vector2(0, 1).markImmutable();
    
    public static final Vector2 ZERO = new Vector2(0, 0).markImmutable();
    public static final Vector2 ONE  = new Vector2(1, 1).markImmutable();
    
    protected double x, y;
    
    private boolean immutable;
    
    public Vector2(double x, double y)
    {
        x(x).y(y);
    }
    
    public Vector2()
    {
        this(0, 0);
    }
    
    public Vector2(Vector2 o)
    {
        this(o.x(), o.y());
    }
    
    public Vector2(double[] array)
    {
        this(array[0], array[1]);
    }
    
    @Override
    public String toString()
    {
        Pair<Integer, Integer> numbers = getFormatNumbers(new double[] {x(), y()});
        
        return String.format("[%s, %s]", format(x(), numbers), format(y(), numbers));
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Vector2)) return false;
        Vector2 v = (Vector2) o;
        return equals(v.x(), v.y());
    }
    
    public boolean equals(double x, double y)
    {
        return Double.compare(x, x()) == 0 && Double.compare(y, y()) == 0;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(x(), y());
    }
    
    public boolean isImmutable()
    {
        return this.immutable;
    }
    
    public Vector2 markImmutable()
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
    
    public Vector2 x(double x)
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
    
    public Vector2 y(double y)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Vector is Immutable");
        this.y = y;
        return this;
    }
    
    public Vector2 set(double x, double y)
    {
        return x(x).y(y);
    }
    
    public Vector2 set(Vector2 o)
    {
        return set(o.x(), o.y());
    }
    
    public Vector2 set(double[] array)
    {
        return set(array[0], array[1]);
    }
    
    public double[] toArray()
    {
        return new double[] {x(), y()};
    }
    
    public Vector2 copy()
    {
        return new Vector2(this);
    }
    
    protected Vector2 defaultOut()
    {
        return isImmutable() ? new Vector2() : this;
    }
    
    public Vector2 add(double x, double y, Vector2 out)
    {
        if (out == null) out = new Vector2();
        out.x(x() + x);
        out.y(y() + y);
        return out;
    }
    
    public Vector2 add(double x, double y)
    {
        return add(x, y, defaultOut());
    }
    
    public Vector2 add(double amount, Vector2 out)
    {
        return add(amount, amount, out);
    }
    
    public Vector2 add(double amount)
    {
        return add(amount, amount, defaultOut());
    }
    
    public Vector2 add(Vector2 o, Vector2 out)
    {
        return add(o.x(), o.y(), out);
    }
    
    public Vector2 add(Vector2 o)
    {
        return add(o.x(), o.y(), defaultOut());
    }
    
    public Vector2 sub(double x, double y, Vector2 out)
    {
        if (out == null) out = new Vector2();
        out.x(x() - x);
        out.y(y() - y);
        return out;
    }
    
    public Vector2 sub(double x, double y)
    {
        return sub(x, y, defaultOut());
    }
    
    public Vector2 sub(double amount, Vector2 out)
    {
        return sub(amount, amount, out);
    }
    
    public Vector2 sub(double amount)
    {
        return sub(amount, amount, defaultOut());
    }
    
    public Vector2 sub(Vector2 o, Vector2 out)
    {
        return sub(o.x(), o.y(), out);
    }
    
    public Vector2 sub(Vector2 o)
    {
        return sub(o.x(), o.y(), defaultOut());
    }
    
    public Vector2 mul(double x, double y, Vector2 out)
    {
        if (out == null) out = new Vector2();
        out.x(x() * x);
        out.y(y() * y);
        return out;
    }
    
    public Vector2 mul(double x, double y)
    {
        return mul(x, y, defaultOut());
    }
    
    public Vector2 mul(double amount, Vector2 out)
    {
        return mul(amount, amount, out);
    }
    
    public Vector2 mul(double amount)
    {
        return mul(amount, amount, defaultOut());
    }
    
    public Vector2 mul(Vector2 o, Vector2 out)
    {
        return mul(o.x(), o.y(), out);
    }
    
    public Vector2 mul(Vector2 o)
    {
        return mul(o.x(), o.y(), defaultOut());
    }
    
    public Vector2 div(double x, double y, Vector2 out)
    {
        if (out == null) out = new Vector2();
        out.x(x() / x);
        out.y(y() / y);
        return out;
    }
    
    public Vector2 div(double x, double y)
    {
        return div(x, y, defaultOut());
    }
    
    public Vector2 div(double amount, Vector2 out)
    {
        return div(amount, amount, out);
    }
    
    public Vector2 div(double amount)
    {
        return div(amount, amount, defaultOut());
    }
    
    public Vector2 div(Vector2 o, Vector2 out)
    {
        return div(o.x(), o.y(), out);
    }
    
    public Vector2 div(Vector2 o)
    {
        return div(o.x(), o.y(), defaultOut());
    }
    
    public double mag()
    {
        return Math.sqrt(magSq());
    }
    
    public Vector2 mag(double mag, Vector2 out)
    {
        if (out == null) out = new Vector2();
        double newMag = mag / mag();
        out.x(x() * newMag);
        out.y(y() * newMag);
        return out;
    }
    
    public Vector2 mag(double mag)
    {
        return mag(mag, defaultOut());
    }
    
    public double magSq()
    {
        return x() * x() + y() * y();
    }
    
    public double dot(double x, double y)
    {
        return x() * x + y() * y;
    }
    
    public double dot(Vector2 o)
    {
        return dot(o.x(), o.y());
    }
    
    public double cross(double x, double y)
    {
        return x() * x - y() * y;
    }
    
    public double cross(Vector2 o)
    {
        return cross(o.x(), o.y());
    }
    
    public double distSq(double x, double y)
    {
        return (x - x()) * (x - x()) + (y - y()) * (y - y());
    }
    
    public double distSq(Vector2 o)
    {
        return distSq(o.x(), o.y());
    }
    
    public double dist(double x, double y)
    {
        return Math.sqrt(distSq(x, y));
    }
    
    public double dist(Vector2 o)
    {
        return dist(o.x(), o.y());
    }
    
    public double heading()
    {
        return Math.atan2(y(), x());
    }
    
    public double angle(Vector2 o)
    {
        return Math.acos(dot(o) / (mag() * o.mag()));
    }
    
    public Vector2 normalize(Vector2 out)
    {
        if (out == null) out = new Vector2();
        double mag = mag();
        out.x(x() / mag);
        out.y(y() / mag);
        return out;
    }
    
    public Vector2 normalize()
    {
        return normalize(defaultOut());
    }
    
    public Vector2 limit(double amount, Vector2 out)
    {
        if (out == null) out = new Vector2();
        if (magSq() > amount * amount) mag(amount, out);
        return out;
    }
    
    public Vector2 limit(double amount)
    {
        return limit(amount, defaultOut());
    }
    
    public Vector2 invert(Vector2 out)
    {
        if (out == null) out = new Vector2();
        double temp = -x();
        out.x(y());
        out.y(temp);
        return out;
    }
    
    public Vector2 invert()
    {
        return invert(defaultOut());
    }
    
    public Vector2 lerp(double x, double y, double amount, Vector2 out)
    {
        if (out == null) out = new Vector2();
        x = (x - x()) * amount;
        y = (y - y()) * amount;
        return add(x, y, out);
    }
    
    public Vector2 lerp(double x, double y, double amount)
    {
        return lerp(x, y, amount, defaultOut());
    }
    
    public Vector2 lerp(Vector2 o, double amount, Vector2 out)
    {
        return lerp(o.x(), o.y(), amount, out);
    }
    
    public Vector2 lerp(Vector2 o, double amount)
    {
        return lerp(o.x(), o.y(), amount, defaultOut());
    }
    
    public Vector2 floor(Vector2 out)
    {
        if (out == null) out = new Vector2();
        out.x(Math.floor(x()));
        out.y(Math.floor(y()));
        return out;
    }
    
    public Vector2 floor()
    {
        return floor(defaultOut());
    }
    
    public Vector2 ceil(Vector2 out)
    {
        if (out == null) out = new Vector2();
        out.x(Math.ceil(x()));
        out.y(Math.ceil(y()));
        return out;
    }
    
    public Vector2 ceil()
    {
        return ceil(defaultOut());
    }
    
    public Vector2 round(int places, Vector2 out)
    {
        if (out == null) out = new Vector2();
        out.x(PixelEngine.round(x(), places));
        out.y(PixelEngine.round(y(), places));
        return out;
    }
    
    public Vector2 round(int places)
    {
        return round(places, defaultOut());
    }
    
    public static Vector2 fromAngle(double angle, Vector2 out)
    {
        if (out == null) out = new Vector2();
        return out.set(Math.cos(angle), Math.sin(angle));
    }
    
    public static Vector2 fromAngle(double angle)
    {
        return fromAngle(angle, null);
    }
    
    public static Vector2 random()
    {
        double x = PixelEngine.random(-1.0, 1.0);
        double y = PixelEngine.random(-1.0, 1.0);
        return new Vector2(x, y).normalize();
    }
}
