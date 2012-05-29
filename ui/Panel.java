package ui;

import javax.swing.*;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
/**
 * The main panel, containing only a DrawPanel in which
 * the grid will be drawn.
 */
public class Panel extends JPanel {
	
	/**
	 * The drawPanel in which the grid is drawn.
	 */
	final public DrawPanel drawPanel;
	
	/**
	 * Construct a new Panel.
	 * 
	 * @param controller the LifeController to link to the drawPanel.
	 */
	public Panel(LifeController controller) {
		super(new BorderLayout());
		drawPanel = new DrawPanel(controller);
		add(drawPanel, BorderLayout.CENTER);
	}
}
