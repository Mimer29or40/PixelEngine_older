package pe.gui.property;

import pe.Color;

import static pe.PixelEngine.print;

public class PropertyTests
{
    public static void main(String[] args)
    {
        print("one Set 0");
        one.set(0);
        print("one Set 1");
        one.set(1);
        
        print("string Set \"\"");
        string.set("");
        print("string Set \"New String\"");
        string.set("New String");
        
        print("pixel Set null");
        pixel.set(new Color());
        print("pixel Set (255, 255, 255)");
        pixel.set(new Color(255, 255, 255));
    }
    
    static IntegerProperty one = new IntegerProperty()
    {
        @Override
        protected void changed(Integer prev, Integer value)
        {
            oneChanged(prev, value);
        }
    };
    
    static void oneChanged(int prev, int value)
    {
        print("one Changed");
    }
    
    static StringProperty string = new StringProperty()
    {
        @Override
        protected void changed(String prev, String value)
        {
            stringChanged(prev, value);
        }
    };
    
    static void stringChanged(String prev, String value)
    {
        print("string Changed");
    }
    
    static PixelProperty pixel = new PixelProperty()
    {
        @Override
        protected void changed(Color prev, Color value)
        {
            pixelChanged(prev, value);
        }
    };
    
    static void pixelChanged(Color prev, Color value)
    {
        print("pixel Changed");
    }
}
