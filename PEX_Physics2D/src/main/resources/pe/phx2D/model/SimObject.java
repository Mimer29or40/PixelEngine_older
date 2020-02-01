package pe.phx2D.model;

import pe.phx2D.util.Nameable;
import pe.phx2D.util.Printable;
import pe.phx2D.util.Util;

/**
 * Represents an object in a Simulation. The purpose of a SimObject is two-fold:
 * <p>
 * 1. To give the outside world a view of what is going on in the Simulation.
 * <p>
 * 2. A SimObject might be used in a Simulation's internal calculations.
 * <p>
 * A set of SimObjects are stored in a {@link SimList}. The SimObjects represent the current state of the Simulation.
 * <p>
 * For an {@link ODESim} the current state is dictated by the variables in its {@link VarsList} and the SimObjects reflect that state in their positions and velocities.
 * <p>
 * A SimObject can give additional information that is not in the VarsList, such as size, shape, and mass of objects. A SimObject can represent forces or anchor objects which are
 * not available in the VarsList.
 * <p>
 * SimObjects are updated to reflect the current state when the {@link Simulation#modifyObjects} method is called.
 * <p>
 * See {@link DisplayObject} for a discussion of how SimObjects are made visible to the user.
 * <p>
 * A SimObject has an *expiration time* so that we can add temporary objects, representing things like forces or collision impact, and set the time at which they should be removed
 * from the simulation display. Permanent SimObjects have infinite expiration time. See {@link SimObject#getExpireTime}.
 */
public abstract class SimObject extends Nameable
{
    private static int ID = 0;
    
    static
    {
        Printable.addElement(SimObject.class, "name", SimObject::getName, true);
        Printable.addElement(SimObject.class, "expireTime", SimObject::getExpireTime, false);
    }
    
    private double expireTime;
    
    public SimObject(String opt_name)
    {
        super(Util.validName(Util.toName(opt_name != null ? opt_name : "SIM_OBJ_" + ID++)));
        
        this.expireTime = Double.MAX_VALUE;
    }
    
    /**
     * Returns the expiration time, when this SimObject should be removed from the SimList. This is intended for temporary SimObjects that illustrate, for example, contact forces
     * or collisions.
     *
     * @return the expiration time, in time frame of the {@link Simulation#getTime} Simulation clock
     */
    public double getExpireTime()
    {
        return this.expireTime;
    }
    
    /**
     * Sets the expiration time, when this SimObject should be removed from the SimList. This is intended for temporary SimObjects that illustrate, for example, contact forces
     * or collisions.
     *
     * @param time the expiration time, in time frame of the {@link Simulation#getTime Simulation clock}
     *
     * @return this SimObject for chaining setters
     */
    public SimObject setExpireTime(double time)
    {
        this.expireTime = time;
        return this;
    }
    
    /**
     * Returns a rectangle that contains this SimObject in world coordinates.
     *
     * @return rectangle that contains this SimObject in world coordinates
     */
    public abstract AABB getBoundsWorld();
    
    /**
     * Whether this implements the {@link MassObject} interface.
     *
     * @return Whether this implements the MassObject interface.
     */
    public boolean isMassObject()
    {
        return false;
    }
    
    /**
     * Returns true if the given SimObject is similar to this SimObject for display purposes. SimObjects are similar when they are the same type and nearly the same size and
     * location. Mainly used when showing forces - to avoid adding too many objects to the display. See {@link SimList#getSimilar}.
     *
     * @param obj           the SimObject to compare to
     * @param opt_tolerance the amount the object components can differ by
     *
     * @return true if this SimObject is similar to `obj` for display purposes
     */
    public boolean similar(SimObject obj, Double opt_tolerance)
    {
        return obj == this;
    }
}
