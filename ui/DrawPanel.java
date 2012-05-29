package ui;

import javax.swing.*;

import java.awt.Graphics;
import java.awt.Point;

@SuppressWarnings("serial")
/**
 * The panel in which the grid is drawn.
 */
public class DrawPanel extends JPanel {
	
	private DrawerThread dt;
	
	private int originx = 0;
	private int originy = 0;
	private int zoom = 0;
	
	/**
	 * This constructor also build a new DrawerThread.
	 * 
	 * @param controller the LifeController to link the DrawPanel to.
	 */
	public DrawPanel(LifeController controller) {
		setFocusable(true);
		requestFocusInWindow();
		dt = new DrawerThread(this);
		controller.setDrawer(this);
	}
	
	/**
	 * The LifeController is also a Listener (in a awt.event meaning).
	 * @param listener the LifeController who must listen to this DrawPanel
	 */
	public void addListener(LifeController listener){
		addComponentListener(listener);
		addMouseMotionListener(listener);
		addMouseWheelListener(listener);
		addMouseListener(listener);
		addKeyListener(listener);
	}

	/**
	 * Run the evolution.
	 */
	public void start() {
		applyTransform();
		dt.setDimension(getWidth(), getHeight());
		Thread t = new Thread(dt);
		t.start();
	}
	
	private void applyTransform(){
		dt.setTransform(originx, originy, zoom);
		dt.requestAnimFrame();
	}
	
	/**
	 * @return the DrawerThread associated with this DrawPanel.
	 */
	public DrawerThread getDrawer(){
		return dt;
	}
	
	/**
	 * The event called when the panel is resized.
	 */
	public void onResize() {
		dt.setDimension(getWidth(), getHeight());
		dt.requestAnimFrame();
	}
	
	@Override
	/**
	 * Overrid the behavior of this panel when
	 * it must be drawn.
	 * 
	 * @param g the Graphics to draw onto
	 */
	public void paintComponent(Graphics g) {
		synchronized(dt.imageLock){
			if (dt.getImage() != null) {
				g.drawImage(dt.getImage(), 0, 0, null);				
			}
		}
	}
	
	/**
	 * Translate the view of a given offset.
	 * @param x the abscissa to translate of
	 * @param y the ordinate to translate of
	 */
	public void translate(int x, int y){
		originx += x;
		originy += y;
		applyTransform();
	}
	
	/**
	 * Zoom in at point on the screen.
	 * The zoom is capped between -19 and 8.
	 * 
	 * @param i the factor of zoom, in power of 2
	 * @param x the x-coordinate of the targetted point
	 * @param y the y-coordinate of the targetted point
	 */
	public void zoomIn(int i, int x, int y) {
		if(zoom + i > 8) {
			i = 8 - zoom;
		}
		zoom += i;
		originx = ((originx - x) << i) + x;
		originy = ((originy - y) << i) + y;
		applyTransform();
	}

	/**
	 * Zoom out at point on the screen.
	 * The zoom is capped between -19 and 8.
	 * 
	 * @param i the factor of zoom, in power of 2
	 * @param x the x-coordinate of the targetted point
	 * @param y the y-coordinate of the targetted point
	 */
	public void zoomOut(int i, int x, int y) {
		if(zoom - i < -19) {
			i = zoom + 19;
		}
		zoom -= i;
		originx = ((originx - x) >> i) + x;
		originy = ((originy - y) >> i) + y;
		applyTransform();
	}
	
	/**
	 * Set the zoom and center the view.
	 * 
	 * @param z the new zoom
	 */
	public void setView(int z) {
		zoom = z;
		if(zoom < -19)
			zoom = -19;
		if(zoom > 8)
			zoom = 8;
		originx = getWidth()/2;
		originy = getHeight()/2;
		applyTransform();
	}
	
	/**
	 * @return the current zoom, in power of 2
	 */
	public int getZoom() {
		return zoom;
	}
	
	/**
	 * @return the current origin, as a Point
	 */
	public Point getOrigin() {
		return new Point(originx, originy);
	}
	
}