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
		LifeAlgo a = new HashLifeAlgo();
		LifeController controller = new LifeController();
		
		
		Window w = new Window(controller);
		EvolveManager evolver = new EvolveManager(controller, a);
		controller.loadFromFile(new File("media/ticker.rle"));

		DrawPanel drawer = w.getDrawPanel();

		drawer.addListener(controller);
		w.setVisible(true);
		w.getDrawPanel().start();
		

		evolver.run();
	}
}
