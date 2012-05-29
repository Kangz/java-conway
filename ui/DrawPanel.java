package ui;

import javax.swing.*;

import java.awt.Graphics;
import java.awt.Point;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	
	private DrawerThread dt;
	
	private int originx = 0;
	private int originy = 0;
	private int zoom = 0;
	
	public DrawPanel(LifeController controller) {
		setFocusable(true);
		requestFocusInWindow();
		dt = new DrawerThread(this);
		controller.setDrawer(this);
	}
	
	public void addListener(LifeController listener){
		addComponentListener(listener);
		addMouseMotionListener(listener);
		addMouseWheelListener(listener);
		addMouseListener(listener);
		addKeyListener(listener);
	}

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
	
	public DrawerThread getDrawer(){
		return dt;
	}
	
	public void onResize() {
		dt.setDimension(getWidth(), getHeight());
		dt.requestAnimFrame();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		synchronized(dt.imageLock){
			if (dt.getImage() != null) {
				g.drawImage(dt.getImage(), 0, 0, null);				
			}
		}
	}

	public void translate(int x, int y){
		originx += x;
		originy += y;
		applyTransform();
	}

	public void zoomIn(int i, int x, int y) {
		if(zoom + i > 8) {
			i = 8 - zoom;
		}
		zoom += i;
		originx = ((originx - x) << i) + x;
		originy = ((originy - y) << i) + y;
		applyTransform();
	}

	public void zoomOut(int i, int x, int y) {
		if(zoom - i < -19) {
			i = zoom + 19;
		}
		zoom -= i;
		originx = ((originx - x) >> i) + x;
		originy = ((originy - y) >> i) + y;
		applyTransform();
	}
	
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
	
	public int getZoom() {
		return zoom;
	}
	
	public Point getOrigin() {
		return new Point(originx, originy);
	}
	
}