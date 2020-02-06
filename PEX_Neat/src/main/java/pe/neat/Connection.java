package pe.neat;

public class Connection
{
    public final Node in, out;
    
    public double  weight;
    public boolean enabled;
    
    public int innovation;
    
    public Connection(Node in, Node out, double weight, int innovation)
    {
        this.in = in;
        this.out = out;
        
        this.weight = weight;
        this.enabled = true;
        
        this.innovation = innovation;
    }
}
