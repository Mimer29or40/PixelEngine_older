package pe.neat;

@SuppressWarnings("unused")
public class NeatTest
{
    private static ISetupFunc      setupFunc;
    private static ITestFunc       testFunc;
    private static ISetupArrayFunc setupArrayFunc;
    private static ITestArrayFunc  testArrayFunc;
    
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
        Counter[] nodeInnovations = new Counter[count];
        Counter[] connInnovations = new Counter[count];
        for (int i = 0; i < count; i++) nodeInnovations[i] = new Counter();
        for (int i = 0; i < count; i++) connInnovations[i] = new Counter();
    
        Genome[] genomes = new Genome[count];
        for (int i = 0; i < count; i++) genomes[i] = new Genome();
    
        if (setupFunc != null) setupFunc.setup(genomes[0], nodeInnovations[0], connInnovations[0]);
        if (setupArrayFunc != null) setupArrayFunc.setup(genomes, nodeInnovations, connInnovations);

        if (output)
        {
            if (count > 1)
            {
                for (int i = 0; i < count; i++)
                {
                     drawer.generateSprite(genomes[i]).saveSprite(String.format("out/%s_%s_before.png", name, i));
                }
            }
            else
            {
                 drawer.generateSprite(genomes[0]).saveSprite(String.format("out/%s_before.png", name));
            }
        }
    
        if (testFunc != null) testFunc.test(genomes[0], nodeInnovations[0], connInnovations[0]);
        if (testArrayFunc != null) testArrayFunc.test(genomes, nodeInnovations, connInnovations);
    
        if (output)
        {
            if (count > 1)
            {
                for (int i = 0; i < count; i++)
                {
                     drawer.generateSprite(genomes[i]).saveSprite(String.format("out/%s_%s_after.png", name, i));
                }
            }
            else
            {
                 drawer.generateSprite(genomes[0]).saveSprite(String.format("out/%s_after.png", name));
            }
        }
    }
    
    public static void run(String name, boolean output, GenomeDrawer drawer)
    {
        run(name, 1, output, drawer);
    }
    
    public static void run(String name, int count, boolean output)
    {
        run(name, count, output, new GenomeDrawer());
    }
    
    public static void run(String name, boolean output)
    {
        run(name, 1, output);
    }
    
    public interface ISetupFunc
    {
        void setup(Genome genome, Counter nodeInnovation, Counter connInnovation);
    }
    
    public interface ITestFunc
    {
        void test(Genome genome, Counter nodeInnovation, Counter connInnovation);
    }
    
    public interface ISetupArrayFunc
    {
        void setup(Genome[] genome, Counter[] nodeInnovation, Counter[] connInnovation);
    }
    
    public interface ITestArrayFunc
    {
        void test(Genome[] genome, Counter[] nodeInnovation, Counter[] connInnovation);
    }
}
