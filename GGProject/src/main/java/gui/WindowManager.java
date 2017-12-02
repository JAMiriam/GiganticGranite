package gui;

import gui.simpleWindow.JavaFXSimpleWindow;
import models.SimpleActor;
import windowutils.WindowInfo;

import java.awt.*;
import java.util.ArrayList;

public class WindowManager {
	private static boolean isSimpleWindowActive;
	private static boolean isDetailsWindowActive;
	private static Dimension screenSize;
	private static WindowInfo windowInfo;

	public WindowManager() {
		isSimpleWindowActive = false;
		isDetailsWindowActive = false;
	}

	public static void createSimpleWindow(ArrayList<SimpleActor> actorsData) {
		isSimpleWindowActive = true;

		if (actorsData.isEmpty()) {
			//TODO show JPaneDialog with info
			System.out.println("No one recognized");
		} else {
			clearSimpleWindow();
			JavaFXSimpleWindow.relocateWindow(windowInfo);
			JavaFXSimpleWindow.loadActors(actorsData);
			JavaFXSimpleWindow.drawRectangles();
		}
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
