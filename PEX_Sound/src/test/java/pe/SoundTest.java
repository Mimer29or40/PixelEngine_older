package pe;

import pe.sound.SoundFile;

public class SoundTest extends PixelEngine
{
    SoundFile sound, sound2;
    
    @Override
    protected boolean onUserCreate()
    {
        sound = new SoundFile("sample2.ogg");
        sound2 = new SoundFile("sample2.ogg");
        
        sound2.play();
        
        return true;
    }
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        if (Keyboard.SPACE.down) sound.play();
        if (Keyboard.SPACE.up) sound.stop();
    
        return true;
    }
    
    public static void main(String[] args)
    {
        Logger.setLevel(Logger.Level.DEBUG);
        start(new SoundTest());
    }
}
