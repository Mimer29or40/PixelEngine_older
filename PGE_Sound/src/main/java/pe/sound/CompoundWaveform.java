package pe.sound;

import java.util.ArrayList;

public class CompoundWaveform implements IWaveform
{
    public final ArrayList<IWaveform> waveforms = new ArrayList<>();
    
    protected double magnitude = 1.0;
    
    public void addWaveform(IWaveform waveform)
    {
        this.waveforms.add(waveform);
        double total = 0.0;
        for (IWaveform w : this.waveforms) total += w.getMagnitude();
        this.magnitude = 1.0 / total;
    }
    
    @Override
    public double getMagnitude()
    {
        return this.magnitude;
    }
    
    public double generate(double time, int scale)
    {
        double sound = 0.0;
        for (IWaveform w : this.waveforms) sound += w.generate(time, scale);
        return getMagnitude() * sound;
    }
}
