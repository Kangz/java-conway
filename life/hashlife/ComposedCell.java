package life.hashlife;

class ComposedCell extends MacroCell {
	
	final MacroCell[] quad ;
	final MacroCell result[];
	
	ComposedCell(MacroCell ... quad) {
		super(	quad[0].dim+1, 
				quad[0].off && 
				quad[1].off && 
				quad[2].off && 
				quad[3].off, calcDensity(quad));
		this.quad = quad.clone();
		this.result = new MacroCell[this.dim-1];
	}

	private static int calcDensity(MacroCell ... quad) {
		int density = 0;
		for(int i=0; i<4; i++) {
			density += quad[i].density;
		}
		return density/4;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof ComposedCell))
			return false;
		ComposedCell c = (ComposedCell) o;
		return 	quad[0] == c.quad[0] &&
				quad[1] == c.quad[1] &&
				quad[2] == c.quad[2] &&
				quad[3] == c.quad[3];
	}
	
	public int hashCode() {
		int hashCode = 1;
		  for(int i=0; i<4; i++)
		      hashCode = 31*hashCode + System.identityHashCode(quad[i]);
		return hashCode;
	}
	
	public MacroCell quad(int i) {
		return quad[i];
	}
	
	protected void calcResult(int s) {
		MacroCell nine[][] = new MacroCell[3][3];
		for(int i=0; i<4 ; i++) {
			nine[(i/2)*2][(i%2)*2] = quad[i];
		}
		nine[0][1] = Memoization.get(quad[0].quad(1), quad[1].quad(0), quad[0].quad(3), quad[1].quad(2));
		nine[1][0] = Memoization.get(quad[0].quad(2), quad[0].quad(3), quad[2].quad(0), quad[2].quad(1));
		nine[1][2] = Memoization.get(quad[1].quad(2), quad[1].quad(3), quad[3].quad(0), quad[3].quad(1));
		nine[2][1] = Memoization.get(quad[2].quad(1), quad[3].quad(0), quad[2].quad(3), quad[3].quad(2));
		nine[1][1] = Memoization.get(quad[0].quad(3), quad[1].quad(2), quad[2].quad(1), quad[3].quad(0));
		
		for(int i=0; i<3; i++)
			for(int j=0; j<3; j++) {
				nine[i][j] = nine[i][j].result((s == dim - 2)?(s-1):s);
			}
			
		MacroCell four[] = new MacroCell[4];
		for(int i=0; i<2; i++)
			for(int j=0; j<2; j++) {
				int idx = i*2+j;
				four[idx] = Memoization.get(nine[i][j], nine[i][j+1], nine[i+1][j], nine[i+1][j+1]);
				if(s == dim-2)
					four[idx] = four[idx].result();
				else {
					MacroCell tmp[] = new MacroCell[4];
					for(int k=0; k<4; k++)
						tmp[k] = four[idx].quad(k).quad(3-k);
					four[idx] = Memoization.get(tmp);
				}
			}
		
		result[s] = Memoization.get(four);
	}
	
	@Override
	public MacroCell result(int s) {
		if(s > dim - 2)
			throw new RuntimeException("Can't compute the result at time 2^"+s+" of a MacroCell of dim " + dim);
		if(s < 0)
			return this;
		if(result[s] == null)
			calcResult(s);
		return result[s];
	}

	public void fillTab(int[][] tab, int i, int j) {
		if(off)
			for(int k=0; k<size; k++)
				for(int l=0; l<size; l++)
					tab[i+k][j+l] = 0;
		else
			for(int k=0; k<4; k++)
				quad[k].fillTab(tab, i+(k/2)*(size/2), j+(k%2)*(size/2));
	}

	@Override
	public MacroCell simplify() {
		if(off)
			return Memoization.empty(1);
		
		if(dim == 1)
			return this;
		
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				if(j != 3-i && !quad[i].quad(j).off)
					return this;
			}
		}
		
		MacroCell[] newQuad = new MacroCell[4];
		for(int i=0; i<4; i++)
			newQuad[i] = quad[i].quad(3-i);
		return Memoization.get(newQuad).simplify();
	}

	@Override
	public MacroCell borderize() {
		
		if(off)
			return Memoization.empty(dim+1);
		MacroCell e = Memoization.empty(dim-1);
		return Memoization.get(
				Memoization.get(e,e,e,quad[0]),
				Memoization.get(e,e,quad[1],e),
				Memoization.get(e,quad[2],e,e),
				Memoization.get(quad[3],e,e,e));
	}

	@Override
	int getCell(int x, int y) {
		if(x < 0 || y < 0 || x >= size || y >= size)
			return 0;
		int halfSize = size/2;
		int i = x/halfSize, j = y/halfSize;
		return quad[2*i+j].getCell(x - i*halfSize, y - j*halfSize);
	}

	@Override
	MacroCell setCell(int x, int y, int state) {
		int halfSize = size/2;
		if(x < 0 || y < 0 || x >= size || y >= size)
			return borderize().setCell(x + halfSize,  y + halfSize, state);
		int i = x/halfSize, j = y/halfSize;
		
		MacroCell[] tmp = new MacroCell[4];
		for(int k=0; k<4; k++)
			tmp[k] = quad[k];
		tmp[2*i+j] = tmp[2*i+j].setCell(x - i*halfSize, y - j*halfSize, state);
		return Memoization.get(tmp);
	}

}
