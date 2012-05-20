package life.hashlife;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import life.LifeDrawer;
import life.LifeState;

public class HashLifeDrawer implements LifeDrawer {

	@Override
	public void draw(int x, int y, int zoom, LifeState state, BufferedImage b) {
		MacroCell cell = ((HashLifeState) state).state;
		int realSize = cell.size;
		if(zoom < 0)
			realSize >>= -zoom;
		else
			realSize <<= zoom;
		recDraw(b, b.getGraphics(), x, y, zoom, cell);
		
	}
	
	private void recDraw(BufferedImage b, Graphics g, int x, int y, int zoom, MacroCell cell) {

		int size = cell.size;
		int realSize = size;
		if(zoom < 0)
			realSize >>= -zoom;
		else
			realSize <<= zoom;
		
		int w = b.getWidth(), h = b.getHeight();
		if(x + realSize < 0 || x > w || y + realSize < 0 || y > h){
			return;
		}
		
		if(cell.off) {
			g.setColor(Color.black);
			g.fillRect(x, y, realSize, realSize);
			return;
		}
		
		if(cell.dim == 0){
			//System.out.println("Draw x: " + x + ", y: " + y);
			int color = cell.density;
			g.setColor(new Color(color, color, color));
			g.fillRect(x, y, 1<<zoom, 1<<zoom);
			return;
		} else if(cell.dim == -zoom) {
			int rgb = cell.density + cell.density<<8 + cell.density<<16;
			b.setRGB(x, y, rgb);
		}
		
		int offset = realSize/2;
		recDraw(b, g, x         , y         , zoom, cell.quad(0));
		recDraw(b, g, x + offset, y         , zoom, cell.quad(1));
		recDraw(b, g, x         , y + offset, zoom, cell.quad(2));
		recDraw(b, g, x + offset, y + offset, zoom, cell.quad(3));
	}


}
