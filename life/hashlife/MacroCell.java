package life.hashlife;

public abstract class MacroCell {
	
	final public int dim;
	final public int size;
	final public int density;
	final public boolean off;

	/**
	 * @param dim The size of the cell in power of two
	 * @param off True if it is completely off
	 */
	MacroCell(int dim, boolean off, int density) {
		this.dim = dim;
		this.size = 1 << dim;
		this.density = density;
		this.off = off;
	}
	
	public MacroCell result() {
		return result(dim-2);
	}
	
	abstract public MacroCell quad(int i);
	abstract public MacroCell result(int s);
	abstract int getCell(int x, int y);
	abstract MacroCell setCell(int x, int y, int state);

	final public int[][] toTab() {
		int[][] tab = new int[size][size];
		fillTab(tab, 0, 0);
		return tab;
	}
	
	final static public String niceStringFromTab(int[][] t) {
		String s = "";
		
		for(int i=0; i<t.length; i++) {
			for(int j=0; j<t[i].length; j++)
				s += (t[i][j] == 1)?'1':'0';
			s += '\n';
		}
		
		return s;		
	}
	
	final public String niceString() {
		return niceStringFromTab(toTab());
	}
	
	abstract public void fillTab(int[][] tab, int i, int j);
	abstract public MacroCell simplify();
	abstract public MacroCell borderize();
}
