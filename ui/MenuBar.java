package ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	
	public MenuBar() {
		JMenu menu = new JMenu("File");
		
		JMenuItem loadItem = new JMenuItem("Load a RLE file");
		loadItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		loadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		menu.add(loadItem);
		
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
		quitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(quitItem);
		add(menu);
	}

}
