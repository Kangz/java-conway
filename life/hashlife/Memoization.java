package life.hashlife;

import life.hashlife.ComposedCell;
import life.hashlife.MacroCell;

import java.util.ArrayList;
import java.util.HashMap;

public class Memoization {
	static private ArrayList<MacroCell> empty = new ArrayList<MacroCell>();
	static private HashMap<MacroCell, MacroCell> built = new HashMap<MacroCell, MacroCell>();

	static public MacroCell get(MacroCell ... quad) {
		assert(quad.length == 4 && quad[0].dim == quad[1].dim && quad[1].dim == quad[2].dim && quad[2].dim == quad[3].dim);
		MacroCell m;
		if(quad[0].dim == 0)
			m = new FourCell(quad);
		else if(quad[0].dim == 1)
			m = new HexaCell(quad);
		else
			m = new ComposedCell(quad);
		if(built.containsKey(m))
			m = built.get(m);
		else
			built.put(m, m);
		
		return m;
	}
	
	static public MacroCell empty(int dim) {
		if(dim < 0)
			throw new RuntimeException("The dimension of an empty MacroCell must be at least 0.");

		empty.ensureCapacity(dim+1);
		if(empty.isEmpty())
			empty.add(BooleanCell.off);
		
		int todo = dim - empty.size() + 1;
		while(todo-- > 0) {
			MacroCell e = empty.get(dim - todo - 1);
			empty.add(get(e, e, e, e));
		}
		return empty.get(dim);
	}
	
}
