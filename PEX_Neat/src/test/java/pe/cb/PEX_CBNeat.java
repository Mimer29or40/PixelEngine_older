package pe.cb;

import pe.Keyboard;
import pe.PEX;
import pe.Profiler;

import static pe.PixelEngine.*;

public class PEX_CBNeat extends PEX
{
    public static int        nextConnectionNo = 1000;
    public static Population pop;
    public static int        speed            = 60;
    
    
    public static boolean showBest     = true;//true if only show the best of the previous generation
    public static boolean runBest      = false; //true if replaying the best ever game
    public static boolean humanPlaying = false; //true if the user is playing
    
    public static Player humanPlayer;
    
    public static boolean runThroughSpecies = false;
    public static int     upToSpecies       = 0;
    public static Player  speciesChamp;
    
    public static boolean showBrain = false;
    
    public static boolean showBestEachGen = false;
    public static int     upToGen         = 0;
    public static Player  genPlayerTemp;
    
    public static boolean showNothing = false;
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    //draws the display screen
    public static void drawToScreen()
    {
        if (!showNothing)
        {
            //pretty stuff
            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            
            drawBrain();
            writeInfo();
        }
    }
    
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void drawBrain()
    {  //show the brain of whatever genome is currently showing
        int startX = 0;//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
        int startY = 0;
        int w      = 0;
        int h      = 0;
        if (runThroughSpecies)
        {
            speciesChamp.brain.drawGenome(startX, startY, w, h);
        }
        else if (runBest)
        {
            pop.bestPlayer.brain.drawGenome(startX, startY, w, h);
        }
        else if (humanPlaying)
        {
            showBrain = false;
        }
        else if (showBestEachGen)
        {
            genPlayerTemp.brain.drawGenome(startX, startY, w, h);
        }
        else
        {
            pop.pop.get(0).brain.drawGenome(startX, startY, w, h);
        }
    }
    
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //writes info about the current player
    public static void writeInfo()
    {
        // fill(200);
        // textAlign(LEFT);
        // textSize(30);
        if (showBestEachGen)
        {
            drawString(650, 50, "Score: " + genPlayerTemp.score);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            drawString(1150, 50, "Gen: " + (genPlayerTemp.gen + 1));
        }
        else if (runThroughSpecies)
        {
            drawString(650, 50, "Score: " + speciesChamp.score);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            drawString(1150, 50, "Species: " + (upToSpecies + 1));
            drawString(50, screenHeight() / 2 + 200, "Players in this Species: " + pop.species.get(upToSpecies).players.size());
        }
        else if (humanPlaying)
        {
            drawString(650, 50, "Score: " + humanPlayer.score);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
        }
        else if (runBest)
        {
            drawString(650, 50, "Score: " + pop.bestPlayer.score);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            drawString(1150, 50, "Gen: " + pop.gen);
        }
        else
        {
            if (showBest)
            {
                drawString(650, 50, "Score: " + pop.pop.get(0).score);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
                drawString(1150, 50, "Gen: " + pop.gen);
                drawString(50, screenHeight() / 2 + 300, "Species: " + pop.species.size());
                drawString(50, screenHeight() / 2 + 200, "Global Best Score: " + pop.bestScore);
            }
        }
    }
    
    public PEX_CBNeat(Profiler profiler)
    {
        super(profiler);
    }
    
    @Override
    public void beforeSetup()
    {
    
    }
    
    @Override
    public void afterSetup()
    {
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
        pop         = new Population(300);
        humanPlayer = new Player();
    }
    
    @Override
    public void beforeDraw(double elapsedTime)
    {
        clear();
        
        if (Keyboard.SPACE.down())
        {
            //toggle showBest
            showBest = !showBest;
        }
        if (Keyboard.EQUALS.down())
        {
            //speed up frame rate
            speed += 10;
            // frameRate(speed);
            println(speed);
        }
        if (Keyboard.MINUS.down())
        {
            //slow down frame rate
            if (speed > 10)
            {
                speed -= 10;
                // frameRate(speed);
                println(speed);
            }
        }
        if (Keyboard.B.down())
        {
            //run the best
            runBest = !runBest;
        }
        if (Keyboard.S.down())
        {
            //show species
            runThroughSpecies = !runThroughSpecies;
            upToSpecies       = 0;
            speciesChamp      = pop.species.get(upToSpecies).champ.cloneForReplay();
        }
        if (Keyboard.G.down())
        {
            //show generations
            showBestEachGen = !showBestEachGen;
            upToGen         = 0;
            genPlayerTemp   = pop.genPlayers.get(upToGen).clone();
        }
        if (Keyboard.N.down())
        {
            //show absolutely nothing in order to speed up computation
            showNothing = !showNothing;
        }
        if (Keyboard.P.down())
        {
            //play
            humanPlaying = !humanPlaying;
            humanPlayer  = new Player();
        }
        if (Keyboard.UP.down())
        {
            //the only time up/ down / left is used is to control the player
            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
        }
        if (Keyboard.DOWN.down())
        {
            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
        }
        if (Keyboard.LEFT.down())
        {
            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
        }
        if (Keyboard.RIGHT.down())
        {
            //right is used to move through the generations
            if (runThroughSpecies)
            {//if showing the species in the current generation then move on to the next species
                upToSpecies++;
                if (upToSpecies >= pop.species.size())
                {
                    runThroughSpecies = false;
                }
                else
                {
                    speciesChamp = pop.species.get(upToSpecies).champ.cloneForReplay();
                }
            }
            else if (showBestEachGen)
            {//if showing the best player each generation then move on to the next generation
                upToGen++;
                if (upToGen >= pop.genPlayers.size())
                {//if reached the current generation then exit out of the showing generations mode
                    showBestEachGen = false;
                }
                else
                {
                    genPlayerTemp = pop.genPlayers.get(upToGen).cloneForReplay();
                }
            }
            else if (humanPlaying)
            {//if the user is playing then move player right
                
                //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            }
        }
    }
    
    @Override
    public void afterDraw(double elapsedTime)
    {
        drawToScreen();
        if (showBestEachGen)
        {//show the best of each gen
            if (!genPlayerTemp.dead)
            {//if current gen player is not dead then update it
                
                genPlayerTemp.look();
                genPlayerTemp.think();
                genPlayerTemp.update();
                genPlayerTemp.show();
            }
            else
            {//if dead move on to the next generation
                upToGen++;
                if (upToGen >= pop.genPlayers.size())
                {//if at the end then return to the start and stop doing it
                    upToGen         = 0;
                    showBestEachGen = false;
                }
                else
                {//if not at the end then get the next generation
                    genPlayerTemp = pop.genPlayers.get(upToGen).cloneForReplay();
                }
            }
        }
        else if (runThroughSpecies)
        {//show all the species
            if (!speciesChamp.dead)
            {//if best player is not dead
                speciesChamp.look();
                speciesChamp.think();
                speciesChamp.update();
                speciesChamp.show();
            }
            else
            {//once dead
                upToSpecies++;
                if (upToSpecies >= pop.species.size())
                {
                    runThroughSpecies = false;
                }
                else
                {
                    speciesChamp = pop.species.get(upToSpecies).champ.cloneForReplay();
                }
            }
        }
        else
        {
            if (humanPlaying)
            {//if the user is controling the ship[
                if (!humanPlayer.dead)
                {//if the player isnt dead then move and show the player based on input
                    humanPlayer.look();
                    humanPlayer.update();
                    humanPlayer.show();
                }
                else
                {//once done return to ai
                    humanPlaying = false;
                }
            }
            else if (runBest)
            {// if replaying the best ever game
                if (!pop.bestPlayer.dead)
                {//if best player is not dead
                    pop.bestPlayer.look();
                    pop.bestPlayer.think();
                    pop.bestPlayer.update();
                    pop.bestPlayer.show();
                }
                else
                {//once dead
                    runBest        = false;//stop replaying it
                    pop.bestPlayer = pop.bestPlayer.cloneForReplay();//reset the best player so it can play again
                }
            }
            else
            {//if just evolving normally
                if (!pop.done())
                {//if any players are alive then update them
                    pop.updateAlive();
                }
                else
                {//all dead
                    //genetic algorithm
                    pop.naturalSelection();
                }
            }
        }
    }
    
    @Override
    public void beforeDestroy()
    {
    
    }
    
    @Override
    public void afterDestroy()
    {
    
    }
}
