package pe.neat.cb;

import org.joml.Vector2d;

import java.util.ArrayList;

import static pe.PixelEngine.*;

public class Player
{
    public Vector2d pos;
    public Vector2d vel;
    public Vector2d acc;
    
    public int                 score         = 0;//how many asteroids have been shot
    public int                 shootCount    = 0;//stops the player from shooting too quickly
    public double              rotation;//the ships current rotation
    public double              spin;//the amount the ship is to spin next update
    public double              maxSpeed      = 10;//limit the players speed at 10
    public boolean             boosting      = false;//whether the booster is on or not
    public ArrayList<Bullet>   bullets       = new ArrayList<Bullet>(); //the bullets currently on screen
    public ArrayList<Asteroid> asteroids     = new ArrayList<Asteroid>(); // all the asteroids
    public int                 asteroidCount = 1000;//the time until the next asteroid spawns
    public int                 lives         = 0;//no lives
    public boolean             dead          = false;//is it dead
    public int                 immortalCount = 0; //when the player looses a life and respawns it is immortal for a small amount of time
    public int                 boostCount    = 10;//makes the booster flash
    //--------AI stuff
    public Genome              brain;
    public double[]            vision        = new double[8];//the input array fed into the neuralNet
    public double[]            decision      = new double[4]; //the out put of the NN
    public boolean             replay        = false;//whether the player is being raplayed
    //since asteroids are spawned randomly when replaying the player we need to use a random seed to repeat the same randomness
    public long                SeedUsed; //the random seed used to intiate the asteroids
    public ArrayList<Long>     seedsUsed     = new ArrayList<Long>();//seeds used for all the spawned asteroids
    public int                 upToSeedNo    = 0;//which position in the arrayList
    public double              fitness;
    public double              unadjustedFitness;
    
    public int shotsFired = 4;//initiated at 4 to encourage shooting
    public int shotsHit   = 1; //initiated at 1 so players dont get a fitness of 1
    
    public int lifespan = 0;//how long the player lived for fitness
    
    public boolean canShoot = true;//whether the player can shoot or not
    
    public int bestScore = 0;//stores the score achieved used for replay
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //constructor
    public Player()
    {
        pos      = new Vector2d(screenWidth() / 2.0, screenHeight() / 2.0);
        vel      = new Vector2d();
        acc      = new Vector2d();
        rotation = 0;
        SeedUsed = nextLong(1000000000);//create and store a seed
        setSeed(SeedUsed);
    
        //generate asteroids
        asteroids.add(new Asteroid(nextInt(screenWidth()), 0, nextDouble(-1, 1), nextDouble(-1, 1), 3));
        asteroids.add(new Asteroid(nextInt(screenWidth()), 0, nextDouble(-1, 1), nextDouble(-1, 1), 3));
        asteroids.add(new Asteroid(0, nextInt(screenHeight()), nextDouble(-1, 1), nextDouble(-1, 1), 3));
        asteroids.add(new Asteroid(nextInt(screenWidth()), nextInt(screenHeight()), nextDouble(-1, 1), nextDouble(-1, 1), 3));
        //aim the fifth one at the player
        double randX = nextDouble(screenWidth());
        double randY = -50 + Math.floor(nextDouble(2)) * (screenHeight() + 100);
        asteroids.add(new Asteroid(randX, randY, pos.x - randX, pos.y - randY, 3));
        brain = new Genome(33, 4);
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //constructor used for replaying players
    public Player(long seed)
    {
        replay   = true;//is replaying
        pos      = new Vector2d(screenWidth() / 2, screenHeight() / 2);
        vel      = new Vector2d();
        acc      = new Vector2d();
        rotation = 0;
        SeedUsed = seed;//use the parameter seed to set the asteroids at the same position as the last one
        setSeed(SeedUsed);
        //generate asteroids
        asteroids.add(new Asteroid(nextInt(screenWidth()), 0, nextDouble(-1, 1), nextDouble(-1, 1), 3));
        asteroids.add(new Asteroid(nextInt(screenWidth()), 0, nextDouble(-1, 1), nextDouble(-1, 1), 3));
        asteroids.add(new Asteroid(0, nextInt(screenHeight()), nextDouble(-1, 1), nextDouble(-1, 1), 3));
        asteroids.add(new Asteroid(nextInt(screenWidth()), nextInt(screenHeight()), nextDouble(-1, 1), nextDouble(-1, 1), 3));
        //aim the fifth one at the player
        double randX = nextDouble(screenWidth());
        double randY = -50 + Math.floor(nextDouble(2)) * (screenHeight() + 100);
        asteroids.add(new Asteroid(randX, randY, pos.x - randX, pos.y - randY, 3));
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //Move player
    public void move()
    {
        if (!dead)
        {
            checkTimers();
            rotatePlayer();
            if (boosting)
            {//are thrusters on
                boost();
            }
            else
            {
                boostOff();
            }
            
            vel.add(acc);//velocity += acceleration
            if (vel.length() > maxSpeed) vel.normalize().mul(maxSpeed);
            vel.mul(0.99);
            pos.add(vel);//position += velocity
            
            for (int i = 0; i < bullets.size(); i++)
            {//move all the bullets
                bullets.get(i).move();
            }
            
            for (int i = 0; i < asteroids.size(); i++)
            {//move all the asteroids
                asteroids.get(i).move();
            }
            if (PEX_CBNeat.isOut(pos))
            {//wrap the player around the gaming area
                loopy();
            }
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //move through time and check if anything should happen at this instance
    public void checkTimers()
    {
        lifespan += 1;
        shootCount--;
        asteroidCount--;
        if (asteroidCount <= 0)
        {//spawn asteorid
            
            if (replay)
            {//if replaying use the seeds from the arrayList
                setSeed(seedsUsed.get(upToSeedNo));
                upToSeedNo++;
            }
            else
            {//if not generate the seeds and then save them
                long seed = nextLong(1000000);
                seedsUsed.add(seed);
                setSeed(seed);
            }
            //aim the asteroid at the player to encourage movement
            double randX = nextDouble(screenWidth());
            double randY = -50 + Math.floor(nextDouble(2)) * (screenHeight() + 100);
            asteroids.add(new Asteroid(randX, randY, pos.x - randX, pos.y - randY, 3));
            asteroidCount = 1000;
        }
        
        if (shootCount <= 0)
        {
            canShoot = true;
        }
    }
    
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //booster
    public void boost()
    {
        acc = new Vector2d(Math.cos(rotation), Math.sin(rotation));
        acc.mul(0.5);
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //boostless
    public void boostOff()
    {
        acc.set(0, 0);
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //spin that player
    public void rotatePlayer()
    {
        rotation += spin;
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //draw the player, bullets and asteroids 
    public void show()
    {
        if (!dead)
        {
            for (int i = 0; i < bullets.size(); i++)
            {//show bullets
                bullets.get(i).show();
            }
            if (immortalCount > 0)
            {//no need to decrease immortalCOunt if its already 0
                immortalCount--;
            }
            if (immortalCount > 0 && Math.floor(((double) immortalCount) / 5) % 2 == 0)
            {//needs to appear to be flashing so only show half of the time
            }
            else
            {
                // pushMatrix();
                // translate(pos.x, pos.y);
                // rotate(rotation);
                // //actually draw the player
                // fill(0);
                // noStroke();
                // beginShape();
                // int size = 12;
                // //black triangle
                // vertex(-size - 2, -size);
                // vertex(-size - 2, size);
                // vertex(2 * size - 2, 0);
                // endShape(CLOSE);
                // stroke(255);
                // //white out lines
                // line(-size - 2, -size, -size - 2, size);
                // line(2 * size - 2, 0, -22, 15);
                // line(2 * size - 2, 0, -22, -15);
                // if (boosting)
                // {//when boosting draw "flames" its just a little triangle
                //     boostCount--;
                //     if (floor(((double) boostCount) / 3) % 2 == 0)
                //     {//only show it half of the time to appear like its flashing
                //         line(-size - 2, 6, -size - 2 - 12, 0);
                //         line(-size - 2, -6, -size - 2 - 12, 0);
                //     }
                // }
                // popMatrix();
                int    rocketWidth  = 12;
                int    rocketHeight = 36;
                double cos          = Math.cos(rotation);
                double sin          = Math.sin(rotation);
                
                int x1 = (int) (pos.x - cos * rocketHeight / 2 + sin * rocketWidth / 2);
                int y1 = (int) (pos.y - sin * rocketHeight / 2 - cos * rocketWidth / 2);
                
                int x2 = (int) (pos.x - cos * rocketHeight / 2 - sin * rocketWidth / 2);
                int y2 = (int) (pos.y - sin * rocketHeight / 2 + cos * rocketWidth / 2);
                
                int x3 = (int) (pos.x + cos * rocketHeight / 2);
                int y3 = (int) (pos.y + sin * rocketHeight / 2);
                
                drawTriangle(x1, y1, x2, y2, x3, y3);
                
                if (boosting)
                {
                    
                    x1 = (int) (pos.x - cos * rocketHeight / 2 + sin * rocketWidth / 2 * 0.95);
                    y1 = (int) (pos.y - sin * rocketHeight / 2 - cos * rocketWidth / 2 * 0.95);
                    
                    x2 = (int) (pos.x - cos * rocketHeight / 2 - sin * rocketWidth / 2 * 0.95);
                    y2 = (int) (pos.y - sin * rocketHeight / 2 + cos * rocketWidth / 2 * 0.95);
                    
                    x3 = (int) (pos.x - cos * rocketHeight / 2 * 1.35);
                    y3 = (int) (pos.y - sin * rocketHeight / 2 * 1.35);
                    
                    drawTriangle(x1, y1, x2, y2, x3, y3);
                }
            }
        }
        for (int i = 0; i < asteroids.size(); i++)
        {//show asteroids
            asteroids.get(i).show();
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //shoot a bullet
    public void shoot()
    {
        if (shootCount <= 0)
        {//if can shoot
            bullets.add(new Bullet(pos.x, pos.y, rotation, vel.length()));//create bullet
            shootCount = 50;//reset shoot count
            canShoot   = false;
            shotsFired++;
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //in charge or moving everything and also checking if anything has been shot or hit 
    public void update()
    {
        for (int i = 0; i < bullets.size(); i++)
        {//if any bullets expires remove it
            if (bullets.get(i).off)
            {
                bullets.remove(i);
                i--;
            }
        }
        move();//move everything
        checkPositions();//check if anything has been shot or hit
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //check if anything has been shot or hit
    public void checkPositions()
    {
        //check if any bullets have hit any asteroids
        for (int i = 0; i < bullets.size(); i++)
        {
            for (int j = 0; j < asteroids.size(); j++)
            {
                if (asteroids.get(j).checkIfHit(bullets.get(i).pos))
                {
                    shotsHit++;
                    bullets.remove(i);//remove bullet
                    score += 1;
                    break;
                }
            }
        }
        //check if player has been hit
        if (immortalCount <= 0)
        {
            for (int j = 0; j < asteroids.size(); j++)
            {
                if (asteroids.get(j).checkIfHitPlayer(pos))
                {
                    playerHit();
                }
            }
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //called when player is hit by an asteroid
    public void playerHit()
    {
        if (lives == 0)
        {//if no lives left
            dead = true;
        }
        else
        {//remove a life and reset positions
            lives -= 1;
            immortalCount = 100;
            resetPositions();
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //returns player to center
    public void resetPositions()
    {
        pos      = new Vector2d(screenWidth() / 2, screenHeight() / 2);
        vel      = new Vector2d();
        acc      = new Vector2d();
        bullets  = new ArrayList<Bullet>();
        rotation = 0;
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //wraps the player around the playing area
    public void loopy()
    {
        if (pos.y < -50)
        {
            pos.y = screenHeight() + 50;
        }
        else if (pos.y > screenHeight() + 50)
        {
            pos.y = -50;
        }
        if (pos.x < -50)
        {
            pos.x = screenWidth() + 50;
        }
        else if (pos.x > screenWidth() + 50)
        {
            pos.x = -50;
        }
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //for genetic algorithm
    public void calculateFitness()
    {
        double hitRate = (double) shotsHit / (double) shotsFired;
        fitness           = (score + 1) * 10;
        fitness *= lifespan;
        fitness *= hitRate * hitRate;//includes hitrate to encourage aiming
        unadjustedFitness = fitness;
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------  
    //returns a clone of this player with the same brian
    public Player clone()
    {
        Player clone = new Player();
        clone.brain   = brain.clone();
        clone.fitness = fitness;
        clone.brain.generateNetwork();
        return clone;
    }
    
    //returns a clone of this player with the same brian and same random seeds used so all of the asteroids will be in  the same positions
    public Player cloneForReplay()
    {
        Player clone = new Player(SeedUsed);
        clone.brain     = brain.clone();
        clone.fitness   = fitness;
        clone.bestScore = score;
        clone.seedsUsed = (ArrayList) seedsUsed.clone();
        clone.brain.generateNetwork();
        return clone;
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------  
    public Player crossover(Player parent2)
    {
        Player child = new Player();
        child.brain = brain.crossover(parent2.brain);
        child.brain.generateNetwork();
        return child;
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------  
    
    //looks in 8 directions to find asteroids
    public void look()
    {
        vision = new double[33];
        //look left
        Vector2d direction;
        for (int i = 0; i < vision.length - 1; i += 2)
        {
            double angle = rotation + i * (Math.PI / 8);
            direction = new Vector2d(Math.cos(angle), Math.sin(angle));
            direction.mul(10);
            lookInDirection(direction, i);
        }
        
        
        if (canShoot && vision[0] != 0)
        {
            vision[32] = 1;
        }
        else
        {
            vision[32] = 0;
        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------  
    
    
    public double lookInDirection(Vector2d direction, int visionPos)
    {
        //set up a temp array to hold the values that are going to be passed to the main vision array
        
        Vector2d position = new Vector2d(pos.x, pos.y);//the position where we are currently looking for food or tail or wall
        double   distance = 0;
        //move once in the desired direction before starting 
        position.add(direction);
        distance += 1;
        Vector2d looped = new Vector2d(0, 0);
        //look in the direction until you reach a wall
        while (distance < 60)
        {//!(position.x < 400 || position.y < 0 || position.x >= 800 || position.y >= 400)) {
            
            
            for (Asteroid a : asteroids)
            {
                if (a.lookForHit(position))
                {
                    
                    vision[visionPos] = 1 / distance;
                    Asteroid asteroidHit = a.getAsteroid(position);
                    //vision[visionPos+1] = map(asteroidHit.size, 1, 3, 0, 1);
                    
                    
                    Vector2d towardsPlayer = new Vector2d(pos.x - asteroidHit.pos.x - looped.x, pos.y - asteroidHit.pos.y - looped.x);
                    towardsPlayer.normalize();
                    double redShift = asteroidHit.vel.dot(towardsPlayer);
                    vision[visionPos + 1] = redShift;
                }
            }
            
            //look further in the direction
            position.add(direction);
            
            //loop it
            if (position.y < -50)
            {
                position.y += screenHeight() + 100;
                looped.y += screenHeight() + 100;
            }
            else if (position.y > screenHeight() + 50)
            {
                position.y -= screenHeight() - 100;
                looped.y -= screenHeight() + 100;
            }
            if (position.x < -50)
            {
                position.x += screenWidth() + 100;
                looped.x += screenWidth() + 100;
            }
            else if (position.x > screenWidth() + 50)
            {
                position.x -= screenWidth() + 100;
                looped.x -= screenWidth() + 100;
            }
            
            
            distance += 1;
        }
        return 0;
    }
    
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------      
    //convert the output of the neural network to actions
    public void think()
    {
        //get the output of the neural network
        decision = brain.feedForward(vision);
        
        //output 0 is boosting
        boosting = decision[0] > 0.8;
        if (decision[1] > 0.8)
        {//output 1 is turn left
            spin = -0.08;
            
        }
        else
        {//cant turn right and left at the same time 
            if (decision[2] > 0.8)
            {//output 2 is turn right
                spin = 0.08;
                
            }
            else
            {//if neither then dont turn
                spin = 0;
            }
        }
        //shooting
        if (decision[3] > 0.8)
        {//output 3 is shooting
            shoot();
        }
    }
}
