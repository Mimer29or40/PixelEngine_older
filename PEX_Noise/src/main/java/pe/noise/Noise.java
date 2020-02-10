package pe.noise;

import pe.Color;
import pe.Sprite;

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
    
    public Noise()
    {
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
    
    private static double fade(double t)
    {
        // 6t^5 - 15t^4 + 10t^3
        return t * t * t * (t * ((t * 6) - 15) + 10);
    }
    
    private static double lerp(double a0, double a1, double x)
    {
        return a0 + x * (a1 - a0);
    }
    
    public static class Perlin extends Noise
    {
        private int[][] grads;
        
        public Perlin(long seed)
        {
            super(seed);
        }
        
        public Perlin()
        {
            super();
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
            int dimension = 2;
            
            int x0 = (int) Math.floor(x);
            int y0 = (int) Math.floor(y);
            int x1 = x0 + 1;
            int y1 = y0 + 1;
            
            setSeed(x0, y0);
            int pMinMin = this.random.nextInt(2 * dimension);
            System.out.printf("x0: %s, y0: %s, pMinMin: %s\n", x0, y0, pMinMin);
            setSeed(x0, y1);
            int pMinMax = this.random.nextInt(2 * dimension);
            setSeed(x1, y0);
            int pMaxMin = this.random.nextInt(2 * dimension);
            setSeed(x1, y1);
            int pMaxMax = this.random.nextInt(2 * dimension);
            
            double sx  = x - x0;
            double sy  = y - y0;
            double dx  = 1.0 - sx;
            double dy  = 1.0 - sy;
            // double fsx = fade(sx);
            // double fsy = fade(sy);
            // double dsx = fade(dx);
            // double dsy = fade(dy);
            
            double n0, n1, ix0, ix1;
            
            n0 = sx * this.grads[pMinMin][0] + sy * this.grads[pMinMin][1];
            n1 = sx * this.grads[pMinMax][0] + dy * this.grads[pMinMax][1];
            ix0 = lerp(n0, n1, sx);
    
            n0 = dx * this.grads[pMaxMin][0] + sy * this.grads[pMaxMin][1];
            n1 = dx * this.grads[pMaxMax][0] + dy * this.grads[pMaxMax][1];
            ix1 = lerp(n0, n1, sx);
            
            return lerp(ix0, ix1, sy);
        }
    }
    
    public static void main(String[] args)
    {
        // double x = 0.5;
        // double y = 0.75;
        //
        // int   dim  = 2;
        // int[] dirs = new int[] {1, -1};
        //
        // int[][] grad = new int[dim * 2][dim];
        // for (int i = 0; i < dim; i++)
        // {
        //     for (int j = 0; j < dirs.length; j++)
        //     {
        //         for (int k = 0; k < dim; k++)
        //         {
        //             grad[i + j * dim][k] = k == i ? dirs[j] : 0;
        //         }
        //     }
        // }
        //
        // int xMin = (int) Math.floor(x);
        // int yMin = (int) Math.floor(y);
        // int xMax = xMin + 1;
        // int yMax = yMin + 1;
        //
        //
        // int pMinMin =
        
        int width  = 200;
        int height = 200;
        
        Perlin noise = new Perlin(0);
    
        Sprite sprite = new Sprite(width, height);
        for (int j = 0; j < height; j++)
        {
            double y = (double) j / height;
            
            for (int i = 0; i < width; i++)
            {
                double x = (double) i / width;
                
                sprite.setPixel(i, j, new Color(noise.generate(0.5, 0.75)));
            }
        }
        sprite.saveSprite("NOISE");
    }
}
