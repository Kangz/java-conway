package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JComponent;

import life.EvolveManagerState;
import life.LifeDrawer;
import life.LifeState;
/**
 * This is a different Thread than the evolver and awt's thread in order to
 * stabilize the frame rate : the controller passes states to draw to the 
 * DrawerThread and they will be popped one after the other with a fixed interval.
 * 
 * There is a lot of concurrency management here with parameters to be set (transform)
 * and double buffering of the images to draw to. There are also several kinds of 
 * frame requested : normal ones, forces ones that flush the frame queue and are
 * drawn immediately and anim frames that do not change the queue but keep up
 * to date with the new transforms.
 */
public class DrawerThread implements Runnable {

	/**
	 * The class of the objects stored in the frame queue, they contain
	 * an evolver state in order to retrieve the currently drawn
	 * evolver state.
	 */
	class DrawState {
		public LifeDrawer drawer;
		public LifeState state;
		public EvolveManagerState evolverState;
		DrawState(LifeDrawer d, LifeState s, EvolveManagerState es){
			state = s;
			drawer = d;
			evolverState = es;
		}
	}

	private boolean running = true;
	private int drawInterval = 33; //The draw interval
	private JComponent component; //Needed to call repaint()
	
	//The transform and its lock
	private Object paramsLock = new Object();
	private int x = 0;
	private int y = 0;
	private int zoom = 0;
	
	//The dimensions of the canvas, we need to save them to detect changes
	//and create new images to draw to
	private int width = 1;
	private int height = 1;
	private Object dimLock = new Object();;
	private int storedWidth = 1;
	private int storedHeight = 1;
	
	//The images and the lock for the double buffer
	private BufferedImage imageA = null;
	private BufferedImage imageB = null;
	public Object imageLock = new Object();
	
	//Some parameters
	private boolean showGrid = true;
	private boolean forcedDraw = false;
	private boolean animFrame = false;
	private boolean paused = false;
	
	//The management of states to draw
	private Queue<DrawState> pendingStates = new LinkedList<DrawState>();
	private DrawState lastDraw;
	private Object lastDrawLock = new Object();
	
	/**
	 * @param container the component on which we will repain
	 */
	public DrawerThread(JComponent container) {
		this.component = container;
	}

	/**
	 * Stops the DrawerThread and notifies it so that it does
	 * not finish its wait()
	 */
	public void stop() {
		running = false;
		synchronized(this){
			notify();
		}
	}
	
	/**
	 * Adds a new frame to be drawn
	 * @param d the object to draw with
	 * @param s the state to draw
	 * @param forced wether this frame is to be drawn first
	 * @param es the current state of the evolver
	 */
	public void addDraw(LifeDrawer d, LifeState s, boolean forced, EvolveManagerState es){
		//If it is forced we flush the queue and notify the thread
		synchronized(pendingStates){
			Queue<DrawState> states;
			if(forced){
				states = new LinkedList<DrawState>();
			}else{
				states = pendingStates;
			}
			states.add(new DrawState(d, s, es));
			pendingStates = states;
		}
		if(forced){
			synchronized(this){
				notify();
			}
			forcedDraw = true;
		}
	}

	/**
	 * Removes all the pending draws
	 */
	public void flushDraws(){
		synchronized(pendingStates){
			pendingStates = new LinkedList<DrawState>();
		}
	}

	/**
	 * @return the number of pending draws
	 */
	public int getPendingDrawsLength(){
		int temp = 0;
		synchronized(pendingStates){
			temp = pendingStates.size();
		}
		return temp;
	}
	
	/**
	 * A callback for a panel resize
	 * @param w the new width of the panel
	 * @param h the new height of the panel
	 */
	public void setDimension(int w, int h) {
		synchronized(dimLock) {
			width = w;
			height = h;
		}
	}
	
	/**
	 * @param p do we pause it ?
	 */
	public void setPaused(boolean p){
		paused = p;
	}
	
	/**
	 * @param i the new draw interval
	 */
	public void setInterval(int i) {
		drawInterval = i;
	}
	
	/**
	 * @return the last drawn evolver state
	 */
	public EvolveManagerState getLastDrawnState(){
		synchronized(lastDrawLock){
			return (lastDraw == null)?null:lastDraw.evolverState;
		}
	}
	
	private void createImageBuffers(){
		imageA = new BufferedImage(storedWidth, storedHeight, BufferedImage.TYPE_INT_RGB);
		imageB = new BufferedImage(storedWidth, storedHeight, BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * Make sure the images are big (or small) enough
	 */
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

		long target_time = System.currentTimeMillis() + drawInterval;
		boolean isAnimFrame = false;
		while (running) {
			
			//TODO skip if it is too close from another interval and an anim frame
			
			//even if it is paused we may want to draw
			if(!paused || isAnimFrame || forcedDraw){
				//if it is not an anim frame we need to update next's frame time target
				if(! isAnimFrame) {
					target_time = System.currentTimeMillis() + drawInterval;
				}
				synchronizeDims();
	
				DrawState toDraw;
				synchronized(lastDrawLock){
					toDraw = lastDraw;
				}
				
				//update the frame to draw
				if(! isAnimFrame) {
					synchronized(pendingStates){
						toDraw = pendingStates.poll();
					}
				}
				//daw it!
				if (toDraw != null){
					draw(toDraw);
					swap();
					component.repaint();
					synchronized(lastDrawLock){
						lastDraw = toDraw;
					}
				}
			}
			isAnimFrame = false;
			forcedDraw = false;
			//wait for someone to ask something
			//or until we need to draw, again...
			try {
				synchronized(this){
					wait(Math.max(target_time - System.currentTimeMillis(), 1));
				}
			} catch (InterruptedException e) {
			}
			
			//A forced draw is more important than an anim frame
			if (animFrame && !forcedDraw) {
				isAnimFrame = true;
			}
			animFrame = false;
		}
	}
	
	/**
	 * Asks for an anim frame (we need feedback NOW!!!)
	 */
	public void requestAnimFrame() {
		animFrame = true;
		synchronized(this){
			notify();
		}
	}
	
	/**
	 * @param x the new x coordinate of the origin
	 * @param y the new y coordinate of the origin
	 * @param zoom the new zoom level
	 */
	public void setTransform(int x, int y, int zoom){
		synchronized(paramsLock){
			this.x = x;
			this.y = y;
			this.zoom = zoom;
		}
	}
	
	//What actually handle the draw logic
	private void draw(DrawState d) {
		int drawX, drawY, drawZoom;
		boolean drawShowGrid;
		
		//get the transform parameters
		synchronized(paramsLock){
			drawX = this.x;
			drawY = this.y;
			drawZoom = this.zoom;
			drawShowGrid = showGrid;
		}
		
		//clear the image
		drawBackground(imageA);
		
		//draw the partial grid before so that it does not show on top of cells
		if (drawShowGrid && zoom < 3) {
			drawPartialGrid(drawX, drawY, imageA);
		}

		d.drawer.draw(drawX, drawY, drawZoom, d.state, imageA);

		//draw the complete grid after so that it shows on top of cells
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
		//A single color grid
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
		//Make the grid with two colors because it is nicer
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
	
	/**
	 * @return the current image to be displayed
	 */
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
