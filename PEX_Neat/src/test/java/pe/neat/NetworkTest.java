package pe.neat;

import static pe.PixelEngine.print;

public class NetworkTest
{
    public static void main(String[] args)
    {
        NeatTest.setup(NetworkTest::setup);
        
        NeatTest.run("NeuronTest", false);
    }
    
    private static void setup(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        genome.random.setSeed(1337);
    
        Network net;
        double[] input, output;
        Node node0, node1, node2, node3, node4, node5;
    
        print("========== Test 1 ==========");
        genome = new Genome();
        node0 = genome.addNode(new Node(0, Node.Type.INPUT, 0));
        node1 = genome.addNode(new Node(1, Node.Type.OUTPUT, 1));
    
        genome.addConnection(new Connection(0, node0, node1, 0.5, true));
    
        net   = new Network(genome);
        input = new double[] {1.0};
        output = net.calculate(input);
        print("Output len=%s=1, output[0]=%s=0.9192", output.length, output[0]);
        print();
    
        print("========== Test 2 ==========");
        genome = new Genome();
        node0 = genome.addNode(new Node(0, Node.Type.INPUT, 0));
        node1 = genome.addNode(new Node(1, Node.Type.OUTPUT, 1));
    
        genome.addConnection(new Connection(0, node0, node1, 0.1, true));
    
        net   = new Network(genome);
        input = new double[] {-0.5};
        output = net.calculate(input);
        print("Output len=%s=1, output[0]=%s=0.50973", output.length, output[0]);
        print();
    
        print("========== Test 3 ==========");
        genome = new Genome();
        node0 = genome.addNode(new Node(0, Node.Type.INPUT, 0));
        node1 = genome.addNode(new Node(1, Node.Type.OUTPUT, 2));
        node2 = genome.addNode(new Node(2, Node.Type.HIDDEN, 1));
    
        genome.addConnection(new Connection(0, node0, node2, 0.4, true));
        genome.addConnection(new Connection(1, node2, node1, 0.7, true));
    
        net   = new Network(genome);
        input = new double[] {0.9};
        output = net.calculate(input);
        print("Output len=%s=1, output[0]=%s=0.9524", output.length, output[0]);
        print();
    
        print("========== Test 4 ==========");
        genome = new Genome();
        node0 = genome.addNode(new Node(0, Node.Type.INPUT, 0));
        node1 = genome.addNode(new Node(1, Node.Type.INPUT, 0));
        node2 = genome.addNode(new Node(2, Node.Type.INPUT, 0));
        node3 = genome.addNode(new Node(3, Node.Type.OUTPUT, 2));
        node4 = genome.addNode(new Node(4, Node.Type.HIDDEN, 1));
    
        genome.addConnection(new Connection(0, node0, node4, 0.4, true));
        genome.addConnection(new Connection(1, node1, node4, 0.7, true));
        genome.addConnection(new Connection(2, node2, node4, 0.1, true));
        genome.addConnection(new Connection(3, node4, node3, 1.0, true));
    
        net   = new Network(genome);
        input = new double[] {0.5, 0.75, 0.9};
        output = net.calculate(input);
        print("Output len=%s=1, output[0]=%s=0.9924", output.length, output[0]);
        print();
    
        print("========== Test 5 ==========");
        genome = new Genome();
        node0 = genome.addNode(new Node(0, Node.Type.INPUT, 0));
        node1 = genome.addNode(new Node(1, Node.Type.INPUT, 0));
        node2 = genome.addNode(new Node(2, Node.Type.INPUT, 0));
        node3 = genome.addNode(new Node(3, Node.Type.OUTPUT, 2));
        node4 = genome.addNode(new Node(4, Node.Type.HIDDEN, 1));
        node5 = genome.addNode(new Node(5, Node.Type.HIDDEN, 1));
    
        genome.addConnection(new Connection(0, node0, node4, 0.4, true));
        genome.addConnection(new Connection(1, node1, node4, 0.7, true));
        genome.addConnection(new Connection(2, node2, node4, 0.1, true));
        genome.addConnection(new Connection(3, node4, node3, 1.0, true));
        genome.addConnection(new Connection(4, node2, node5, 0.2, true));
        genome.addConnection(new Connection(5, node5, node4, 0.75, true));
        genome.addConnection(new Connection(6, node5, node3, 0.55, true));
    
        net   = new Network(genome);
        input = new double[] {1., 2., 3.};
        output = net.calculate(input);
        print("Output len=%s=1, output[0]=%s=0.99895", output.length, output[0]);
        print();
    }
}
