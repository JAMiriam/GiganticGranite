package main;

import Connection.NewClient;
import Screens.GlobalKeyListener;
import gui.TrayGUI;
import json.JSONModelParser;
import models.Actor;
import models.SimpleActor;
import org.json.JSONArray;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
	public static NewClient client;
	public static void main(String args[]) {
//		Client client = new Client();
//		client.startConnection("localhost", 5000);

        /*testing new client*/
        NewClient client = new NewClient();
		Main.client = client;
        JSONModelParser parser= new JSONModelParser();
		JSONArray actor = client.postRequest("http://127.0.0.1:5000/actors/image", "images/photo.jpg");
		ArrayList<SimpleActor> simpleActors = parser.parseToSimpleActor(actor);
		System.out.println(simpleActors.get(0).getName());
        actor = client.getRequest("http://127.0.0.1:5000/actordetails/nm0000614");
        ArrayList<Actor> actors =  parser.parseToActor(actor);
        System.out.println(actors.get(0).getBiography());

		SwingUtilities.invokeLater(TrayGUI::new);
		GlobalKeyListener.startListener();
	}
}
