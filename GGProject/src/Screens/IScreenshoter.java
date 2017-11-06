package Screens;

public interface IScreenshoter {
	void takeFullScreen(String savePath) throws Exception;
	void takeActiveWindow(String savePath) throws Exception;
}
