package life.hashlife;

public class FourCell extends ComposedCell {
	
	final int[] quad = new int[4];
	
	FourCell(MacroCell ... quad) {
		super(quad);
		for(int i=0; i<4; i++)
			this.quad[i] = ((BooleanCell) quad[i]).v;
	}

	@Override
	public BooleanCell quad(int i) {
		return (quad[i] == 0)?BooleanCell.off:BooleanCell.on;
	}

	@Override
	public MacroCell result(int s) {
		throw new RuntimeException("Can't compute the result of a FourCell");
	}

}
