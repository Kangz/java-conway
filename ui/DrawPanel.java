package ui;

import hashlife.MacroCell;

import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import life.LifeAlgo;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	
	private LifeAlgo currentAlgo = null;
	
	public void setLifeAlgo(LifeAlgo a){
		currentAlgo = a;
	}
	
	public void reDraw(){
		paintComponent(getGraphics());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		currentAlgo.getDrawer().draw(0, 0, getWidth(), getHeight(), 1, currentAlgo.getState(), g);
	}
}