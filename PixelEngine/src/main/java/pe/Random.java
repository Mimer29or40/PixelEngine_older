package pe;

import java.util.Collection;

@SuppressWarnings({"unused", "StatementWithEmptyBody"})
public class Random extends java.util.Random
{
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
}
