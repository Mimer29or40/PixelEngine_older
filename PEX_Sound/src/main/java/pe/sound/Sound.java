package pe.sound;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.openal.AL10.alDeleteSources;

public abstract class Sound
{
    protected int sourceID = 0;
    
    protected int sampleRate = 0;
    protected int channels   = 0;
    protected int format     = 0;
    
    protected AtomicBoolean playing = new AtomicBoolean(false);
    protected AtomicBoolean paused  = new AtomicBoolean(false);
    
    public int getSampleRate()
    {
        return this.sampleRate;
    }
    
    public int getChannels()
    {
        return this.channels;
    }
    
    public int getFormat()
    {
        return this.format;
    }
    
    public boolean isPlaying()
    {
        return this.playing.get();
    }
    
    protected void setPlaying(boolean playing)
    {
        this.playing.set(playing);
    }
    
    public boolean isPaused()
    {
        return this.paused.get();
    }
    
    protected void setPaused(boolean paused)
    {
        this.paused.set(paused);
    }
    
    public abstract void play();
    
    public abstract void pause();
    
    public abstract void stop();
    
    public void destroy()
    {
        stop();
        alDeleteSources(this.sourceID);
    }
}
