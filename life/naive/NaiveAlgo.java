package life.naive;

import life.LifeAlgo;
import life.LifeDrawer;
import life.LifeState;

/**
 * An implementation of the interface LifeAlgo for the Naive algorithm.
 */
public class NaiveAlgo implements LifeAlgo {

	/**
	 * The current state of the algorithm, represented by a NaiveState.
	 */
	NaiveState s;
	
	@Override
	public void setState(LifeState state) {
		if (state instanceof NaiveState) {
			s = ((NaiveState) state).copy();
		} else {
			throw new RuntimeException("NaiveAlgo.setState needs a NaiveState");
		}
	}

	@Override
	public LifeState getState() {
		return s.copy();
	}

	@Override
	public LifeDrawer getDrawer() {
		return new NaiveDrawer();
	}

	@Override
	public void loadFromArray(int[][] array) {
		s = new NaiveState(array);
	}

	@Override
	public int[][] saveToArray() {
		return s.toArray();
	}

	@Override
	public int getCellAt(int x, int y) {
		return s.getCellAt(x, y);
	}

	@Override
	public void setCellAt(int x, int y, int status) {
		s.setCellAt(x, y, status);
	}

	@Override
	public int toggleCellAt(int x, int y) {
		int state = s.getCellAt(x, y);
		s.setCellAt(x, y, 1 - state);
		return state;
	}

	@Override
	public void evolve(int steps) {
		for (int i=0; i<steps; i++) {
			s.evolveOnce();
		}
	}

}
