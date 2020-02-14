package pe;

import java.util.ArrayList;

public class RandomTest extends PixelEngine
{
    @Override
    protected boolean onUserCreate()
    {
        random().setSeed(1337);
    
        println("Next Int: -1460590454=%s", random().nextInt());
        println("Next Int [0, 100): 44=%s", random().nextInt(100));
        println("Next Int [20, 200): 79=%s", random().nextInt(20, 200));
    
        println("Next Long: -2317866942458877870=%s", random().nextLong());
        println("Next Long [0, Integer.MAX_VALUE * 2): 1786116305=%s", random().nextLong((long) Integer.MAX_VALUE * 2));
        println("Next Long [Integer.MAX_VALUE, Long.MAX_VALUE): 7493871844812409706=%s", random().nextLong(Integer.MAX_VALUE, Long.MAX_VALUE));
    
        println("Next Float: 0.6410989=%s", random().nextFloat());
        println("Next Float [0F, 10F): 9.419557=%s", random().nextFloat(10F));
        println("Next Float [-1F, 1F): 0.8421564=%s", random().nextFloat(-1F, 1F));
    
        println("Next Double: 0.14807938070711846=%s", random().nextDouble());
        println("Next Double [0F, 10F): 3.187595156771933=%s", random().nextDouble(10.0));
        println("Next Double [-1F, 1F): 0.6260251135502299=%s", random().nextDouble(-1.0, 1.0));
    
        println("Next Index {1, 2, 3, 4}: 3=%s", random().nextIndex(new int[] {1, 2, 3, 4}));
        println("Next Index {1L, 2L, 3L, 4L}: 4L=%s", random().nextIndex(new long[] {1, 2, 3, 4}));
        println("Next Index {1F, 2F, 3F, 4F}: 1F=%s", random().nextIndex(new float[] {1, 2, 3, 4}));
        println("Next Index {1D, 2D, 3D, 4D}: 4D=%s", random().nextIndex(new double[] {1, 2, 3, 4}));
    
        ArrayList<Integer> squares = new ArrayList<>();
        for (int i = 0; i < 200; i++)
        {
            squares.add(i * i);
        }
        println("Next Index [1, 2, 4, 8, ..., 200^2]: 11025=%s", random().nextIndex(squares));
    
        println("Choose (10, 20, 30): 10=%s", random().choose(10, 20, 30));
        
        return false;
    }
    
    public static void main(String[] args)
    {
        start(new RandomTest());
    }
}
