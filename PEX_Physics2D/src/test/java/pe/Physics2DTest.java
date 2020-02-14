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
        println(new Test().toShortString());
        println(new Test().toLongString());
        println(new Test1(new Test()).toShortString());
        println(new Test1(new Test()).toLongString());
        println(new Event<>(null, "Test", 1.234567890).toShortString());
        println(new Event<>(null, "Test", 1.234567890).toLongString());
        println(new ParameterInteger(null, "Test", () -> 1, (v) -> new Object(), Arrays.asList("one", "two"), Arrays.asList(1, 2)).toShortString());
        println(new ParameterInteger(null, "Test", () -> 1, (v) -> new Object(), Arrays.asList("one", "two"), Arrays.asList(1, 2)).toLongString());
        println(new ParameterBoolean(null, "Test", () -> true, (v) -> new Object(), Arrays.asList("true", "false"), Arrays.asList(true, false)).toShortString());
        println(new ParameterBoolean(null, "Test", () -> true, (v) -> new Object(), Arrays.asList("true", "false"), Arrays.asList(true, false)).toLongString());
        println(new ParameterString(null, "Test", () -> "true", (v) -> new Object(), Arrays.asList("one", "two"), Arrays.asList("true", "false")).toShortString());
        println(new ParameterString(null, "Test", () -> "true", (v) -> new Object(), Arrays.asList("one", "two"), Arrays.asList("true", "false")).toLongString());
    
        SimList          list      = new SimList();
        ParameterInteger parameter = new ParameterInteger(list, "int", () -> 1, (v) -> new Object());
        list.addParameter(parameter);
        new GenericObserver(list, (v) -> println("Value Changed: " + v));
        println(list.toLongString());
        parameter.setValue(10);
        println(list.toLongString());
        //start(new Physics2DTest());
        //start();
    }
}
