package main;

import gui.TrayGUI;
import screenshots.GlobalKeyListener;

import javax.swing.*;

public class SmallWinMain {
	public static void main(String args[]) {
		SwingUtilities.invokeLater(TrayGUI::new);
		GlobalKeyListener.startListener();
	}
}
