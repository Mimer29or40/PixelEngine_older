package pe.neat;

import java.util.ArrayList;
import java.util.HashMap;

public class Network
{
    private final ArrayList<Integer>       inputs  = new ArrayList<>();
    private final ArrayList<Integer>       outputs = new ArrayList<>();
    private final HashMap<Integer, Neuron> neurons = new HashMap<>();
    
    public Network(Genome genome)
    {
        for (Node node : genome.getNodes())
        {
            Neuron neuron = new Neuron();
            if (node.type == Node.Type.INPUT)
            {
                neuron.addInput();
                this.inputs.add(node.id);
            }
            else if (node.type == Node.Type.OUTPUT)
            {
                this.outputs.add(node.id);
            }
            this.neurons.put(node.id, neuron);
        }
        
        for (Connection connection : genome.getConnections())
        {
            if (!connection.enabled) continue;
            this.neurons.get(connection.in).addOutput(connection);
            this.neurons.get(connection.out).addInput();
        }
    }
    
    public double[] calculate(double[] inputs)
    {
        if (inputs.length != this.inputs.size()) throw new RuntimeException("Input length is not correct");
        
        ArrayList<Neuron> unprocessed = new ArrayList<>();
        for (Neuron neuron : this.neurons.values())
        {
            neuron.reset();
            unprocessed.add(neuron);
        }
        
        for (int i = 0; i < inputs.length; i++)
        {
            Neuron neuron = this.neurons.get(this.inputs.get(i));
            neuron.feedInput(inputs[i]);
            neuron.calculate();
            for (int j = 0; j < neuron.outputs.size(); j++)
            {
                this.neurons.get(neuron.outputs.get(j)).feedInput(neuron.output * neuron.weights.get(j));
            }
            unprocessed.remove(neuron);
        }
        
        for (int i = 0; unprocessed.size() > 0; i++)
        {
            if (i > 1000) return new double[inputs.length];
            
            for (int j = 0; j < unprocessed.size(); j++)
            {
                Neuron neuron = unprocessed.get(j);
                if (neuron.isReady())
                {
                    neuron.calculate();
                    for (int k = 0; k < neuron.outputs.size(); k++)
                    {
                        this.neurons.get(neuron.outputs.get(k)).feedInput(neuron.output * neuron.weights.get(k));
                    }
                    unprocessed.remove(j);
                    j--;
                }
            }
        }
        
        double[] output = new double[this.outputs.size()];
        for (int i = 0; i < output.length; i++) output[i] = this.neurons.get(this.outputs.get(i)).output;
        
        return output;
        
        // for input, parameter in zip (self.input, parameters):
        //     n = self.neurons[input]
        //     n.feed_input(parameter)
        //     n.calculate()
        //     for output, weight in zip (n.output_ids, n.weights):
        //         self.neurons[output].feed_input(n.output * weight)
        //     unprocessed.remove(n)
        //
        // loops = 0
        // while len(unprocessed) > 0:
        //     loops += 1
        //     if loops > 1000:
        //         return None
        //
        //     for n in unprocessed.copy():
        //         if n.is_ready():
        //             n.calculate()
        //             for id, weight in zip (n.output_ids, n.weights):
        //                 self.neurons[id].feed_input(n.output * weight)
        //             unprocessed.remove(n)
        //
        // return [self.neurons[o].output for o in self.output]
    }
}
