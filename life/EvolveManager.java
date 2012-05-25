package life;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import ui.LifeController;
import util.RLE;

public class EvolveManager implements Runnable {

	LifeAlgo algo;
	LifeController control;
	int speed = 1;
	boolean running = false;
	boolean forcedState = false;
	boolean preventEvolve = false;
	long stepNumber = 0;
	
	Queue<Order> orders = new LinkedList<Order>();
	
	abstract class Order{
		abstract void doOrder();
	}

	class ResetFromStateOrder extends Order{
		EvolveManagerState state;
		ResetFromStateOrder(EvolveManagerState s){
			state = s;
		}
		void doOrder(){
			algo = state.algo;
			stepNumber = state.nSteps;
			algo.setState(state.state);
			System.out.println("Reset to state n°" + stepNumber);
		}
	}
	
	class LoadFromFileOrder extends Order{
		File file;
		LoadFromFileOrder(File f){
			file = f;
		}
		void doOrder(){
			algo.loadFromArray(RLE.read(file));
		}
	}
	
	class SaveToFileOrder extends Order{
		File file;
		SaveToFileOrder(File f){
			file = f;
		}
		void doOrder(){
			RLE.write(file, algo.saveToArray());
		}
	}

	class ForcedOrder extends Order{
		void doOrder(){
			forcedState = true;
		}
	}

	class PreventEvolveOrder extends Order{
		void doOrder(){
			preventEvolve = true;
		}
	}

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
	
	public EvolveManager(LifeController controller, LifeAlgo a){
		controller.setEvolver(this);
		setController(controller);
		setAlgo(a);
	}
	
	public void loadFromFile(File f){
		synchronized(orders){
			orders.add(new LoadFromFileOrder(f));
			orders.add(new ForcedOrder());
			orders.add(new PreventEvolveOrder());
		}
	}
	
	public void saveToFile(File f){
		synchronized(orders){
			orders.add(new SaveToFileOrder(f));
			orders.add(new ForcedOrder());
			orders.add(new PreventEvolveOrder());
		}
	}

	public void toggleCell(int x, int y){
		synchronized(orders){
			orders.add(new ToggleCellOrder(x, y));
			orders.add(new ForcedOrder());
			orders.add(new PreventEvolveOrder());
		}
	}
	
	public void setController(LifeController c){
		control = c;
	}
	
	public LifeAlgo getAlgo(){
		return algo;
	}
	
	public void setAlgo(LifeAlgo a){
		algo = a;
	}
	
	public int getSpeed(){
		return speed;	
	}
	
	public void setSpeed(int s){
		speed = s;
	}
	
	public void stop(){
		running = false;
	}
	
	public void run(){
		running = true;
		
		while(running){
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
						
			Queue<Order> copiedOrders;
			synchronized(orders){
				copiedOrders = orders;
				orders = new LinkedList<Order>();
			}
			
			for(Order order : copiedOrders){
				order.doOrder();
			}

			if(! preventEvolve){
				algo.evolve(speed);
				stepNumber += speed;
			}
			
			preventEvolve = false;
			
			control.onNewState(new EvolveManagerState(stepNumber, forcedState, algo));
			
			System.out.println("Sent state n°" + stepNumber);
			
			forcedState = false;
		}
	}

	public void resetState(EvolveManagerState lastDrawnState) {
		synchronized(orders){
			orders.add(new ResetFromStateOrder(lastDrawnState));
		}
	}
}
