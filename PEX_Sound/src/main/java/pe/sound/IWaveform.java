package pe.sound;

public interface IWaveform
{
    double getMagnitude();
    
    double generate(double time, int scale);
}
