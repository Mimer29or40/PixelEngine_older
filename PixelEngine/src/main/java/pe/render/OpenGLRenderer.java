package pe.render;

import pe.Profiler;
import pe.Sprite;
import pe.color.Colorc;

import static org.lwjgl.opengl.GL43.GL_POINTS;
import static org.lwjgl.opengl.GL43.glDrawArrays;

public class OpenGLRenderer extends Renderer
{
    // private final Shader
    
    @Override
    public void init()
    {
    
    }
    
    @Override
    public void render(boolean update, Profiler profiler)
    {
    
    }
    
    @Override
    protected void clearImpl()
    {
    
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
    public void string(int x, int y, String text, double scale)
    {
    
    }
}
