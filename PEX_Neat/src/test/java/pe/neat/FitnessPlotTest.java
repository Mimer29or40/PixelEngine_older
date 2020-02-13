package pe.neat;

import pe.PixelEngine;

import java.util.ArrayList;

public class FitnessPlotTest extends PixelEngine
{
    @Override
    protected boolean onUserCreate()
    {
        ArrayList<Double> fitnessList = new ArrayList<>();
        
        for (int i = 0; i < 1000; i++)
        {
            fitnessList.add((double) i / 1000.0);
        }
        
        FitnessPlotter plotter = new FitnessPlotter();
        
        plotter.generatePlot(fitnessList).saveSprite("out/fitness.png");
        
        return false;
    }
    
    public static void main(String[] args)
    {
        start(new FitnessPlotTest());
    }
}
