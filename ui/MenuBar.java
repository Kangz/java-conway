package ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuBar extends JMenuBar {
	
	public MenuBar() {
		JMenu menu;
		JMenuItem menuItem;
		
		menu = new JMenu("File");
		add(menu);
		
		menuItem = new JMenuItem("Quit");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(menuItem);
	}

}
