package life;

/**
 * A representation of a single state of the EvolveManager.
 */
public class EvolveManagerState {

	/**
	 * The number of steps since the initial state corresponding to this state.
	 */
	public long nSteps;
	
	/**
	 * TODO
	 */
	public boolean forced;
	
	/**
	 * The LifeAlgo associated to the state.
	 */
	public LifeAlgo algo;
	
	/**
	 * The LifeState representing the stored state.
	 */
	public LifeState state;
	
	/**
	 * Simply builds an EvolveManagerState with given parameters.
	 * 
	 * @param s number of steps since the initial state
	 * @param f TODO
	 * @param a the LifeAlgo associated to the state
	 */
	EvolveManagerState(long s, boolean f, LifeAlgo a){
		nSteps = s;
		algo = a;
		forced = f;
		state = a.getState();
	}
	
}
