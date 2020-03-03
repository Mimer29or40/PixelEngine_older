package pe.neat;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import pe.Keyboard;
import pe.PixelEngine;
import pe.Random;

import java.util.HashSet;
import java.util.Set;

// import static pe.neat.PEX_Neat.setOrganismFactory;
// import static pe.neat.PEX_Neat.setPopulationSize;

public class GameTest extends PixelEngine
{
    // ---------- Game Constants ----------
    public static final double  rocketMaxVel  = 500;
    public static final double  rocketMaxAcc  = 100;
    public static final int     rocketWidth   = 5;
    public static final int     rocketHeight  = 10;
    public static final double  rocketDAng    = 8;
    public static final double  rocketDrift   = 0.9999;
    public static final boolean rocketCollide = true;
    
    public static final double bulletSpeed = 200;
    public static final double bulletLife  = 0.85;
    public static final double bulletRate  = 0.125;
    
    public static final double asteroidSize  = 20;
    public static final double asteroidSpeed = 10;
    
    public static final int rayCount = 8;
    
    public static boolean debug = false;
    
    @Override
    protected void setup()
    {
        size(200, 200, 2, 2);
    
        Counter nodeInno = new Counter();
        Counter connInno = new Counter();
    
        Genome initialBrain = new Genome();
    
        for (int i = 0; i < rayCount + 1; i++) initialBrain.addNode(new Node(nodeInno.inc(), Node.Type.INPUT, 0));
        for (int i = 0; i < 4; i++) initialBrain.addNode(new Node(nodeInno.inc(), Node.Type.OUTPUT, 1));
    
        Node bias = initialBrain.addNode(new Node(nodeInno.inc(), Node.Type.BIAS, 0));
        
        for (int j = 0, ni = initialBrain.inputSize(), nj = initialBrain.outputSize(); j < nj; j++)
        {
            for (int i = 0; i < ni; i++)
            {
                initialBrain.addConnection(new Connection(connInno.inc(), i, ni + j, 0, true));
            }
            initialBrain.addConnection(new Connection(connInno.inc(), bias.id, ni + j, 0, true));
        }
        
        PEX_Neat.setDefaultOrganism(() -> new Game(initialBrain.copy()));
        PEX_Neat.setPopulationSize(10);
        PEX_Neat.random.setSeed(10);
        
        disableExtension("PEX_CBNeat");
    }
    
    @Override
    protected void draw(double elapsedTime)
    {
        if (Keyboard.ESCAPE.down()) stop();
        
        if (Keyboard.D.down()) debug = !debug;
    }
    
    public static void main(String[] args)
    {
        start(new GameTest());
    }
    
    public static class Bullet
    {
        public final Vector2d pos = new Vector2d();
        public final double   dir;
        public       double   time;
        
        public Bullet(Vector2dc pos, double dir)
        {
            this.pos.set(pos);
            this.dir  = dir;
            this.time = 0;
        }
    }
    
    public static class Asteroid
    {
        public final Vector2d pos = new Vector2d();
        public final double   dir;
        public final double   speed;
        public final double   radius;
        
        public Asteroid(Vector2dc pos, double dir, double speed, double radius)
        {
            this.pos.set(pos);
            this.dir    = dir;
            this.speed  = speed;
            this.radius = radius;
        }
    }
    
    @SuppressWarnings("FieldCanBeLocal")
    public static class Game extends Organism
    {
        public final Random random;
    
        public final Vector2d rocketPos = new Vector2d(-10, -10);
        public final Vector2d rocketVel = new Vector2d();
        public final Vector2d rocketAcc = new Vector2d();
        public       double   rocketAng = 0;
    
        public final Set<Bullet> bullets = new HashSet<>();
    
        public final Set<Asteroid> asteroids = new HashSet<>();
    
        private double lastFire = 0.0;
    
        private int bulletsFired = 0;
        private int bulletsHit   = 0;
    
        private boolean accelerate;
        private boolean rotateRight;
        private boolean rotateLeft;
        private boolean fire;
    
        public Game(Genome initialGenome)
        {
            super(initialGenome);
        
            random = new Random();
            rocketPos.set(screenWidth() / 2.0, screenHeight() / 2.0);
        }
    
        @Override
        public void gatherInputs(double elapsedTime, boolean userPlaying)
        {
            Vector2dc[] rays = fireRays();
            for (int i = 0, n = this.inputs.length - 1; i < n; i++)
            {
                this.inputs[i] = this.rocketPos.distance(rays[i]) / 1000;
            }
            this.inputs[this.inputs.length - 1] = lastFire > 0 ? 1 : 0;
        }
    
        @Override
        public void makeDecision(double elapsedTime, boolean userPlaying)
        {
            if (!alive) return;
        
            if (userPlaying)
            {
                accelerate  = Keyboard.UP.held();
                rotateRight = Keyboard.RIGHT.held();
                rotateLeft  = Keyboard.LEFT.held();
                fire        = Keyboard.SPACE.held();
            }
            else
            {
                accelerate  = outputs[0] > 0.8;
                rotateRight = outputs[1] > 0.8;
                rotateLeft  = outputs[2] > 0.8;
                fire        = outputs[3] > 0.8;
            }
        
            if (lastFire > 0)
            {
                fire = false;
                lastFire -= elapsedTime;
            }
            if (fire) lastFire = bulletRate;
        
            // ---------- ROCKET ----------
            if (rotateRight) rocketAng += rocketDAng * elapsedTime;
            if (rotateLeft) rocketAng -= rocketDAng * elapsedTime;
        
            double cos = Math.cos(rocketAng);
            double sin = Math.sin(rocketAng);
        
            if (accelerate)
            {
                rocketAcc.x += cos * rocketMaxAcc;
                rocketAcc.y += sin * rocketMaxAcc;
            }
            
            rocketVel.x += rocketAcc.x * elapsedTime;
            rocketVel.y += rocketAcc.y * elapsedTime;
        
            if (rocketVel.length() > rocketMaxVel) rocketVel.normalize().mul(rocketMaxVel);
        
            rocketPos.x += rocketVel.x * elapsedTime;
            rocketPos.y += rocketVel.y * elapsedTime;
        
            rocketAcc.set(0);
            rocketVel.mul(rocketDrift);
        
            overlapScreen(rocketPos);
        
            if (fire)
            {
                bullets.add(new Bullet(rocketPos, rocketAng));
                bulletsFired++;
            }
        
            // ---------- BULLETS ----------
            Set<Bullet> badBullets = new HashSet<>();
            for (Bullet bullet : bullets)
            {
                cos = Math.cos(bullet.dir);
                sin = Math.sin(bullet.dir);
            
                bullet.pos.x += cos * bulletSpeed * elapsedTime;
                bullet.pos.y += sin * bulletSpeed * elapsedTime;
                
                overlapScreen(bullet.pos);
                
                bullet.time += elapsedTime;
                if (bullet.time > bulletLife) badBullets.add(bullet);
            }
            bullets.removeAll(badBullets);
            
            // ---------- ASTEROID ----------
            Set<Asteroid> newAsteroids = new HashSet<>();
            Set<Asteroid> badAsteroids = new HashSet<>();
            for (Asteroid asteroid : asteroids)
            {
                cos = Math.cos(asteroid.dir);
                sin = Math.sin(asteroid.dir);
                
                asteroid.pos.x += cos * asteroid.speed * elapsedTime;
                asteroid.pos.y += sin * asteroid.speed * elapsedTime;
                
                overlapScreen(asteroid.pos);
                
                if (rocketCollide)
                {
                    // TODO - Make a way to automatically check overlaps
                    if (playerCollide(asteroid.pos.x, asteroid.pos.y, asteroid.radius)) alive = false;
                    if (asteroid.pos.x + asteroid.radius >= screenWidth() && playerCollide(asteroid.pos.x + screenWidth(), asteroid.pos.y, asteroid.radius)) alive = false;
                    if (asteroid.pos.x - asteroid.radius < 0 && playerCollide(asteroid.pos.x + screenWidth(), asteroid.pos.y, asteroid.radius)) alive = false;
                    if (asteroid.pos.y + asteroid.radius >= screenHeight() && playerCollide(asteroid.pos.x, asteroid.pos.y - screenWidth(), asteroid.radius)) alive = false;
                    if (asteroid.pos.y - asteroid.radius < 0 && playerCollide(asteroid.pos.x, asteroid.pos.y + screenWidth(), asteroid.radius)) alive = false;
                }
                
                badBullets = new HashSet<>();
                for (Bullet bullet : bullets)
                {
                    if (asteroid.pos.distance(bullet.pos) < asteroid.radius)
                    {
                        score++;
                        bulletsHit++;
                        if (asteroid.radius > 5)
                        {
                            newAsteroids.add(new Asteroid(asteroid.pos, random.nextDouble(2.0 * Math.PI), asteroid.speed * 1.5, asteroid.radius * 0.5));
                            newAsteroids.add(new Asteroid(asteroid.pos, random.nextDouble(2.0 * Math.PI), asteroid.speed * 1.5, asteroid.radius * 0.5));
                        }
                        badBullets.add(bullet);
                        badAsteroids.add(asteroid);
                    }
                }
                bullets.removeAll(badBullets);
            }
            asteroids.addAll(newAsteroids);
            asteroids.removeAll(badAsteroids);
            
            if (asteroids.isEmpty()) genAsteroids();
        }
        
        @Override
        public void draw(double elapsedTime)
        {
            clear();
    
            // ---------- ROCKET ----------
            drawRocket(rocketPos.x, rocketPos.y, accelerate);
            if (rocketPos.x + Math.max(rocketWidth, rocketHeight) >= screenWidth()) drawRocket(rocketPos.x - screenWidth(), rocketPos.y, accelerate);
            if (rocketPos.x - Math.max(rocketWidth, rocketHeight) < 0) drawRocket(rocketPos.x + screenWidth(), rocketPos.y, accelerate);
            if (rocketPos.y + Math.max(rocketWidth, rocketHeight) >= screenHeight()) drawRocket(rocketPos.x, rocketPos.y - screenHeight(), accelerate);
            if (rocketPos.y - Math.max(rocketWidth, rocketHeight) < 0) drawRocket(rocketPos.x, rocketPos.y + screenHeight(), accelerate);
    
            noFill();
            stroke(255);
    
            // ---------- BULLETS ----------
            for (Bullet bullet : bullets) point((int) bullet.pos.x, (int) bullet.pos.y);
    
            // ---------- ASTEROID ----------
            for (Asteroid asteroid : asteroids)
            {
                circle((int) asteroid.pos.x, (int) asteroid.pos.y, (int) asteroid.radius);
                if (asteroid.pos.x + asteroid.radius >= screenWidth()) circle((int) asteroid.pos.x - screenWidth(), (int) asteroid.pos.y, (int) asteroid.radius);
                if (asteroid.pos.x - asteroid.radius < 0) circle((int) asteroid.pos.x + screenWidth(), (int) asteroid.pos.y, (int) asteroid.radius);
                if (asteroid.pos.y + asteroid.radius >= screenHeight()) circle((int) asteroid.pos.x, (int) asteroid.pos.y - screenHeight(), (int) asteroid.radius);
                if (asteroid.pos.y - asteroid.radius < 0) circle((int) asteroid.pos.x, (int) asteroid.pos.y + screenHeight(), (int) asteroid.radius);
            }
            
            if (debug)
            {
                for (Vector2dc ray : fireRays())
                {
                    line((int) rocketPos.x(), (int) rocketPos.y(), (int) ray.x(), (int) ray.y());
                    circle((int) ray.x(), (int) ray.y(), 3);
                }
            }
    
            string(1, 1, "Score: " + score);
        }
        
        @Override
        public void calculateFitness()
        {
            double hitRate = Math.min((double) bulletsHit / (double) bulletsFired, 1);
    
            this.fitness = (score + 1) * 10;
            this.fitness *= lifeTime;
            this.fitness *= hitRate * hitRate;//includes hitrate to encourage aiming
        }
        
        private void overlapScreen(Vector2d pos)
        {
            if (pos.x() < 0) pos.x = screenWidth() - 1;
            if (pos.x() > screenWidth() - 1) pos.x = 0;
            if (pos.y() < 0) pos.y = screenHeight() - 1;
            if (pos.y() > screenHeight() - 1) pos.y = 0;
        }
        
        private void genAsteroids()
        {
            Vector2d vec = new Vector2d();
            for (int i = 0; i < 4; i++)
            {
                vec.set(random.nextDouble(screenWidth()), random.nextDouble(screenHeight()));
                while (vec.distance(rocketPos) < asteroidSize + Math.max(rocketWidth, rocketHeight) + 20)
                {
                    vec.set(random.nextDouble(screenWidth()), random.nextDouble(screenHeight()));
                }
                asteroids.add(new Asteroid(vec, random.nextDouble(2.0 * Math.PI), asteroidSpeed * (100D + score) / 100D, asteroidSize));
            }
            bullets.clear();
        }
        
        private boolean playerCollide(double x, double y, double radius)
        {
            double cos = Math.cos(rocketAng);
            double sin = Math.sin(rocketAng);
            
            int x1 = (int) (rocketPos.x - cos * rocketHeight / 2 + sin * rocketWidth / 2);
            int y1 = (int) (rocketPos.y - sin * rocketHeight / 2 - cos * rocketWidth / 2);
            
            int x2 = (int) (rocketPos.x - cos * rocketHeight / 2 - sin * rocketWidth / 2);
            int y2 = (int) (rocketPos.y - sin * rocketHeight / 2 + cos * rocketWidth / 2);
            
            int x3 = (int) (rocketPos.x + cos * rocketHeight / 2);
            int y3 = (int) (rocketPos.y + sin * rocketHeight / 2);
    
            return circleSegIntersect(x1, y1, x2, y2, x, y, radius) || circleSegIntersect(x2, y2, x3, y3, x, y, radius) || circleSegIntersect(x3, y3, x1, y1, x, y, radius);
        }
        
        private void drawRocket(double x, double y, boolean accelerate)
        {
            double cos = Math.cos(rocketAng);
            double sin = Math.sin(rocketAng);
    
            int x1 = (int) (x - cos * rocketHeight / 2 + sin * rocketWidth / 2);
            int y1 = (int) (y - sin * rocketHeight / 2 - cos * rocketWidth / 2);
    
            int x2 = (int) (x - cos * rocketHeight / 2 - sin * rocketWidth / 2);
            int y2 = (int) (y - sin * rocketHeight / 2 + cos * rocketWidth / 2);
    
            int x3 = (int) (x + cos * rocketHeight / 2);
            int y3 = (int) (y + sin * rocketHeight / 2);
    
            noFill();
            stroke(255);
    
            triangle(x1, y1, x2, y2, x3, y3);
    
            if (accelerate)
            {
        
                x1 = (int) (x - cos * rocketHeight / 2 + sin * rocketWidth / 2 * 0.95);
                y1 = (int) (y - sin * rocketHeight / 2 - cos * rocketWidth / 2 * 0.95);
        
                x2 = (int) (x - cos * rocketHeight / 2 - sin * rocketWidth / 2 * 0.95);
                y2 = (int) (y - sin * rocketHeight / 2 + cos * rocketWidth / 2 * 0.95);
                
                x3 = (int) (x - cos * rocketHeight / 2 * 1.35);
                y3 = (int) (y - sin * rocketHeight / 2 * 1.35);
        
                triangle(x1, y1, x2, y2, x3, y3);
            }
        }
    
        private boolean circleSegIntersect(double x1, double y1, double x2, double y2, double px, double py, double radius)
        {
            x2 -= x1;
            y2 -= y1;
            px -= x1;
            py -= y1;
        
            double dot = px * x2 + py * y2;
            double projectedLenSq;
            if (dot <= 0.0)
            {
                projectedLenSq = 0.0;
            }
            else
            {
                px             = x2 - px;
                py             = y2 - py;
                dot            = px * x2 + py * y2;
                projectedLenSq = dot <= 0.0 ? 0.0 : dot * dot / (x2 * x2 + y2 * y2);
            }
            return px * px + py * py - projectedLenSq - (radius * radius) <= 0;
        }
    
        private Vector2dc circleSegIntersect(Vector2dc a, Vector2dc b, Vector2dc c, double radius)
        {
            double abx = b.x() - a.x();
            double aby = b.y() - a.y();
            double acx = c.x() - a.x();
            double acy = c.y() - a.y();
        
            double lenSq = (abx * abx) + (aby * aby);
            double cross = (acx * aby) - (acy * abx);
            double scale = cross / lenSq;
            
            double offset = radius * radius / lenSq - scale * scale;
            if (offset < 0) return b;
            offset = Math.sqrt(offset);
            
            double p1x = abx * -offset + (-aby * scale) + c.x();
            double p1y = aby * -offset + (+abx * scale) + c.y();
            if (a.distanceSquared(p1x, p1y) <= lenSq && b.distanceSquared(p1x, p1y) <= lenSq) return new Vector2d(p1x, p1y);
            return b;
        }
        
        public Vector2dc[] fireRays()
        {
            Vector2dc[] results = new Vector2dc[rayCount];
            Vector2d    temp    = new Vector2d();
    
            for (int i = 0; i < rayCount; i++)
            {
                double angle = (((double) i / (double) rayCount) * 2.0 * Math.PI) + rocketAng;
        
                double cos  = Math.cos(angle);
                double sin  = Math.sin(angle);
                double dist = 1000.0;
        
                Vector2dc endPos = new Vector2d(rocketPos).add(cos * dist, sin * dist);
        
                for (Asteroid asteroid : asteroids)
                {
                    endPos = circleSegIntersect(rocketPos, endPos, temp.set(asteroid.pos), asteroid.radius);
                    dist   = Math.min(dist, endPos.distanceSquared(rocketPos));
                    
                    if (asteroid.pos.x + asteroid.radius >= screenWidth())
                    {
                        endPos = circleSegIntersect(rocketPos, endPos, temp.set(asteroid.pos.x() - screenWidth(), asteroid.pos.y()), asteroid.radius);
                        dist   = Math.min(dist, endPos.distanceSquared(rocketPos));
                    }
                    if (asteroid.pos.x - asteroid.radius < 0)
                    {
                        endPos = circleSegIntersect(rocketPos, endPos, temp.set(asteroid.pos.x() + screenWidth(), asteroid.pos.y()), asteroid.radius);
                        dist   = Math.min(dist, endPos.distanceSquared(rocketPos));
                    }
                    if (asteroid.pos.y + asteroid.radius >= screenHeight())
                    {
                        endPos = circleSegIntersect(rocketPos, endPos, temp.set(asteroid.pos.x(), asteroid.pos.y() - screenWidth()), asteroid.radius);
                        dist   = Math.min(dist, endPos.distanceSquared(rocketPos));
                    }
                    if (asteroid.pos.y - asteroid.radius < 0)
                    {
                        endPos = circleSegIntersect(rocketPos, endPos, temp.set(asteroid.pos.x(), asteroid.pos.y() + screenWidth()), asteroid.radius);
                        dist   = Math.min(dist, endPos.distanceSquared(rocketPos));
                    }
                }
                results[i] = endPos;
            }
            
            return results;
        }
    }
}
