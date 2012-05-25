package life;

public class EvolveManagerState {

	public long nSteps;
	public boolean forced;
	public LifeAlgo algo;
	public LifeState state;
	
	EvolveManagerState(long s, boolean f, LifeAlgo a){
		nSteps = s;
		algo = a;
		forced = f;
		state = a.getState();
	}
	
}
