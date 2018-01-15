package cleaner;

import transmission.Client;

import java.io.File;
import java.util.Vector;

public class Cleaner {
	public Cleaner() {
		deleteScreenshotsFromSession(Client.screenshotPaths);
		Client.closeConnection();
		System.exit(0);
	}

	private void deleteScreenshotsFromSession(Vector<String> paths) {
		for(String path : paths) {
			try {
				File file = new File(path);
				if(file.delete())
					System.out.println(file.getName() + " is deleted!");
				else
					System.out.println("Delete operation failed.");
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
