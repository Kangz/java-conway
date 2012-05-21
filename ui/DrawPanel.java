package ui;

import javax.swing.*;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import life.LifeAlgo;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	
	private LifeAlgo currentAlgo = null;
	private BufferedImage b = null;
	private DrawerThread dt;
	
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
		this.addComponentListener(new ResizeListener(this));
		dt = new DrawerThread();
		Thread t = new Thread(dt);
		dt.setTransform(0, 0, -1);
		t.start();
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
}