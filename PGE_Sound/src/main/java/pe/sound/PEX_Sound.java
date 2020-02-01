package pe.sound;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import pe.Logger;
import pe.PEX;
import pe.Profiler;

import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class PEX_Sound extends PEX
{
    private static final Logger LOGGER = Logger.getLogger();
    
    private static long device;
    private static long context;
    
    public PEX_Sound(Profiler profiler)
    {
        super(profiler);
    }
    
    public static void checkError()
    {
        int err = alcGetError(PEX_Sound.device);
        if (err != ALC_NO_ERROR) throw new RuntimeException(alcGetString(PEX_Sound.device, err));
    }
    
    public void initialize()
    {
        LOGGER.info("Initializing Sound System");
        
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        PEX_Sound.device = alcOpenDevice(defaultDeviceName);
        if (PEX_Sound.device == NULL) throw new IllegalStateException("Failed to open the default OpenAL device.");
        
        int[] attributes = {0};
        PEX_Sound.context = alcCreateContext(PEX_Sound.device, attributes);
        if (PEX_Sound.context == NULL) throw new IllegalStateException("Failed to create OpenAL context.");
        
        alcMakeContextCurrent(PEX_Sound.context);
        ALCCapabilities alcCapabilities = ALC.createCapabilities(PEX_Sound.device);
        ALCapabilities  alCapabilities  = AL.createCapabilities(alcCapabilities);
    }
    
    @Override
    public void beforeUserUpdate(double elapsedTime)
    {
    
    }
    
    @Override
    public void afterUserUpdate(double elapsedTime)
    {
    
    }
    
    @Override
    public void destroy()
    {
        alcDestroyContext(PEX_Sound.context);
        alcCloseDevice(PEX_Sound.device);
        
        LOGGER.info("Sound System Stopped");
    }
}
