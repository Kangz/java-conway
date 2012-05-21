package life;

import java.awt.image.BufferedImage;

public interface LifeDrawer {
	/**
	 * 
	 * @param x x-coordinate of the origin of the drawing
	 * @param y y-coordinate of the origin of the drawing
	 * @param zoom size (in power of 2) of a single cell
	 * @param state the current state to be drawn
	 * @param b the image to draw onto
	 */
	void draw(int x, int y, int zoom, LifeState state, BufferedImage b);
}
