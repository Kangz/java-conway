package life;

import java.awt.image.BufferedImage;

/**
 * An interface for a drawer, capable to draw a LifeState.
 */
public interface LifeDrawer {
	/**
	 * Draw a LifeState at a given position, with a certain zoom,
	 * in a given BufferedImage.
	 * 
	 * @param x x-coordinate of the origin of the drawing
	 * @param y y-coordinate of the origin of the drawing
	 * @param zoom size (in power of 2) of a single cell
	 * @param state the current state to be drawn
	 * @param b the image to draw onto
	 */
	void draw(int x, int y, int zoom, LifeState state, BufferedImage b);
}
