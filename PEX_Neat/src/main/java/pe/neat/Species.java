package pe.neat;

import java.util.ArrayList;

public class Species
{
    private final ArrayList<Organism> organisms = new ArrayList<>();
    
    public Organism champion;
    
    public Species() {}
    
    public Species(Organism organism)
    {
        this.organisms.add(organism);
        this.champion = organism;
    }
}
