package gui;

import gui.simpleWindow.JavaFXSimpleWindow;
import models.SimpleActor;
import windowutils.WindowInfo;
import java.util.ArrayList;

public class WindowManager {
	private static boolean isSimpleWindowActive;
	private static boolean isDetailsWindowActive;

	public WindowManager() {
		isSimpleWindowActive = false;
		isDetailsWindowActive = false;
	}

	public static void createSimpleWindow(ArrayList<SimpleActor> actorsData) {
//		if(!isSimpleWindowActive) {
		isSimpleWindowActive = true;

		if (actorsData.isEmpty()) {
			System.out.println("No one recognized");
		} else {
			System.out.println("\nACTORS:");

			for (SimpleActor a : actorsData) {
				System.out.println(a.getName());
			}
			System.out.println();

			//TODO set real screen size!
			WindowInfo fullScreenInfo = new WindowInfo("", 0, 0, 1366, 768);
			JavaFXSimpleWindow.relocateWindow(fullScreenInfo);
			JavaFXSimpleWindow.loadActors(actorsData);
			JavaFXSimpleWindow.drawRectangles();
		}
//	}
	}
}
