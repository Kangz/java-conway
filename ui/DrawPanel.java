package ui;

import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class DrawPanel extends JPanel {
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D) g;
		AffineTransform transformation = new AffineTransform();
		
		transformation.setToIdentity();
		transformation.scale(10, 3);
		
		g2D.setColor(Color.black);
		g2D.setTransform(transformation);
		
		g2D.drawLine(10, 10, 10, 10);
	}
}
