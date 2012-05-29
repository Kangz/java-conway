package ui;

import java.awt.Dimension;

import javax.swing.JLabel;

@SuppressWarnings("serial")
/**
 * Located at the bottom of the screen, displays some information.
 */
class StatusBar extends JLabel {
	
	private String info;
	private long numSteps;
	private int speed;
	private int command;
	private String algo;
	
	/**
	 * @param controller the LifeController to link the StatusBar to.
	 */
	public StatusBar(LifeController controller) {
		setPreferredSize(new Dimension(100, 16));
		
		info = "Normal mode";
		speed = 1;
		numSteps = 1;
		command = 0;
		
		refresh();
		controller.setStatusBar(this);
	}
	
	/**
	 * Rebuild the text to be displayed in the bar.
	 */
	public void refresh() {
		String sSpeed = (speed >= 0)?Integer.toString(1<<speed):("1/"+(1<<-speed));
		String msg = info + " | Speed: "+sSpeed+" | Step: "+numSteps + " | " + algo + " | > " + command;
		setText(msg);
	}
	
	public void setAlgoName(String name){
		algo = name;
	}
	
	/**
	 * @param s the info to write in the first part of the text.
	 */
	public void setInfo(String s) {
		info = s;
		refresh();
	}
	
	/**
	 * @param s the speed to display.
	 */
	public void setSpeed(int s) {
		speed = s;
		refresh();
	}
	
	/**
	 * @param n the number of steps to display.
	 */
	public void setNumSteps(long n) {
		numSteps = n;
		refresh();
	}
	
	/**
	 * @param i the command to display.
	 */
	public void setCommand(int i) {
		command = i;
		refresh();
	}
}
