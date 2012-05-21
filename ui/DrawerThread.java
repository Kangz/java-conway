package ui;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

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
	
	private Object transformLock = new Object();
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
	
	private Queue<DrawState> pendingStates = new LinkedList<DrawState>();
	
	public void stop() {
		running = false;
		notify();
	}
	
	public void addOp(LifeDrawer d, LifeState s){
		synchronized(pendingStates){
			pendingStates.add(new DrawState(d, s));
//			throw new RuntimeException("AddOP");
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
	
	public void setInterval(int i) {
		interval = i;
	}
	
	private void createImageBuffers(){
		System.out.println("Creating new Buffers");
		imageA = new BufferedImage(storedWidth, storedHeight, BufferedImage.TYPE_INT_RGB);
		imageB = new BufferedImage(storedWidth, storedHeight, BufferedImage.TYPE_INT_RGB);
	}
	
	public void run() {
		createImageBuffers();
		
		while (running) {
			long timea = System.currentTimeMillis();

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

			DrawState d = null;
			synchronized(pendingStates){
				d = pendingStates.poll();
			}
			
			if (d != null){
				draw(d);
				trySwap();
			}
			
			long timeb = System.currentTimeMillis();
			
			try {
				synchronized(this){
					wait(Math.max(interval - timeb + timea, 1));
				}
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void setTransform(int x, int y, int zoom){
		synchronized(transformLock){
			this.x = x;
			this.y = y;
			this.zoom = zoom;
		}
	}
	
	private void draw(DrawState d) {
		synchronized(transformLock){
			d.drawer.draw(x, y, zoom, d.state, imageA);
		}
	}
	
	public BufferedImage getImage(){
		return imageB;
	}
	
	private void trySwap() {
		synchronized(imageLock){
			BufferedImage temp = imageB;
			imageB = imageA;
			imageA = temp;
		}
	}
}
