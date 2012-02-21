package hashlife;

public class BooleanCell extends MacroCell {
	
	static final public BooleanCell True = new BooleanCell(true);
	static final public BooleanCell False = new BooleanCell(false);
	final Boolean v;
	
	
	private BooleanCell(boolean v) {
		super(null, null, null, null, 0);
		this.v = v;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof BooleanCell))
			return false;
		BooleanCell m = (BooleanCell) o;
		return v.equals(m.v);
	}
	
	public int hashCode() {
		return v.hashCode();
	}
	
}
