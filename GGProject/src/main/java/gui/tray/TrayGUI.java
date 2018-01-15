package gui.tray;

import cleaner.Cleaner;
import gui.GUIManager;
import transmission.Client;
import transmission.LoginException;
import transmission.User;

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

		helpItem.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"This dialog box is run from the About menu item"));

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
			User.openHistoryInBrowser();
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
				String userid = User.login(username.getText(), password.getText());
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