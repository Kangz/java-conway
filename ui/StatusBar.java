package ui;

import java.awt.Dimension;

import javax.swing.JLabel;

@SuppressWarnings("serial")
class StatusBar extends JLabel {
	
	private String info;
	private long numSteps;
	private int speed;
	
	public StatusBar(LifeController controller) {
		setPreferredSize(new Dimension(100, 16));
		
		info = "Normal mode";
		speed = 0;
		numSteps = 0;
		
		refresh();
		controller.setStatusBar(this);
	}
	
	public void refresh() {
		String msg = info + " / Speed "+speed+" / Step "+numSteps;
		setText(msg);
	}
	
	public void setInfo(String s) {
		info = s;
		refresh();
	}
	
	public void setSpeed(int s) {
		speed = s;
		refresh();
	}
	
	public void setNumSteps(long n) {
		numSteps = n;
		refresh();
	}
}
