package pe.neat;

import pe.Random;

import static pe.PixelEngine.print;

public class GeneCountingTest
{
    public static void main(String[] args)
    {
        NeatTest.setup(GeneCountingTest::setup);
        
        NeatTest.run("GeneCountingTest", 2, false);
    }
    
    private static void setup(Random random, Genome[] genome, Counter[] nodeInnovation, Counter[] connInnovation)
    {
        int nodes = 10;
        for (int i = 0; i < nodes; i++)
        {
            Node node = new Node(nodeInnovation[0].inc(), Node.Type.HIDDEN, -1);
            genome[0].addNode(node);
            genome[1].addNode(node);
        }
        
        int cons = 5;
        for (int i = 0; i < cons; i++)
        {
            Connection con = new Connection(connInnovation[0].inc(), 0, 0, 1.0, true);
            genome[0].addConnection(con);
            genome[1].addConnection(con);
        }
    
        print(genome[0]);
        print(genome[1]);
    
         int matching = Genome.countMatchingGenes(genome[0], genome[1]);
        int  disjoint = Genome.countDisjointGenes(genome[0], genome[1]);
        int  excess   = Genome.countExcessGenes(genome[0], genome[1]);
        print("Matching Genes: %s\t - Correct Answer: %s", matching, nodes + cons);
        print("Disjoint Genes: %s\t - Correct Answer: 0", disjoint);
        print("Excess Genes:   %s\t - Correct Answer: 0", excess);
        print();
    
        genome[0].addNode(new Node(nodeInnovation[0].inc(), Node.Type.HIDDEN, -1));
        genome[0].addNode(new Node(nodeInnovation[0].inc(), Node.Type.HIDDEN, -1));
        genome[0].addNode(new Node(nodeInnovation[0].inc(), Node.Type.HIDDEN, -1));
        genome[0].addConnection(new Connection(connInnovation[0].inc(), 0, 0, 1.0, true));
        genome[0].addConnection(new Connection(connInnovation[0].inc(), 0, 0, 1.0, true));
        genome[0].addConnection(new Connection(connInnovation[0].inc(), 0, 0, 1.0, true));
    
        matching = Genome.countMatchingGenes(genome[0], genome[1]);
        disjoint = Genome.countDisjointGenes(genome[0], genome[1]);
        excess   = Genome.countExcessGenes(genome[0], genome[1]);
        print("Matching Genes: %s\t - Correct Answer: %s", matching, nodes + cons);
        print("Disjoint Genes: %s\t - Correct Answer: 0", disjoint);
        print("Excess Genes:   %s\t - Correct Answer: 6", excess);
        print();
    
        genome[1].addNode(new Node(nodeInnovation[0].inc(), Node.Type.HIDDEN, -1));
        genome[1].addNode(new Node(nodeInnovation[0].inc(), Node.Type.HIDDEN, -1));
        genome[1].addNode(new Node(nodeInnovation[0].inc(), Node.Type.HIDDEN, -1));
        genome[1].addConnection(new Connection(connInnovation[0].inc(), 0, 0, 1.0, true));
        genome[1].addConnection(new Connection(connInnovation[0].inc(), 0, 0, 1.0, true));
        genome[1].addConnection(new Connection(connInnovation[0].inc(), 0, 0, 1.0, true));
    
        matching = Genome.countMatchingGenes(genome[0], genome[1]);
        disjoint = Genome.countDisjointGenes(genome[0], genome[1]);
        excess   = Genome.countExcessGenes(genome[0], genome[1]);
        print("Matching Genes: %s\t - Correct Answer: %s", matching, nodes + cons);
        print("Disjoint Genes: %s\t - Correct Answer: 6", disjoint);
        print("Excess Genes:   %s\t - Correct Answer: 6", excess);
        print();
    
        matching  = Genome.countMatchingGenes(genome[1], genome[0]);
         disjoint = Genome.countDisjointGenes(genome[1], genome[0]);
         excess = Genome.countExcessGenes(genome[1], genome[0]);
         print("Counting genes between same genomes, but with opposite parameters:");
         print("Matching Genes: %s\t - Correct Answer: %s", matching, nodes + cons);
         print("Disjoint Genes: %s\t - Correct Answer: 6", disjoint);
         print("Excess Genes:   %s\t - Correct Answer: 6", excess);
         print();

         Node node = new Node(nodeInnovation[0].inc(), Node.Type.HIDDEN, -1);
         genome[0].addNode(node);
         genome[1].addNode(node);
    
        Connection con = new Connection(connInnovation[0].inc(), 0, 0, 1.0, true);
         genome[0].addConnection(con);
         genome[1].addConnection(con);

         matching = Genome.countMatchingGenes(genome[1], genome[0]);
         disjoint = Genome.countDisjointGenes(genome[1], genome[0]);
         excess = Genome.countExcessGenes(genome[1], genome[0]);
         print("Matching Genes: %s\t - Correct Answer: %s", matching, nodes + cons + 2);
         print("Disjoint Genes: %s\t - Correct Answer: 12", disjoint);
         print("Excess Genes:   %s\t - Correct Answer: 0", excess);
         print();
    }
}
