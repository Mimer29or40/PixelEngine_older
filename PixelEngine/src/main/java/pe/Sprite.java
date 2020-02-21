package pe;

import org.lwjgl.BufferUtils;
import pe.color.Color;
import pe.color.Colorc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.BufferUtils.zeroBuffer;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImageWrite.stbi_write_png;
import static pe.PixelEngine.getPath;

@SuppressWarnings("unused")
public class Sprite
{
    protected static final Logger LOGGER = Logger.getLogger();
    
    protected final Color COLOR = new Color();
    
    protected final int width;
    protected final int height;
    protected final int channels;
    
    protected final ByteBuffer data;
    
    protected Mode sampleMode = Mode.NORMAL;
    
    protected Sprite(int width, int height, int channels, ByteBuffer data)
    {
        if (channels < 1 || 4 < channels) throw new RuntimeException("Sprites can only have 1-4 channels");
        
        this.width    = width;
        this.height   = height;
        this.channels = channels;
        
        this.data = data;
    }
    
    public Sprite(int width, int height, int channels, Colorc initial)
    {
        this(width, height, channels, BufferUtils.createByteBuffer(width * height * channels));
        
        for (int i = 0; i < width * height; i++)
        {
            for (int j = 0; j < this.channels; j++) this.data.put(i * channels + j, (byte) initial.getComponent(j));
        }
    }
    
    public Sprite(int width, int height, int channels)
    {
        this(width, height, channels, Color.BLACK);
    }
    
    public Sprite(int width, int height, Colorc initial)
    {
        this(width, height, 4, initial);
    }
    
    public Sprite(int width, int height)
    {
        this(width, height, 4, Color.BLACK);
    }
    
    public int getWidth()
    {
        return this.width;
    }
    
    public int getHeight()
    {
        return this.height;
    }
    
    public int getChannels()
    {
        return this.channels;
    }
    
    public Mode getSampleMode()
    {
        return sampleMode;
    }
    
    public void setSampleMode(Mode mode)
    {
        this.sampleMode = mode;
    }
    
    public ByteBuffer getData()
    {
        return this.data;
    }
    
    public void copy(Sprite other)
    {
        if (this.width != other.width || this.height != other.height || this.channels != other.channels) throw new RuntimeException("Sprites are not same size.");
    
        for (int i = 0, n = this.width * this.height * this.channels; i < n; i++) other.data.put(i, this.data.get(i));
    }
    
    public Colorc getPixel(int x, int y)
    {
        if (this.sampleMode == Mode.NORMAL)
        {
            if (0 <= x && x < this.width && 0 <= y && y < this.height)
            {
                int index = this.channels * (y * this.width + x);
                for (int i = 0; i < this.channels; i++) this.COLOR.setComponent(i, this.data.get(index + i));
                return this.COLOR;
            }
            else
            {
                return this.COLOR.set(0, 0);
            }
        }
        else
        {
            int index = this.channels * (Math.abs(y % this.height) * this.width + Math.abs(x % this.width));
            for (int i = 0; i < this.channels; i++) this.COLOR.setComponent(i, this.data.get(index + i));
            return this.COLOR;
        }
    }
    
    public void setPixel(int x, int y, Colorc color)
    {
        if (0 <= x && x < this.width && 0 <= y && y < this.height)
        {
            int index = this.channels * (y * this.width + x);
            for (int i = 0; i < this.channels; i++) this.data.put(index + i, (byte) color.getComponent(i));
        }
    }
    
    public Colorc sample(float x, float y)
    {
        int sx = Math.min((int) (x * (float) this.width), this.width - 1);
        int sy = Math.min((int) (y * (float) this.height), this.height - 1);
        
        return getPixel(sx, sy);
    }
    
    public Colorc sampleBL(float u, float v)
    {
        u = u * width - 0.5f;
        v = v * height - 0.5f;
        
        int x = (int) Math.floor(u);
        int y = (int) Math.floor(v);
        
        float u_ratio    = u - x;
        float v_ratio    = v - y;
        float u_opposite = 1 - u_ratio;
        float v_opposite = 1 - v_ratio;
        
        Color p1 = new Color(getPixel(Math.max(x, 0), Math.max(y, 0)));
        Color p2 = new Color(getPixel(Math.min(x + 1, this.width - 1), Math.max(y, 0)));
        Color p3 = new Color(getPixel(Math.max(x, 0), Math.min(y + 1, this.height - 1)));
        Color p4 = new Color(getPixel(Math.min(x + 1, this.width - 1), Math.min(y + 1, this.height - 1)));
        
        return this.COLOR.set((p1.r() * u_opposite + p2.r() * u_ratio) * v_opposite + (p3.r() * u_opposite + p4.r() * u_ratio) * v_ratio,
                              (p1.g() * u_opposite + p2.g() * u_ratio) * v_opposite + (p3.g() * u_opposite + p4.g() * u_ratio) * v_ratio,
                              (p1.b() * u_opposite + p2.b() * u_ratio) * v_opposite + (p3.b() * u_opposite + p4.b() * u_ratio) * v_ratio);
        
    }
    
    public void clear()
    {
        if (this.data != null)
        {
            this.data.clear();
            zeroBuffer(this.data);
        }
    }
    
    public void clear(Colorc color)
    {
        for (int i = 0; i < this.width * this.height; i++)
        {
            for (int j = 0; j < this.channels; j++) this.data.put(i * this.channels + j, (byte) color.getComponent(j));
        }
    }
    
    public void saveSprite(String filePath)
    {
        if (this.data == null) return;
        
        try (FileOutputStream out = new FileOutputStream(filePath))
        {
            out.write(this.width);
            out.write(this.height);
            out.write(this.channels);
            for (int i = 0; i < this.width * this.height * this.channels; i++) out.write(this.data.get(i));
        }
        catch (IOException e)
        {
            Sprite.LOGGER.error("Sprite could not be saved: " + filePath);
        }
    }
    
    public void saveImage(String imagePath)
    {
        if (this.data == null) return;
        
        if (!imagePath.endsWith(".png")) imagePath += ".png";
        
        if (!stbi_write_png(imagePath, this.width, this.height, this.channels, this.data, this.width * 4))
        {
            Sprite.LOGGER.error("Image could not be saved: " + imagePath);
        }
    }
    
    public static Sprite loadSprite(String filePath)
    {
        try (FileInputStream in = new FileInputStream(getPath(filePath).toString()))
        {
            int width    = in.read();
            int height   = in.read();
            int channels = in.read();
            
            ByteBuffer data = BufferUtils.createByteBuffer(width * height * channels);
            
            for (int i = 0; in.available() > 0; i++) data.put(i, (byte) in.read());
            
            return new Sprite(width, height, channels, data);
        }
        catch (IOException e)
        {
            Sprite.LOGGER.error("Sprite could not be loaded: " + filePath);
        }
        
        return new Sprite(0, 0, 0, (ByteBuffer) null);
    }
    
    public static Sprite loadImage(String imagePath, boolean flip)
    {
        String actualPath = getPath(imagePath).toString();
        
        stbi_set_flip_vertically_on_load(flip);
        
        int[] width    = new int[1];
        int[] height   = new int[1];
        int[] channels = new int[1];
        
        if (stbi_info(actualPath, width, height, channels))
        {
            return new Sprite(width[0], height[0], channels[0], stbi_load(actualPath, width, height, channels, 0));
        }
        else
        {
            Sprite.LOGGER.error("Failed to load Sprite: " + imagePath);
        }
        
        stbi_set_flip_vertically_on_load(false);
        
        return new Sprite(0, 0, 0, (ByteBuffer) null);
    }
    
    public static Sprite loadImage(String imagePath)
    {
        return loadImage(imagePath, false);
    }
    
    public enum Mode
    {
        NORMAL, PERIODIC
    }
}
