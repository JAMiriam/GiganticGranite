package Connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
	public static void main(String args[]) {
		Client.startConnection("localhost", 8080);
	}

	private static Socket socket;

	static void startConnection(String serverIp, int serverPort) {
		try {
			socket = new Socket(serverIp, serverPort);
		} catch (IOException e) {
			e.printStackTrace();
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

class DummyServer {
	private static ServerSocket socket;
	private static Socket clientSocket;

	public static void main(String args[]) {
		DummyServer.runServer(8080);
		DummyServer.listenForFile();
	}

	public static void runServer(int serverPort) {
		try {
			socket = new ServerSocket(serverPort);
			clientSocket = socket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void listenForFile() {
		try {
			while (true) {
				DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
				FileOutputStream fout = new FileOutputStream("screens/serverScreens/image.png");
				int i;
				while ( (i = dis.read()) > -1) {
					fout.write(i);
				}

				fout.flush();
				fout.close();
				dis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void closeConnection() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}