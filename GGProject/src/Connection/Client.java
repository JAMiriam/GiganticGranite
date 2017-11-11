package Connection;

import json.JSONAnalizer;

import java.io.*;
import java.net.Socket;

public class Client {
	private static Socket socket;

	public void startConnection(String serverIp, int serverPort) {
		try {
			socket = new Socket(serverIp, serverPort);
			new ClientThread(socket).start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class ClientThread extends Thread {
		Socket socket;
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
				sendGETRequest();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		void sendGETRequest() throws IOException {
			String line;
			boolean sent = false;
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