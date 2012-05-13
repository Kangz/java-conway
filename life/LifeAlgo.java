package life;

public interface LifeAlgo {
	public void loadState(LifeState state);
	public LifeState saveState();
	
	public LifeDrawer getDrawer();
	
	public void loadFromArray(int[][] a);
	public int[][] saveToArray();
	
	public int getCellAt(int x, int y);
	public void setCellAt(int x, int y, int status);
	public int toggleCellAt(int x, int y);
	
	void evolve(int steps);
}
