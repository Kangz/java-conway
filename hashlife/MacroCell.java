package hashlife;

import java.util.HashMap;

abstract public class MacroCell {
	
	static private HashMap<Integer, MacroCell> empty = new HashMap<Integer, MacroCell>();
	
	final public int dim;
	final public int size;
	final public boolean off;
	
	/**
	 * @param dim The size of the cell in power of two
	 * @param off True if it is completely off
	 */
	protected MacroCell(int dim, boolean off) {
		this.dim = dim;
		this.size = (int) Math.pow(2, dim);
		this.off = off;
	}
	
	/**
	 * @param quad an array of the 4 sub-cells of the MacroCell to get
	 * @return the built MacroCell 
	 */
	static public MacroCell get(MacroCell ... quad) {
		return BigCell.get(quad);
	}
	
	/**
	 * @param dim the dimension of the result
	 * @return an empty MacroCell of dimension dim1
	 */
	static public MacroCell empty(Integer dim) {
		if(empty.containsKey(dim))
			return empty.get(dim);
		if(dim == 0) {
			MacroCell e = BooleanCell.False;
			empty.put(0, e);
			return e;
		}
		MacroCell e = MacroCell.empty(dim-1);
		MacroCell m = BigCell.get(e, e, e, e);
		empty.put(dim, m);
		return m;
	}	
	
	final public boolean[][] toTab() {
		boolean[][] tab = new boolean[size][size];
		fillTab(tab, 0, 0);
		return tab;
	}
	
	static public String niceStringFromTab(boolean[][] t) {
		String s = "";
		
		for(int i=0; i<t.length; i++) {
			for(int j=0; j<t[i].length; j++)
				s += t[i][j]?'1':'0';
			s += '\n';
		}
		
		return s;		
	}
	
	final public String niceString() {
		return niceStringFromTab(toTab());
	}
	
	/**
	 * Used to retrieves sub cells.
	 * @param depth
	 * @param x
	 * @param y
	 * @return the sub-cell at position (x, y) at the given depth
	 */
	abstract public MacroCell part(int depth, int x, int y);
	
	/**
	 * @param s
	 * @return The result of the MacroCell at t=2^s
	 */
	abstract public MacroCell result(int s);

	/**
	 * 
	 * @param t
	 * @return
	 */
	abstract public MacroCell evolve(int t);
	abstract public void fillTab(boolean[][] tab, int i, int j);
	
	/**
	 * Tries to reduce the dim of the MacroCell if large parts of it are empty
	 * @return the reduced MacroCell
	 */
	abstract public MacroCell simplify();
	
	/**
	 * Adds a border around the MacroCell
	 * @return the borderized MacroCell
	 */
	abstract public MacroCell borderize();
}
