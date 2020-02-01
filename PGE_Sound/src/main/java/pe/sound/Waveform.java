package pe.sound;

public class Waveform implements IWaveform
{
    public Wave   wave;
    public int    offset;
    public double magnitude;
    
    public double lfoFreq = 0.0;
    public double lfoAmp  = 0.0;
    public double custom  = 50.0;
    
    public Waveform(Wave wave, int offset, double magnitude)
    {
        this.wave = wave;
        this.offset = offset;
        this.magnitude = magnitude;
    }
    
    public Waveform(Wave wave, int offset)
    {
        this(wave, offset, 1.0);
    }
    
    public double getMagnitude()
    {
        return this.magnitude;
    }
    
    public double generate(double time, int scale)
    {
        return getMagnitude() * this.wave.generate(time, 8.0 * Math.pow(1.0594630943592952645618252949463, scale + this.offset), this.lfoFreq, this.lfoAmp, this.custom);
    }
}
