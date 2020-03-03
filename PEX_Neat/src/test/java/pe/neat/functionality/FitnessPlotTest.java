package pe.neat.functionality;

import pe.PixelEngine;
import pe.neat.FitnessPlotter;

import java.util.ArrayList;

public class FitnessPlotTest extends PixelEngine
{
    @Override
    protected void setup()
    {
        ArrayList<Double> fitnessList = new ArrayList<>();
    
        for (int i = 0; i < 1000; i++)
        {
            fitnessList.add((double) i / 1000.0);
        }
    
        FitnessPlotter plotter = new FitnessPlotter();
        
        plotter.generatePlot(fitnessList).saveSprite("out/fitness.png");
    }
    
    public static void main(String[] args)
    {
        start(new FitnessPlotTest());
    }
}
