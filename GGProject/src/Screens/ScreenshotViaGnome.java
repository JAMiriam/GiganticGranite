package Screens;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ScreenshotViaGnome implements IScreenshoter {
	@Override
	public void takeFullScreen(String savePath) throws Exception {
		BashCommandExecutor.runCommand("gnome-screenshot --file=" + savePath);
	}

	@Override
	public void takeActiveWindow(String savePath) throws Exception {
		BashCommandExecutor.runCommand("gnome-screenshot -w --file=" + savePath);
	}
}

class BashCommandExecutor {
	static void runCommand(String command) throws Exception {
		System.out.println("Executing BASH command:\n   " + command);
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
