package pe.phx2D.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Subject extends Nameable implements Observable
{
    static
    {
        Printable.addElement(Subject.class, "name", Subject::getName, true);
        Printable.addElement(Subject.class, "parameters", Subject::getParameters, true);
        Printable.addElement(Subject.class, "observers", Subject::getObservers, true);
    }
    
    protected Set<Observer>  observers;
    protected Set<Parameter> paramList;
    protected boolean        doBroadcast;
    protected boolean        isBroadcasting;
    protected List<Command>  commandList;
    
    public Subject(String name)
    {
        super(name);
        this.observers = new HashSet<>();
        this.paramList = new HashSet<>();
        this.doBroadcast = true;
        this.isBroadcasting = false;
        this.commandList = new ArrayList<>();
    }
    
    /**
     * Returns whether broadcasting is enabled for this Subject.
     *
     * @return whether broadcasting is enabled for this Subject
     *
     * @see Subject#setBroadcast
     */
    protected boolean getBroadcast()
    {
        return this.doBroadcast;
    }
    
    /**
     * Sets whether this Subject will broadcast events, typically used to temporarily disable broadcasting. Intended to be used in situations where a subclass overrides a method
     * that broadcasts an event. This allows the subclass to prevent the superclass broadcasting that event, so that the subclass can broadcast the event when the method is
     * completed.
     *
     * @param value whether this Subject should broadcast events
     *
     * @return the previous value
     */
    protected boolean setBroadcast(boolean value)
    {
        boolean saveBroadcast = this.doBroadcast;
        this.doBroadcast = value;
        return saveBroadcast;
    }
    
    /**
     * Execute the set of delayed commands to add/remove observers.
     * This addresses the issue that an Observer can call addObserver or removeObserver
     * during it's observe() method.
     */
    private void doCommands()
    {
        if (!this.isBroadcasting)
        {
            for (Command cmd : this.commandList)
            {
                if (cmd.action)
                {
                    this.observers.add(cmd.observer);
                }
                else
                {
                    this.observers.remove(cmd.observer);
                }
            }
            this.commandList.clear();
        }
    }
    
    /**
     * Adds the given Observer to the Subject's set of Observers, so that the Observer will be notified of changes in this Subject. An Observer may call this during its
     * {@link Observer#observe} method.
     *
     * @param observer the Observer to add
     */
    @Override
    public void addObserver(Observer observer)
    {
        this.commandList.add(new Command(true, observer));
        doCommands(); // if not broadcasting, this happens immediately
    }
    
    /**
     * Removes the Observer from the Subject's set of Observers. An Observer may call this during its {@link Observer#observe} method.
     *
     * @param observer the Observer to detach from list of Observers
     */
    @Override
    public void removeObserver(Observer observer)
    {
        this.commandList.add(new Command(false, observer));
        doCommands(); // if not broadcasting, this happens immediately
    }
    
    @Override
    public void broadcast(ObservableEvent evt)
    {
        if (this.doBroadcast)
        {
            this.isBroadcasting = true;
            try
            {
                // For debugging: can see events being broadcast here.
                //if (!this.getName().match(/.*GRAPH.*/i)) { console.log('broadcast '+evt); }
                this.observers.forEach(observer -> observer.observe(evt));
            }
            finally
            {
                this.isBroadcasting = false;
                // do add/remove commands afterwards, in case an Observer called addObserver or
                // removeObserver during observe()
                doCommands();
            }
        }
    }
    
    @Override
    public void broadcastParameter(String name)
    {
        Parameter p = getParam(name);
        if (p == null) throw new Error("unknown Parameter " + name);
        broadcast(p);
    }
    
    @Override
    public Set<Observer> getObservers()
    {
        return new HashSet<>(this.observers);
    }
    
    /**
     * Adds the Parameter to the list of this Subject's available Parameters.
     *
     * @param parameter the Parameter to add
     *
     * @throws RuntimeException if a Parameter with the same name already exists.
     */
    public void addParameter(Parameter parameter)
    {
        String    name = parameter.getName();
        Parameter p    = getParam(name);
        if (p != null) throw new RuntimeException("parameter " + name + " already exists: " + p);
        this.paramList.add(parameter);
    }
    
    /**
     * Returns the Parameter with the given name, or null if not found
     *
     * @param name name of parameter to search for
     *
     * @return the Parameter with the given name, or
     * null if not found
     */
    private Parameter getParam(String name)
    {
        name = Util.toName(name);
        for (Parameter parameter : this.paramList) if (parameter.getName().equals(name)) return parameter;
        return null;
    }
    
    @Override
    public Parameter getParameter(String name)
    {
        Parameter p = getParam(name);
        if (p != null) return p;
        throw new Error("Parameter not found " + name);
    }
    
    @Override
    public Set<Parameter> getParameters()
    {
        return new HashSet<>(this.paramList);
    }
    
    @Override
    public ParameterBoolean getParameterBoolean(String name)
    {
        Parameter p = getParam(name);
        if (p instanceof ParameterBoolean)
        {
            return (ParameterBoolean) p;
        }
        throw new RuntimeException("ParameterBoolean not found " + name);
    }
    
    @Override
    public ParameterNumber getParameterNumber(String name)
    {
        Parameter p = getParam(name);
        if (p instanceof ParameterNumber) return (ParameterNumber) p;
        throw new RuntimeException("ParameterNumber not found " + name);
    }
    
    @Override
    public ParameterString getParameterString(String name)
    {
        Parameter p = getParam(name);
        if (p instanceof ParameterString) return (ParameterString) p;
        throw new RuntimeException("ParameterString not found " + name);
    }
    
    /**
     * A delayed command to add (`action=true`) or remove (`action=false`) an Observer.
     */
    private static class Command
    {
        public boolean  action;
        public Observer observer;
        
        private Command(boolean action, Observer observer)
        {
            this.action = action;
            this.observer = observer;
        }
    }
}
