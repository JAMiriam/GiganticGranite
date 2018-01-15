package screenshots;

/**
 * Interface for taking screenshots
 */
public interface IScreenshoter {
	void takeFullScreen(String savePath) throws Exception;
	void takeActiveWindow(String savePath) throws Exception;
}
