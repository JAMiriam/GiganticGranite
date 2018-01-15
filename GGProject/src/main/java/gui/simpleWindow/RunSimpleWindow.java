package gui.simpleWindow;

import javafx.application.Application;

/**
 * Thread to run JavaFX window from the outside thread
 */
public class RunSimpleWindow extends Thread {

	/**
	 *
	 */
	public RunSimpleWindow() {
		start();
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		Application.launch(JavaFXSimpleWindow.class, "");
	}
}
