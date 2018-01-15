package transmission;

import gui.GUIManager;
import json.JSONModelParser;
import models.Actor;
import models.Complaint;
import models.SimpleActor;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.*;
import org.apache.http.ssl.*;
import org.apache.http.util.*;
import org.json.JSONArray;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Vector;


public class Client {
        static final String SERVER_IP = "156.17.227.136";
//    static final String SERVER_IP = "192.168.0.101";
    static final int SERVER_HOST = 5000;
    private static CloseableHttpClient httpClient;
    public static Vector<String> screenshotPaths;

    public Client() {
    	httpClient = HttpClients.createDefault();

		try {
		SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(null, (certificate, authType) -> true).build();

		httpClient = HttpClients.custom()
				.setSSLContext(sslContext)
				.setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();

		} catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
			e.printStackTrace();
			//TODO no connection - countdown for retry
		}

        screenshotPaths = new Vector<>();
    }


    public static void sendImageToServer(String path) {
        SessionData.setScreenshotPath(path);
        screenshotPaths.add(path);
        JSONArray postResponse = postRequest("https://" + SERVER_IP + ":" + SERVER_HOST + "/actors/image", path);
        ArrayList<SimpleActor> simpleActorsList = JSONModelParser.parseToSimpleActor(postResponse);
        GUIManager.createSimpleWindow(simpleActorsList);
        SessionData.getActorsData(simpleActorsList);
    }
        
    public static void sendSuggestionToServer(Complaint complaint) {
        String ret = postComplaint("https://" + SERVER_IP + ":" + SERVER_HOST + "/actors/suggestion", complaint);
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
        String resp = "";
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
            System.out.println(responseBody);
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
        JSONArray array = getRequest("https://" + SERVER_IP + ":" + SERVER_HOST + "/actordetails/" + imdb_id);
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
