package pe.neat;

import pe.PixelEngine;
import pe.Random;

@SuppressWarnings("unused")
public class NeatTest extends PixelEngine
{
    private static String       name;
    private static int          count;
    private static boolean      output;
    private static GenomeDrawer drawer;
    
    private static ISetupFunc      setupFunc;
    private static ITestFunc       testFunc;
    private static ISetupArrayFunc setupArrayFunc;
    private static ITestArrayFunc  testArrayFunc;
    
    @Override
    protected boolean setup()
    {
        Random random = new Random();
    
        Genome[] genomes = new Genome[count];
        for (int i = 0; i < count; i++) genomes[i] = new Genome();
    
        Counter[] nodeInnovations = new Counter[count];
        Counter[] connInnovations = new Counter[count];
        for (int i = 0; i < count; i++) nodeInnovations[i] = new Counter();
        for (int i = 0; i < count; i++) connInnovations[i] = new Counter();
    
        if (setupFunc != null) setupFunc.setup(random, genomes[0], nodeInnovations[0], connInnovations[0]);
        if (setupArrayFunc != null) setupArrayFunc.setup(random, genomes, nodeInnovations, connInnovations);
    
        if (output)
        {
            if (count > 1)
            {
                for (int i = 0; i < count; i++)
                {
                    drawer.generateGraph(genomes[i]).saveImage(String.format("out/%s_%s_before.png", name, i));
                }
            }
            else
            {
                drawer.generateGraph(genomes[0]).saveImage(String.format("out/%s_before.png", name));
            }
        }
    
        if (testFunc != null) testFunc.test(random, genomes[0], nodeInnovations[0], connInnovations[0]);
        if (testArrayFunc != null) testArrayFunc.test(random, genomes, nodeInnovations, connInnovations);
    
        if (output)
        {
            if (count > 1)
            {
                for (int i = 0; i < count; i++)
                {
                    drawer.generateGraph(genomes[i]).saveImage(String.format("out/%s_%s_after.png", name, i));
                }
            }
            else
            {
                drawer.generateGraph(genomes[0]).saveImage(String.format("out/%s_after.png", name));
            }
        }
        return false;
    }
    
    public static void setup(ISetupFunc setupFunc)
    {
        NeatTest.setupFunc = setupFunc;
    }
    
    public static void test(ITestFunc testFunc)
    {
        NeatTest.testFunc = testFunc;
    }
    
    public static void setup(ISetupArrayFunc setupArrayFunc)
    {
        NeatTest.setupArrayFunc = setupArrayFunc;
    }
    
    public static void test(ITestArrayFunc testArrayFunc)
    {
        NeatTest.testArrayFunc = testArrayFunc;
    }
    
    public static void run(String name, int count, boolean output, GenomeDrawer drawer)
    {
        NeatTest.name = name;
        NeatTest.count = count;
        NeatTest.output = output;
        NeatTest.drawer = drawer;
        
        start(new NeatTest());
    }
    
    public static void run(String name, int count, boolean output)
    {
        run(name, count, output, new GenomeDrawer());
    }
    
    public static void run(String name, int count, GenomeDrawer drawer)
    {
        run(name, count, true, drawer);
    }
    
    public static void run(String name, int count)
    {
        run(name, count, true, new GenomeDrawer());
    }
    
    public static void run(String name, boolean output, GenomeDrawer drawer)
    {
        run(name, 1, output, drawer);
    }
    
    public static void run(String name, boolean output)
    {
        run(name, 1, output);
    }
    
    public static void run(String name, GenomeDrawer drawer)
    {
        run(name, 1, true, drawer);
    }
    
    public static void run(String name)
    {
        run(name, 1, true);
    }
    
    public interface ISetupFunc
    {
        void setup(Random random, Genome genome, Counter nodeInnovation, Counter connInnovation);
    }
    
    public interface ITestFunc
    {
        void test(Random random, Genome genome, Counter nodeInnovation, Counter connInnovation);
    }
    
    public interface ISetupArrayFunc
    {
        void setup(Random random, Genome[] genomes, Counter[] nodeInnovations, Counter[] connInnovations);
    }
    
    public interface ITestArrayFunc
    {
        void test(Random random, Genome[] genomes, Counter[] nodeInnovations, Counter[] connInnovations);
    }
}
