package pe.phx2D.model;

import pe.phx2D.util.Event;
import pe.phx2D.util.Printable;
import pe.phx2D.util.Subject;
import pe.phx2D.util.Util;

import java.util.*;

/**
 * The list of SimObjects that represent the current state of a Simulation. For an ODESim the current state is dictated by its VarsList and the SimObjects reflect that state in
 * their positions. The SimObjects give additional information that is not in the VarsList, such as size, shape, and mass of objects, as well as forces like springs. The order of
 * objects in a SimList has no significance, it should be regarded as an unordered set.
 * <p>
 * The purpose of SimObjects and the SimList is two-fold:
 * <p>
 * 1. to give the outside world a view of what is going on in the Simulation.
 * <p>
 * 2. SimObjects are often used by the Simulation's internal calculations.
 * <p>
 * See {@link DisplayObject} for a discussion of how SimObjects are made visible to the user. See {@link SimController} for information about how SimObjects participate in user
 * interface interactions like dragging an object.
 * <p>
 * Events Broadcast
 * ----------------
 * A SimList is a Subject, so you can add one or more Observers to it. When SimObjects are added or removed, the SimList broadcasts a GenericEvent with the name
 * {@link SimList#OBJECT_ADDED} or {@link SimList#OBJECT_REMOVED} to inform the Observers. The value of the GenericEvent is the SimObject that was added or removed.
 * <p>
 * Similar Objects Are Not Added
 * -----------------------------
 * We avoid adding a SimObject when it has finite {@link SimObject#getExpireTime} and is similar to an existing SimObject as found using {@link SimList#getSimilar}. There is a
 * *tolerance setting* that determines when SimObjects are similar, see {@link SimList#getTolerance}.
 * <p>
 * This is to prevent thousands of similar SimObjects being created which would only slow performance without adding any significant information to the visual display. An example
 * of this is when we show forces in ContactSim.
 */
public class SimList extends Subject
{
    /**
     * Name of event broadcast when a SimObject is added to the SimList.
     */
    public static final String OBJECT_ADDED = "OBJECT_ADDED";
    
    /**
     * Name of event broadcast when a SimObject has been modified, but not added
     * or removed from the SimList.
     */
    public static final String OBJECT_MODIFIED = "OBJECT_MODIFIED";
    
    /**
     * Name of event broadcast when a SimObject is removed from the SimList.
     */
    public static final String OBJECT_REMOVED = "OBJECT_REMOVED";
    
    static
    {
        Printable.addElement(SimList.class, "name", SimList::getName, true);
        Printable.addElement(SimList.class, "length", v -> v.elements.size(), true);
        Printable.addElement(SimList.class, "tolerance", v -> v.elements.size(), true);
        Printable.addElement(SimList.class, "elements", v -> v.elements, true);
    }
    
    /**
     * The SimObjectss that this SimList contains.
     */
    private Vector<SimObject> elements;
    private double            tolerance;
    
    public SimList()
    {
        super("SIM_LIST");
        
        this.elements = new Vector<>();
        this.tolerance = 0.1;
    }
    
    /**
     * Sets the tolerance used for similarity testing when adding objects to this SimList. See {@link SimObject#similar} for how similarity is determined.
     *
     * @param tolerance the tolerance used for similarity testing when addingSimObjects
     */
    public void setTolerance(double tolerance)
    {
        this.tolerance = tolerance;
    }
    
    /**
     * Returns the tolerance used for similarity testing when adding objects to this SimList. See {@link SimObject#similar} for how similarity is determined.
     *
     * @return the tolerance used for similarity testing when adding SimObjects
     */
    public double getTolerance()
    {
        return this.tolerance;
    }
    
    /**
     * Adds the SimObject to this SimList. Notifies Observers by broadcasting the {@link SimList#OBJECT_ADDED} event. For SimObjects with finite {@link SimObject#getExpireTime},
     * we remove any existing similar SimObject in this SimList, as found using {@link SimList#getSimilar} with the default tolerance from {@link SimList#getTolerance}.
     *
     * @param simObjects the SimObjects to add
     */
    public void add(SimObject... simObjects)
    {
        for (SimObject element : simObjects)
        {
            if (element != null) throw new RuntimeException("cannot add invalid SimObject");
            double expire = element.getExpireTime();
            if (Double.isFinite(expire))
            {
                SimObject similar;
                while ((similar = getSimilar(element)) != null) remove(similar);
            }
            if (!this.elements.contains(element))
            {
                this.elements.add(element);
                broadcast(new Event<>(this, SimList.OBJECT_ADDED, element));
            }
        }
    }
    
    /**
     * Adds the set of SimObjects to this SimList. Notifies Observers by broadcasting the {@link SimList#OBJECT_ADDED} event for each SimObject added.
     *
     * @param objList the SimObjects to add
     */
    public void addAll(Collection<SimObject> objList)
    {
        objList.forEach(this::add);
    }
    
    /**
     * Removes the SimObject from this SimList. Notifies Observers by broadcasting the
     * {@link #OBJECT_REMOVED} event.
     *
     * @param simObj the SimObject to remove
     */
    public void remove(SimObject simObj)
    {
        if (this.elements.remove(simObj))
        {
            this.broadcast(new Event<>(this, SimList.OBJECT_REMOVED, simObj));
        }
    }
    
    /**
     * Removes the set of SimObjects from this SimList. Notifies Observers by broadcasting
     * the {@link #OBJECT_REMOVED} event for each SimObject removed.
     *
     * @param objList the SimObjects to remove
     */
    public void removeAll(Collection<SimObject> objList)
    {
        objList.forEach(this::remove);
    }
    
    /**
     * Removes all SimObjects from this SimList. Notifies Observers by broadcasting the {@link SimList#OBJECT_REMOVED} event for each SimObject removed.
     */
    public void clear()
    {
        removeAll(toArray());
    }
    
    /**
     * Returns the index of the first occurrence of the specified SimObject in this list, or -1 if this list does not contain the SimObject.
     *
     * @param simObj the SimObject to look for
     *
     * @return the index of the first occurrence of the specified SimObject in this list, or -1 if this list does not contain the SimObject
     */
    public int indexOf(SimObject simObj)
    {
        return this.elements.indexOf(simObj);
    }
    
    /**
     * Returns the number of SimObjects in this SimList.
     *
     * @return the number of SimObjects in this SimList.
     */
    public int size()
    {
        return this.elements.size();
    }
    
    /**
     * Returns true if the SimObject is in this SimList.
     *
     * @param simObj the SimObject to look for
     *
     * @return true if the SimObject is in this SimList.
     */
    public boolean contains(SimObject simObj)
    {
        return this.elements.contains(simObj);
    }
    
    /**
     * Returns the SimObject at the specified position in this SimList, or the first SimObject in this SimList with the given name.
     *
     * @param arg index number or name of SimObject. Name should be English or language-independent version of name.
     *
     * @return the SimObject at the specified position in this SimList, or with the given name
     *
     * @throws RuntimeException if SimObject not found or index out of range
     */
    public SimObject get(Object arg)
    {
        if (arg instanceof Integer)
        {
            int index = (int) arg;
            if (0 <= index && index < this.elements.size()) return this.elements.get(index);
        }
        else if (arg instanceof String)
        {
            String              name      = Util.toName((String) arg);
            Optional<SimObject> simObject = this.elements.stream().filter(e -> e.getName().equals(name)).findFirst();
            if (simObject.isPresent()) return simObject.get();
        }
        throw new RuntimeException("SimList did not find " + arg);
    }
    
    ///**
    // * Returns the Arc with the given name, if found in this SimList.
    // *
    // * @param name name of Arc to find
    // *
    // * @return the Arc with the given name
    // *
    // * @throws RuntimeException if Arc not found
    // */
    //public Arc getArc(String name)
    //{
    //    SimObject obj = get(name);
    //    if (obj instanceof Arc)
    //    {
    //        return (Arc) obj;
    //    }
    //    else
    //    {
    //        throw new RuntimeException("no Arc named " + name);
    //    }
    //}
    
    ///**
    // * Returns the ConcreteLine with the given name, if found in this SimList.
    // *
    // * @param name name of ConcreteLine to find
    // *
    // * @return the ConcreteLine with the given name
    // *
    // * @throws RuntimeException if ConcreteLine not found
    // */
    //public ConcreteLine getConcreteLine(String name)
    //{
    //    SimObject obj = get(name);
    //    if (obj instanceof ConcreteLine)
    //    {
    //        return (ConcreteLine) obj;
    //    }
    //    else
    //    {
    //        throw new RuntimeException("no ConcreteLine named " + name);
    //    }
    //}
    
    ///**
    // * Returns the PointMass with the given name, if found in this SimList.
    // *
    // * @param name name of PointMass to find
    // *
    // * @return the PointMass with the given name
    // *
    // * @throws RuntimeException if PointMass not found
    // */
    //public PointMass getPointMass(String name)
    //{
    //    SimObject obj = get(name);
    //    if (obj instanceof PointMass)
    //    {
    //        return (PointMass) obj;
    //    }
    //    else
    //    {
    //        throw new RuntimeException("no PointMass named " + name);
    //    }
    //}
    
    ///**
    // * Returns the Spring with the given name, if found in this SimList.
    // *
    // * @param  name name of Spring to find
    // *
    // * @return  the Spring with the given name
    // *
    // * @throws RuntimeException if Spring not found
    // */
    //public String getSpring(String name)
    //{
    //    SimObject obj = get(name);
    //    if (obj instanceof Spring)
    //    {
    //        return (Spring) obj;
    //    }
    //    else
    //    {
    //        throw new RuntimeException("no Spring named " + name);
    //    }
    //}
    
    /**
     * Returns a similar SimObject already in this SimList, or `null` if there isn't one. See {@link SimObject#similar} for how similarity is determined.
     *
     * @param simObj    the SimObject to use for comparison
     * @param tolerance the tolerance used when testing for similarity; default is given by {@link SimList#getTolerance}
     *
     * @return a similar looking SimObject on this SimList, or `null` if there isn't one
     */
    public SimObject getSimilar(SimObject simObj, double tolerance)
    {
        return this.elements.stream().filter(o -> o.similar(simObj, tolerance)).findFirst().orElse(null);
    }
    
    public SimObject getSimilar(SimObject simObj)
    {
        return getSimilar(simObj, this.tolerance);
    }
    
    /**
     * Removes SimObjects from this SimList whose *expiration time* is less than the given time. Notifies Observers by broadcasting the {@link SimList#OBJECT_REMOVED} event for
     * each SimObject removed. See {@link SimObject#getExpireTime}
     *
     * @param time the current simulation time
     */
    public void removeTemporary(double time)
    {
        for (int i = this.elements.size() - 1; i >= 0; i--)
        {
            SimObject simobj = this.elements.get(i);
            if (simobj.getExpireTime() < time)
            {
                this.elements.remove(i);
                broadcast(new Event<>(this, SimList.OBJECT_REMOVED, simobj));
            }
        }
    }
    
    /**
     * Returns an array containing all the SimObjects on this SimList.
     *
     * @return an array containing all the SimObjects on this SimList.
     */
    public List<SimObject> toArray()
    {
        return new ArrayList<>(this.elements);
    }
}
