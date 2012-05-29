package life.hashlife;

/**
 * An abstract class to represent a MacroCell.
 */
abstract class MacroCell {

	/**
	 * The dimension of the MacroCell.
	 */
	final int dim;
	
	/**
	 * The real size of the MacroCell.
	 * size is 2^dim.
	 */
	final int size;
	
	/**
	 * The density of the MacroCell, described by
	 * an integer between 0 and 255 included.
	 */
	final int density;
	
	/**
	 * True if the MacroCell is completely off.
	 */
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
	
	/**
	 * The result of a MacroCell is the state of the central MacroCell,
	 * whose dimension is dim-1, after 2^(dim-2) steps.
	 * 
	 * @return the result of the MacroCell
	 */
	final MacroCell result() {
		return result(dim-2);
	}
	
	/**
	 * @return an int array representing the MacroCell
	 */
	final int[][] toTab() {
		int[][] tab = new int[size][size];
		fillArray(tab, 0, 0);
		return tab;
	}
	
	/**
	 * 0 = north-west<br />
	 * 1 = north-east<br />
	 * 2 = south-west<br />
	 * 3 = south-east<br />
	 * 
	 * @param i an integer from 0 to 3
	 * @return one of the four quads in the MacroCell
	 */
	abstract MacroCell quad(int i);
	
	/**
	 * Compute the result of the MacroCell, but after 2^s steps.
	 * 0 <= s <= dim-2
	 *
	 * @param s gives the number of steps to do
	 * @return the result
	 */
	abstract MacroCell result(int s);
	
	/**
	 * Get the single cell at the given coordinates,
	 * starting at (0,0) in the top-left corner.
	 * 
	 * @param x the line of the cell
	 * @param y the column of the cell
	 * @return the value of the cell (0 = off, 1 = on)
	 */
	abstract int getCell(int x, int y);
	
	/**
	 * Set the state of the single cell at the given coordinates,
	 * starting at (0,0) in the top-left corner.
	 * 
	 * @param x the line of the cell
	 * @param y the column of the cell
	 * @param state the new state of the cell (0 = off, 1 = on)
	 * @return a new MacroCell, with the cell being modified
	 */
	abstract MacroCell setCell(int x, int y, int state);
	
	/**
	 * Fill the given array with the current state of the MacroCell,
	 * starting at (i,j).
	 * 
	 * @param array the array to be filled
	 * @param i the line of the first cell
	 * @param j the column of the first cell
	 */
	abstract void fillArray(int[][] array, int i, int j);
	
	/**
	 * Simplify the MacroCell by removing empty borders.
	 * 
	 * @return the simplified MacroCell
	 */
	abstract MacroCell simplify();
	
	/**
	 * Add an empty border around the MacroCell
	 * 
	 * @return the borderized MacroCell
	 */
	abstract MacroCell borderize();
}
