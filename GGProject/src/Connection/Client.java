package Connection;

import json.JSONAnalizer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.JsonObject;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;

public class Client {
	public static void main(String args[]) {
		Client client = new Client();
		client.startConnection("localhost", 5000);
	}

	private static Socket socket;

	private void startConnection(String serverIp, int serverPort) {
		try {
			socket = new Socket(serverIp, serverPort);
			new ClientThread(socket).start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class ClientThread extends Thread {
		Socket socket;
		boolean sent = false;
		BufferedReader bufferedReader;
		PrintWriter printWriter;

		ClientThread(Socket socket) {
			try {
				this.socket = socket;
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		public void run() {
			try {
				String line;
				if(!sent) {
					printWriter.write("GET /actors/asdf HTTP/1.1\r\n\r\n");
					printWriter.flush();
					sent = true;
				}

				while ((line = bufferedReader.readLine()) != null) {
					if(line.startsWith("[")) {
						System.out.println(line);
						JSONAnalizer.analyze(line);
					}
				}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	static void sendFile (String imagePath) {
		int i;
		try {
			FileInputStream fis = new FileInputStream(imagePath);
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			while ((i = fis.read()) > -1)
				os.write(i);

			fis.close();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}