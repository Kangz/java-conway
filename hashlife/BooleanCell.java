package hashlife;

public class BooleanCell extends MacroCell {
	
	static final public BooleanCell True = new BooleanCell(true);
	static final public BooleanCell False = new BooleanCell(false);
	final Boolean v;
	
	
	private BooleanCell(boolean v) {
		super(0, !v);
		this.v = v;
	}
	
	public boolean equals(Object o) {
		return (o instanceof BooleanCell) && v.equals(((BooleanCell) o).v);
	}
	
	public int hashCode() {
		return v.hashCode();
	}
	
	public MacroCell part(int depth, int i, int j) {
		if(depth > 0)
			throw new RuntimeException("A BooleanCell is undivisible");
		return this;
	}
	
	public MacroCell simplify() {
		throw new RuntimeException("A BooleanCell cannot be simplified");
	}
	
	public MacroCell borderize() {
		throw new RuntimeException("A BooleanCell cannot be borderized");
	}
	
	public MacroCell result(int s) {
		throw new RuntimeException("Cannot return the result of a 1x1 cell");
	}
	
	public MacroCell evolve(int t) {
		throw new RuntimeException("A BooleanCell cannot evolve");
	}
	
	public void fillTab(boolean[][] tab, int i, int j) {
		tab[i][j] = v;
	}
	
}
