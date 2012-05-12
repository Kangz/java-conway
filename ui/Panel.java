package ui;

import javax.swing.*;
import java.awt.BorderLayout;

public class Panel extends JPanel {
	
	final public JSlider slider;
	final public DrawPanel drawPanel;
	
	public Panel() {
		super(new BorderLayout());
		
		slider = new JSlider(0, 1000);
		slider.setMinorTickSpacing(50);
		slider.setMajorTickSpacing(100);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		drawPanel = new DrawPanel();
		
		add(slider, BorderLayout.PAGE_END);
		add(drawPanel, BorderLayout.CENTER);
		
	}
	
	public DrawPanel getDrawPanel(){
		return drawPanel;
	}
}
