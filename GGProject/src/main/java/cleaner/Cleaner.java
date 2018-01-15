package cleaner;

import transmission.Client;

import java.io.File;
import java.util.Vector;

/**
 * Class with actions to perform before exiting the program
 */
public class Cleaner {
	public Cleaner() {
		deleteScreenshotsFromSession(Client.screenshotPaths);
		Client.closeConnection();
		System.exit(0);
	}

	/**
	 * Removes screens from drive
	 * @param paths vactor of screenshots' paths
	 */
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
