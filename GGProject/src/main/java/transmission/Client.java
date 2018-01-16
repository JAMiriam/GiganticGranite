package transmission;

import gui.GUIManager;
import json.JSONModelParser;
import models.Actor;
import models.Complaint;
import models.SimpleActor;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.*;
import org.apache.http.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Client class to communicate with server
 */
public class Client {
	private static final String SERVER_IP = "156.17.227.136";
    private static final int SERVER_HOST = 5000;
    private static CloseableHttpClient httpClient;
	private static String user = "";
	private static String userToken = "";
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
		}

        screenshotPaths = new Vector<>();
    }

    public static void sendImageToServer(String path) {
        SessionData.setScreenshotPath(path);
        screenshotPaths.add(path);
		JSONArray postResponse;
        if(!user.isEmpty()) {
			postResponse = postRequest("https://" + SERVER_IP + ":" + SERVER_HOST + "/actors/image?token=" + userToken, path);
		}
		else {
			postResponse = postRequest("https://" + SERVER_IP + ":" + SERVER_HOST + "/actors/image", path);
		}
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
            actor = new JSONArray(responseBody);
        } catch (HttpHostConnectException e) {
			JOptionPane.showMessageDialog(null, "Could not connect to sever.\nPlease try again later.",
					"Error", JOptionPane.ERROR_MESSAGE);
        }
        catch(IOException ex) {
			ex.printStackTrace();
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

	public static void openRegistration() {
		try {
			String url = "http://" + SERVER_IP + "/register.php";
			if (Runtime.getRuntime().exec(new String[] {"which", "xdg-open"}).getInputStream().read() != -1) {
				Runtime.getRuntime().exec(new String[] {"xdg-open", url});
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Logs in a user to the server using given credentials.
	 * @param username String containing username.
	 * @param password String containing password.
	 * @return user's id from server's database.
	 * @throws IOException when connection fails.
	 * @throws LoginException when given username or password were incorrect.
	 */
	public static String login(String username, String password) throws IOException, LoginException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		user = username;

		try {
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(null, (certificate, authType) -> true).build();

			httpClient = HttpClients.custom()
					.setSSLContext(sslContext)
					.setSSLHostnameVerifier(new NoopHostnameVerifier())
					.build();

		} catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
			e.printStackTrace();
		}
		HttpPost httppost = new HttpPost("https://" + Client.SERVER_IP + ":" + Client.SERVER_HOST + "/login");
		List<NameValuePair> params = new ArrayList<>(2);
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		JSONObject response;
		String responseBody = EntityUtils.toString(httpClient.execute(httppost).getEntity(), "UTF-8");
		response = new JSONObject(responseBody);

		if(response.get("data").equals("Wrong username")) {
			throw new LoginException(response.getString("data"));
		}
		else if(response.get("data").equals("Wrong password")) {
			throw new LoginException(response.getString("data"));
		}
		else {
			userToken = response.getString("data");
		}
		return userToken;
	}

	public static void openHistoryInBrowser() {
		if(!userToken.isEmpty() && !user.isEmpty()) {
			try {
				String url = "http://" + SERVER_IP + "/r.php?u=" + user + "&t=" + userToken;
				if (Runtime.getRuntime().exec(new String[] {"which", "xdg-open"}).getInputStream().read() != -1) {
					Runtime.getRuntime().exec(new String[] {"xdg-open", url});
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		else {
			JOptionPane.showConfirmDialog(null, "You are not logged in.", "Login", JOptionPane.OK_CANCEL_OPTION);
		}
	}
}
