package pe.neat.cb;

import org.joml.Vector2d;
import pe.Keyboard;
import pe.PEX;
import pe.Profiler;
import pe.color.Color;

import static pe.PixelEngine.*;

public class PEX_CBNeat extends PEX
{
    public static Player     humanPlayer;//the player which the user (you) controls
    public static Population pop;
    public static int        speed              = 100;
    public static double     globalMutationRate = 0.1;
    public static int        nextConnectionNo   = 1000;
    
    public static boolean showBest     = true;//true if only show the best of the previous generation
    public static boolean runBest      = false; //true if replaying the best ever game
    public static boolean humanPlaying = false; //true if the user is playing
    
    
    public static boolean runThroughSpecies = false;
    public static int     upToSpecies       = 0;
    public static Player  speciesChamp;
    
    public static boolean showBrain = false;
    
    public static boolean showBestEachGen = false;
    public static int     upToGen         = 0;
    public static Player  genPlayerTemp;
    
    //function which returns whether a vector is out of the play area
    public static boolean isOut(Vector2d pos)
    {
        return pos.x < -50 || pos.y < -50 || pos.x > screenWidth() + 50 || pos.y > 50 + screenHeight();
    }
    
    //shows the score and the generation on the screen
    public static void showScore()
    {
        if (showBestEachGen)
        {
            // textFont(font);
            // fill(255);
            // textAlign(LEFT);
            string(80, 60, "Score: " + genPlayerTemp.score);
            string(screenWidth() - 250, 60, "Gen: " + (upToGen + 1));
        }
        else if (runThroughSpecies)
        {
            // textFont(font);
            // fill(255);
            // textAlign(LEFT);
            string(80, 60, "Score: " + speciesChamp.score);
            string(screenWidth() - 250, 60, "Species: " + (upToSpecies + 1));
        }
        else if (humanPlaying)
        {
            // textFont(font);
            // fill(255);
            // textAlign(LEFT);
            string(80, 60, "Score: " + humanPlayer.score);
        }
        else if (runBest)
        {
            // textFont(font);
            // fill(255);
            // textAlign(LEFT);
            string(80, 60, "Score: " + pop.bestPlayer.score);
            string(screenWidth() - 200, 60, "Gen: " + pop.gen);
        }
        else
        {
            if (showBest)
            {
                // textFont(font);
                // fill(255);
                // textAlign(LEFT);
                string(80, 60, "Score: " + pop.pop.get(0).score);
                string(screenWidth() - 200, 60, "Gen: " + pop.gen);
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
        if (Keyboard.SPACE.down())
        {
            if (humanPlaying)
            {//if the user is controlling a ship shoot
                humanPlayer.shoot();
            }
            else
            {//if not toggle showBest
                showBest = !showBest;
            }
        }
        if (Keyboard.P.down())
        {//play
            humanPlaying = !humanPlaying;
            humanPlayer  = new Player();
        }
        if (Keyboard.EQUALS.down())
        {//speed up frame rate
            speed += 10;
            // frameRate(speed);
            println(speed);
        }
        if (Keyboard.MINUS.down())
        {//slow down frame rate
            if (speed > 10)
            {
                speed -= 10;
                // frameRate(speed);
                println(speed);
            }
        }
        if (Keyboard.H.down())
        {//halve the mutation rate
            globalMutationRate /= 2;
            println(globalMutationRate);
        }
        if (Keyboard.D.down())
        {//double the mutation rate
            globalMutationRate *= 2;
            println(globalMutationRate);
        }
        if (Keyboard.B.down())
        {//run the best
            runBest = true;
        }
        if (Keyboard.S.down())
        {
            runThroughSpecies = !runThroughSpecies;
            upToSpecies       = 0;
            speciesChamp      = pop.species.get(upToSpecies).champ.cloneForReplay();
        }
        if (Keyboard.G.down())
        {//show genome
            showBestEachGen = !showBestEachGen;
            upToGen         = 0;
            genPlayerTemp   = pop.genPlayers.get(upToGen).clone();
        }
        if (Keyboard.N.down())
        {
            showBrain = !showBrain;
        }
    
        //player controls
        if (Keyboard.UP.down())
        {
            humanPlayer.boosting = true;
        }
        if (Keyboard.LEFT.down())
        {
            humanPlayer.spin = -0.08;
        }
        else if (Keyboard.RIGHT.down())
        {
            if (runThroughSpecies)
            {
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
            {
                upToGen++;
                if (upToGen >= pop.gen)
                {
                    showBestEachGen = false;
                }
                else
                {
                    genPlayerTemp = pop.genPlayers.get(upToGen).cloneForReplay();
                }
            }
            else
            {
                humanPlayer.spin = 0.08;
            }
        }
    
        //once key released
        if (Keyboard.UP.up())
        {//stop boosting
            humanPlayer.boosting = false;
        }
        if (Keyboard.LEFT.up())
        {// stop turning
            humanPlayer.spin = 0;
        }
        else if (Keyboard.RIGHT.up())
        {
            humanPlayer.spin = 0;
        }
    }
    
    @Override
    public void afterDraw(double elapsedTime)
    {
        clear(Color.BLACK); //deep space background
    
        if (showBrain)
        {
            clear(Color.WHITE);
            //show the brain of whatever genome is currently shoeing
            if (runThroughSpecies)
            {
                speciesChamp.brain.drawGenome();
            }
            else if (runBest)
            {
                pop.bestPlayer.brain.drawGenome();
            }
            else if (humanPlaying)
            {
                showBrain = false;
            }
            else
            {
                pop.pop.get(0).brain.drawGenome();
            }
        }
        else if (showBestEachGen)
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
                    genPlayerTemp = pop.genPlayers.get(upToGen).clone();
                    println(genPlayerTemp.bestScore);
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
        else if (humanPlaying)
        {//if the user is controling the ship[
            if (!humanPlayer.dead)
            {//if the player isnt dead then move and show the player based on input
                humanPlayer.look();
                humanPlayer.update();
                humanPlayer.show();
                println(humanPlayer.vision[1]);
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
        else if (!pop.done()) //if just evolving normally
        {//if any players are alive then update them
            pop.updateAlive();
        }
        else
        {//all dead
            //genetic algorithm
            pop.naturalSelection();
        }
        showScore();//display the score
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
