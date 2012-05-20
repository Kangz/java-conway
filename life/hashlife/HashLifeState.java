package life.hashlife;

import life.LifeState;


public class HashLifeState implements LifeState {

	public MacroCell state;
	
	HashLifeState(MacroCell state) {
		this.state = state.simplify();
	}
	
	public HashLifeState(int[][] array) {
		this.state = Memoization.fromTab(array);
	}
	
	HashLifeState(HashLifeState other) {
		this.state = other.state;
	}
	
	HashLifeState copy() {
		return new HashLifeState(this);
	}
	
	//TODO
	int getCellAt(int x, int y) {
		return 0;
	}
	
	//TODO
	void setCellAt(int x, int y, int state) {
		return;
	}
	
	int[][] toArray() {
		return state.toTab();
	}
	
	public void evolve(int steps) {
		int s = 32 - Integer.numberOfLeadingZeros(steps);
		int n = 1<<s;
		for(int i = 0; i<=s; i++)
			state = state.borderize();
		while(n > 0) {
			if((steps&n) != 0)
				state = state.result(s).borderize();
			n /= 2;
			s--;
		}
		state = state.simplify();
	}
}
