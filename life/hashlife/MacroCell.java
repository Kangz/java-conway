package life.hashlife;

abstract class MacroCell {

	final int dim;
	final int size;
	final int density;
	final boolean off;

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

	final MacroCell result() {
		return result(dim-2);
	}

	final int[][] toTab() {
		int[][] tab = new int[size][size];
		fillTab(tab, 0, 0);
		return tab;
	}

	abstract MacroCell quad(int i);
	abstract MacroCell result(int s);
	abstract int getCell(int x, int y);
	abstract MacroCell setCell(int x, int y, int state);
	abstract void fillTab(int[][] tab, int i, int j);
	abstract MacroCell simplify();
	abstract MacroCell borderize();
}
