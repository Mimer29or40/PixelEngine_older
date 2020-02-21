package pe;

import pe.render.DrawPattern;

import static pe.PixelEngine.print;
import static pe.PixelEngine.println;

public class PatternTest
{
    public static void main(String[] args)
    {
        DrawPattern pattern1 = new DrawPattern(0xFFFFFFFF);
        ;
        DrawPattern pattern2 = new DrawPattern(0xAAAAAAAA);
        ;
        DrawPattern pattern3 = new DrawPattern(0xF0F0F0F0);
        ;
        
        for (int i = 0; i < 32; i++) print(pattern1.shouldDraw() ? "X" : "O");
        println();
        for (int i = 0; i < 32; i++) print(pattern2.shouldDraw() ? "X" : "O");
        println();
        for (int i = 0; i < 32; i++) print(pattern3.shouldDraw() ? "X" : "O");
        println();
    }
}
