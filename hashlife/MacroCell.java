package hashlife;

import java.util.HashMap;

abstract public class MacroCell {
	
	static private HashMap<Integer, MacroCell> empty = new HashMap<Integer, MacroCell>();
	
	final public int n;
	final public int size; // 2^n
	final public boolean off; // True if the MacroCell is completely off
	
	protected MacroCell(int n, boolean off) {
		this.n = n;
		this.size = (int) Math.pow(2, n);
		this.off = off;
	}
	
	static public MacroCell get(MacroCell ... quad) {
		return BigCell.get(quad);
	}
	
	// Return an empty MacroCell of dimension n
	static public MacroCell empty(Integer n) {
		if(empty.containsKey(n))
			return empty.get(n);
		if(n == 0) {
			MacroCell e = BooleanCell.False;
			empty.put(0, e);
			return e;
		}
		MacroCell e = empty(n-1);
		MacroCell m = BigCell.get(e, e, e, e);
		empty.put(n, m);
		return m;
	}	
	
	final public boolean[][] toTab() {
		boolean[][] tab = new boolean[size][size];
		fillTab(tab, 0, 0);
		return tab;
	}
	
	final public String niceString() {
		boolean[][] t = toTab();
		String s = "";
		
		for(int i=0; i<t.length; i++) {
			for(int j=0; j<t[i].length; j++)
				s += t[i][j]?'1':'0';
			s += '\n';
		}
		
		return s;
	}
	
	abstract public MacroCell part(int depth, int i, int j);
	abstract public MacroCell result(int s);
	abstract public MacroCell evolve(int t);
	abstract public void fillTab(boolean[][] tab, int i, int j);
	abstract public MacroCell simplify();
	abstract public MacroCell borderize();
	
}
