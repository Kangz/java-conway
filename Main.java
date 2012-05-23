import ui.DrawPanel;
import ui.LifeControler;
import ui.Window;
import life.EvolveManager;
import life.LifeAlgo;
import life.hashlife.*;
import util.RLE;

public class Main {
	
	final static BooleanCell f = BooleanCell.off;
	final static BooleanCell t = BooleanCell.on;
	
	static public void main(String[] args) {
		
		//testHashlife();
		testApp();
		//testRLE();
	}
	
	static void testRLE() {
		
		int[][] t = RLE.read("media/puffer.rle");
		
		System.out.println(MacroCell.niceStringFromTab(t));
		
	}
	
	static void testHashlife() {
		int[][] t = RLE.read("media/ticker.rle");

		HashLifeState s = new HashLifeState(t);
		
		long time = System.currentTimeMillis();
		s.evolve(1);
		s.evolve(1);
		s.evolve(1);
		s.evolve(1);
		s.evolve(1);
		System.out.println(System.currentTimeMillis() - time);
	}
	
	static void testApp() {
		LifeAlgo a = new HashLifeAlgo();
		a.loadFromArray(RLE.read("media/ticker.rle"));
		
		Window w = new Window();
		w.setVisible(true);

		DrawPanel drawer = w.getDrawPanel();
		EvolveManager evolver = new EvolveManager();
		LifeControler controler = new LifeControler(drawer, evolver);

		evolver.setAlgo(a);
		evolver.setControler(controler);
		drawer.addListener(controler);
		drawer.start();

		evolver.run();
		
/*		
		while(true){
			
			while(drawer.getDrawer().opLength() > 15){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			a.evolve(32);
			
			drawer.getDrawer().addOp(a.getDrawer(), a.getState());
		}
		*/
	}
}
