package pe.cb;

public class Player
{
    public double   fitness;
    public Genome   brain;
    public double[] vision    = new double[8];//the input array fed into the neuralNet
    public double[] decision  = new double[4]; //the out put of the NN
    public double   unadjustedFitness;
    public int      lifespan  = 0;//how long the player lived for fitness
    public int      bestScore = 0;//stores the score achieved used for replay
    public boolean  dead;
    public int      score;
    public int      gen       = 0;
    
    public int genomeInputs  = 13;
    public int genomeOutputs = 4;
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    //constructor
    
    public Player()
    {
        brain = new Genome(genomeInputs, genomeOutputs);
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    public void show()
    {
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    public void move()
    {
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    public void update()
    {
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
    }
    //----------------------------------------------------------------------------------------------------------------------------------------------------------
    
    public void look()
    {
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
        
    }
    
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    //gets the output of the brain then converts them to actions
    public void think()
    {
        
        double max      = 0;
        int    maxIndex = 0;
        //get the output of the neural network
        decision = brain.feedForward(vision);
        
        for (int i = 0; i < decision.length; i++)
        {
            if (decision[i] > max)
            {
                max      = decision[i];
                maxIndex = i;
            }
        }
        
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
        
        
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    //returns a clone of this player with the same brian
    public Player clone()
    {
        Player clone = new Player();
        clone.brain   = brain.clone();
        clone.fitness = fitness;
        clone.brain.generateNetwork();
        clone.gen       = gen;
        clone.bestScore = score;
        return clone;
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //since there is some randomness in games sometimes when we want to replay the game we need to remove that randomness
    //this fuction does that
    
    public Player cloneForReplay()
    {
        Player clone = new Player();
        clone.brain   = brain.clone();
        clone.fitness = fitness;
        clone.brain.generateNetwork();
        clone.gen       = gen;
        clone.bestScore = score;
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
        return clone;
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    //fot Genetic algorithm
    public void calculateFitness()
    {
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
        
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    public Player crossover(Player parent2)
    {
        Player child = new Player();
        child.brain = brain.crossover(parent2.brain);
        child.brain.generateNetwork();
        return child;
    }
}
