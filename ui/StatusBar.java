package ui;

import java.awt.Dimension;

import javax.swing.JLabel;

@SuppressWarnings("serial")
class StatusBar extends JLabel {
	
	public StatusBar(LifeController controller) {
		setPreferredSize(new Dimension(100, 16));
		refresh();
		controller.setStatusBar(this);
	}
	
	public void refresh() {
		String msg = "Hello world!";
		setText(msg);
	}
}
