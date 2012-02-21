package hashlife;

public class BigCell extends MacroCell {
	
	public BigCell(MacroCell m0, MacroCell m1, MacroCell m2, MacroCell m3) {
		super(m0, m1, m2, m3, m0.n+1);
	}
	
	public BigCell(MacroCell quad[]) {
		super(quad[0], quad[1], quad[2], quad[3], quad[0].n+1);
	}
	
}
