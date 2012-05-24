import ui.DrawPanel;
import ui.LifeController;
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
		
		int[][] t = RLE.read("media/glider.rle");
		System.out.println(MacroCell.niceStringFromTab(t));
		RLE.write("media/test.rle", t);
		t = RLE.read("media/test.rle");
		System.out.println(MacroCell.niceStringFromTab(t));	
		
	}
	
	static void testHashlife() {
		int[][] t = RLE.read("media/magic.rle");

		HashLifeState s = new HashLifeState(t);
		System.out.println(MacroCell.niceStringFromTab(s.toArray()));
		s.setCellAt(20, 20, 1);
		System.out.println(MacroCell.niceStringFromTab(s.toArray()));
		s.setCellAt(19, 20, 1);
		System.out.println(s.getCellAt(19, 20));
		System.out.println(s.getCellAt(2, 20));
		s.setCellAt(20, 20, 0);
		s.setCellAt(21, 20, 1);
		System.out.println(MacroCell.niceStringFromTab(s.toArray()));
	}
	
	static void testApp() {
		LifeAlgo a = new HashLifeAlgo();
		a.loadFromArray(RLE.read("media/metapixel.rle"));
		
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
