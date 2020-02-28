package pe.neat;

import pe.Keyboard;
import pe.Mouse;
import pe.PixelEngine;

import java.util.function.Supplier;

public class PopulationTest extends PixelEngine
{
    @Override
    protected boolean setup()
    {
        Counter nodeInno = new Counter();
        Counter connInno = new Counter();
        
        Genome initialBrain = new Genome();
        
        initialBrain.addNode(new Node(nodeInno.inc(), Node.Type.INPUT, 0));
        initialBrain.addNode(new Node(nodeInno.inc(), Node.Type.INPUT, 0));
        
        initialBrain.addNode(new Node(nodeInno.inc(), Node.Type.OUTPUT, 1));
        initialBrain.addNode(new Node(nodeInno.inc(), Node.Type.OUTPUT, 1));
        initialBrain.addNode(new Node(nodeInno.inc(), Node.Type.OUTPUT, 1));
        initialBrain.addNode(new Node(nodeInno.inc(), Node.Type.OUTPUT, 1));
        
        initialBrain.addNode(new Node(nodeInno.inc(), Node.Type.BIAS, 0));
        
        int inSize  = initialBrain.inputSize();
        int outSize = initialBrain.outputSize();
        for (int i = 0; i < outSize; i++)
        {
            for (int j = 0; j < inSize; j++)
            {
                initialBrain.addConnection(new Connection(connInno.inc(), j, inSize + i, 0, true));
            }
            initialBrain.addConnection(new Connection(connInno.inc(), inSize + outSize, inSize + i, 0, true));
        }
    
        PEX_Neat.setDefaultOrganism(() -> new Game(initialBrain.copy()));
        PEX_Neat.setPopulationSize(10);
        PEX_Neat.random.setSeed(10);
    
        disableExtension("PEX_CBNeat");
    
        return true;
    }
    
    @Override
    protected boolean draw(double elapsedTime)
    {
        return true;
    }
    
    public static void main(String[] args)
    {
        start(new PopulationTest());
    }
    
    private static class Game extends Organism
    {
        private int x      = screenWidth() / 2;
        private int y      = screenHeight() / 2;
        private int radius = 10;
        
        public Game(Genome brain)
        {
            super(brain);
        }
        
        public Game(Supplier<Genome> initialGenome)
        {
            super(initialGenome);
        }
        
        @Override
        public void gatherInputs(double elapsedTime, boolean userPlaying)
        {
            this.inputs[0] = (x - Mouse.x()) / (double) screenWidth();
            this.inputs[1] = (y - Mouse.y()) / (double) screenHeight();
        }
    
        @Override
        public void makeDecision(double elapsedTime, boolean userPlaying)
        {
            if (userPlaying)
            {
                if (Keyboard.UP.held()) y -= 1;
                if (Keyboard.DOWN.held()) y += 1;
                if (Keyboard.LEFT.held()) x -= 1;
                if (Keyboard.RIGHT.held()) x += 1;
            }
            else
            {
                if (outputs[0] > 0.8) y -= 1;
                if (outputs[1] > 0.8) y += 1;
                if (outputs[2] > 0.8) x -= 1;
                if (outputs[3] > 0.8) x += 1;
            }
            
            if (x < 0) x = screenWidth() - 1;
            if (x > screenWidth() - 1) x = 0;
            if (y < 0) y = screenHeight() - 1;
            if (y > screenHeight() - 1) y = 0;
        }
        
        @Override
        public void draw(double elapsedTime)
        {
            clear();
    
            noFill();
            stroke(255);
    
            circle(x, y, radius);
    
            if (x - radius < 0) circle(x + screenWidth(), y, radius);
            if (x + radius > screenWidth() - 1) circle(x - screenWidth(), y, radius);
            if (y - radius < 0) circle(x, y + screenHeight(), radius);
            if (y + radius > screenHeight() - 1) circle(x, y - screenHeight(), radius);
        }
        
        @Override
        public void calculateFitness()
        {
        
        }
    }
}
