package pe.sound;

import org.lwjgl.BufferUtils;
import pe.Logger;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.BufferUtils.zeroBuffer;
import static org.lwjgl.openal.AL10.*;

public class SoundGenerator
{
    private static final Logger LOGGER = Logger.getLogger();
    
    protected static final long   lSecond = 1_000_000_000L;
    protected static final double dSecond = (double) lSecond;
    
    protected int sourceID;
    
    protected boolean playing;
    
    protected int sampleRate;
    protected int channels;
    protected int format;
    
    protected int bufferCount;
    protected int bufferSamples;
    
    protected IntBuffer   names;
    protected ShortBuffer pcm;
    
    protected long globalTime;
    
    private ISample sampleFunc;
    
    private Thread thread;
    
    public SoundGenerator(int sampleRate, int channels, int bufferCount, int bufferSamples)
    {
        this.sourceID = alGenSources();
        
        this.playing = false;
        
        this.channels = channels;
        this.sampleRate = sampleRate;
        this.format = channels == 1 ? AL_FORMAT_MONO16 : channels == 2 ? AL_FORMAT_STEREO16 : 0;
        
        this.bufferCount = bufferCount;
        this.bufferSamples = bufferSamples;
        
        this.names = BufferUtils.createIntBuffer(bufferCount);
        this.pcm = BufferUtils.createShortBuffer(bufferSamples * channels);
        
        this.globalTime = 0;
        
        alGenBuffers(this.names);
        
        LOGGER.trace("Sound Generator Created (%s, %s, %s, %s)", sampleRate, channels, bufferCount, bufferSamples);
    }
    
    public boolean isPlaying()
    {
        return this.playing;
    }
    
    public int getSampleRate()
    {
        return this.sampleRate;
    }
    
    public int getSourceID()
    {
        return this.sourceID;
    }
    
    public int getChannels()
    {
        return this.channels;
    }
    
    public int getFormat()
    {
        return this.format;
    }
    
    public int getBufferCount()
    {
        return this.bufferCount;
    }
    
    public int getBufferSamples()
    {
        return this.bufferSamples;
    }
    
    public double getTime()
    {
        return (double) globalTime / dSecond;
    }
    
    public void sample(ISample sampleFunc)
    {
        this.sampleFunc = sampleFunc;
    }
    
    public void play()
    {
        if (isPlaying()) stop();
        
        this.playing = true;
        this.globalTime = 0L;
        
        zeroBuffer(this.pcm);
        this.pcm.clear();
        for (int i = 0; i < this.bufferCount; i++) alBufferData(this.names.get(i), this.format, this.pcm, this.sampleRate);
        this.pcm.clear();
        
        alSourceQueueBuffers(this.sourceID, this.names);
        alSourcePlay(this.sourceID);
        
        this.thread = new Thread(() -> {
            final short MAX_SAMPLE = Short.MAX_VALUE;
            final long  TIME_STEP  = lSecond / (long) this.sampleRate;
            while (isPlaying())
            {
                int processed = alGetSourcei(this.sourceID, AL_BUFFERS_PROCESSED);
                
                for (int i = 0; i < processed; i++)
                {
                    int buffer = alSourceUnqueueBuffers(this.sourceID);
                    
                    this.pcm.clear();
                    for (int s = 0; s < this.bufferSamples; s++)
                    {
                        for (int c = 0; c < this.channels; c++)
                        {
                            this.pcm.put(s * this.channels + c, (short) Math.round(clip(sample(c, getTime()), 1.0) * MAX_SAMPLE));
                        }
                        this.globalTime += TIME_STEP;
                    }
                    this.pcm.clear();
                    
                    alBufferData(buffer, this.format, this.pcm, this.sampleRate);
                    
                    alSourceQueueBuffers(this.sourceID, buffer);
                }
                if (alGetSourcei(this.sourceID, AL_SOURCE_STATE) != AL_PLAYING) alSourcePlay(this.sourceID);
            }
        }, "Sound Generator Thread");
        this.thread.start();
        
        LOGGER.trace("Sound Generator Started");
    }
    
    public void stop()
    {
        this.playing = false;
        
        alSourceStop(this.sourceID);
        
        try
        {
            if (this.thread != null) this.thread.join();
        }
        catch (InterruptedException ignored)
        {
        
        }
        
        LOGGER.trace("Sound Generator Stopped");
    }
    
    public void destroy()
    {
        stop();
        
        alDeleteSources(this.sourceID);
        alDeleteBuffers(this.names);
        
        this.names.clear();
        zeroBuffer(this.names);
        
        this.pcm.clear();
        zeroBuffer(this.pcm);
        
        LOGGER.trace("Sound Generator Destroyed");
    }
    
    protected double clip(double sample, double max)
    {
        return sample >= 0.0 ? Math.min(sample, max) : Math.max(sample, -max);
    }
    
    protected double sample(int channel, double time)
    {
        return this.sampleFunc != null ? this.sampleFunc.sample(channel, time) : 0.0;
    }
}
