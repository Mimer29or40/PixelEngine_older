package pe.neat;

import static pe.PixelEngine.print;

public class NeuronTest
{
    public static void main(String[] args)
    {
        NeatTest.setup(NeuronTest::setup);
        
        NeatTest.run("NeuronTest", false);
    }
    
    private static void setup(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        genome.random.setSeed(1337);
    
        print("========== Test 1 ==========");
        Neuron test1 = new Neuron();
        test1.addInput();
        test1.addInput();
        test1.addInput();
    
        test1.feedInput(1.0);
        print("test1.is_ready()=%s, Actual: False", test1.isReady());
        test1.feedInput(1.0);
        print("test1.is_ready()=%s, Actual: False", test1.isReady());
        test1.feedInput(1.0);
        print("test1.is_ready()=%s, Actual: True", test1.isReady());
        print("sum=3");
        print();
    
        test1.calculate();
        print("Calculating . . . %s", test1.output);
        print();
    
        print("========== Test 2 ==========");
        Neuron test2 = new Neuron();
        test2.addInput();
        test2.addInput();
        test2.addInput();
    
        test2.feedInput(0.0);
        test2.feedInput(0.5);
        test2.feedInput(-0.5);
        print("sum=0");
    
        test2.calculate();
        print("Calculating . . . %s", test2.output);
        print();
    
        print("========== Test 3 ==========");
        Neuron test3 = new Neuron();
        test3.addInput();
        test3.addInput();
        test3.addInput();
    
        test3.feedInput(-2.0);
        test3.feedInput(-2.0);
        test3.feedInput(-2.0);
        print("sum=-6");
    
        test3.calculate();
        print("Calculating . . . %s", test3.output);
        print();
    
        print("========== Test 4 ==========");
        Neuron test4 = new Neuron();
        test4.addInput();
        test4.addInput();
        test4.addInput();
    
        test4.feedInput(-20.0);
        test4.feedInput(-20.0);
        test4.feedInput(-20.0);
        print("sum=-60");
    
        test4.calculate();
        print("Calculating . . . %s", test4.output);
        print();
    }
}
