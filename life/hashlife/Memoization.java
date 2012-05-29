package life.hashlife;

import life.hashlife.ComposedCell;
import life.hashlife.MacroCell;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A static class responsible for the memoization of all built MacroCell.
 */
class Memoization {
	static private ArrayList<MacroCell> empty = new ArrayList<MacroCell>();
	static private HashMap<MacroCell, MacroCell> built = new HashMap<MacroCell, MacroCell>();
	
	/**
	 * Builds a MacroCell out of 4 MacroCell.
	 * It first makes sure the MacroCell isn't built yet. Otherwise, it returns the build cell.
	 * 
	 * @param quad the four base MacroCell
	 * @return the new MacroCell
	 */
	static MacroCell get(MacroCell ... quad) {
		assert(quad.length == 4 && quad[0].dim == quad[1].dim && quad[1].dim == quad[2].dim && quad[2].dim == quad[3].dim);
		MacroCell m;
		if(quad[0].dim == 0)
			m = new FourCell(quad);
		else if(quad[0].dim == 1)
			m = new HexaCell(quad);
		else
			m = new ComposedCell(quad);
		if(built.containsKey(m))
			m = built.get(m);
		else
			built.put(m, m);
		
		return m;
	}
	
	/**
	 * Builds an empty MacroCell.
	 * If it has been already built, it returns the previously built cell.
	 * 
	 * @param dim the dimension of the empty MacroCell
	 * @return the new empty MacroCell
	 */
	static MacroCell empty(int dim) {
		if(dim < 0)
			throw new RuntimeException("The dimension of an empty MacroCell must be at least 0.");

		empty.ensureCapacity(dim+1);
		if(empty.isEmpty())
			empty.add(BooleanCell.off);
		
		int todo = dim - empty.size() + 1;
		while(todo-- > 0) {
			MacroCell e = empty.get(dim - todo - 1);
			empty.add(get(e, e, e, e));
		}
		return empty.get(dim);
	}
	
	/**
	 * Builds a MacroCell out of an int array.
	 * A value of 0 means the cell is off, otherwise it's on.
	 * 
	 * @param array the source array
	 * @return the built MacroCell
	 */
	static MacroCell fromTab(int[][] array) {
		if(array == null || array.length == 0)
			return empty(1);
		int h = array.length, w = array[0].length, dim;
		if(h < w)
			dim = 32 - Integer.numberOfLeadingZeros(w);
		else
			dim = 32 - Integer.numberOfLeadingZeros(h);
		if(dim < 1)
			dim = 1;
		return fromTab(array, 0, 0, dim);
	}
	
	/**
	 * Builds a MacroCell out of a portion of an int array.
	 * 
	 * @param array the source array
	 * @param i the line of the top left cell
	 * @param j the column of the top left cell
	 * @param dim the dimension of the MacroCell to build
	 * @return the built MacroCell
	 */
	static private MacroCell fromTab(int[][] array, int i, int j, int dim) {
		if(dim == 0) {
			if(i >= array.length || j >= array[i].length || array[i][j] == 0)
				return BooleanCell.off;
			return BooleanCell.on;
		}
		int offset = 1 << (dim-1);
		return get(	fromTab(array, i, j, dim-1),
					fromTab(array, i, j + offset, dim-1),
					fromTab(array, i + offset, j, dim-1),
					fromTab(array, i + offset, j + offset, dim-1));
	}
}
