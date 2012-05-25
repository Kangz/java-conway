import ui.DrawPanel;
import ui.LifeController;
import ui.Window;
import life.EvolveManager;
import life.LifeAlgo;
import life.hashlife.*;

public class Main {
	
	static public void main(String[] args) {
		
		testApp();
	}
	
	static void testApp() {
		LifeAlgo a = new HashLifeAlgo();
		int[][] init = {{0}};
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
