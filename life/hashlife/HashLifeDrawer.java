package life.hashlife;

import java.awt.Color;
import java.awt.Graphics;

import life.LifeDrawer;
import life.LifeState;

public class HashLifeDrawer implements LifeDrawer {

	@Override
	public void draw(int x, int y, int w, int h, int zoom, LifeState state, Graphics g) {
		recDraw(g, x, y, w, h, zoom, ((HashLifeState) state).state);
		
	}
	
	private void recDraw(Graphics g, int x, int y, int w, int h, int zoom, MacroCell cell){

		int size = cell.size;
		if(x + size < 0 || x > w || y + size < 0 || y > h){
			return;
		}
		
		if(cell.dim == 0){
			System.out.println("Draw x: " + x + ", y: " + y);
			int density = cell.density;
			g.setColor(new Color(density, density, density));
			g.fillRect(x, y, zoom, zoom);
			return;
		}
		
		int offset = zoom*size/2;
		recDraw(g, x         , y         , w, h, zoom, cell.quad(0));
		recDraw(g, x + offset, y         , w, h, zoom, cell.quad(1));
		recDraw(g, x         , y + offset, w, h, zoom, cell.quad(2));
		recDraw(g, x + offset, y + offset, w, h, zoom, cell.quad(3));
	}


}
