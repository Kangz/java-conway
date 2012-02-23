import ui.Window;
import hashlife.*;


public class Main {
	
	final static BooleanCell f = BooleanCell.False;
	final static BooleanCell t = BooleanCell.True;
	
	static public void main(String[] args) {
		Window w = new Window();
		w.setVisible(true);
	}
	
	static void testHashlife() {
		
		MacroCell a = get(f, t, f, f);
		MacroCell b = get(f, f, t, f);
		MacroCell c = get(t, t, f, f);
		MacroCell d = get(t, f, f, f);
		MacroCell planeur = get(a,b,c,d);
		
		MacroCell r = planeur.evolve(1);
		
		System.out.println(a.off);
		System.out.println(planeur.simplify().niceString());
		System.out.println(r.niceString());
		System.out.println(r.evolve(4).niceString());
	}
	
	static MacroCell empty(int n) {
		return MacroCell.empty(n);
	}
	
	static MacroCell get(MacroCell m0, MacroCell m1, MacroCell m2, MacroCell m3) {
		return MacroCell.get(m0, m1, m2, m3);
	}

}
