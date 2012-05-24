package ui;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import life.EvolveManager;
import life.LifeAlgo;

public class LifeController extends ComponentAdapter implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener{
	
	protected final DrawPanel drawer;
	protected final EvolveManager evolver;
	protected Point lastMousePos = new Point(0, 0);
	protected boolean paused = false;
	protected int speed = 0;
	
	public LifeController(DrawPanel p, EvolveManager evolver) {
		this.drawer = p;
		this.evolver = evolver;
	}

	public void setSpeed(int s){
		if (s >= 0){
			drawer.getDrawer().setInterval(33);
			evolver.setSpeed(1 << s);
		}else{
			drawer.getDrawer().setInterval(33 << -s);
			evolver.setSpeed(1);
		}
	}
	
	public void componentResized(ComponentEvent e) {
		drawer.onResize();
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		int i = e.getWheelRotation();
		if(i < 0){
			drawer.zoomIn(-i, lastMousePos.x, lastMousePos.y);
		}else{
			drawer.zoomOut(i, lastMousePos.x, lastMousePos.y);
		}		
	}
	
	public void mouseClicked(MouseEvent e) {
	
	}
	
	public void mouseEntered(MouseEvent e) {
	
	}
	
	public void mouseExited(MouseEvent e) {
	
	}

	public void mousePressed(MouseEvent e) {
		lastMousePos = e.getPoint();
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		Point pt = e.getPoint();
		drawer.translate(pt.x - lastMousePos.x, pt.y - lastMousePos.y);
		lastMousePos = pt;
	}

	public void mouseMoved(MouseEvent e) {
		lastMousePos = e.getPoint();
	}
	
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_ADD:
			case KeyEvent.VK_PLUS:
			case KeyEvent.VK_I:
				drawer.zoomIn(1, drawer.getWidth()/2, drawer.getHeight()/2);
				break;
			case KeyEvent.VK_SUBTRACT:
			case KeyEvent.VK_MINUS:
			case KeyEvent.VK_K:
				drawer.zoomOut(1, drawer.getWidth()/2, drawer.getHeight()/2);	
				break;
			case KeyEvent.VK_UP:
				drawer.translate(0, 50);
				break;
			case KeyEvent.VK_DOWN:
				drawer.translate(0, -50);
				break;
			case KeyEvent.VK_LEFT:
				drawer.translate(50, 0);
				break;
			case KeyEvent.VK_RIGHT:
				drawer.translate(-50, 0);
				break;

			case KeyEvent.VK_P:
				paused = ! paused;
				drawer.getDrawer().setPaused(paused);
				break;
			case KeyEvent.VK_T:
				speed = Math.min(speed + 1, 12);
				setSpeed(speed);
				break;			
			case KeyEvent.VK_R:
				speed = Math.max(speed - 1, -6);
				setSpeed(speed);
				break;

			default:
				break;
		}
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public boolean needsMoreEvolve() {
		return (! paused) && drawer.getDrawer().opLength() < 4 ;
	}
	
	public void onNewState(LifeAlgo algo){
		drawer.getDrawer().addOp(algo.getDrawer(), algo.getState());
	}
}
