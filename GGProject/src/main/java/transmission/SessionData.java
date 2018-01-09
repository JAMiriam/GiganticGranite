package transmission;

import models.Actor;
import models.SimpleActor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SessionData {
	private static Map<String, Actor> mapDetails;
	private static String screenshotPath;

	public static void setScreenshotPath(String path) {
		screenshotPath = path;
	}

	public static String getScreenshotPath() {
		return screenshotPath;
	}

	public static void getActorsData(ArrayList<SimpleActor> recognized) {
		mapDetails = new HashMap<>();
		ArrayList<Actor> clientActors;
		for(SimpleActor a : recognized) {
			clientActors = Client.getDetails(a.getImdb_id());
			mapDetails.put(a.getImdb_id(), clientActors.get(0));
			System.out.println(clientActors.get(0).getName() + " details received");
		}
	}

	public static void clearActorsData() {
		mapDetails.clear();
	}

	public static Actor getActorDetails(String imdb_id) throws Exception {
		if(mapDetails.containsKey(imdb_id))
			return mapDetails.get(imdb_id);
		else {
			throw new Exception("No actor details in session");
		}
	}
}
