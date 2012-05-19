package life;

public interface LifeAlgo {
	public void setState(LifeState state);
	public LifeState getState();
	
	public LifeDrawer getDrawer();
	
	public void loadFromArray(int[][] array);
	public int[][] saveToArray();
	
	public int getCellAt(int x, int y);
	public void setCellAt(int x, int y, int status);
	public int toggleCellAt(int x, int y);
	
	void evolve(int steps);
}
