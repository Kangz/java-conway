package life;

import ui.LifeController;

public class EvolveManager implements Runnable {

	LifeAlgo algo;
	LifeController control;
	int speed = 1;
	boolean running = false;
	
	public EvolveManager(){
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
			
			algo.evolve(speed);
			
			control.onNewState(algo);
		}
	}
}
