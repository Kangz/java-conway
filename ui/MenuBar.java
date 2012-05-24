package ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	
	public MenuBar() {
		final JMenu menu = new JMenu("File");
		
		final JMenuItem loadItem = new JMenuItem("Load a RLE file");
		
		final JFileChooser fc = new JFileChooser();
		loadItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		loadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int choice = fc.showOpenDialog(getParent());
				if(choice == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					System.out.println("Opening: " + file.getName());
				} else {
					System.out.println("Cancelled opening");
				}
			}
		});
		menu.add(loadItem);
		
		final JMenuItem quitItem = new JMenuItem("Quit");
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
