package gui.simpleWindow;

import javafx.application.Application;

public class RunSimpleWindow extends Thread {
	public RunSimpleWindow() {
		start();
	}

	public void run() {
		Application.launch(JavaFXSimpleWindow.class, "");
	}
}
