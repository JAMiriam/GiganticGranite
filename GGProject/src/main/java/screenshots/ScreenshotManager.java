package screenshots;

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
	}

	public void captureActiveWindow() throws Exception {
		screenshoter.takeActiveWindow(path);
	}

	public void sendScreenshot() {

	}
}
