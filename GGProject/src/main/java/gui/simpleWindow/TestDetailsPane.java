package gui.simpleWindow;

import gui.WindowManager;
import gui.simpleWindow.JavaFXSimpleWindow;
import gui.simpleWindow.RunSimpleWindow;
import models.SimpleActor;
import transmission.Client;
import transmission.SessionData;

import java.util.ArrayList;

public class TestDetailsPane {
	public static void main(String args[]) {
		String imdb = "nm0000228";
		new Client();
		new RunSimpleWindow();
		ArrayList<SimpleActor> simpleActorsList = new ArrayList<>();
		SimpleActor kevinSpacey = new SimpleActor();
		kevinSpacey.setImdb_id(imdb);
		simpleActorsList.add(kevinSpacey);
		SessionData.getActorsData(simpleActorsList);
		WindowManager.showDetailsPane();
		JavaFXSimpleWindow.loadActorDetails(imdb);
	}
}
