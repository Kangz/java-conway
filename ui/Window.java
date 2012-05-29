package ui;


import java.awt.BorderLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
/**
 * The main frame of the GUI.
 */
public class Window extends JFrame {
	
	/**
	 * The MenuBar, useful to map some menu shortcuts and some actions.
	 */
	final public MenuBar menuBar;
	
	/**
	 * The main Panel, containing the drawing zone.
	 */
	final public Panel panel;
	
	/**
	 * The StatusBar, useful tu display some information.
	 */
	final public StatusBar statusBar;
	
	/**
	 * Builds a simple Window.
	 * 
	 * @param controller The LifeController, passed to all the components
	 */
	public Window(LifeController controller) {
		
		menuBar = new MenuBar(controller);
		setJMenuBar(menuBar);

		panel = new Panel(controller);
		statusBar = new StatusBar(controller);
		
		setSize(640, 480);
		setTitle("Main Window");
		getContentPane().add(panel);
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
