package gui;

import gui.simpleWindow.JavaFXSimpleWindow;
import jdk.nashorn.internal.objects.Global;
import keylistener.GlobalKeyListener;
import keylistener.KeyTranslator;
import models.SimpleActor;
import org.jnativehook.keyboard.NativeKeyEvent;
import windowutils.WindowInfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class with static methods to communicate between javafx GUI and other components
 */
public class GUIManager {
	private static WindowInfo windowInfo;

	/**
	 * Creates javafx window from given actors data
	 * @param actorsData list of actors to display
	 */
	public static void createSimpleWindow(ArrayList<SimpleActor> actorsData) {
		if (actorsData.isEmpty()) {
			System.out.println("No one recognized");
			JOptionPane.showMessageDialog(null, "Sorry, but no one was recognized");
		} else {
			clearSimpleWindow();
			JavaFXSimpleWindow.relocateWindow(windowInfo);
			JavaFXSimpleWindow.loadActors(actorsData);
			JavaFXSimpleWindow.drawRectangles();
		}
	}

	/**
	 * Shows javafx details window
	 */
	public static void showDetailsPane() {
		JavaFXSimpleWindow.showDetailsPanel();
	}

	/**
	 * Sets local window info
	 * @param info new windowInfo
	 */
	public static void setScreenSize(WindowInfo info) {
		windowInfo = info;
		System.out.println("Window prop set: " + windowInfo.getY() + ", " + windowInfo.getY() +
				", " + windowInfo.getWidth() + ", " + windowInfo.getHeight());
	}

	/**
	 * Clear javafx window data
	 */
	public static void clearSimpleWindow() {
		JavaFXSimpleWindow.clearData();
	}

	/**
	 * Enables or disables key listener
	 * @param isEnabled new listener state
	 */
	public static void setProgramMode(boolean isEnabled) {
		GlobalKeyListener.setListenerState(isEnabled);
	}

	/**
	 * Loads current listener settings
	 * @return settings
	 */
	public static String[] loadSettings() {
		int[] keys = GlobalKeyListener.getCurrentConfig();
		return new String[] {KeyTranslator.getKeyName(keys[0]), KeyTranslator.getKeyName(keys[1])};
	}

	/**
	 * Sets new listener settings
	 * @param basic new basic key
	 * @param extra new extra key
	 */
	public static void saveConfig(String basic, String extra) {
		GlobalKeyListener.setConfig(KeyTranslator.getKeyNumber(basic), KeyTranslator.getKeyNumber(extra));
	}
}
