package screenshots;

import transmission.Client;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScreenshotManager {
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hh_mm_ss_a");
	private IScreenshoter screenshoter;
	private String path;

	public ScreenshotManager(IScreenshoter screenshoter) {
		this.screenshoter = screenshoter;
		path = "screens/" + formatter.format(Calendar.getInstance().getTime()) + ".png";
	}

	public void captureFullScreen() throws Exception{
		screenshoter.takeFullScreen(path);
		System.out.println("Window captured");
	}

	public void captureActiveWindow() throws Exception {
		screenshoter.takeActiveWindow(path);
		System.out.println("Window captured");
	}

	//TODO delete screenshot eventually!!
	public void sendScreenshot() {
		System.out.println("Screenshot sent");
		Client.sendImageToServer(path);
//		deleteScreenshot();
	}

	private void deleteScreenshot() {
		try {
			File file = new File(path);
			if(file.delete())
				System.out.println(file.getName() + " is deleted!");
			else
				System.out.println("Delete operation is failed.");
		}
		catch (Exception x) {
			x.printStackTrace();
		}
	}
}
