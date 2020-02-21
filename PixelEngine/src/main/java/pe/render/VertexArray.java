package pe.render;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class VertexArray
{
    private final int                vao;
    private final ArrayList<Integer> vboList = new ArrayList<>();
    
    public VertexArray()
    {
        this.vao = glGenVertexArrays();
    }
    
    public VertexArray bind()
    {
        glBindVertexArray(this.vao);
        for (int i = 0, n = this.vboList.size(); i < n; i++) glEnableVertexAttribArray(i);
        return this;
    }
    
    public VertexArray unbind()
    {
        glBindVertexArray(this.vao);
        for (int i = 0, n = this.vboList.size(); i < n; i++) glDisableVertexAttribArray(i);
        glBindVertexArray(0);
        return this;
    }
    
    public VertexArray delete()
    {
        glDeleteVertexArrays(this.vao);
        for (int vbo : this.vboList) glDeleteBuffers(vbo);
        return this;
    }
    
    public VertexArray reset()
    {
        for (int vbo : this.vboList) glDeleteBuffers(vbo);
        this.vboList.clear();
        return this;
    }
    
    public VertexArray add(int size, int[] data, int usage)
    {
        int vbo = glGenBuffers();
        
        glBindVertexArray(this.vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, data, usage);
        
        glVertexAttribPointer(this.vboList.size(), size, GL_INT, false, 0, 0);
        glEnableVertexAttribArray(this.vboList.size());
        
        this.vboList.add(vbo);
        
        return this;
    }
    
    public VertexArray add(int size, int[] data)
    {
        return add(size, data, GL_STATIC_DRAW);
    }
    
    public VertexArray add(int size, float[] data, int usage)
    {
        int vbo = glGenBuffers();
        
        glBindVertexArray(this.vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, data, usage);
        
        glVertexAttribPointer(this.vboList.size(), size, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(this.vboList.size());
        
        this.vboList.add(vbo);
        
        return this;
    }
    
    public VertexArray add(int size, float[] data)
    {
        return add(size, data, GL_STATIC_DRAW);
    }
}
