package gui.tray;

import cleaner.Cleaner;
import gui.GUIManager;
import transmission.Client;
import transmission.LoginException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.NoRouteToHostException;


/**
 * Tray icon with context menu. Linux-friendly.
 */
public class TrayGUI {
	private static TrayIcon trayIcon;

	public TrayGUI() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShow();
	}

	/**
	 * Creates simple context menu attached to tray image
	 */
	private void createAndShow() {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final JPopupMenu popup = new JPopupMenu();
		Image image = new ImageIcon("images/icon16x16.png", "").getImage();
		int trayIconWidth = new TrayIcon(image).getSize().width;
		trayIcon = new TrayIcon(image.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH));
		final SystemTray tray = SystemTray.getSystemTray();
		trayIcon.setImageAutoSize(false);

		// Create a popup menu components
		JMenu optionMenu = new JMenu("More");
		JMenuItem clearItem = new JMenuItem("Clear");
		JMenuItem helpItem = new JMenuItem("Help");
		JMenuItem settingsItem = new JMenuItem("Settings");
		JMenuItem historyItem = new JMenuItem("History");
		JMenuItem aboutItem = new JMenuItem("About");
		JMenuItem loginItem = new JMenuItem("Log in");
		JMenuItem exitItem = new JMenuItem("Exit");
		JCheckBoxMenuItem enableBox = new JCheckBoxMenuItem("Enabled");
		enableBox.setSelected(true);

		//Add components to popup menu
		popup.add(loginItem);
		popup.add(historyItem);
		popup.add(clearItem);
		popup.add(optionMenu);
		optionMenu.add(enableBox);
		optionMenu.add(settingsItem);
		optionMenu.add(helpItem);
		optionMenu.add(aboutItem);
		popup.addSeparator();
		popup.add(exitItem);

		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				showPopup(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				showPopup(e);
			}

			private void showPopup(MouseEvent e) {
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

		helpItem.addActionListener(e -> {
			String helpMsg =
					"Program takes screenshot and sends it to the server, which recognises actors’ faces and\n" +
					"sends back the response. \n\n" +
					"Default shortcut to take a screenshot is prtsc key for capturing the whole screen and alt+prtsc\n" +
					"for capturing only active window. Shortcuts can be changed in More->Settings. Subsequently, frames\n" +
					"around recognised faces will be shown. Green colour  means that certain recognition is most likely\n" +
					"correct, and recognitions that may be incorrect are marked red. Clicking on a rectangle shows a\n" +
					"details pane with basic info about the actor and link to their IMDb profile. \n\n" +
					"Users can also make complaints when they think a recognition is incorrect. To suggest the real name " +
					"of wrongly recognised actor, click on “Complaint” and type actor’s full name. \n\n" +
					"To remove frames and details pane, press esc key or choose “Clear” from the menu.\n" +
					"Logged-in users have access to their search history. To log in, choose “Log in” option from \n" +
					"the menu. To create an account, choose “Log in” and then click on the link to the registration page.\n" +
					"To display your history, click on “History”.  You will be redirected to the web page. " +
					"After closing the app, you will be logged out.";

			JOptionPane.showMessageDialog(null, helpMsg, "Help", 1);
		});

		aboutItem.addActionListener(e -> {
			String aboutMsg =
					"Gigantic Granite is a software to recognise actors’ faces from films’ screenshots. \n" +
					"Copyright © 2018  Miriam Jańczak, Agata Jasionowska, Małgorzata Kaczmarczyk, Michał Krasoń,\n" +
					"Wojciech Przytarski, Paweł Rakosza\n\n" +
					"Source code available on: https://github.com/JAMiriam/GiganticGranite/\n\n" +
					"This program is free software: you can redistribute it and/or modify it under the terms of\n" +
					"the GNU General Public License as published by the Free Software Foundation, either version 3\n" +
					"of the License, or  (at your option) any later version.\n" +
					"This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; \n" +
					"without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  " +
					"See the GNU General Public License (http://www.gnu.org/licenses/)  for more details.\n";
			JOptionPane.showMessageDialog(null, aboutMsg, "About", 1);
		});

		enableBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				GUIManager.setProgramMode(true);
				showTooltipBalloon("GGApp enabled");
			}
			else {
				GUIManager.setProgramMode(false);
				showTooltipBalloon("GGApp disabled");
			}
		});

		settingsItem.addActionListener(e -> showSettingsWindow());

		loginItem.addActionListener(e -> showLoginWindow());

		clearItem.addActionListener(e -> GUIManager.clearSimpleWindow());

		historyItem.addActionListener(e -> {
			Client.openHistoryInBrowser();
		});

		exitItem.addActionListener(e -> {
			tray.remove(trayIcon);
			new Cleaner();
		});
	}

	/**
	 * Displays dialog asking for login credentials.
	 */
	private void showLoginWindow() {
		JTextField username = new JTextField();
		JTextField password = new JPasswordField();
				
		// Setting up register message with clickable hyperlink.
		JLabel register = new JLabel("<html>Don't have account yet? Please " +
				"<a href=giganticgranite.com/register>register</a> on our web page.</html>");
		register.setCursor(new Cursor(Cursor.HAND_CURSOR));
		register.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Client.openRegistration();
			}
		});

		Object[] message = {
				"Username:", username,
				"Password:", password,
				register
		};

		int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			try {
				String userid = Client.login(username.getText(), password.getText());
				System.out.println("userid: " + userid);
				showTooltipBalloon(username.getText() + " logged in");
			} catch (NoRouteToHostException e) {
				JOptionPane.showMessageDialog(null, "Could not connect to sever.\nPlease try again later.",
						"Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (LoginException le) {
				JOptionPane.showMessageDialog(null, "Login failed. " + le.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
				showLoginWindow();
			}
		}
	}

	/**
	 * Displays dialog with settings
	 */
	private void showSettingsWindow() {
		String[] basicSelect = {"PrtSc", "Insert", "Delete", "PgUp", "PgDown"};
		String[] extraSelect = { "ALT", "CTRL", "SHIFT"};
		JComboBox<String> basic = new JComboBox<>(basicSelect);
		JComboBox<String> extra = new JComboBox<>(extraSelect);

		String[] loaded = GUIManager.loadSettings();
		basic.setSelectedItem(loaded[0]);
		extra.setSelectedItem(loaded[1]);

		Object[] message = {"Basic key:", basic, "Extra key:", extra };

		int option = JOptionPane.showConfirmDialog(null, message, "Settings", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			String selectedBasic = (String)basic.getSelectedItem();
			String selectedExtra = (String)extra.getSelectedItem();
			assert selectedBasic != null;
			assert selectedExtra != null;
			GUIManager.saveConfig(selectedBasic, selectedExtra);
		}
	}

	/**
	 * Shows balloon tooltip with message
	 * @param message string to display
	 */
	public static void showTooltipBalloon(String message) {
		trayIcon.displayMessage("GGApp info", message, TrayIcon.MessageType.INFO);
	}
}