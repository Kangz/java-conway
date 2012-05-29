package life.naive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import life.LifeState;

import util.Position2D;

/**
 * A specialized LifeState for the Naive algorithm.
 */
public class NaiveState implements LifeState {

	private ArrayList<Position2D> aliveCells;

	/**
	 * Initialize the NaiveState with an array.
	 * 
	 * @param array an int array
	 */
	NaiveState(int[][] array) {
		aliveCells = new ArrayList<Position2D>();
		
		for (int i=0; i<array.length; i++) {
			for (int j=0; j<array[i].length; j++) {
				if (array[i][j] == 1) {
					aliveCells.add(new Position2D(j, i));
				}
			}
		}
	}

	/**
	 * @param other another NaiveState
	 */
	NaiveState(NaiveState other) {
		aliveCells = new ArrayList<Position2D>();
		aliveCells.addAll(other.aliveCells);
	}

	/**
	 * @return a copy of this NaiveState
	 */
	NaiveState copy() {
		return new NaiveState(this);
	}

	/**
	 * @param x the x coordinate of the cell to get
	 * @param y the y coordinate of the cell to get
	 * @return the state of the cell
	 */
	int getCellAt(int x, int y) {
		return aliveCells.contains(new Position2D(x, y)) ? 1 : 0;
	}
	
	/**
	 * @param x the x coordinate of the cell to get
	 * @param y the y coordinate of the cell to get
	 * @param newState the new state of the cell
	 */
	void setCellAt(int x, int y, int state) {
		if (state == 1) {
			aliveCells.add(new Position2D(x, y));
		} else if(state == 0) {
			aliveCells.remove(new Position2D(x, y));
		}
	}
	
	/**
	 * Perform a single step forward.
	 */
	void evolveOnce() {
		HashMap<Position2D, Integer> counters = new HashMap<Position2D, Integer>();
		
		for (Position2D pos : getAliveCells()) {
			for (int offsetx = -1; offsetx <= 1; offsetx ++) {
				for (int offsety = -1; offsety <= 1; offsety ++) {
					if(offsetx == 0 && offsety == 0)
						continue;
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
			
			if (counter == 3 || (counter == 2 && aliveCells.contains(pos))) {
				newAlive.add(pos);
			}
		}
		
		aliveCells = newAlive;
	}
	
	/**
	 * @return a List containing the positions of every alive cell.
	 */
	List<Position2D> getAliveCells() {
		return aliveCells;
	}

	/**
	 * @return an array representing the state of the universe
	 */
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
		
		int[][] array = new int[maxy - miny + 1][maxx - minx + 1];
		
		for (Position2D pos : aliveCells) {
			array[pos.y - miny][pos.x - minx] = 1;
		}
		
		return array;
	}
}
