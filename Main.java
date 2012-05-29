import java.io.File;

import ui.LifeController;
import ui.Window;
import life.EvolveManager;
import life.LifeAlgo;
import life.hashlife.*;

public class Main {
	
	static public void main(String[] args) {
		
		app();
	}
	
	static void app() {
		LifeAlgo a = new HashlifeAlgo();
		LifeController controller = new LifeController();
		
		
		Window w = new Window(controller);
		EvolveManager evolver = new EvolveManager(controller, a);
		controller.loadFromFile(new File("media/ticker.rle"));

		w.panel.drawPanel.addListener(controller);
		w.setVisible(true);
		w.panel.drawPanel.start();
		

		evolver.run();
	}
}
