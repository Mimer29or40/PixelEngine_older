package pe.render;

import org.lwjgl.opengl.GL;
import pe.Profiler;
import pe.Sprite;
import pe.Window;
import pe.color.Colorc;

import static org.lwjgl.opengl.GL43.*;
import static pe.PixelEngine.screenHeight;
import static pe.PixelEngine.screenWidth;

public class OpenGLRenderer extends Renderer
{
    private int framebuffer, texColorBuffer;
    
    private Shader      quadShader;
    private VertexArray quadArray;
    
    private VertexArray vertexArray;
    private Shader      lineShader;
    
    public OpenGLRenderer()
    {
    
    }
    
    @Override
    public void init()
    {
        Window.makeCurrent();
        
        GL.createCapabilities();
        
        int framebuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        
        texColorBuffer = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texColorBuffer);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, screenWidth(), screenHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);
        
        // attach it to currently bound framebuffer object
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texColorBuffer, 0);
        
        quadShader = new Shader().loadVertexFile("shader/pixel.vert").loadFragmentFile("shader/pixel.frag").validate();
        quadArray  = new VertexArray().add(2, new float[] {-1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F, 1.0F});
        
        glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        this.vertexArray = new VertexArray();
        
        this.lineShader = new Shader().loadVertexFile("shader/line.vert").loadGeometryFile("shader/line.geom").loadFragmentFile("shader/line.frag").validate();
    }
    
    @Override
    public void render(boolean update, Profiler profiler)
    {
    
        // second pass
        // glBindFramebuffer(GL_FRAMEBUFFER, 0); // back to default
        // // glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        // // glClear(GL_COLOR_BUFFER_BIT);
        // quadShader.bind();
        // quadArray.bind();
        // // glDisable(GL_DEPTH_TEST);
        // glBindTexture(GL_TEXTURE_2D, texColorBuffer);
        // glDrawArrays(GL_TRIANGLES, 0, 6);
        // quadArray.unbind();
    
        Window.swap();
    
        // glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        // glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        // glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // we're not using the stencil buffer now
        // glEnable(GL_DEPTH_TEST);
    }
    
    @Override
    public void clear(Colorc color)
    {
        glClearColor(color.rf(), color.gf(), color.bf(), color.af());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    @Override
    protected void point(int x, int y, Colorc color)
    {
        if (color.a() == 0) return;
    
        // self.vertex.set(p.base.xyz, GL_DYNAMIC_DRAW)
        //
        // self.point_shader.use()
        // self.point_shader.set_floatm('pv', self.view @self.proj)
        // self.point_shader.set_floatv('color', engine.stroke.to_gl())
        // self.point_shader.set_floatv('viewport', engine.viewport)
        // self.point_shader.set_float('thickness', engine.weight)
        
        glDrawArrays(GL_POINTS, 0, 1);
    }
    
    @Override
    public void line(int x1, int y1, int x2, int y2)
    {
        if (this.stroke.a() > 0)
        {
            float   width  = screenWidth();
            float   height = screenHeight();
            float[] data   = new float[] {(float) x1 / width, (float) y1 / height, 0, (float) x2 / width, (float) y2 / height, 0};
            // println(Arrays.toString(data));
            this.vertexArray.add(3, data, GL_DYNAMIC_DRAW);
            this.vertexArray.bind();
        
            this.lineShader.bind();
            // this.lineShader.set_floatm('pv', self.view @self.proj)
            this.lineShader.setColor("color", this.stroke);
            this.lineShader.setFloat("thickness", this.weight);
            this.lineShader.setVec2("viewport", screenWidth(), screenHeight());
        
            glDrawArrays(GL_LINES, 0, 2);
        }
    }
    
    @Override
    public void bezier(int x1, int y1, int x2, int y2, int x3, int y3)
    {
    
    }
    
    @Override
    public void circle(int x, int y, int radius)
    {
    
    }
    
    @Override
    public void ellipse(int x, int y, int w, int h)
    {
    
    }
    
    @Override
    public void rect(int x, int y, int w, int h)
    {
    
    }
    
    @Override
    public void triangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {
    
    }
    
    @Override
    public void partialSprite(int x, int y, Sprite sprite, int ox, int oy, int w, int h, double scale)
    {
    
    }
    
    @Override
    public void text(int x, int y, String text, double scale)
    {
    
    }
}
