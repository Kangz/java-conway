package ui;

import javax.swing.*;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class Panel extends JPanel {
	
	final public DrawPanel drawPanel;
	
	public Panel(LifeController controller) {
		super(new BorderLayout());
		drawPanel = new DrawPanel(controller);
		add(drawPanel, BorderLayout.CENTER);
	}
	
	public DrawPanel getDrawPanel(){
		return drawPanel;
	}
}
