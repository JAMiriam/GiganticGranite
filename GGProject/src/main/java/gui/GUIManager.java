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

public class GUIManager {
	private static WindowInfo windowInfo;

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

	public static void showDetailsPane() {
		JavaFXSimpleWindow.showDetailsPanel();
	}

	public static void setScreenSize(WindowInfo info) {
		windowInfo = info;
		System.out.println("Window prop set: " + windowInfo.getY() + ", " + windowInfo.getY() +
				", " + windowInfo.getWidth() + ", " + windowInfo.getHeight());
	}

	public static void clearSimpleWindow() {
		JavaFXSimpleWindow.clearData();
	}

	public static void setProgramMode(boolean isEnabled) {
		GlobalKeyListener.setListenerState(isEnabled);
	}

	public static String[] loadSettings() {
		int[] keys = GlobalKeyListener.getCurrentConfig();
		return new String[] {KeyTranslator.getKeyName(keys[0]), KeyTranslator.getKeyName(keys[1])};
	}

	public static void saveConfig(String basic, String extra) {
		GlobalKeyListener.setConfig(KeyTranslator.getKeyNumber(basic), KeyTranslator.getKeyNumber(extra));
	}
}
