package ui;

import javax.swing.*;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class Panel extends JPanel {
	
	final public DrawPanel drawPanel;
	
	public Panel() {
		super(new BorderLayout());
		drawPanel = new DrawPanel();
		add(drawPanel, BorderLayout.CENTER);
	}
	
	public DrawPanel getDrawPanel(){
		return drawPanel;
	}
}
