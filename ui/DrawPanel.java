package ui;

import hashlife.MacroCell;

import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	
	private MacroCell currentCell = null;
	
	public void setCell(MacroCell c){
		currentCell = c;
	}
	
	public void reDraw(){
		paintComponent(getGraphics());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(currentCell == null){
			System.out.println("DrawPanel: asked a redraw without a cell to draw");
			return;
		}
		
		recDraw(g, 0, 0, currentCell);
	}
	
	private void recDraw(Graphics g, int x, int y, MacroCell cell){
		final int zoom = 16;

		int size = cell.getSize();
		if(x + size < 0 || x > getWidth() || y + size < 0 || y > getHeight()){
			return;
		}
		
		if(cell.getDim() == 0){
			System.out.println("Draw x: " + x + ", y: " + y);
			int density = cell.getDensity();
			g.setColor(new Color(density, density, density));
			g.fillRect(x, y, zoom, zoom);
			return;
		}
		
		int offset = zoom*size/2;
		//TODO: do not call part
		recDraw(g, x         , y         , cell.part(1, 0, 0));
		recDraw(g, x + offset, y         , cell.part(1, 1, 0));
		recDraw(g, x         , y + offset, cell.part(1, 0, 1));
		recDraw(g, x + offset, y + offset, cell.part(1, 1, 1));
	}
}