package gui.simpleWindow;

import gui.GUIManager;
import models.SimpleActor;
import transmission.Client;
import transmission.SessionData;

import java.util.ArrayList;

/**
 * Test class for details panel with dummy data
 */
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
		GUIManager.showDetailsPane();
		JavaFXSimpleWindow.setActorDetails(imdb);
	}
}
