package connection;

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


public class NewClient {
    private CloseableHttpClient client;

    public NewClient() {
        client = HttpClientBuilder.create().build();
    }

    public JSONArray postRequest(String url, String path) {
        JSONArray actor = new JSONArray();
        HttpPost post = new HttpPost(url);
        File file = new File(path);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fileBody = new FileBody(file);
        builder.addPart("image", fileBody);
        HttpEntity entity = builder.build();

        post.setEntity(entity);
        System.out.println("executing request " + post.getRequestLine());

        try {
            String responseBody = EntityUtils.toString(client.execute(post).getEntity(), "UTF-8");
            actor = new JSONArray(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(actor);
        return actor;
    }

    public JSONArray getRequest(String url) {
        JSONArray actor = new JSONArray();
        HttpGet get = new HttpGet(url);

        System.out.println("executing request " + get.getRequestLine());
        try {
            String responseBody = EntityUtils.toString(client.execute(get).getEntity(), "UTF-8");
            actor = new JSONArray(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return actor;
    }

    public void closeConnection() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}