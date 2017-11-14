package screenshots;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Class implements interface for taking screenshots using a java Robot class
 */
public class ScreenshotViaRobot implements IScreenshoter {
	@Override
	public void takeFullScreen(String savePath) throws Exception {
		BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, "png", new File(savePath));
	}

	@Override
	public void takeActiveWindow(String savePath) throws Exception {
		//sadly it seems to be impossible...
	}
}
