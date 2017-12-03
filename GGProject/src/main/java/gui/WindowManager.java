package gui;

import gui.simpleWindow.JavaFXSimpleWindow;
import models.SimpleActor;
import windowutils.WindowInfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WindowManager {
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
}
