package main;

import connection.NewClient;
import screenshots.GlobalKeyListener;
import gui.TrayGUI;
import gui.mainWindow.GUI;
import json.JSONModelParser;
import models.Actor;
import models.SimpleActor;
import org.json.JSONArray;

import javax.swing.*;
import java.util.ArrayList;


public class Main extends Thread{
	private static NewClient client;

	public void run () {
		SwingUtilities.invokeLater(TrayGUI::new);
		GlobalKeyListener.startListener();

        /*testing new client*/
        NewClient client = new NewClient();
		Main.client = client;
        JSONModelParser parser= new JSONModelParser();
		JSONArray actor = client.postRequest("http://127.0.0.1:5000/actors/image", "screens/20171114_11_34_25_AM.png");
		ArrayList<SimpleActor> simpleActors = parser.parseToSimpleActor(actor);
		System.out.println(simpleActors.get(0).getName());

        actor = client.getRequest("http://127.0.0.1:5000/actordetails/nm0000614");
        ArrayList<Actor> actors =  parser.parseToActor(actor);
        System.out.println(actors.get(0).getBiography());

        //run gui updater after json was received
		GUI.setScreenshot("File:screens/20171114_11_34_25_AM.png");
		GUI.loadActor(actors.get(0));

	}
}
