package screenshots;

import gui.WindowManager;
import windowutils.ActiveWindowInfo;
import windowutils.WindowInfo;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Class implements interface for taking screenshots using a Linux
 * program called gnome-screenshot
 */
public class ScreenshotViaGnome implements IScreenshoter {
	@Override
	public void takeFullScreen(String savePath) throws Exception {
		BashCommandExecutor.runCommand("gnome-screenshot --file=" + savePath);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		WindowManager.setScreenSize(new WindowInfo("", 0, 0, screenSize.width, screenSize.height));
	}

	@Override
	public void takeActiveWindow(String savePath) throws Exception {
		BashCommandExecutor.runCommand("gnome-screenshot -w --file=" + savePath);
		WindowInfo info = ActiveWindowInfo.getActiveWindowInfo();
		WindowManager.setScreenSize(new WindowInfo("", info.getX(), info.getY(), info.getWidth(), info.getHeight()));
	}
}

/**
 * Class executing given bash command
 */
class BashCommandExecutor {
	static void runCommand(String command) throws Exception {
		Runtime r = Runtime.getRuntime();
		String[] commands = {"bash", "-c", command};
		String line;
		Process p = r.exec(commands);
		p.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		while ((line = reader.readLine()) != null)
			System.out.println(line);

		reader.close();
	}
}
