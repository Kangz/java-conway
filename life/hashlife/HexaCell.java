package life.hashlife;

/**
 * A MacroCell of size 4x4, composed of four HexaCell.
 */
class HexaCell extends ComposedCell {

	final FourCell[] quad = new FourCell[4];

	/**
	 * Builds a HexaCell out of its four FourCell.
	 * @param quad the four quadrants of the HexaCell
	 */
	HexaCell(MacroCell ... quad) {
		super(quad);
		for(int i=0; i<4; i++)
			this.quad[i] = (FourCell) quad[i];
	}

	@Override
	void calcResult(int s) {
		assert(s == 0);
		int[] count = new int[4];
		
		count[0] = 	quad[0].quad[0] 	+ quad[0].quad[1] 	+ quad[1].quad[0] + 
					quad[0].quad[2] 						+ quad[1].quad[2] + 
					quad[2].quad[0] 	+ quad[2].quad[1] 	+ quad[3].quad[0];
		count[1] = 	quad[0].quad[1] 	+ quad[1].quad[0] 	+ quad[1].quad[1] + 
					quad[0].quad[3] 						+ quad[1].quad[3] + 
					quad[2].quad[1] 	+ quad[3].quad[0] 	+ quad[3].quad[1];
		count[2] = 	quad[0].quad[2] 	+ quad[0].quad[3] 	+ quad[1].quad[2] + 
					quad[2].quad[0] 						+ quad[3].quad[0] + 
					quad[2].quad[2] 	+ quad[2].quad[3] 	+ quad[3].quad[2];
		count[3] = 	quad[0].quad[3] 	+ quad[1].quad[2] 	+ quad[1].quad[3] + 
					quad[2].quad[1] 						+ quad[3].quad[1] + 
					quad[2].quad[3] 	+ quad[3].quad[2] 	+ quad[3].quad[3];
		
		BooleanCell[] newQuad = new BooleanCell[4];
		for(int i=0; i<4; i++) {
			if(count[i] < 2 || count[i] > 3)
				newQuad[i] = BooleanCell.off;
			else if(count[i] == 3)
				newQuad[i] = BooleanCell.on;
			else
				newQuad[i] = quad[i].quad(3-i);
		}
		result[0] = Memoization.get(newQuad);
	}

	@Override
	FourCell quad(int i) {
		return quad[i];
	}
}
