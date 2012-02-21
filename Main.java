import hashlife.*;


public class Main {

	static public void main(String[] args) {
		final BooleanCell f = BooleanCell.False, t = BooleanCell.True;
		MacroCell m1 = MacroCell.get(t, f, f, t);
		MacroCell m2 = MacroCell.get(t, t, f, t);
		MacroCell m3 = MacroCell.get(t, f, f, t);
		
		System.out.println(m1.equals(m3));
		System.out.println(m2.equals(m3));
		System.out.println(m1.equals(m2));
	}

}
