package pe.neat;

public class Settings
{
    /**
     * Rate at which mutation happens without crossover
     */
    public static final double ASEXUAL_REPRODUCTION_RATE = 0.25;
    
    /**
     * Rate at which a genome will have its weights mutated
     */
    public static final double WEIGHT_MUTATION_RATE = 0.80;
    
    /**
     * Rate at which a node mutation will occur
     */
    public static final double NODE_MUTATION_RATE = 0.05;
    
    /**
     * Rate at which a connection mutation will occur
     */
    public static final double CONNECTION_MUTATION_RATE = 0.05;
    
    /**
     * Rate at which a genome's weights will be nudged and not completely random
     */
    public static final double WEIGHT_PERTURBING_RATE = 0.90;
    
    /**
     * Rate at which a child genome's connection will be disabled
     */
    public static final double DISABLED_GENE_INHERITING_RATE = 0.75;
    
    /**
     * Genomic distance before two genomes are different species
     */
    public static final double SPECIES_DISTANCE = 3.0;
    
    /**
     * Genomic distance before two genomes are different species
     */
    public static final double EXCESS_GENE_WEIGHT = 1.0;
    
    /**
     * Genomic distance before two genomes are different species
     */
    public static final double DISJOINT_GENE_WEIGHT = 1.0;
    
    /**
     * Genomic distance before two genomes are different species
     */
    public static final double C3 = 0.4;
    
    public double asexualReproductionRate    = ASEXUAL_REPRODUCTION_RATE;
    public double weightMutationRate         = WEIGHT_MUTATION_RATE;
    public double nodeMutationRate           = NODE_MUTATION_RATE;
    public double connectionMutationRate     = CONNECTION_MUTATION_RATE;
    public double weightPerturbingRate       = WEIGHT_PERTURBING_RATE;
    public double disabledGeneInheritingRate = DISABLED_GENE_INHERITING_RATE;
    public double speciesDistance            = SPECIES_DISTANCE;
    public double excessGeneWeight           = EXCESS_GENE_WEIGHT;
    public double disjointGeneWeight         = DISJOINT_GENE_WEIGHT;
    public double c3                         = C3;
    
    public int populationSize;
    
    public Settings(int populationSize)
    {
        this.populationSize = populationSize;
    }
}
