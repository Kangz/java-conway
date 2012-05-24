package ui;


import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame {
	
	final public MenuBar menuBar;
	final public Panel panel;
	
	public Window() {
		
		menuBar = new MenuBar();
		setJMenuBar(menuBar);

		panel = new Panel();
		
		setSize(640, 480);
		setTitle("Main Window");
		getContentPane().add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public DrawPanel getDrawPanel(){
		return panel.getDrawPanel();
	}
}
