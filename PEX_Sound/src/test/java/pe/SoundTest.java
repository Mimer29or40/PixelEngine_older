package pe;

import pe.sound.SoundFile;

public class SoundTest extends PixelEngine
{
    SoundFile sound, sound2;
    
    @Override
    protected void setup()
    {
        size();
    
        sound  = new SoundFile("sample2.ogg");
        sound2 = new SoundFile("sample2.ogg");
    
        sound2.play();
    }
    
    @Override
    protected void draw(double elapsedTime)
    {
        if (Keyboard.SPACE.down()) sound.play();
        if (Keyboard.SPACE.up()) sound.stop();
    }
    
    public static void main(String[] args)
    {
        Logger.setLevel(Logger.Level.DEBUG);
        start(new SoundTest());
    }
}
