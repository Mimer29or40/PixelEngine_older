package pe.render;

import pe.Sprite;
import pe.color.Colorc;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30.*;

@SuppressWarnings("unused")
public class Texture extends Sprite
{
    public final int id;
    
    public int wrapS     = GL_REPEAT;
    public int wrapT     = GL_REPEAT;
    public int minFilter = GL_NEAREST;
    public int magFilter = GL_NEAREST;
    
    private boolean firstUpload = true;
    
    protected Texture(int width, int height, int channels, ByteBuffer data)
    {
        super(width, height, channels, data);
        
        this.id = glGenTextures();
        bind();
        upload();
    }
    
    public Texture(int width, int height, int channels, Colorc initial)
    {
        super(width, height, channels, initial);
        
        this.id = glGenTextures();
        bind();
        upload();
    }
    
    public Texture(int width, int height, int channels)
    {
        super(width, height, channels);
        
        this.id = glGenTextures();
        bind();
        upload();
    }
    
    public Texture(int width, int height, Colorc initial)
    {
        super(width, height, initial);
        
        this.id = glGenTextures();
        bind();
        upload();
    }
    
    public Texture(int width, int height)
    {
        super(width, height);
        
        this.id = glGenTextures();
        bind();
        upload();
    }
    
    public Texture bind()
    {
        glBindTexture(GL_TEXTURE_2D, this.id);
        return this;
    }
    
    public Texture unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
        return this;
    }
    
    public Texture upload()
    {
        if (this.data != null)
        {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, this.wrapS);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, this.wrapT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, this.minFilter);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, this.magFilter);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            
            int format;
            switch (this.channels)
            {
                case 1:
                    format = GL_RED;
                    break;
                case 2:
                    format = GL_RG;
                    break;
                case 3:
                    format = GL_RGB;
                    break;
                default:
                    format = GL_RGBA;
                    break;
            }
            
            if (this.firstUpload)
            {
                glTexImage2D(GL_TEXTURE_2D, 0, format, this.width, this.height, 0, format, GL_UNSIGNED_BYTE, this.data);
                
                this.firstUpload = false;
            }
            else
            {
                glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, this.width, this.height, format, GL_UNSIGNED_BYTE, this.data);
            }
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        return this;
    }
    
    public static Texture loadSprite(String filePath)
    {
        Sprite sprite = Sprite.loadSprite(filePath);
        return new Texture(sprite.getWidth(), sprite.getHeight(), sprite.getChannels(), sprite.getData());
    }
    
    public static Texture loadImage(String imagePath, boolean flip)
    {
        Sprite sprite = Sprite.loadImage(imagePath, flip);
        return new Texture(sprite.getWidth(), sprite.getHeight(), sprite.getChannels(), sprite.getData());
    }
    
    public static Texture loadImage(String imagePath)
    {
        return loadImage(imagePath, false);
    }
}
