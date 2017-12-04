package transmission;


import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class User extends Client{

    /**
     * Logs in a user to the server using given credentials.
     * @param username String containing username.
     * @param password String containing password.
     * @return user's id from server's database.
     * @throws IOException when connection fails.
     * @throws LoginException when given username or password were incorrect.
     */
    public static int login(String username, String password) throws IOException, LoginException{
        HttpPost httppost = new HttpPost("http://" + Client.SERVER_IP + ":" + Client.SERVER_HOST + "/login");
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpClient httpClient = HttpClients.createDefault();
        JSONObject response;
        Integer userId;
            String responseBody = EntityUtils.toString(httpClient.execute(httppost).getEntity(), "UTF-8");
            response = new JSONObject(responseBody);

            if(response.get("data") instanceof Integer){
                userId = response.getInt("data");
            }
            else throw new LoginException(response.getString("data"));
        return userId;
    }

}
