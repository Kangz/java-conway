package life;

import java.util.LinkedList;
import java.util.Queue;

import ui.LifeController;

public class EvolveManager implements Runnable {

	LifeAlgo algo;
	LifeController control;
	int speed = 1;
	boolean running = false;
	boolean forcedState = false;

	Queue<Order> orders = new LinkedList<Order>();
	
	abstract class Order{
		abstract void doOrder();
	}
	
	class LoadTabOrder extends Order{
		int[][] tab;
		LoadTabOrder(int[][] t){
			tab = t;
		}
		void doOrder(){
			algo.loadFromArray(tab);
		}
	}

	class ForcedOrder extends Order{
		void doOrder(){
			forcedState = true;
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
	
	public EvolveManager(){
	}
	
	public void loadTab(int[][] t){
		synchronized(orders){
			orders.add(new LoadTabOrder(t));
			orders.add(new ForcedOrder());
		}
	}

	public void toggleCell(int x, int y){
		synchronized(orders){
			orders.add(new ToggleCellOrder(x, y));
			orders.add(new ForcedOrder());
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
			while(! control.needsMoreEvolve()){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
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
			
			algo.evolve(speed);
			
			control.onNewState(algo, forcedState);
			
			forcedState = false;
		}
	}
}
