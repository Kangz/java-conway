import ui.DrawPanel;
import ui.Window;
import life.hashlife.*;
import util.RLE;

public class Main {
	
	final static BooleanCell f = BooleanCell.off;
	final static BooleanCell t = BooleanCell.on;
	
	static public void main(String[] args) {
		//Window w = new Window();
		//w.setVisible(true);
		
		testHashlife();
		//testDrawer(w.getDrawPanel());
		//testRLE();
	}
	
	static void testRLE() {
		
		int[][] t = RLE.read("media/glider.rle");
		
		System.out.println(MacroCell.niceStringFromTab(t));
		
	}
	
	static void testHashlife() {
		MacroCell a = get(f, t, f, f);
		MacroCell b = get(f, f, t, f);
		MacroCell c = get(t, t, f, f);
		MacroCell d = get(t, f, f, f);
		MacroCell planeur = Memoization.get(a, b, c, d);
		
		HashLifeState s = new HashLifeState(planeur);
		
		for(int i=0; i<10; i++) {
			s.evolve(1);
			System.out.println(s.state.niceString());
		}
	}
	
	static void testDrawer(DrawPanel drawer){
		/*MacroCell a = get(f, t, f, f);
		MacroCell b = get(f, f, t, f);
		MacroCell c = get(t, t, f, f);
		MacroCell d = get(t, f, f, f);
		MacroCell planeur = get(a,b,c,d);
		
		while(true){
			drawer.setCell(planeur);
			drawer.reDraw();
			planeur = planeur.evolve(4);
			System.out.println("Draw!!");
			for(int i=0; i<1000000000; i++){
				
			}
		}*/
	}
	
	static MacroCell empty(int n) {
		return Memoization.empty(n);
	}
	
	static MacroCell get(MacroCell m0, MacroCell m1, MacroCell m2, MacroCell m3) {
		return Memoization.get(m0, m1, m2, m3);
	}

}
