package pe.noise;

import java.util.Random;

public class Noise
{
    protected final Random random = new Random();
    
    public long   baseSeed    = 1;
    public int    octaves     = 1;
    public double persistence = 0.5;
    
    public Noise(long seed, int octaves, double persistence)
    {
        this.baseSeed = seed;
        this.octaves = Math.max(octaves, 1);
        this.persistence = persistence;
    }
    
    public Noise(long seed)
    {
        this.baseSeed = seed;
    }
    
    public Noise(int octaves)
    {
        this.octaves = octaves;
    }
    
    public Noise(double persistence)
    {
        this.persistence = persistence;
    }
    
    public double get(int x)
    {
        return 0.0;
    }
    
    protected long getBaseSeed()
    {
        return this.baseSeed;
    }
    
    protected void setSeed(double x, double y)
    {
        String seedStr = String.format("%s,%s:%s", x, y, getBaseSeed());
        long   seed    = 0;
        for (int i = 0, n = seedStr.length(); i < n; i++)
        {
            seed = (31 * seed + 17 * (long) seedStr.charAt(i));
        }
        this.random.setSeed(seed);
    }
    
    public double generate(double x, double y)
    {
        setup(2);
        
        double value = 0.0;
        double max   = 0.0;
        double amp   = 1.0;
        double freq  = 1.0;
        for (int i = 0; i < this.octaves; i++)
        {
            value += calculate(x, y, freq, amp) * amp;
            max += amp;
            amp *= this.persistence;
            freq *= 2.0;
        }
        value /= max;
        
        return Math.max(0.0, Math.min(value, 1.0));
    }
    
    public void setup(int dimensions)
    {
    
    }
    
    public double calculate(double x, double y, double frequency, double amplitude)
    {
        return 0.0;
    }
    
    public static class Perlin extends Noise
    {
        private int[][] grads;
        
        public Perlin(long seed)
        {
            super(seed);
        }
        
        @Override
        public void setup(int dimension)
        {
            int[] dirs = new int[] {1, -1};
            
            this.grads = new int[dimension * 2][dimension];
            for (int i = 0; i < dimension; i++)
            {
                for (int j = 0; j < dirs.length; j++)
                {
                    for (int k = 0; k < dimension; k++)
                    {
                        this.grads[i + j * dimension][k] = k == i ? dirs[j] : 0;
                    }
                }
            }
        }
        
        @Override
        public double calculate(double x, double y, double frequency, double amplitude)
        {
            int     dimension = 2;
            int[][] p         = new int[dimension][dimension];
            for (int i = 0; i < dimension; i++)
            {
                for (int j = 0; j < dimension; j++)
                {
                    setSeed(i, j);
                    p[i][j] = this.random.nextInt(dimension * 2);
                }
            }
            
            int xMin = (int) Math.floor(x);
            int yMin = (int) Math.floor(y);
            int xMax = xMin + 1;
            int yMax = yMin + 1;
            
            return 0.0;
        }
    }
    
    public static void main(String[] args)
    {
        int   dim  = 2;
        int[] dirs = new int[] {1, -1};
    
        int[][] grad = new int[dim * 2][dim];
        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dirs.length; j++)
            {
                for (int k = 0; k < dim; k++)
                {
                    grad[i + j * dim][k] = k == i ? dirs[j] : 0;
                }
            }
        }
        // int width  = 200;
        // int height = 200;
        //
        // Sprite sprite = new Sprite(width, height);
        // for (int j = 0; j < height; j++)
        // {
        //     for (int i = 0; i < width; i++)
        //     {
        //
        //     }
        // }
    }
}
