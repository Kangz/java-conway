package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JComponent;

import life.LifeDrawer;
import life.LifeState;

public class DrawerThread implements Runnable {

	class DrawState {
		public LifeDrawer drawer;
		public LifeState state;
		DrawState(LifeDrawer d, LifeState s){
			state = s;
			drawer = d;
		}
	}

	private boolean running = true;
	private int interval = 33;
	private JComponent component;
	
	private Object paramsLock = new Object();
	private int x = 0;
	private int y = 0;
	private int zoom = 0;
	
	private int width = 1;
	private int height = 1;
	private Object dimLock = new Object();;
	private int storedWidth = 1;
	private int storedHeight = 1;
	
	private BufferedImage imageA = null;
	private BufferedImage imageB = null;
	public Object imageLock = new Object();
	
	private boolean showGrid = true;
	
	private boolean forcedDraw = false;
	private boolean animFrame = false;
	private boolean paused = false;
	
	private Queue<DrawState> pendingStates = new LinkedList<DrawState>();
	
	public DrawerThread(JComponent container) {
		this.component = container;
	}
	
	public void stop() {
		running = false;
		synchronized(this){
			notify();
		}
	}
	
	public void addOp(LifeDrawer d, LifeState s, boolean forced){
		synchronized(pendingStates){
			Queue<DrawState> states;
			if(forced){
				states = new LinkedList<DrawState>();
			}else{
				states = pendingStates;
			}
			states.add(new DrawState(d, s));
		}
		if(forced){
			synchronized(this){
				notify();
			}
			forcedDraw = true;			
		}
	}

	public void flushOps(){
		synchronized(pendingStates){
			pendingStates = new LinkedList<DrawState>();
		}
	}

	public int opLength(){
		int temp = 0;
		synchronized(pendingStates){
			temp = pendingStates.size();
		}
		return temp;
	}
	
	public void setDim(int w, int h) {
		synchronized(dimLock) {
			width = w;
			height = h;
		}
	}
	
	public void setPaused(boolean p){
		paused = p;
	}
	
	public void setInterval(int i) {
		interval = i;
	}
	
	private void createImageBuffers(){
		imageA = new BufferedImage(storedWidth, storedHeight, BufferedImage.TYPE_INT_RGB);
		imageB = new BufferedImage(storedWidth, storedHeight, BufferedImage.TYPE_INT_RGB);
	}

	private void synchronizeDims(){
		int w, h;
		synchronized(dimLock) {
			w = width;
			h = height;
		}
		
		if (w != storedWidth || h != storedHeight) {
			storedWidth = w;
			storedHeight = h;
			createImageBuffers();			
		}
	}
	
	public void run() {
		createImageBuffers();

		long target_time = System.currentTimeMillis() + interval;
		DrawState lastd = null;
		boolean isAnimFrame = false;
		while (running) {
			
			//TODO skip if it is too close from another interval and an anim frame
			
			if(!paused || isAnimFrame){
				if(! isAnimFrame) {
					target_time = System.currentTimeMillis() + interval;
				}
	
				synchronizeDims();
	
				DrawState d = null;
				if(! isAnimFrame) {
					synchronized(pendingStates){
						d = pendingStates.poll();
					}
				}
				
				DrawState toDraw = isAnimFrame ? lastd : d;
				
				if (toDraw != null){
					draw(toDraw);				
					swap();
					component.repaint();
					lastd = toDraw;
				}
			}
			isAnimFrame = false;
			try {
				synchronized(this){
					wait(Math.max(target_time - System.currentTimeMillis(), 1));
				}
			} catch (InterruptedException e) {
			}
			if (animFrame && !forcedDraw) {
				isAnimFrame = true;
			}
			animFrame = false;
			forcedDraw = false;
		}
	}
	
	public void requestAnimFrame() {
		animFrame = true;
		synchronized(this){
			notify();
		}
	}
	
	public void setTransform(int x, int y, int zoom){
		synchronized(paramsLock){
			this.x = x;
			this.y = y;
			this.zoom = zoom;
		}
	}
	
	private void draw(DrawState d) {
		int drawX, drawY, drawZoom;
		boolean drawShowGrid;
		synchronized(paramsLock){
			drawX = this.x;
			drawY = this.y;
			drawZoom = this.zoom;
			drawShowGrid = showGrid;
		}
		
		drawBackground(imageA);
		
		if (drawShowGrid && zoom < 3) {
			drawPartialGrid(drawX, drawY, imageA);
		}

		d.drawer.draw(drawX, drawY, drawZoom, d.state, imageA);

		if (drawShowGrid && zoom >= 3) {
			drawCellGrid(drawX, drawY, drawZoom, imageA);
		}
	}
	
	private void drawBackground(BufferedImage img) {
		Graphics g = img.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		
	}
	
	final static Color DARKER_GRAY = new Color(20, 20, 20);
	final static Color DARK_GRAY = new Color(30, 30, 30);
	
	private void drawCellGrid(int drawX, int drawY, int drawZoom, BufferedImage img) {
		int size = 1 << zoom;
		Graphics g = img.getGraphics();
		g.setColor(DARK_GRAY);
		int startX = drawX >= 0 ? drawX % size : (drawX % size) + size;
		int startY = drawY >= 0 ? drawY % size : (drawY % size) + size;
		
		for(int x = startX; x < img.getWidth(); x += size){
			g.drawLine(x, 0, x, img.getHeight());
		}

		for(int y = startY; y < img.getHeight(); y += size){
			g.drawLine(0, y, img.getWidth(), y);
		}
	}

	private void drawPartialGrid(int drawX, int drawY, BufferedImage img) {
		final int size = 16;
		int startX = drawX >= 0 ? drawX % size : (drawX % size) + size;
		int startY = drawY >= 0 ? drawY % size : (drawY % size) + size;
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

		g.setColor(DARKER_GRAY);
		for(int x = startX - size/2; x < img.getWidth() + size/2; x += size){
			g.drawLine(x, 0, x, img.getHeight());
		}
		for(int y = startY - size/2; y < img.getHeight() + size/2; y += size){
			g.drawLine(0, y, img.getWidth(), y);
		}
		
		g.setColor(DARK_GRAY);
		for(int x = startX; x < img.getWidth(); x += size){
			g.drawLine(x, 0, x, img.getHeight());
		}
		for(int y = startY; y < img.getHeight(); y += size){
			g.drawLine(0, y, img.getWidth(), y);
		}
		
	}
	
	public BufferedImage getImage(){
		return imageB;
	}
	
	private void swap() {
		synchronized(imageLock){
			BufferedImage temp = imageB;
			imageB = imageA;
			imageA = temp;
		}
	}
}
