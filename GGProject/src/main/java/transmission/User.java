package transmission;


import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.swing.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class User extends Client {
	private static String userToken = "";

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
    	if(!userToken.isEmpty()) {
    		//TODO run in browser!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		}
		else {
			JOptionPane.showConfirmDialog(null, "You are not logged in.", "Login", JOptionPane.OK_CANCEL_OPTION);
		}
	}

}
