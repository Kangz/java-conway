package life.hashlife;

class BooleanCell extends MacroCell {

	final static BooleanCell on = new BooleanCell(1);
	final static BooleanCell off = new BooleanCell(0);
	
	final int v;
	
	private BooleanCell(int v) {
		super(0, v == 0, (v == 0)?0:255);
		this.v = (v == 0)?0:1;
	}
	
	@Override
	MacroCell quad(int i) {
		throw new RuntimeException("Can't get quads of a BooleanCell");
	}

	@Override
	MacroCell result(int s) {
		throw new RuntimeException("Can't compute the result of a BooleanCell");
	}

	@Override
	void fillArray(int[][] array, int i, int j) {
		array[i][j] = v;
	}

	@Override
	MacroCell simplify() {
		throw new RuntimeException("Can't simplify a BooleanCell");
	}

	@Override
	MacroCell borderize() {
		throw new RuntimeException("Can't borderize a BooleanCell");
	}

	@Override
	int getCell(int x, int y) {
		return v;
	}

	@Override
	MacroCell setCell(int x, int y, int state) {
		if(state == 0)
			return off;
		return on;
	}

}
