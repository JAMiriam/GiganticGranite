package transmission;

import gui.WindowManager;
import json.JSONModelParser;
import models.Actor;
import models.Complaint;
import models.SimpleActor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Client {
        static final String SERVER_IP = "156.17.227.136";
//    static final String SERVER_IP = "192.168.0.101";
    static final int SERVER_HOST = 5000;
    private static CloseableHttpClient httpClient;

    public Client() {
        httpClient = HttpClientBuilder.create().build();
    }

    public static void sendImageToServer(String path) {
        JSONArray postResponse = postRequest("http://" + SERVER_IP + ":" + SERVER_HOST + "/actors/image", path);
        ArrayList<SimpleActor> simpleActorsList = JSONModelParser.parseToSimpleActor(postResponse);
        WindowManager.createSimpleWindow(simpleActorsList);
        SessionData.getActorsData(simpleActorsList);
    }
        
    public static void sendSuggestionToServer(Complaint complaint) {
        String ret = postComplaint("http://" + SERVER_IP + ":" + SERVER_HOST + "/actors/suggestion", complaint);
        System.out.println(ret);
    }

    private static String postComplaint(String url, Complaint complaint) {
        HttpPost post = new HttpPost(url);
        File file = new File(complaint.getLocalPath());

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fileBody = new FileBody(file);
        builder.addTextBody("complaint", complaint.toJSON(), ContentType.APPLICATION_JSON);
        builder.addPart("image", fileBody);
        HttpEntity entity = builder.build();
        post.setEntity(entity);

        System.out.println("Executing request: \"" + post.getRequestLine() + "\"");
        String resp = ":(";
        try {
            resp = EntityUtils.toString(httpClient.execute(post).getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    private static JSONArray postRequest(String url, String path) {
        JSONArray actor = new JSONArray();
        HttpPost post = new HttpPost(url);
        File file = new File(path);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fileBody = new FileBody(file);
        builder.addPart("image", fileBody);
        HttpEntity entity = builder.build();
        post.setEntity(entity);

        System.out.println("Executing request: \"" + post.getRequestLine() + "\"");

        try {
            String responseBody = EntityUtils.toString(httpClient.execute(post).getEntity(), "UTF-8");
            actor = new JSONArray(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actor;
    }

    private static JSONArray getRequest(String url) {
        JSONArray actor = new JSONArray();
        HttpGet get = new HttpGet(url);

        System.out.println("executing request " + get.getRequestLine());
        try {
            String responseBody = EntityUtils.toString(httpClient.execute(get).getEntity(), "UTF-8");
            actor = new JSONArray(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return actor;
    }

    static ArrayList<Actor> getDetails(String imdb_id) {
        JSONArray array = getRequest("http://" + SERVER_IP + ":" + SERVER_HOST + "/actordetails/" + imdb_id);
        ArrayList<Actor> actors =  JSONModelParser.parseToActor(array);
        return actors;
    }



    public static void closeConnection() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
