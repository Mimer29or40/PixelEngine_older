package pe.sound;

import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.libc.LibCStdlib.free;
import static pe.sound.PEX_Sound.checkError;

public class SoundFile extends Sound
{
    public SoundFile(String fileName)
    {
        try (MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer sampleRate = stack.mallocInt(1);
            IntBuffer channels   = stack.mallocInt(1);
            
            ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(fileName, channels, sampleRate);
            
            if (rawAudioBuffer != null)
            {
                this.sourceID = alGenSources();
                
                this.sampleRate = sampleRate.get();
                this.channels = channels.get();
                
                switch (this.channels)
                {
                    case 1:
                        this.format = AL_FORMAT_MONO16;
                        break;
                    case 2:
                        this.format = AL_FORMAT_STEREO16;
                        break;
                    default:
                        this.format = -1;
                }
                
                //Send the data to OpenAL
                int buffer = alGenBuffers();
                alBufferData(buffer, this.format, rawAudioBuffer, this.sampleRate);
                
                //StringBuilder string = new StringBuilder(rawAudioBuffer.get(0));
                //for (int i = 1; i < rawAudioBuffer.capacity(); i++)
                //{
                //    string.append(",").append(rawAudioBuffer.get(i));
                //}
                //print(string.toString());
                
                //Free the memory allocated by STB
                free(rawAudioBuffer);
                
                alSourcei(this.sourceID, AL_BUFFER, buffer);
                checkError();
                
                alDeleteBuffers(buffer);
                
                return;
            }
        }
        
        System.err.println("Sound could not be loaded.");
        
        alDeleteSources(this.sourceID);
        this.sourceID = 0;
        
        this.sampleRate = 0;
        this.channels = 0;
        this.format = -1;
    }
    
    public void play()
    {
        setPlaying(true);
        setPaused(false);
        
        alSourcePlay(this.sourceID);
        checkError();
    }
    
    public void pause()
    {
        setPaused(true);
        
        alSourcePause(this.sourceID);
        checkError();
    }
    
    public void stop()
    {
        setPlaying(false);
        setPaused(false);
        
        alSourceStop(this.sourceID);
        checkError();
    }
}
