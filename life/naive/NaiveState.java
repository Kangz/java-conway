package life.naive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import life.LifeState;

import util.Position2D;

public class NaiveState implements LifeState {

	private ArrayList<Position2D> aliveCells;
	
	NaiveState(int[][] array) {
		aliveCells = new ArrayList<Position2D>();
		
		for (int i=0; i<array.length; i++) {
			for (int j=0; j<array[i].length; j++) {
				if (array[i][j] == 1) {
					aliveCells.add(new Position2D(i, j));
				}
			}
		}
	}
	
	NaiveState(NaiveState other) {
		aliveCells = new ArrayList<Position2D>();
		aliveCells.addAll(other.aliveCells);
	}
	
	NaiveState copy() {
		return new NaiveState(this);
	}
	
	int getCellAt(int x, int y) {
		return aliveCells.contains(new Position2D(x, y)) ? 1 : 0;
	}
	
	void setCellAt(int x, int y, int state) {
		if (state == 1) {
			aliveCells.add(new Position2D(x, y));
		} else if(state == 0) {
			aliveCells.remove(new Position2D(x, y));
		}
	}
	
	void evolveOnce() {
		HashMap<Position2D, Integer> counters = new HashMap<Position2D, Integer>();
		
		for (Position2D pos : getAliveCells()) {
			for (int offsetx = -1; offsetx <= 1; offsetx ++) {
				for (int offsety = -1; offsety <= 1; offsety ++) {
					Position2D current = new Position2D(pos.x + offsetx, pos.y + offsety);
					if (! counters.containsKey(current)) {
						counters.put(current, 1);
					} else {
						counters.put(current, 1 + counters.get(current));
					}
				}
			}
		}
		
		ArrayList<Position2D> newAlive = new ArrayList<Position2D>();
		
		for (Entry<Position2D, Integer> entry : counters.entrySet()) {
			Position2D pos = entry.getKey();
			int counter = entry.getValue();
			
			if (counter == 2 || (counter == 3 && aliveCells.contains(pos))) {
				newAlive.add(pos);
			}
		}
		
		aliveCells = newAlive;
	}
	
	List<Position2D> getAliveCells() {
		return aliveCells;
	}

	int[][] toArray() {
		int minx = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE;
		int miny = Integer.MAX_VALUE;
		int maxy = Integer.MIN_VALUE;
		
		for (Position2D pos : aliveCells) {
			if (pos.x < minx) minx = pos.x;
			if (pos.x > maxx) maxx = pos.x;
			if (pos.y < miny) miny = pos.y;
			if (pos.y > maxy) maxy = pos.y;
		}
		
		int[][] array = new int[maxx - minx + 1][maxy - miny + 1];
		
		for (Position2D pos : aliveCells) {
			array[pos.x - minx][pos.y - miny] = 1;
		}
		
		return array;
	}
}
