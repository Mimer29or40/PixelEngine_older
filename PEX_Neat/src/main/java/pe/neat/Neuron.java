package pe.neat;

import java.util.ArrayList;
import java.util.Objects;

public class Neuron
{
    public static final IActivator SIGMOID = value -> 1.0 / (1.0 + Math.exp(-4.9 * value));
    public static final IActivator STEP    = value -> value < 0.0 ? -1.0 : 1.0;
    
    public static IActivator activator = SIGMOID;
    
    private final ArrayList<Double> inputs = new ArrayList<>();
    public        double            output = 0.0;
    
    public final ArrayList<Integer> outputs = new ArrayList<>();
    public final ArrayList<Double>  weights = new ArrayList<>();
    
    @Override
    public String toString()
    {
        return "Neuron{inputs=" + this.inputs +
               ", output=" + this.output +
               ", outputs=" + this.outputs +
               ", weights=" + this.weights +
               '}';
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Neuron neuron = (Neuron) o;
        return Double.compare(this.output, neuron.output) == 0 &&
               Objects.equals(this.inputs, neuron.inputs) &&
               Objects.equals(this.outputs, neuron.outputs) &&
               Objects.equals(this.weights, neuron.weights);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(this.inputs, this.output, this.outputs, this.weights);
    }
    
    public void addInput()
    {
        this.inputs.add(null);
    }
    
    public void feedInput(double input)
    {
        for (int i = 0, n = this.inputs.size(); i < n; i++)
        {
            if (this.inputs.get(i) == null)
            {
                this.inputs.set(i, input);
                return;
            }
        }
        throw new RuntimeException("Not enough inputs to feed");
    }
    
    public void addOutput(Connection connection)
    {
        this.outputs.add(connection.out);
        this.weights.add(connection.weight);
    }
    
    public boolean isReady()
    {
        return this.inputs.stream().noneMatch(Objects::isNull);
    }
    
    public void calculate()
    {
        this.output = activator.activate(this.inputs.stream().mapToDouble(i -> i).sum());
    }
    
    public void reset()
    {
        this.inputs.replaceAll(v -> null);
        this.output = 0.0;
    }
}
