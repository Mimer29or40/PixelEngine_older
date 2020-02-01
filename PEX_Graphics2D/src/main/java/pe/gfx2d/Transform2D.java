package pe.gfx2d;

@SuppressWarnings("ParameterCanBeLocal")
public class Transform2D
{
    private float[][][] matrix = new float[4][3][3];
    private int         tMatrix, sMatrix;
    private boolean dirty;
    
    public Transform2D()
    {
        this.reset();
    }
    
    public void reset()
    {
        this.tMatrix = 0;
        this.sMatrix = 1;
        this.dirty = true;
        
        // Columns Then Rows
        
        // Matrices 0 & 1 are used as swaps in Transform accumulation
        this.matrix[0][0][0] = 1.0F;
        this.matrix[0][1][0] = 0.0F;
        this.matrix[0][2][0] = 0.0F;
        this.matrix[0][0][1] = 0.0F;
        this.matrix[0][1][1] = 1.0F;
        this.matrix[0][2][1] = 0.0F;
        this.matrix[0][0][2] = 0.0F;
        this.matrix[0][1][2] = 0.0F;
        this.matrix[0][2][2] = 1.0F;
        
        this.matrix[1][0][0] = 1.0F;
        this.matrix[1][1][0] = 0.0F;
        this.matrix[1][2][0] = 0.0F;
        this.matrix[1][0][1] = 0.0F;
        this.matrix[1][1][1] = 1.0F;
        this.matrix[1][2][1] = 0.0F;
        this.matrix[1][0][2] = 0.0F;
        this.matrix[1][1][2] = 0.0F;
        this.matrix[1][2][2] = 1.0F;
        
        // Matrix 2 is a cache matrix to hold the immediate transform operation
        // Matrix 3 is a cache matrix to hold the inverted transform
    }
    
    public void rotate(float theta)
    {
        // Construct Rotation Matrix
        this.matrix[2][0][0] = (float) Math.cos(theta);
        this.matrix[2][1][0] = (float) Math.sin(theta);
        this.matrix[2][2][0] = 0.0F;
        this.matrix[2][0][1] = -(float) Math.sin(theta);
        this.matrix[2][1][1] = (float) Math.cos(theta);
        this.matrix[2][2][1] = 0.0F;
        this.matrix[2][0][2] = 0.0F;
        this.matrix[2][1][2] = 0.0F;
        this.matrix[2][2][2] = 1.0F;
        
        this.multiply();
    }
    
    public void scale(float sx, float sy)
    {
        // Construct Scale Matrix
        this.matrix[2][0][0] = sx;
        this.matrix[2][1][0] = 0.0F;
        this.matrix[2][2][0] = 0.0F;
        this.matrix[2][0][1] = 0.0F;
        this.matrix[2][1][1] = sy;
        this.matrix[2][2][1] = 0.0F;
        this.matrix[2][0][2] = 0.0F;
        this.matrix[2][1][2] = 0.0F;
        this.matrix[2][2][2] = 1.0F;
        
        this.multiply();
    }
    
    public void shear(float sx, float sy)
    {
        // Construct Shear Matrix
        this.matrix[2][0][0] = 1.0F;
        this.matrix[2][1][0] = sx;
        this.matrix[2][2][0] = 0.0F;
        this.matrix[2][0][1] = sy;
        this.matrix[2][1][1] = 1.0F;
        this.matrix[2][2][1] = 0.0F;
        this.matrix[2][0][2] = 0.0F;
        this.matrix[2][1][2] = 0.0F;
        this.matrix[2][2][2] = 1.0F;
        
        this.multiply();
    }
    
    public void translate(float ox, float oy)
    {
        // Construct Translate Matrix
        this.matrix[2][0][0] = 1.0F;
        this.matrix[2][1][0] = 0.0F;
        this.matrix[2][2][0] = ox;
        this.matrix[2][0][1] = 0.0F;
        this.matrix[2][1][1] = 1.0F;
        this.matrix[2][2][1] = oy;
        this.matrix[2][0][2] = 0.0F;
        this.matrix[2][1][2] = 0.0F;
        this.matrix[2][2][2] = 1.0F;
        
        this.multiply();
    }
    
    public void perspective(float ox, float oy)
    {
        // Construct Translate Matrix
        this.matrix[2][0][0] = 1.0F;
        this.matrix[2][1][0] = 0.0F;
        this.matrix[2][2][0] = 0.0F;
        this.matrix[2][0][1] = 0.0F;
        this.matrix[2][1][1] = 1.0F;
        this.matrix[2][2][1] = 0.0F;
        this.matrix[2][0][2] = ox;
        this.matrix[2][1][2] = oy;
        this.matrix[2][2][2] = 1.0F;
        
        this.multiply();
    }
    
    public float forward_x(float in_x, float in_y, float out_x)
    {
        out_x = in_x * this.matrix[this.sMatrix][0][0] + in_y * this.matrix[this.sMatrix][1][0] + this.matrix[this.sMatrix][2][0];
        float out_z = in_x * this.matrix[this.sMatrix][0][2] + in_y * this.matrix[this.sMatrix][1][2] + this.matrix[this.sMatrix][2][2];
        
        if (out_z != 0) return out_x / out_z;
        return out_x;
    }
    
    public float forward_y(float in_x, float in_y, float out_y)
    {
        out_y = in_x * this.matrix[this.sMatrix][0][1] + in_y * this.matrix[this.sMatrix][1][1] + this.matrix[this.sMatrix][2][1];
        float out_z = in_x * this.matrix[this.sMatrix][0][2] + in_y * this.matrix[this.sMatrix][1][2] + this.matrix[this.sMatrix][2][2];
        
        if (out_z != 0) return out_y / out_z;
        return out_y;
    }
    
    public float backward_x(float in_x, float in_y, float out_x)
    {
        out_x = in_x * this.matrix[3][0][0] + in_y * this.matrix[3][1][0] + this.matrix[3][2][0];
        float out_z = in_x * this.matrix[3][0][2] + in_y * this.matrix[3][1][2] + this.matrix[3][2][2];
        
        if (out_z != 0) return out_x / out_z;
        return out_x;
    }
    
    public float backward_y(float in_x, float in_y, float out_y)
    {
        out_y = in_x * this.matrix[3][0][1] + in_y * this.matrix[3][1][1] + this.matrix[3][2][1];
        float out_z = in_x * this.matrix[3][0][2] + in_y * this.matrix[3][1][2] + this.matrix[3][2][2];
        
        if (out_z != 0) return out_y / out_z;
        return out_y;
    }
    
    public void invert()
    {
        if (this.dirty) // Obviously costly so only do if needed
        {
            float det = this.matrix[this.sMatrix][0][0] *
                        (matrix[this.sMatrix][1][1] * this.matrix[this.sMatrix][2][2] - this.matrix[this.sMatrix][1][2] * this.matrix[this.sMatrix][2][1]) -
                        this.matrix[this.sMatrix][1][0] *
                        (matrix[this.sMatrix][0][1] * this.matrix[this.sMatrix][2][2] - this.matrix[this.sMatrix][2][1] * this.matrix[this.sMatrix][0][2]) +
                        this.matrix[this.sMatrix][2][0] *
                        (matrix[this.sMatrix][0][1] * this.matrix[this.sMatrix][1][2] - this.matrix[this.sMatrix][1][1] * this.matrix[this.sMatrix][0][2]);
            
            float detI = 1.0F / det;
            this.matrix[3][0][0] = (matrix[this.sMatrix][1][1] * this.matrix[this.sMatrix][2][2] - this.matrix[this.sMatrix][1][2] * this.matrix[this.sMatrix][2][1]) * detI;
            this.matrix[3][1][0] = (matrix[this.sMatrix][2][0] * this.matrix[this.sMatrix][1][2] - this.matrix[this.sMatrix][1][0] * this.matrix[this.sMatrix][2][2]) * detI;
            this.matrix[3][2][0] = (matrix[this.sMatrix][1][0] * this.matrix[this.sMatrix][2][1] - this.matrix[this.sMatrix][2][0] * this.matrix[this.sMatrix][1][1]) * detI;
            this.matrix[3][0][1] = (matrix[this.sMatrix][2][1] * this.matrix[this.sMatrix][0][2] - this.matrix[this.sMatrix][0][1] * this.matrix[this.sMatrix][2][2]) * detI;
            this.matrix[3][1][1] = (matrix[this.sMatrix][0][0] * this.matrix[this.sMatrix][2][2] - this.matrix[this.sMatrix][2][0] * this.matrix[this.sMatrix][0][2]) * detI;
            this.matrix[3][2][1] = (matrix[this.sMatrix][0][1] * this.matrix[this.sMatrix][2][0] - this.matrix[this.sMatrix][0][0] * this.matrix[this.sMatrix][2][1]) * detI;
            this.matrix[3][0][2] = (matrix[this.sMatrix][0][1] * this.matrix[this.sMatrix][1][2] - this.matrix[this.sMatrix][0][2] * this.matrix[this.sMatrix][1][1]) * detI;
            this.matrix[3][1][2] = (matrix[this.sMatrix][0][2] * this.matrix[this.sMatrix][1][0] - this.matrix[this.sMatrix][0][0] * this.matrix[this.sMatrix][1][2]) * detI;
            this.matrix[3][2][2] = (matrix[this.sMatrix][0][0] * this.matrix[this.sMatrix][1][1] - this.matrix[this.sMatrix][0][1] * this.matrix[this.sMatrix][1][0]) * detI;
            this.dirty = false;
        }
    }
    
    private void multiply()
    {
        for (int c = 0; c < 3; c++)
        {
            for (int r = 0; r < 3; r++)
            {
                this.matrix[this.tMatrix][c][r] = this.matrix[2][0][r] * this.matrix[this.sMatrix][c][0] +
                                                  this.matrix[2][1][r] * this.matrix[this.sMatrix][c][1] +
                                                  this.matrix[2][2][r] * this.matrix[this.sMatrix][c][2];
            }
        }
        
        int temp = this.tMatrix;
        this.tMatrix = this.sMatrix;
        this.sMatrix = temp;
        this.dirty = true; // Any transform multiply dirties the inversion
    }
}
