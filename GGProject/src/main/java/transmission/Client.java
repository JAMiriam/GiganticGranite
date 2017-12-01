package transmission;

import gui.WindowManager;
import json.JSONModelParser;
import models.SimpleActor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
    //	private static final String SERVER_IP = "25.29.196.52";
    private static final String SERVER_IP = "192.168.0.101";
    private static final int SERVER_HOST = 5000;
    private static CloseableHttpClient httpClient;

    public Client(String serverIp, int serverHost) {
        httpClient = HttpClientBuilder.create().build();

        //Throughout proxy
//        HttpHost proxy = new HttpHost(serverIp, serverHost);
//        HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
//        httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();
    }

    public static void sendImageToServer(String path) {
        String screenpath = path;
        JSONArray postResponse = postRequest("http://" + SERVER_IP + ":" + SERVER_HOST + "/actors/image", screenpath);
        ArrayList<SimpleActor> simpleActorsList = JSONModelParser.parseToSimpleActor(postResponse);
        WindowManager.createSimpleWindow(simpleActorsList);
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

    public static JSONArray getRequest(String url) {
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

    public static void closeConnection() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}