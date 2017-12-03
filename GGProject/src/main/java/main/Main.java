package main;

import gui.WindowManager;
import gui.simpleWindow.RunSimpleWindow;
import transmission.Client;
import gui.tray.TrayGUI;
import keylistener.GlobalKeyListener;
import transmission.SessionData;


public class Main {
	public static void main(String args[]) {
		//TODO show logo or sth
		new RunSimpleWindow();
		new TrayGUI();
		new GlobalKeyListener().start();
		new Client();
	}

	private static void testRequests() {
//		JSONArray actor = Client.postRequest("http://" + SERVER_IP + ":" + SERVER_HOST + "/actors/image", "screens/photo.jpg");

		// send photo, get simple actor
//		ArrayList<SimpleActor> simpleActors = JSONModelParser.parseToSimpleActor(actor);

		// send request, get details
//		for(SimpleActor a: simpleActors) {
//			actor = Client.getRequest("http://" + SERVER_IP + ":" + SERVER_HOST + "/actordetails/" + a.getImdb_id());
//			ArrayList<Actor> actors =  JSONModelParser.parseToActor(actor);
//			System.out.println(actors.get(0).getName() + " details received");
//		}
	}
}
