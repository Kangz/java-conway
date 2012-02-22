package hashlife;

import java.util.Arrays;

public class BigCell extends MacroCell {

	/*  +---+---+
	 *  | 0 | 1 |
	 *  +---+---+
	 *  | 2 | 3 |
	 *  +---+---+
	 */	
	final protected MacroCell quad[] = new MacroCell[4];
	private MacroCell result[];
	
	public BigCell(MacroCell m0, MacroCell m1, MacroCell m2, MacroCell m3) {
		super(m0.n + 1);
		this.quad[0] = m0;
		this.quad[1] = m1;
		this.quad[2] = m2;
		this.quad[3] = m3;
		this.result = new MacroCell[this.n-1];
	}
	
	public BigCell(MacroCell quad[]) {
		super(quad[0].n+1);
		Arrays.fill(this.quad, quad);
	}
	
	public boolean equals(Object o) {
		return (o instanceof BigCell) && equals((BigCell) o);
	}
	
	public boolean equals(BigCell m) {
		return Arrays.equals(quad, m.quad);		
	}
	
	public int hashCode() {
		int hashCode = 1;
		  for(int i=0; i<4; i++)
		      hashCode = 31*hashCode + System.identityHashCode(quad[i]);
		return hashCode;
	}
	
	public MacroCell part(int depth, int i, int j) {
		if(depth > n)
			throw new RuntimeException("Too large depth for this MacroCell");
		if(depth == 0)
			return this;
		int factor = ((int) Math.pow(2, depth))/2;
		return quad[2*(i/factor) + j/factor].part(depth-1, i%factor, j%factor);
	}
	
	public MacroCell oneStep() {
		if(n == 2) {			
			MacroCell tmp[] = new BooleanCell[4];

			for(int i=0; i<4; i++) {
				int baseI = 1+i/2, baseJ = 1+i%2;
				int count = 0;
				for(int j=0; j<9; j++)
					if(j != 4 && part(2, baseI+j/3-1, baseJ+j%3-1) == BooleanCell.True)
						count++;

				if(count == 2)
					tmp[i] = part(2, baseI, baseJ);
				else if(count == 3)
					tmp[i] = BooleanCell.True;
				else
					tmp[i] = BooleanCell.False;
			}		
			return get(tmp);
		}
		
		MacroCell resQuad[] = new MacroCell[4];
		MacroCell tmp[][] = new MacroCell[3][3];
		MacroCell cut[][] = new MacroCell[6][6];
		for(int i=0; i<6; i++)
			for(int j=0; j<6; j++)
				cut[i][j] = part(3, i+1, j+1);
		for(int i=0; i<3; i++)
			for(int j=0; j<3; j++)
				tmp[i][j] = get(cut[i*2][j*2], cut[i*2][j*2+1], cut[i*2+1][j*2], cut[i*2+1][j*2+1]);
		for(int i=0; i<4; i++)
			resQuad[i] = get(tmp[i/2][i%2], tmp[i/2][i%2+1], tmp[i/2+1][i%2], tmp[i/2+1][i%2+1]).result(0);
		
		return get(resQuad);		
	}
	
	public MacroCell result(int s) {
		if(s > n-2)
			throw new RuntimeException("Cannot return the result at time " + s + " of a " + n + " cell");
		if(s == -1)
			return this;
		if(result[s] != null)
			return result[s];
		
		if(s == 0) {			
			result[0] = oneStep();			
			return result[0];
		}
		
		// Case n > 2

		MacroCell tmp[][] = new MacroCell[3][3];
		MacroCell hexa[][] = new MacroCell[4][4];
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
				hexa[i][j] = part(2, i, j);

		for(int i=0; i<4 ;i++)
			tmp[(i/2)*2][(i%2)*2] = quad[i].result(s-1);
		for(int i=0; i<2; i++) {
			tmp[i*2][1] = get(hexa[i*2][1], hexa[i*2][2], hexa[i*2+1][1], hexa[i*2+1][2]).result(s-1);
			tmp[1][i*2] = get(hexa[1][i*2], hexa[1][i*2+1], hexa[2][i*2], hexa[2][i*2+1]).result(s-1);
		}
		tmp[1][1] = get(hexa[1][1], hexa[1][2], hexa[2][1], hexa[2][2]).result(s-1);
		
				
		MacroCell resQuad[] = new MacroCell[4];		
		for(int i=0; i<4; i++)
			resQuad[i] = get(tmp[i/2][i%2], tmp[i/2][i%2+1], tmp[i/2+1][i%2], tmp[i/2+1][i%2+1]).result(s-1);
		
		result[s] = get(resQuad);
		return result[s];
	}
	
	public MacroCell simplify() {
		if(n < 2)
			return this;
		for(int i=0; i<4; i++) {
			if(!part(2, i, 0).isEmpty() || !part(2, i, 3).isEmpty() || !part(2, (i/2)*3, i%2+1).isEmpty())
				return this;
		}
		return get(part(2, 1, 1), part(2, 1, 2), part(2, 2,1), part(2, 2, 2)).simplify();
	}
	
	public MacroCell borderize() {
		MacroCell e = empty(n-1);
		return get(get(e,e,e,quad[0]), get(e,e,quad[1],e), get(e,quad[2],e,e), get(quad[3],e,e,e));
	}
	
	public boolean[][] toTab() {
		int size = (int) Math.pow(2, n);
		boolean[][] t = new boolean[size][size];
		
		for(int i=0; i<4; i++) {
			boolean[][] tmp = quad[i].toTab();
			int baseI = (i/2)*(size/2), baseJ = (i%2)*(size/2);
			for(int j=0; j<size/2; j++)
				for(int k=0; k<size/2; k++)
					t[baseI+j][baseJ+k] = tmp[j][k];
		}
		
		return t;
	}
	
}
