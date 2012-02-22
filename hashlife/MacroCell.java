package hashlife;

import java.util.HashMap;

abstract public class MacroCell {
	
	static private HashMap<MacroCell, MacroCell> built = new HashMap<MacroCell, MacroCell>();
	static private HashMap<Integer, MacroCell> empty = new HashMap<Integer, MacroCell>();
	
	final public int n;
	
	protected MacroCell(int n) {
		this.n = n;
	}
	
	static public MacroCell get(MacroCell m0, MacroCell m1, MacroCell m2, MacroCell m3) {
		assert(m0.n == m1.n && m1.n == m2.n && m2.n == m3.n);
		MacroCell m = new BigCell(m0, m1, m2, m3);

		if(built.containsKey(m))
			m = built.get(m);
		else
			built.put(m, m);
		
		return m;
	}
	
	static public MacroCell empty(Integer n) {
		if(empty.containsKey(n))
			return empty.get(n);
		if(n == 0) {
			MacroCell e = BooleanCell.False;
			empty.put(0, e);
			return e;
		}
		MacroCell e = empty(n-1);
		MacroCell m = get(e, e, e, e);
		empty.put(n, m);
		return m;
	}
	
	static public MacroCell get(MacroCell[] quad) {
		return get(quad[0], quad[1], quad[2], quad[3]);
	}
	
	abstract public MacroCell part(int depth, int i, int j);
	abstract public MacroCell result(int s);
	abstract public boolean[][] toTab();
	
	public boolean isEmpty() {
		return equals(empty(n));
	}
	
	abstract public MacroCell simplify();
	abstract public MacroCell borderize();
	
	public MacroCell evolve(int t) {
		MacroCell r = this.simplify();
		
		int n=1, s=0;
		while(n < t) {
			n *= 2;
			s += 1;
		}
		while(r.n - 3 < s)
			r = r.borderize();
		System.out.println(s);
		while(t > 0) {
			if((t&n) != 0) {
				r = r.result(s).borderize();
				t -= n;
			}
			n /= 2;
			s -= 1;
		}
		
		return r.simplify();
	}
	
	public String niceString() {
		boolean[][] t = toTab();
		String s = "";
		
		for(int i=0; i<t.length; i++) {
			for(int j=0; j<t[i].length; j++)
				s += t[i][j]?'1':'0';
			s += '\n';
		}
		
		return s;
	}
	
}
