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

/**
 * Class with methods to manage created screenshots
 */
public class ScreenshotManager {
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hh_mm_ss_a");
	private IScreenshoter screenshoter;
	private String path;

	public ScreenshotManager(IScreenshoter screenshoter) {
		this.screenshoter = screenshoter;
		path = "screens/" + formatter.format(Calendar.getInstance().getTime()) + ".png";
	}

	/**
	 * Takes full screenshot
	 * @throws Exception
	 */
	public void captureFullScreen() throws Exception{
		screenshoter.takeFullScreen(path);
		System.out.println("Window captured");
	}

	/**
	 * Takes screenshot of active window
	 * @throws Exception
	 */
	public void captureActiveWindow() throws Exception {
		screenshoter.takeActiveWindow(path);
		System.out.println("Window captured");
	}

	/**
	 * Runs method on client to send screenshot
	 */
	public void sendScreenshot() {
		System.out.println("Screenshot sent");
		Client.sendImageToServer(path);
	}
}
