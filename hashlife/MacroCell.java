package hashlife;

import java.util.Arrays;
import java.util.HashMap;

abstract public class MacroCell {
	
	static private HashMap<MacroCell, MacroCell> built = new HashMap<MacroCell, MacroCell>();
	
	/*  +---+---+
	 *  | 0 | 1 |
	 *  +---+---+
	 *  | 2 | 3 |
	 *  +---+---+
	 */	
	final protected MacroCell quad[] = new MacroCell[4];
	protected MacroCell result = null;
	final protected int n;
	
	protected MacroCell(MacroCell m0, MacroCell m1, MacroCell m2, MacroCell m3, int n) {
		this.quad[0] = m0;
		this.quad[1] = m1;
		this.quad[2] = m2;
		this.quad[3] = m3;
		this.n = n;
	}
	
	static public MacroCell get(MacroCell m0, MacroCell m1, MacroCell m2, MacroCell m3) {
		MacroCell m = new BigCell(m0, m1, m2, m3);

		if(built.containsKey(m))
			m = built.get(m);
		else
			built.put(m, m);
		
		return m;
	}
	
	static public MacroCell get(MacroCell quad[]) {
		return get(quad[0], quad[1], quad[2], quad[3]);
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof MacroCell))
			return false;
		MacroCell m = (MacroCell) o;
		if(n != m.n)
			return false;
		return Arrays.equals(quad, m.quad);
	}
	
	public int hashCode() {
		int hashCode = 1;
		  for(int i=0; i<4; i++)
		      hashCode = 31*hashCode + System.identityHashCode(quad[i]);
		return hashCode;
	}
	
}
