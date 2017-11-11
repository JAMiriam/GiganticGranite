import Screens.GlobalKeyListener;
import gui.TrayGUI;

import javax.swing.*;

public class Main {
	public static void main(String args[]) {
//		Client client = new Client();
//		client.startConnection("localhost", 5000);
		SwingUtilities.invokeLater(TrayGUI::new);
		GlobalKeyListener.startListener();
	}
}
