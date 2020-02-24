package pe.neat;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import pe.Keyboard;
import pe.PixelEngine;
import pe.Random;
import pe.color.Color;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static pe.neat.PEX_Neat.setOrganismFactory;
import static pe.neat.PEX_Neat.setPopulationSize;

public class GameTest extends PixelEngine
{
    // ---------- Game Constants ----------
    public static final double  rocketMaxVel  = 500;
    public static final double  rocketMaxAcc  = 100;
    public static final int     rocketWidth   = 5;
    public static final int     rocketHeight  = 10;
    public static final double  rocketDAng    = 0.01;
    public static final double  rocketDrift   = 0.9999;
    public static final boolean rocketCollide = true;
    
    public static final double bulletSpeed = 200;
    public static final double bulletLife  = 0.85;
    
    public static final double asteroidSize  = 20;
    public static final double asteroidSpeed = 10;
    
    public static boolean debug = false;
    
    @Override
    protected boolean setup()
    {
        Supplier<Genome> createBrain = () -> {
            Counter nodeInno = new Counter();
            Counter connInno = new Counter();
            Genome  genome   = new Genome();
            
            for (int i = 0; i < 8; i++)
            {
                genome.addNode(new Node(nodeInno.inc(), Node.Type.INPUT, 0));
            }
            
            for (int i = 0; i < 4; i++)
            {
                genome.addNode(new Node(nodeInno.inc(), Node.Type.OUTPUT, 1));
            }
            
            Node bias = genome.addNode(new Node(nodeInno.inc(), Node.Type.BIAS, 0));
            
            for (int j = 0, ni = genome.inputSize(), nj = genome.outputSize(); j < nj; j++)
            {
                for (int i = 0; i < ni; i++)
                {
                    genome.addConnection(new Connection(connInno.inc(), i, ni + j, 0, true));
                }
                genome.addConnection(new Connection(connInno.inc(), bias.id, ni + j, 0, true));
            }
            
            return genome;
        };
        
        setOrganismFactory(() -> new Game(createBrain));
        
        setPopulationSize(10);
        
        PEX_Neat.showBestEachGen = true;
        
        return true;
    }
    
    @Override
    protected boolean draw(double elapsedTime)
    {
        renderer().clear();
    
        if (Keyboard.ESCAPE.down()) return false;
    
        if (Keyboard.F1.down()) debug = !debug;
    
        return true;
    }
    
    public static void main(String[] args)
    {
        start(new GameTest(), 200, 200, 2, 2);
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
        
        private boolean accelerate;
        private boolean rotateRight;
        private boolean rotateLeft;
        private boolean fire;
        
        public Game(Supplier<Genome> initialGenome)
        {
            super(initialGenome);
            
            random = new Random();
            rocketPos.set(screenWidth() / 2.0, screenHeight() / 2.0);
        }
        
        @Override
        public void gatherInputs(double elapsedTime)
        {
            Vector2dc[] rays = fireRays();
            for (int i = 0, n = this.inputs.length; i < n; i++)
            {
                this.inputs[i] = this.rocketPos.distance(rays[i]);
            }
        }
        
        @Override
        public void makeDecision(double elapsedTime, int decision)
        {
            // accelerate   = Keyboard.W.held() || Keyboard.UP.held();
            // rotateRight  = Keyboard.D.held() || Keyboard.RIGHT.held();
            // rotateLeft   = Keyboard.A.held() || Keyboard.LEFT.held();
            // fire         = Keyboard.SPACE.down();
            accelerate  = decision == 0;
            rotateRight = decision == 1;
            rotateLeft  = decision == 2;
            fire        = decision == 3;
            
            // ---------- ROCKET ----------
            if (rotateRight) rocketAng += rocketDAng;
            if (rotateLeft) rocketAng -= rocketDAng;
            
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
            
            if (fire) bullets.add(new Bullet(rocketPos, rocketAng));
            
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
            // ---------- ROCKET ----------
            drawRocket(rocketPos.x, rocketPos.y, accelerate);
            
            int dia = Math.max(rocketWidth, rocketHeight);
            
            if (rocketPos.x + dia >= screenWidth()) drawRocket(rocketPos.x - screenWidth(), rocketPos.y, accelerate);
            if (rocketPos.x - dia < 0) drawRocket(rocketPos.x + screenWidth(), rocketPos.y, accelerate);
            if (rocketPos.y + dia >= screenHeight()) drawRocket(rocketPos.x, rocketPos.y - screenHeight(), accelerate);
            if (rocketPos.y - dia < 0) drawRocket(rocketPos.x, rocketPos.y + screenHeight(), accelerate);
    
            // ---------- BULLETS ----------
            // for (Bullet bullet : bullets) PixelEngine.draw((int) bullet.pos.x, (int) bullet.pos.y);
            
            // ---------- ASTEROID ----------
            for (Asteroid asteroid : asteroids)
            {
                drawAsteroid(asteroid.pos.x, asteroid.pos.y, asteroid.radius);
                
                if (asteroid.pos.x + asteroid.radius >= screenWidth()) drawAsteroid(asteroid.pos.x - screenWidth(), asteroid.pos.y, asteroid.radius);
                if (asteroid.pos.x - asteroid.radius < 0) drawAsteroid(asteroid.pos.x + screenWidth(), asteroid.pos.y, asteroid.radius);
                if (asteroid.pos.y + asteroid.radius >= screenHeight()) drawAsteroid(asteroid.pos.x, asteroid.pos.y - screenHeight(), asteroid.radius);
                if (asteroid.pos.y - asteroid.radius < 0) drawAsteroid(asteroid.pos.x, asteroid.pos.y + screenHeight(), asteroid.radius);
            }
    
            if (debug)
            {
                for (Vector2dc ray : fireRays())
                {
                    renderer().line((int) rocketPos.x(), (int) rocketPos.y(), (int) ray.x(), (int) ray.y());
                    renderer().circle((int) ray.x(), (int) ray.y(), 3);
                }
            }
    
            renderer().string(1, 1, String.format("Score: %s", score));
    
            // if (!playing)
            // {
            //     drawScreenText("Press ENTER to Play");
            // }
            // else if (paused)
            // {
            //     drawScreenText("Paused");
            // }
        }
        
        @Override
        public void calculateFitness()
        {
            this.fitness = this.lifeTime + this.score * 10;
        }
        
        private void drawScreenText(String text)
        {
            int width  = textWidth(text);
            int height = textHeight(text);
    
            renderer().noStroke();
            renderer().fill(Color.GREY);
            renderer().rect((screenWidth() - width) / 2 - 2, (screenHeight() - height) / 2 - 2, width + 4, height + 4);
            renderer().stroke(Color.BLACK);
            renderer().string((screenWidth() - width) / 2, (screenHeight() - height) / 2, text);
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
                while (vec.distance(rocketPos) < asteroidSize + Math.max(rocketWidth, rocketHeight) + 10)
                {
                    vec.set(random.nextDouble(screenWidth()), random.nextDouble(screenHeight()));
                }
                
                asteroids.add(new Asteroid(vec, random.nextDouble(2.0 * Math.PI), asteroidSpeed * (100D + score) / 100D, asteroidSize));
            }
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
            
            return circleSegmentIntersect(x1, y1, x2, y2, x, y, radius) || circleSegmentIntersect(x2, y2, x3, y3, x, y, radius) ||
                   circleSegmentIntersect(x3, y3, x1, y1, x, y, radius);
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
    
            renderer().noFill();
            renderer().stroke(0);
            renderer().triangle(x1, y1, x2, y2, x3, y3);
    
            if (accelerate)
            {
        
                x1 = (int) (x - cos * rocketHeight / 2 + sin * rocketWidth / 2 * 0.95);
                y1 = (int) (y - sin * rocketHeight / 2 - cos * rocketWidth / 2 * 0.95);
        
                x2 = (int) (x - cos * rocketHeight / 2 - sin * rocketWidth / 2 * 0.95);
                y2 = (int) (y - sin * rocketHeight / 2 + cos * rocketWidth / 2 * 0.95);
        
                x3 = (int) (x - cos * rocketHeight / 2 * 1.35);
                y3 = (int) (y - sin * rocketHeight / 2 * 1.35);
    
                renderer().triangle(x1, y1, x2, y2, x3, y3);
            }
        }
        
        private void drawAsteroid(double x, double y, double radius)
        {
            // drawCircle((int) x, (int) y, (int) radius);
        }
        
        private boolean circleSegmentIntersect(double x1, double y1, double x2, double y2, double px, double py, double radius)
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
        
        private Vector2dc circleSegmentIntersect(Vector2dc a, Vector2dc b, Vector2dc c, double radius)
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
            if (a.distanceSquared(p1x, p1y) <= lenSq && b.distanceSquared(p1x, p1y) <= lenSq)
            {
                return new Vector2d(p1x, p1y);
            }
            return b;
        }
        
        public Vector2dc[] fireRays()
        {
            Vector2dc[] results = new Vector2dc[8];
            Vector2d    temp    = new Vector2d();
            
            for (int i = 0; i < 8; i++)
            {
                double angle = (((double) i / 8.0) * 2.0 * Math.PI) + rocketAng;
                
                double cos  = Math.cos(angle);
                double sin  = Math.sin(angle);
                double dist = 1000.0;
                
                Vector2dc endPos = new Vector2d(rocketPos).add(cos * dist, sin * dist);
                
                for (Asteroid asteroid : asteroids)
                {
                    endPos = circleSegmentIntersect(rocketPos, endPos, temp.set(asteroid.pos), asteroid.radius);
                    dist   = Math.min(dist, endPos.distanceSquared(rocketPos));
                    
                    if (asteroid.pos.x + asteroid.radius >= screenWidth())
                    {
                        endPos = circleSegmentIntersect(rocketPos, endPos, temp.set(asteroid.pos.x() - screenWidth(), asteroid.pos.y()), asteroid.radius);
                        dist   = Math.min(dist, endPos.distanceSquared(rocketPos));
                    }
                    if (asteroid.pos.x - asteroid.radius < 0)
                    {
                        endPos = circleSegmentIntersect(rocketPos, endPos, temp.set(asteroid.pos.x() + screenWidth(), asteroid.pos.y()), asteroid.radius);
                        dist   = Math.min(dist, endPos.distanceSquared(rocketPos));
                    }
                    if (asteroid.pos.y + asteroid.radius >= screenHeight())
                    {
                        endPos = circleSegmentIntersect(rocketPos, endPos, temp.set(asteroid.pos.x(), asteroid.pos.y() - screenWidth()), asteroid.radius);
                        dist   = Math.min(dist, endPos.distanceSquared(rocketPos));
                    }
                    if (asteroid.pos.y - asteroid.radius < 0)
                    {
                        endPos = circleSegmentIntersect(rocketPos, endPos, temp.set(asteroid.pos.x(), asteroid.pos.y() + screenWidth()), asteroid.radius);
                        dist   = Math.min(dist, endPos.distanceSquared(rocketPos));
                    }
                }
                results[i] = endPos;
            }
            
            return results;
        }
    }
}
