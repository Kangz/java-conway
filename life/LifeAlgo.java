package life;

/**
 * An interface for every life algorithm.
 */
public interface LifeAlgo {
	

	/**
	 * Set the current state to another LifeState.
	 * 
	 * @param state the new state of the algorithm
	 */
	public void setState(LifeState state);
	
	/**
	 * @return the current LifeState of the algorithm
	 */
	public LifeState getState();

	/**
	 * @return a new LifeDrawer
	 */
	public LifeDrawer getDrawer();

	/**
	 * Load the state of the algorithm from an array.
	 * 
	 * @param array the int array to load
	 */
	public void loadFromArray(int[][] array);
	
	/**
	 * Save the current state to an array.
	 * @return an int array representing the current state
	 */
	public int[][] saveToArray();
	
	/**
	 * Get the state of the cell at given coordinates,
	 * starting at (0,0) in the top-left corner.<br />
	 * 0 = off<br />
	 * 1 = on<br />
	 * @param x the line of the cell
	 * @param y the column of the cell
	 * @return
	 */
	public int getCellAt(int x, int y);
	
	/**
	 * Set the state of the cell at the given coordinates,
	 * starting at (0,0) in the top-left corner.
	 * 
	 * @param x the line of the cell
	 * @param y the column of the cell
	 * @param status the new value of the cell
	 */
	public void setCellAt(int x, int y, int status);
	
	/**
	 * Invert the state of the cell at the given coordinates,
	 * starting at (0,0) in the top-left corner.
	 * 
	 * @param x the line of the cell
	 * @param y the column of the cell
	 */
	public int toggleCellAt(int x, int y);
	
	/**
	 * Make the state evolves for a given number of steps.
	 * 
	 * @param steps the number of steps to compute
	 */
	void evolve(int steps);
}
