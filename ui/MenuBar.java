package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
/**
 * A very basic menu bar, to have some shortcuts.
 */
public class MenuBar extends JMenuBar {
	
	/**
	 * Construct the MenuBar.
	 * 
	 * @param controller the controller to link the MenuBar to
	 */
	public MenuBar(final LifeController controller) {
		final JMenu menu = new JMenu("File");
		
		
		final JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("RLE files", "rle"));
		fc.setAcceptAllFileFilterUsed(false);
		
		final JMenuItem loadItem = new JMenuItem("Load from a RLE file");
		
		loadItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		loadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int choice = fc.showOpenDialog(getParent());
				if(choice == JFileChooser.APPROVE_OPTION) {
					controller.loadFromFile(fc.getSelectedFile());
				} else {
					
				}
			}
		});
		menu.add(loadItem);
		
		final JMenuItem saveItem = new JMenuItem("Save to a RLE file");
		
		saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int choice = fc.showSaveDialog(getParent());
				if(choice == JFileChooser.APPROVE_OPTION) {
					controller.saveToFile(fc.getSelectedFile());
				} else {
					
				}
			}
		});
		menu.add(saveItem);
		
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
