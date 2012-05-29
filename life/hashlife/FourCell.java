package life.hashlife;

/**
 * A MacroCell of size 2x2, composed of four BooleanCell.
 */
class FourCell extends ComposedCell {
	
	final int[] quad = new int[4];

	/**
	 * Builds a FourCell out of its four BooleanCell.
	 * @param quad the four quadrants of the FourCell
	 */
	FourCell(MacroCell ... quad) {
		super(quad);
		for(int i=0; i<4; i++)
			this.quad[i] = ((BooleanCell) quad[i]).v;
	}

	@Override
	BooleanCell quad(int i) {
		return (quad[i] == 0)?BooleanCell.off:BooleanCell.on;
	}

	@Override
	MacroCell result(int s) {
		throw new RuntimeException("Can't compute the result of a FourCell");
	}
}
