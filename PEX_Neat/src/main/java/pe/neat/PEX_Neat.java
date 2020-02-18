package pe.neat;

import pe.PEX;
import pe.Profiler;

public class PEX_Neat extends PEX
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
     * Weight for excess genes
     */
    public static final double EXCESS_GENE_WEIGHT = 1.0;
    
    /**
     * Weight for disjoint genes
     */
    public static final double DISJOINT_GENE_WEIGHT = 1.0;
    
    /**
     * Weight for average weight distance
     */
    public static final double AVERAGE_WEIGHT_DIFFERENCE_WEIGHT = 0.4;
    
    protected PEX_Neat(Profiler profiler)
    {
        super(profiler);
    }
    
    @Override
    public void initialize()
    {
    
    }
    
    @Override
    public void beforeUserUpdate(double elapsedTime)
    {
    
    }
    
    @Override
    public void afterUserUpdate(double elapsedTime)
    {
    
    }
    
    @Override
    public void destroy()
    {
    
    }
}
