package ui;


import java.awt.BorderLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame {
	
	final public MenuBar menuBar;
	final public Panel panel;
	final public StatusBar statusBar;
	
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
	
	public DrawPanel getDrawPanel(){
		return panel.getDrawPanel();
	}
}
