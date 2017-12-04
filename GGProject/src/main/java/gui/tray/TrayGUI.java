package gui.tray;

import gui.WindowManager;
import transmission.Client;
import transmission.LoginException;
import transmission.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Graphical user interface. Linux-friendly.
 * Tray icon with context menu.
 */
public class TrayGUI {
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
	 * Creating simple context menu attached to tray image
	 */
	private void createAndShow() {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final JPopupMenu popup = new JPopupMenu();
		Image image = createImage("images/icon.png");
		int trayIconWidth = new TrayIcon(image).getSize().width;
		final TrayIcon trayIcon = new TrayIcon(image.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH));
		final SystemTray tray = SystemTray.getSystemTray();
		trayIcon.setImageAutoSize(false);

		// Create a popup menu components
		JMenu displayMenu = new JMenu("Settings");
		JMenuItem clearItem = new JMenuItem("Clear");
		JMenuItem aboutItem = new JMenuItem("About");
		JMenuItem loginItem = new JMenuItem("Log in");
		JMenuItem exitItem = new JMenuItem("Exit");
		JCheckBoxMenuItem cb1 = new JCheckBoxMenuItem("Set sth1");

		//Add components to popup menu
		popup.add(clearItem);
		popup.addSeparator();
		popup.add(loginItem);
		popup.addSeparator();
		popup.add(displayMenu);
		displayMenu.add(cb1);
		displayMenu.add(aboutItem);
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

		trayIcon.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"This dialog box is run from System Tray"));

		aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"This dialog box is run from the About menu item"));

		cb1.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED)
				trayIcon.setImageAutoSize(true);
			else
				trayIcon.setImageAutoSize(false);
		});

		ActionListener listener = e -> {
			JMenuItem item = (JMenuItem) e.getSource();

			if ("Clear".equals(item.getLabel())) {
				WindowManager.clearSimpleWindow();
			}
			else if ("Log in".equals(item.getLabel())) {
				trayIcon.displayMessage("Sun TrayIcon Demo",
						"This is a \"log\" in message", TrayIcon.MessageType.WARNING);
			}
		};

		clearItem.addActionListener(listener);
		loginItem.addActionListener(listener);
		exitItem.addActionListener(e -> {
			tray.remove(trayIcon);
			System.exit(0);
		});


		showLoginWindow();
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
				try {
					Desktop.getDesktop().browse(new URI("http://giganticgranite.com/register"));
				} catch (URISyntaxException | IOException ex) {
					System.out.println(ex.getMessage());
				}
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
				int userid = User.login(username.getText(), password.getText());
				System.out.println("User: " + username.getText() + " (with id: " + userid + ") is logged in.");
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
	 * @param iconPath path to image
	 * @return Image object
	 */
	private Image createImage(String iconPath) {
		return (new ImageIcon(iconPath, "")).getImage();
	}
}