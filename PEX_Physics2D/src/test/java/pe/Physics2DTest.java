package pe;

import pe.phx2D.model.SimList;
import pe.phx2D.util.*;

import java.util.Arrays;

public class Physics2DTest extends PixelEngine
{
    private static class Test implements Printable
    {
        static
        {
            Printable.addElement(Test.class, "value", Test::getValue, true);
            Printable.addElement(Test.class, "other", Test::getValue, false);
        }
        
        public double value;
        
        public Test()
        {
            this.value = 1.234;
        }
        
        public double getValue()
        {
            return this.value;
        }
        
        public void setValue(double value)
        {
            this.value = value;
        }
    }
    
    private static class Test1 implements Printable
    {
        static
        {
            Printable.addElement(Test1.class, "test", Test1::getTest, true);
            Printable.addElement(Test1.class, "value", Test1::getValue, true);
            Printable.addElement(Test1.class, "other", Test1::getValue, false);
        }
        
        public Test   test;
        public double value;
        
        public Test1(Test test)
        {
            this.test = test;
            this.value = 1.234;
        }
        
        public Test getTest()
        {
            return this.test;
        }
        
        public double getValue()
        {
            return this.value;
        }
        
        public void setValue(double value)
        {
            this.value = value;
        }
    }
    
    @Override
    protected boolean onUserCreate()
    {
        return true;
    }
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        return true;
    }
    
    @Override
    protected void onUserDestroy()
    {
    
    }
    
    public static void main(String[] args)
    {
        print(new Test().toShortString());
        print(new Test().toLongString());
        print(new Test1(new Test()).toShortString());
        print(new Test1(new Test()).toLongString());
        print(new Event<>(null, "Test", 1.234567890).toShortString());
        print(new Event<>(null, "Test", 1.234567890).toLongString());
        print(new ParameterInteger(null, "Test", () -> 1, (v) -> new Object(), Arrays.asList("one", "two"), Arrays.asList(1, 2)).toShortString());
        print(new ParameterInteger(null, "Test", () -> 1, (v) -> new Object(), Arrays.asList("one", "two"), Arrays.asList(1, 2)).toLongString());
        print(new ParameterBoolean(null, "Test", () -> true, (v) -> new Object(), Arrays.asList("true", "false"), Arrays.asList(true, false)).toShortString());
        print(new ParameterBoolean(null, "Test", () -> true, (v) -> new Object(), Arrays.asList("true", "false"), Arrays.asList(true, false)).toLongString());
        print(new ParameterString(null, "Test", () -> "true", (v) -> new Object(), Arrays.asList("one", "two"), Arrays.asList("true", "false")).toShortString());
        print(new ParameterString(null, "Test", () -> "true", (v) -> new Object(), Arrays.asList("one", "two"), Arrays.asList("true", "false")).toLongString());
        
        SimList          list      = new SimList();
        ParameterInteger parameter = new ParameterInteger(list, "int", () -> 1, (v) -> new Object());
        list.addParameter(parameter);
        new GenericObserver(list, (v) -> print("Value Changed: " + v));
        print(list.toLongString());
        parameter.setValue(10);
        print(list.toLongString());
        //start(new Physics2DTest());
        //start();
    }
}
