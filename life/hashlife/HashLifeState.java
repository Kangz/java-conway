package life.hashlife;

import life.LifeState;


class HashLifeState implements LifeState {

	MacroCell state;
	
	HashLifeState(MacroCell state) {
		this.state = state.simplify();
	}
	
	HashLifeState(int[][] array) {
		this.state = Memoization.fromTab(array);
	}
	
	HashLifeState(HashLifeState other) {
		this.state = other.state;
	}
	
	HashLifeState copy() {
		return new HashLifeState(this);
	}
	
	int getCellAt(int x, int y) {
		return state.getCell(x + state.size/2, y + state.size/2);
	}
	
	void setCellAt(int x, int y, int newState) {
		state = state.setCell(x + state.size/2, y + state.size/2, newState);
	}
	
	int[][] toArray() {
		return state.toTab();
	}
	
	void evolve(int steps) {
		int s = 32 - Integer.numberOfLeadingZeros(steps);
		int n = 1<<s;
		for(int i = 0; i<=s; i++)
			state = state.borderize();
		while(n > 0) {
			if((steps&n) != 0){
				state = state.result(s).borderize();
			}
			n /= 2;
			s--;
		}
		state = state.simplify();
	}
}
