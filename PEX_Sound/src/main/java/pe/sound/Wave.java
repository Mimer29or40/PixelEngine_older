package pe.sound;

import java.util.ArrayList;

import static pe.PixelEngine.nextDouble;

public abstract class Wave
{
    private static final ArrayList<Wave> WAVES = new ArrayList<>();
    
    public static final Wave SINE = new Wave("SINE")
    {
        @Override
        protected double generate(double time, double freq, double _freq, double custom)
        {
            return Math.sin(_freq);
        }
    };
    
    public static final Wave SQUARE = new Wave("SQUARE")
    {
        @Override
        protected double generate(double time, double freq, double _freq, double custom)
        {
            return Math.sin(_freq) > 0 ? 1.0 : -1.0;
        }
    };
    
    public static final Wave TRIANGLE = new Wave("TRIANGLE")
    {
        @Override
        protected double generate(double time, double freq, double _freq, double custom)
        {
            return Math.asin(Math.sin(_freq)) * TWO_OVER_PI;
        }
    };
    
    public static final Wave SAW_ANA = new Wave("SAW_ANA")
    {
        @Override
        protected double generate(double time, double freq, double _freq, double custom)
        {
            double total = 0.0;
            for (double i = 1; i < custom; i++)
            {
                total += Math.sin(i * _freq) / i;
            }
            return total * TWO_OVER_PI;
        }
    };
    
    public static final Wave SAW_DIG = new Wave("SAW_DIG")
    {
        @Override
        protected double generate(double time, double freq, double _freq, double custom)
        {
            return TWO_OVER_PI * (freq * Math.PI * (time % (1.0 / freq)) - HALF_PI);
        }
    };
    
    public static final Wave NOISE = new Wave("NOISE")
    {
        @Override
        protected double generate(double time, double freq, double _freq, double custom)
        {
            return nextDouble(-1.0, 1.0);
        }
    };
    
    private static final double TWO_PI      = 2.0 * Math.PI;
    private static final double HALF_PI     = 0.5 * Math.PI;
    private static final double TWO_OVER_PI = 2.0 / Math.PI;
    
    public static Wave fromName(String name)
    {
        for (Wave wave : WAVES) if (wave.name.equals(name)) return wave;
        return null;
    }
    
    public final String name;
    
    private Wave(String name)
    {
        this.name = name;
        WAVES.add(this);
    }
    
    protected abstract double generate(double time, double freq, double _freq, double custom);
    
    public double generate(double time, double freq, double lfoFreq, double lfoAmp, double custom)
    {
        double _freq = TWO_PI * freq * time + lfoAmp * freq * Math.sin(TWO_PI * freq * time);
        
        return generate(time, freq, _freq, custom);
    }
}
