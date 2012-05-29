package life.hashlife;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import life.LifeDrawer;
import life.LifeState;

/**
 * An implementation of the interface LifeDrawer for the Hashlife algorithm.
 */
class HashlifeDrawer implements LifeDrawer {
	
	@Override
	public void draw(int x, int y, int zoom, LifeState state, BufferedImage b) {
		MacroCell cell = ((HashlifeState) state).state;
		int realSize = cell.size;
		if(zoom < 0)
			realSize >>= -zoom;
		else
			realSize <<= zoom;
		
		x -= realSize/2;
		y -= realSize/2;

		Graphics g = b.getGraphics();
		recDraw(b, g, x, y, zoom, cell);
	}
	
	/**
	 * Draws a MacroCell recursively quadrant by quadrant
	 * @param image the image to draw onto
	 * @param g a Graphics object associated with the image
	 * @param x x coordinate of the drawn posiion of the origin
	 * @param y x coordinate of the drawn posiion of the origin
	 * @param zoom the zoom of the current view
	 * @param cellToDraw the cell to draw
	 */
	private void recDraw(BufferedImage image, Graphics g, int x, int y, int zoom, MacroCell cellToDraw) {
		//Compute the screenspace size of the cell
		int size = cellToDraw.size;
		int realSize = size;
		if(zoom < 0){
			realSize >>= -zoom;
		}else{
			realSize <<= zoom;
		}
		
		//do not draw cells outside of the screen
		int w = image.getWidth(), h = image.getHeight();
		if(x + realSize <= 0 || x >= w || y + realSize <= 0 || y >= h){
			return;
		}

		if(cellToDraw.off) {
			return;
		}
		
		//fill a rectangle for BooleanCells
		if(cellToDraw.dim == 0){
			g.setColor(Color.white);
			g.fillRect(x, y, 1<<zoom, 1<<zoom);
			return;
		}
		
		//do draw a pixel for non-empty 1-pixel large cells
		if(cellToDraw.dim <= -zoom) {
			int color = 255;
			int rgb = (new Color(color, color, color).getRGB());
			image.setRGB(x, y, rgb);
			return;
		}
		
		//Make the recursive call
		int offset = realSize/2;
		recDraw(image, g, x         , y         , zoom, cellToDraw.quad(0));
		recDraw(image, g, x + offset, y         , zoom, cellToDraw.quad(1));
		recDraw(image, g, x         , y + offset, zoom, cellToDraw.quad(2));
		recDraw(image, g, x + offset, y + offset, zoom, cellToDraw.quad(3));

	}
}
