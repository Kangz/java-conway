package life;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import ui.LifeController;
import util.RLE;

/**
 * The evolver is started with run and is on a thread of its own.
 * It can receive orders through an order system that makes him
 * like an actor in a single actor system. It contains an history stack
 * that makes it able to get back into the past (which is neat
 * when you love watching the inner workings of a big life system).
 */
public class EvolveManager implements Runnable {

	//The other two important objects
	LifeAlgo algo;
	LifeController control;
	
	//Some internal state
	int speed = 1;
	int currentSpeed = 1;
	boolean running = false;
	boolean forcedState = false;
	boolean preventEvolve = false;
	long stepNumber = 0;
	
	//The data structures for history and orders
	Queue<Order> orders = new LinkedList<Order>();
	Stack<EvolveManagerState> stateStack = new Stack<EvolveManagerState>();
	
	/**
	 * A simple order nested class that can modify the inner variables of the evolver
	 */
	abstract class Order{
		abstract void doOrder();
	}
	
	//Flushes the history
	class FlushHistoryOrder extends Order{
		FlushHistoryOrder() {
		}
		void doOrder() {
			stateStack.clear();
			stepNumber = 0;
		}
	}

	//Allows to feed a state to go back to
	class ResetFromStateOrder extends Order{
		EvolveManagerState state;
		ResetFromStateOrder(EvolveManagerState s){
			state = s;
		}
		void doOrder(){
			algo = state.algo;
			stepNumber = state.nSteps;
			algo.setState(state.state);
		}
	}
	
	//Makes it load a new state from an rle file
	class LoadFromFileOrder extends Order{
		File file;
		LoadFromFileOrder(File f){
			file = f;
		}
		void doOrder(){
			int[][] array = RLE.read(file);
			algo.loadFromArray(array);
			
			//here is some logic to set the zoom after a load properly
			//it should be in the controller and as such, is fugly
			int dimw = array[0].length;
			int dimh = array.length;
			int w = control.getDrawer().getWidth(), h = control.getDrawer().getHeight();
			int zoom;
			if(w > dimw) {
				zoom = 31 - Integer.numberOfLeadingZeros(w / dimw);
			} else {
				zoom = Integer.numberOfLeadingZeros(dimw / w) - 33;
			}
			if(h > dimh) {
				zoom = Math.min(zoom, 31 - Integer.numberOfLeadingZeros(h / dimh));
			} else {
				zoom = Math.min(zoom, Integer.numberOfLeadingZeros(dimh / h) - 33);
			}
			control.getDrawer().setView(zoom);
		}
	}
	
	//Saves the current state to a file
	class SaveToFileOrder extends Order{
		File file;
		SaveToFileOrder(File f){
			file = f;
		}
		void doOrder(){
			RLE.write(file, algo.saveToArray());
		}
	}

	//Sets the order as forced which means that the resulting state will be forced
	//and that the evolver step should be done immediatly
	class ForcedOrder extends Order{
		void doOrder(){
			forcedState = true;
		}
	}
	
	//Modifies for a single evolver step the evolving speed
	class JumpOrder extends Order{
		int askedSpeed;
		JumpOrder(int s){
			askedSpeed = s;
		}
		void doOrder(){
			currentSpeed = askedSpeed;
		}
	}
	
	//We do not want to evolve but only carry orders
	class PreventEvolveOrder extends Order{
		void doOrder(){
			preventEvolve = true;
		}
	}

	//For some interaction with humans
	class ToggleCellOrder extends Order{
		int x, y;
		ToggleCellOrder(int a, int b){
			x = a;
			y = b;
		}
		void doOrder(){
			algo.toggleCellAt(x, y);
		}
	}
	
	/**
	 * constructs a new EvolveManager
	 * @param controller the associated controller
	 * @param a the starting algorithm
	 */
	public EvolveManager(LifeController controller, LifeAlgo a){
		controller.setEvolver(this);
		setController(controller);
		setAlgo(a);
	}
	
	/**
	 * Pops the stack of history until we are before the target event
	 * or there is only one element left
	 * @param target the target time
	 */
	public void getBackToClosestState(long target){
		while (stateStack.size() > 1 && stateStack.peek().nSteps > target){
			stateStack.pop();
		}
	}
	
	/**
	 * @param f the file to load from
	 */
	public void loadFromFile(File f){
		synchronized(orders){
			orders.add(new LoadFromFileOrder(f));
			orders.add(new ForcedOrder());
			orders.add(new PreventEvolveOrder());
		}
	}
	
	/**
	 * @param f the file to save to
	 */
	public void saveToFile(File f){
		synchronized(orders){
			orders.add(new SaveToFileOrder(f));
			orders.add(new ForcedOrder());
			orders.add(new PreventEvolveOrder());
		}
	}

	/**
	 * @param x the x coordinate of the cell to toggle
	 * @param y the y coordinate of the cell to toggle
	 */
	public void toggleCell(int x, int y){
		synchronized(orders){
			orders.add(new ToggleCellOrder(x, y));
			orders.add(new ForcedOrder());
			orders.add(new PreventEvolveOrder());
		}
	}
	
	/**
	 * @param steps the number of steps we want to make
	 */
	public void jump(int steps){
		synchronized(orders){
			orders.add(new JumpOrder(steps));
			orders.add(new ForcedOrder());
		}
	}
	
	/**
	 * Makes the next evolver step forced
	 */
	public void forceNext(){
		synchronized(orders){
			orders.add(new ForcedOrder());
		}
	}
	
	/**
	 * @param c the controller to be set
	 */
	public void setController(LifeController c){
		control = c;
	}
	
	/**
	 * @return the current life algo
	 */
	public LifeAlgo getAlgo(){
		return algo;
	}
	
	/**
	 * @param a the algo to be set
	 */
	public void setAlgo(LifeAlgo a){
		algo = a;
	}
	
	/**
	 * @return the current speed
	 */
	public int getSpeed(){
		return speed;	
	}
	
	/**
	 * -@param s the speed to be set
	 */
	public void setSpeed(int s){
		speed = s;
	}
	
	/**
	 * asks the evolver to stop his never ending cycle
	 */
	public void stop(){
		running = false;
	}
	
	public void run(){
		running = true;
		
		while(running){
			//We wait until the controller needs us to evolve more or there are new orders
			boolean hasOrders = false;
			while(! control.needsMoreEvolve() && ! hasOrders){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized(orders){
					hasOrders = !orders.isEmpty();
				}
			}
			
			//Copies and apply orders
			Queue<Order> copiedOrders;
			synchronized(orders){
				copiedOrders = orders;
				orders = new LinkedList<Order>();
			}

			currentSpeed = speed;			
			
			for(Order order : copiedOrders){
				order.doOrder();
			}

			if(! preventEvolve){
				//Simple case: go in the future
				if(currentSpeed >= 0){
					algo.evolve(currentSpeed);
					stepNumber += currentSpeed;
				}else{
					//hard case: go into the past
					//try to get back before the target event
					long target = stepNumber + currentSpeed;
					getBackToClosestState(target);
					
					algo.setState(stateStack.peek().state);
					long reachedEvent = stateStack.peek().nSteps;
					stepNumber = reachedEvent;
					
					//if we are before the target event we need to evolve more
					if(reachedEvent < target){
						int offset = (int) (target - reachedEvent);

						algo.evolve(offset);
						stepNumber += offset;
					}
				}
			}
			
			//tell the history and the controller about the new state
			EvolveManagerState evolveState = new EvolveManagerState(stepNumber, forcedState, algo);
			stateStack.add(evolveState);
			control.onNewState(evolveState);
			
			forcedState = false;
			preventEvolve = false;
		}
	}

	//asks the controller to reset with an older state
	public void resetState(EvolveManagerState lastDrawnState) {
		synchronized(orders){
			orders.add(new ResetFromStateOrder(lastDrawnState));
		}
	}
}
