package life.hashlife;

import life.LifeAlgo;
import life.LifeDrawer;
import life.LifeState;

/**
 * An implementation of the interface LifeAlgo for the Hashlife algorithm.
 */
public class HashlifeAlgo implements LifeAlgo {

	/**
	 * The current state of the algorithm, represented by a HashlifeState.
	 */
	HashlifeState s;
	
	@Override
	public void setState(LifeState state) {
		if (state instanceof HashlifeState) {
			s = ((HashlifeState) state).copy();
		} else {
			throw new RuntimeException("HashlifeAlgo.setState needs a HashlifeState");
		}
	}

	@Override
	public LifeState getState() {
		return s.copy();
	}

	@Override
	public LifeDrawer getDrawer() {
		return new HashlifeDrawer();
	}

	@Override
	public void loadFromArray(int[][] array) {
		s = new HashlifeState(array);
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
		s.evolve(steps);
	}

}
