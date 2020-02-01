package pe.sound;

public class SignalGenerator extends SoundGenerator
{
    protected Wave   wave;
    protected double frequency;
    
    protected double lfoFreq = 0.0;
    protected double lfoAmp  = 0.0;
    protected double custom  = 50.0;
    
    public SignalGenerator(int sampleRate, int channels, int bufferCount, int bufferSamples, Wave wave, double frequency)
    {
        super(sampleRate, channels, bufferCount, bufferSamples);
        
        this.wave = wave;
        this.frequency = frequency;
    }
    
    public Wave getWave()
    {
        return this.wave;
    }
    
    public void setWave(Wave wave)
    {
        this.wave = wave;
    }
    
    public double getFrequency()
    {
        return this.frequency;
    }
    
    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }
    
    public double getLFOFreq()
    {
        return this.lfoFreq;
    }
    
    public void setLFOFreq(double lfoFreq)
    {
        this.lfoFreq = lfoFreq;
    }
    
    public double getLFOAmp()
    {
        return this.lfoAmp;
    }
    
    public void setLFOAmp(double lfoAmp)
    {
        this.lfoAmp = lfoAmp;
    }
    
    public double getCustom()
    {
        return this.custom;
    }
    
    public void setCustom(double custom)
    {
        this.custom = custom;
    }
    
    @Override
    protected double sample(int channel, double time)
    {
        return this.wave.generate(time, this.frequency, this.lfoFreq, this.lfoAmp, this.custom);
    }
}
