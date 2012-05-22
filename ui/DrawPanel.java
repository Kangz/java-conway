package ui;

import javax.swing.*;

import java.awt.Graphics;
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
import java.awt.image.BufferedImage;

import life.LifeAlgo;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	
	private LifeAlgo currentAlgo = null;
	private DrawerThread dt;
	
	private int originx = 0;
	private int originy = 0;
	private int zoom = 0;

	private Point lastMousePos = null;
	
	public DrawPanel() {
		DrawPanelListener listener = new DrawPanelListener(this);
		addComponentListener(listener);
		addMouseMotionListener(listener);
		addMouseWheelListener(listener);
		addMouseListener(listener);
		addKeyListener(listener);
		setFocusable(true);
		requestFocusInWindow();
		dt = new DrawerThread(this);
		applyTransform();
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
		dt.setDim(getWidth(), getHeight());
	}
	
	public void setLifeAlgo(LifeAlgo a){
		currentAlgo = a;
	}
	/*
	public void reDraw(){
		paintComponent(getGraphics());	
	}*/
	
	@Override
	public void paintComponent(Graphics g) {
		synchronized(dt.imageLock){
			if (currentAlgo != null && dt.getImage() != null) {
				g.drawImage(dt.getImage(), 0, 0, null);				
			}
		}
	}

	private class DrawPanelListener extends ComponentAdapter implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {
		final DrawPanel p;
		public DrawPanelListener(DrawPanel p) {
			this.p = p;
		}
		
		public void componentResized(ComponentEvent e) {
			p.onResize();
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			int i = e.getWheelRotation();
			if(i < 0){
				p.zoomIn(-i, p.lastMousePos.x, p.lastMousePos.y);
			}else{
				p.zoomOut(i, p.lastMousePos.x, p.lastMousePos.y);			
			}		
		}
		public void mouseClicked(MouseEvent e) {
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			p.lastMousePos = e.getPoint();
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
			Point pt = e.getPoint();
			p.originx += pt.x - p.lastMousePos.x;  
			p.originy += pt.y - p.lastMousePos.y;  
			p.lastMousePos = pt;
			p.applyTransform();
		}

		public void mouseMoved(MouseEvent e) {
			p.lastMousePos = e.getPoint();
		}

		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_I){
				p.zoomIn(1, p.getWidth()/2, p.getHeight()/2);
			} else if(e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_K){
				p.zoomOut(1, p.getWidth()/2, p.getHeight()/2);	
			}
		}

		public void keyReleased(KeyEvent arg0) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}

	private void zoomIn(int i, int x, int y) {
		//TODO prevent from zooming too far
		zoom += i;
		originx = ((originx - x) << i) + x;
		originy = ((originy - y) << i) + y;
		applyTransform();
	}

	private void zoomOut(int i, int x, int y) {
		zoom -= i;
		originx = ((originx - x) >> i) + x;
		originy = ((originy - y) >> i) + y;
		applyTransform();
	}
	
	}