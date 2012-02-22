import hashlife.*;


public class Main {

	static public void main(String[] args) {
		final BooleanCell f = BooleanCell.False, t = BooleanCell.True;
		
		MacroCell empty = MacroCell.empty(1);
		MacroCell medEmpty = MacroCell.empty(2);
		MacroCell bigEmpty = MacroCell.empty(3);
		
		MacroCell a = MacroCell.get(f, t, f, f);
		MacroCell b = MacroCell.get(f, f, t, f);
		MacroCell c = MacroCell.get(t, t, f, f);
		MacroCell d = MacroCell.get(t, f, f, f);
		MacroCell base = MacroCell.get(a,b,c,d);
		
		MacroCell m1 = MacroCell.get(medEmpty, medEmpty, medEmpty, base);
		
		MacroCell planeur = MacroCell.get(m1, bigEmpty, bigEmpty, bigEmpty);
		MacroCell r = planeur.evolve(1024*1024*1024-1);

		System.out.println(planeur.simplify().niceString());
		//System.out.println(r.niceString());
		
	}

}
