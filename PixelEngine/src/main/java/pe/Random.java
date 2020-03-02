package pe;

import org.joml.*;

import java.util.Collection;

@SuppressWarnings({"unused", "StatementWithEmptyBody"})
public class Random extends java.util.Random
{
    public Random()
    {
        super();
    }
    
    public Random(long seed)
    {
        super(seed);
    }
    
    public int nextInt(int origin, int bound)
    {
        if (origin >= bound) throw new IllegalArgumentException("origin must be less than bound");
        return origin + nextInt(bound - origin);
    }
    
    public long nextLong(long bound)
    {
        if (bound <= 0) throw new IllegalArgumentException("bound must be positive");
        
        long r = nextLong();
        long m = bound - 1;
        if ((bound & m) == 0L) // power of two
        {
            r &= m;
        }
        else
        { // reject over-represented candidates
            for (long u = r >>> 1; u + m - (r = u % bound) < 0L; u = nextLong()) ;
        }
        return r;
    }
    
    public long nextLong(long origin, long bound)
    {
        if (origin >= bound) throw new IllegalArgumentException("origin must be less than bound");
        return origin + nextLong(bound - origin);
    }
    
    public float nextFloat(float bound)
    {
        if (bound <= 0) throw new IllegalArgumentException("bound must be positive");
        return nextFloat() * bound;
    }
    
    public float nextFloat(float origin, float bound)
    {
        if (origin >= bound) throw new IllegalArgumentException("origin must be less than bound");
        return origin + (bound - origin) * nextFloat();
    }
    
    public double nextDouble(double bound)
    {
        if (bound <= 0) throw new IllegalArgumentException("bound must be positive");
        return nextDouble() * bound;
    }
    
    public double nextDouble(double origin, double bound)
    {
        if (origin >= bound) throw new IllegalArgumentException("origin must be less than bound");
        return origin + (bound - origin) * nextDouble();
    }
    
    public int nextIndex(int[] array)
    {
        return array[nextInt(array.length)];
    }
    
    public long nextIndex(long[] array)
    {
        return array[nextInt(array.length)];
    }
    
    public float nextIndex(float[] array)
    {
        return array[nextInt(array.length)];
    }
    
    public double nextIndex(double[] array)
    {
        return array[nextInt(array.length)];
    }
    
    public <T> T nextIndex(T[] array)
    {
        return array[nextInt(array.length)];
    }
    
    public <T> T nextIndex(Collection<T> collection)
    {
        int index = nextInt(collection.size());
        for (T value : collection)
        {
            if (index == 0) return value;
            index--;
        }
        return null;
    }
    
    public int choose(int... options)
    {
        return nextIndex(options);
    }
    
    public long choose(long... options)
    {
        return nextIndex(options);
    }
    
    public float choose(float... options)
    {
        return nextIndex(options);
    }
    
    public double choose(double... options)
    {
        return nextIndex(options);
    }
    
    public Vector2i nextVector2i()
    {
        return new Vector2i(nextInt(), nextInt());
    }
    
    public Vector2i nextVector2i(int bound)
    {
        return new Vector2i(nextInt(bound), nextInt(bound));
    }
    
    public Vector2i nextVector2i(int origin, int bound)
    {
        return new Vector2i(nextInt(origin, bound), nextInt(origin, bound));
    }
    
    public Vector3i nextVector3i()
    {
        return new Vector3i(nextInt(), nextInt(), nextInt());
    }
    
    public Vector3i nextVector3i(int bound)
    {
        return new Vector3i(nextInt(bound), nextInt(bound), nextInt(bound));
    }
    
    public Vector3i nextVector3i(int origin, int bound)
    {
        return new Vector3i(nextInt(origin, bound), nextInt(origin, bound), nextInt(origin, bound));
    }
    
    public Vector4i nextVector4i()
    {
        return new Vector4i(nextInt(), nextInt(), nextInt(), nextInt());
    }
    
    public Vector4i nextVector4i(int bound)
    {
        return new Vector4i(nextInt(bound), nextInt(bound), nextInt(bound), nextInt(bound));
    }
    
    public Vector4i nextVector4i(int origin, int bound)
    {
        return new Vector4i(nextInt(origin, bound), nextInt(origin, bound), nextInt(origin, bound), nextInt(origin, bound));
    }
    
    public Vector2f nextVector2f()
    {
        return new Vector2f(nextFloat(), nextFloat());
    }
    
    public Vector2f nextVector2fUnit()
    {
        return new Vector2f(nextFloat(-1, 1), nextFloat(-1, 1)).normalize();
    }
    
    public Vector2f nextVector2f(float bound)
    {
        return new Vector2f(nextFloat(bound), nextFloat(bound));
    }
    
    public Vector2f nextVector2f(float origin, float bound)
    {
        return new Vector2f(nextFloat(origin, bound), nextFloat(origin, bound));
    }
    
    public Vector3f nextVector3f()
    {
        return new Vector3f(nextFloat(), nextFloat(), nextFloat());
    }
    
    public Vector3f nextVector3fUnit()
    {
        return new Vector3f(nextFloat(-1, 1), nextFloat(-1, 1), nextFloat(-1, 1)).normalize();
    }
    
    public Vector3f nextVector3f(float bound)
    {
        return new Vector3f(nextFloat(bound), nextFloat(bound), nextFloat(bound));
    }
    
    public Vector3f nextVector3f(float origin, float bound)
    {
        return new Vector3f(nextFloat(origin, bound), nextFloat(origin, bound), nextFloat(origin, bound));
    }
    
    public Vector4f nextVector4f()
    {
        return new Vector4f(nextFloat(), nextFloat(), nextFloat(), nextFloat());
    }
    
    public Vector4f nextVector4fUnit()
    {
        return new Vector4f(nextFloat(-1, 1), nextFloat(-1, 1), nextFloat(-1, 1), nextFloat(-1, 1)).normalize();
    }
    
    public Vector4f nextVector4f(float bound)
    {
        return new Vector4f(nextFloat(bound), nextFloat(bound), nextFloat(bound), nextFloat(bound));
    }
    
    public Vector4f nextVector4f(float origin, float bound)
    {
        return new Vector4f(nextFloat(origin, bound), nextFloat(origin, bound), nextFloat(origin, bound), nextFloat(origin, bound));
    }
    
    public Vector2d nextVector2d()
    {
        return new Vector2d(nextDouble(), nextDouble());
    }
    
    public Vector2d nextVector2dUnit()
    {
        return new Vector2d(nextDouble(-1, 1), nextDouble(-1, 1)).normalize();
    }
    
    public Vector2d nextVector2d(float bound)
    {
        return new Vector2d(nextDouble(bound), nextDouble(bound));
    }
    
    public Vector2d nextVector2d(float origin, float bound)
    {
        return new Vector2d(nextDouble(origin, bound), nextDouble(origin, bound));
    }
    
    public Vector3d nextVector3d()
    {
        return new Vector3d(nextDouble(), nextDouble(), nextDouble());
    }
    
    public Vector3d nextVector3dUnit()
    {
        return new Vector3d(nextDouble(-1, 1), nextDouble(-1, 1), nextDouble(-1, 1)).normalize();
    }
    
    public Vector3d nextVector3d(float bound)
    {
        return new Vector3d(nextDouble(bound), nextDouble(bound), nextDouble(bound));
    }
    
    public Vector3d nextVector3d(float origin, float bound)
    {
        return new Vector3d(nextDouble(origin, bound), nextDouble(origin, bound), nextDouble(origin, bound));
    }
    
    public Vector4d nextVector4d()
    {
        return new Vector4d(nextDouble(), nextDouble(), nextDouble(), nextDouble());
    }
    
    public Vector4d nextVector4dUnit()
    {
        return new Vector4d(nextDouble(-1, 1), nextDouble(-1, 1), nextDouble(-1, 1), nextDouble(-1, 1)).normalize();
    }
    
    public Vector4d nextVector4d(float bound)
    {
        return new Vector4d(nextDouble(bound), nextDouble(bound), nextDouble(bound), nextDouble(bound));
    }
    
    public Vector4d nextVector4d(float origin, float bound)
    {
        return new Vector4d(nextDouble(origin, bound), nextDouble(origin, bound), nextDouble(origin, bound), nextDouble(origin, bound));
    }
}
