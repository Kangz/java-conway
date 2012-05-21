package ui;

import javax.swing.*;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import life.LifeAlgo;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
	
	private LifeAlgo currentAlgo = null;
	private DrawerThread dt;
	
	private int originx = 0;
	private int originy = 0;
	private int zoom = -2;

	private Point lastMousePos = null;
	
	private class ResizeListener extends ComponentAdapter {
		final DrawPanel p;
		public ResizeListener(DrawPanel p) {
			this.p = p;
		}
		
		public void componentResized(ComponentEvent e) {
			p.onResize();
		}
	}
	
	public DrawPanel() {
		addComponentListener(new ResizeListener(this));
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addMouseListener(this);
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
			}else{
				System.out.println("Skipping a frame");
			}
		}
	}

	public void mouseWheelMoved(MouseWheelEvent arg0) {
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
		originx += pt.x - lastMousePos.x;  
		originy += pt.y - lastMousePos.y;  
		lastMousePos = pt;
		applyTransform();
	}

	public void mouseMoved(MouseEvent e) {
	}
}