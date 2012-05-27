import java.io.File;

import ui.DrawPanel;
import ui.LifeController;
import ui.Window;
import util.RLE;
import life.EvolveManager;
import life.LifeAlgo;
import life.hashlife.*;
import life.naive.NaiveAlgo;

public class Main {
	
	static public void main(String[] args) {
		
		testApp();
	}
	
	static void testApp() {
		LifeAlgo a = new NaiveAlgo();
		int[][] init = RLE.read(new File("media/ticker.rle"));
		a.loadFromArray(init);
		
		LifeController controller = new LifeController();
		
		Window w = new Window(controller);
		w.setVisible(true);

		DrawPanel drawer = w.getDrawPanel();
		EvolveManager evolver = new EvolveManager(controller, a);

		drawer.addListener(controller);
		w.getDrawPanel().start();

		evolver.run();
	}
}
