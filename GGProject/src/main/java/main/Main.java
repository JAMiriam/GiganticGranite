package main;

import gui.simpleWindow.RunSimpleWindow;
import gui.tray.TrayGUI;
import keylistener.GlobalKeyListener;
import transmission.Client;

/**
 * Main class
 */
public class Main {
	public static void main(String args[]) {
		new RunSimpleWindow();
		new GlobalKeyListener().start();
		new TrayGUI();
		new Client();
		TrayGUI.showTooltipBalloon("Ready to work :)");
	}
}
