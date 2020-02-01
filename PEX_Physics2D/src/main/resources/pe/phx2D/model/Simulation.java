package pe.phx2D.model;

/**
 * The mathematical model of a simulation.
 * <p>
 * To communicate its state to the outside world, a Simulation contains a {@link SimList} to which are added {@link SimObject}s like PointMass, Spring, etc.
 * <p>
 * An {@link AdvanceStrategy} moves the Simulation forward in time, by solving the mathematical model for the next small increment in time. The method
 * {@link Simulation#modifyObjects} is called separately to ensure the SimObjects match the new Simulation state.
 * <p>
 * A Simulation usually keeps track of the current time, see {@link Simulation#getTime}. There are no explicit units for the time, so you can regard a time unit as seconds or
 * years as desired.
 * See [About Units Of Measurement](Architecture.html#aboutunitsofmeasurement). Changing the Simulation time by a large amount can affect synchronization with the Clock used to
 * advance the Simulation; see {@link SimRunner} section *How Simulation Advances with Clock*.
 * <p>
 * A Simulation can store its initial state with {@link Simulation#saveInitialState} and return to that initial state with {@link Simulation#reset}. The current time is saved
 * with the initial state.
 */
public interface Simulation
{
    /**
     * Name of event broadcast when the Simulation state is reset to initial conditions.
     */
    String RESET = "RESET";
    
    /**
     * Name of event broadcast when initial state is saved.
     */
    String INITIAL_STATE_SAVED = "INITIAL_STATE_SAVED";
    
    /**
     * Returns the list of {@link SimObject} that represent this Simulation.
     *
     * @return the list of SimObjects that represent this simulation
     */
    SimList getSimList();
    
    /**
     * Returns the current Simulation time.
     *
     * @return the current Simulation time.
     *
     * @throws Error if there is no time variable for the simulation
     */
    double getTime();
    
    /**
     * Updates the SimObjects to match the current internal state of the Simulation.
     */
    void modifyObjects();
    
    /**
     * Sets the Simulation back to its initial conditions, see {@link Simulation#saveInitialState}, and calls {@link Simulation#modifyObjects}. Broadcasts event named
     * {@link Simulation#RESET}.
     */
    void reset();
    
    /**
     * Saves the current variables and time as the initial state, so that this initial state can be restored with {@link Simulation#reset}. Broadcasts event named
     * {@link Simulation#INITIAL_STATE_SAVED}.
     */
    void saveInitialState();
}
