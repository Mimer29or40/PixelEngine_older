package pe;

import org.lwjgl.BufferUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.BufferUtils.zeroBuffer;
import static org.lwjgl.stb.STBImage.stbi_info;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImageWrite.stbi_write_png;
import static pe.PixelEngine.getPath;
import static pe.PixelEngine.incOverdrawCount;

@SuppressWarnings("unused")
public class Sprite
{
    private final Color COLOR = new Color(0, 0, 0);
    
    protected int width      = 0;
    protected int height     = 0;
    protected int components = 4;
    
    protected Mode sampleMode = Mode.NORMAL;
    
    protected ByteBuffer data;
    
    private Sprite()
    {
    
    }
    
    public Sprite(int width, int height, Color initial)
    {
        this.width = width;
        this.height = height;
        this.data = BufferUtils.createByteBuffer(width * height * 4);
        
        int value = initial.toInt();
        for (int i = 0; i < width * height; i++)
        {
            this.data.putInt(4 * i, value);
        }
    }
    
    public Sprite(int width, int height)
    {
        this(width, height, Color.BLACK);
    }
    
    public static Sprite loadPGESprite(String imagePath)
    {
        Sprite sprite = new Sprite();
        
        try (FileInputStream in = new FileInputStream(getPath(imagePath).toString()))
        {
            sprite.width = in.read();
            sprite.height = in.read();
            sprite.components = in.read();
            sprite.data = BufferUtils.createByteBuffer(4 * sprite.width * sprite.height);
            
            for (int i = 0; in.available() > 0; i++)
            {
                sprite.data.putInt(4 * i, in.read());
            }
            sprite.data.flip();
            
            return sprite;
        }
        catch (IOException e)
        {
            System.err.println("PGE Sprite could not be loaded: " + imagePath);
        }
        
        sprite.width = 0;
        sprite.height = 0;
        sprite.components = 0;
        sprite.data = null;
        
        return sprite;
    }
    
    public static Sprite loadSprite(String imagePath)
    {
        Sprite sprite = new Sprite();
        
        int[] width      = new int[1];
        int[] height     = new int[1];
        int[] components = new int[1];
        
        if (stbi_info(getPath(imagePath).toString(), width, height, components))
        {
            sprite.width = width[0];
            sprite.height = height[0];
            sprite.components = components[0];
            sprite.data = stbi_load(getPath(imagePath).toString(), width, height, components, 4);
            
            if (sprite.data != null) return sprite;
        }
        
        System.err.println("PGE Sprite could not be loaded: " + imagePath);
        
        sprite.width = 0;
        sprite.height = 0;
        sprite.components = 0;
        sprite.data = null;
        
        return sprite;
    }
    
    public int getWidth()
    {
        return this.width;
    }
    
    public int getHeight()
    {
        return this.height;
    }
    
    public int getComponents()
    {
        return this.components;
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
        if (this.width != other.width || this.height != other.height) throw new RuntimeException("Sprites are not same size.");
        
        for (int j = 0; j < this.height; j++)
        {
            for (int i = 0; i < this.width; i++)
            {
                int index = 4 * (j * this.width + i);
                other.data.putInt(index, this.data.getInt(index));
            }
        }
    }
    
    public Color getPixel(int x, int y, Color out)
    {
        if (this.sampleMode == Mode.NORMAL)
        {
            if (0 <= x && x < this.width && 0 <= y && y < this.height)
            {
                return out.set(this.data.getInt(4 * (y * this.width + x)));
            }
            else
            {
                return out.set(0, 0, 0, 0);
            }
        }
        else
        {
            return out.set(this.data.getInt(4 * (Math.abs(y % this.height) * this.width + Math.abs(x % this.width))));
        }
    }
    
    public Color getPixel(int x, int y)
    {
        return getPixel(x, y, this.COLOR);
    }
    
    public void setPixel(int x, int y, Color p)
    {
        incOverdrawCount();
        
        if (0 <= x && x < this.width && 0 <= y && y < this.height)
        {
            this.data.putInt(4 * (y * this.width + x), p.toInt());
        }
    }
    
    public Color sample(float x, float y, Color out)
    {
        int sx = Math.min((int) (x * (float) this.width), this.width - 1);
        int sy = Math.min((int) (y * (float) this.height), this.height - 1);
        
        return getPixel(sx, sy, out);
    }
    
    public Color sample(float x, float y)
    {
        return sample(x, y, this.COLOR);
    }
    
    public Color sampleBL(float u, float v, Color out)
    {
        u = u * width - 0.5f;
        v = v * height - 0.5f;
        
        int x = (int) Math.floor(u);
        int y = (int) Math.floor(v);
        
        float u_ratio    = u - x;
        float v_ratio    = v - y;
        float u_opposite = 1 - u_ratio;
        float v_opposite = 1 - v_ratio;
        
        Color p1 = getPixel(Math.max(x, 0), Math.max(y, 0)).copy();
        Color p2 = getPixel(Math.min(x + 1, this.width - 1), Math.max(y, 0)).copy();
        Color p3 = getPixel(Math.max(x, 0), Math.min(y + 1, this.height - 1)).copy();
        Color p4 = getPixel(Math.min(x + 1, this.width - 1), Math.min(y + 1, this.height - 1)).copy();
        
        return out.set((p1.r() * u_opposite + p2.r() * u_ratio) * v_opposite + (p3.r() * u_opposite + p4.r() * u_ratio) * v_ratio,
                       (p1.g() * u_opposite + p2.g() * u_ratio) * v_opposite + (p3.g() * u_opposite + p4.g() * u_ratio) * v_ratio,
                       (p1.b() * u_opposite + p2.b() * u_ratio) * v_opposite + (p3.b() * u_opposite + p4.b() * u_ratio) * v_ratio);
        
    }
    
    public Color sampleBL(float u, float v)
    {
        return sampleBL(u, v, this.COLOR);
    }
    
    public void clear()
    {
        if (this.data != null)
        {
            this.data.clear();
            zeroBuffer(this.data);
        }
    }
    
    public void clear(Color p)
    {
        int color = p.toInt();
        
        for (int j = 0; j < this.height; j++)
        {
            for (int i = 0; i < this.width; i++)
            {
                this.data.putInt(4 * (j * this.width + i), color);
            }
        }
        
        incOverdrawCount(this.width * this.height);
    }
    
    public void savePGESprite(String imagePath)
    {
        if (this.data == null) return;
        
        if (!imagePath.endsWith(".pge")) imagePath += ".pge";
        
        try (FileOutputStream out = new FileOutputStream(imagePath))
        {
            out.write(this.width);
            out.write(this.height);
            out.write(this.components);
            for (int i = 0; i < this.width * this.height; i++)
            {
                out.write(this.data.getInt(4 * i));
            }
        }
        catch (IOException e)
        {
            System.err.println("PGE Sprite could not be saved: " + imagePath);
        }
    }
    
    public void saveSprite(String imagePath)
    {
        if (this.data == null) return;
        
        if (!imagePath.endsWith(".png")) imagePath += ".png";
        
        if (!stbi_write_png(getPath(imagePath).toString(), this.width, this.height, this.components, this.data, this.width * 4))
        {
            System.err.println("Sprite could not be saved: " + imagePath);
        }
    }
    
    public enum Mode
    {
        NORMAL, PERIODIC
    }
}
