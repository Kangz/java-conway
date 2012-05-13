package life.naive;

import life.LifeAlgo;
import life.LifeDrawer;
import life.LifeState;

public class NaiveAlgo implements LifeAlgo {
	
	NaiveState s;
	
	@Override
	public void loadState(LifeState state) {
		if (state instanceof NaiveState) {
			s = (NaiveState) state;
		} else {
			throw new RuntimeException("NaiveLife.loadState needs a NaiveState");
		}
	}

	@Override
	public LifeState saveState() {
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
