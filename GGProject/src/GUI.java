import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GUI {
	GUI() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShow();
		GlobalKeyListener.startListener();
	}

	private void createAndShow() {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final JPopupMenu popup = new JPopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage("images/bulb.gif"));
		final SystemTray tray = SystemTray.getSystemTray();
		trayIcon.setImageAutoSize(false);

		// Create a popup menu components
		JMenu displayMenu = new JMenu("JMenu");
		JMenuItem item0 = new JMenuItem("About");
		JMenuItem item1 = new JMenuItem("Item1");
		JMenuItem item2 = new JMenuItem("Item2");
		JMenuItem item3 = new JMenuItem("Item3");
		JMenuItem item4 = new JMenuItem("Item4");
		JMenuItem exitItem = new JMenuItem("Exit");
		JCheckBoxMenuItem cb1 = new JCheckBoxMenuItem("Set sth1");

		//Add components to popup menu
		popup.add(item0);
		popup.addSeparator();
		popup.add(cb1);
		popup.addSeparator();
		popup.add(displayMenu);
		displayMenu.add(item1);
		displayMenu.add(item2);
		displayMenu.add(item3);
		displayMenu.add(item4);
		popup.add(exitItem);

		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}

			private void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.setLocation(e.getX(), e.getY());
					popup.setInvoker(popup);
					popup.setVisible(true);
				}
			}
		});

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		trayIcon.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"This dialog box is run from System Tray"));

		item0.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"This dialog box is run from the About menu item"));

		cb1.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED)
				trayIcon.setImageAutoSize(true);
			else
				trayIcon.setImageAutoSize(false);
		});

		ActionListener listener = e -> {
			JMenuItem item = (JMenuItem) e.getSource();
			//TrayIcon.MessageType type = null;
			System.out.println(item.getLabel());
			if ("Item1".equals(item.getLabel())) {
				//type = TrayIcon.MessageType.ERROR;
				trayIcon.displayMessage("Sun TrayIcon Demo",
						"This is an error message", TrayIcon.MessageType.ERROR);

			} else if ("Item2".equals(item.getLabel())) {
				//type = TrayIcon.MessageType.WARNING;
				trayIcon.displayMessage("Sun TrayIcon Demo",
						"This is a warning message", TrayIcon.MessageType.WARNING);

			} else if ("Item3".equals(item.getLabel())) {
				//type = TrayIcon.MessageType.INFO;
				trayIcon.displayMessage("Sun TrayIcon Demo",
						"This is an info message", TrayIcon.MessageType.INFO);

			} else if ("Item4".equals(item.getLabel())) {
				//type = TrayIcon.MessageType.NONE;
				trayIcon.displayMessage("Sun TrayIcon Demo",
						"This is an ordinary message", TrayIcon.MessageType.NONE);
			}
		};

		item1.addActionListener(listener);
		item2.addActionListener(listener);
		item3.addActionListener(listener);
		item4.addActionListener(listener);
		exitItem.addActionListener(e -> {
			tray.remove(trayIcon);
			System.exit(0);
		});
	}

	private Image createImage(String iconPath) {
		return (new ImageIcon(iconPath, "")).getImage();
	}
}