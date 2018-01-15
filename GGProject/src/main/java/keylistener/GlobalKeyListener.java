package keylistener;

import gui.GUIManager;
import org.jnativehook.*;
import org.jnativehook.keyboard.*;
import screenshots.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class of global keyboard listener (defined actions for PRTSCR and combination ALT+PRTSCR).
 */
public class GlobalKeyListener extends Thread implements NativeKeyListener  {
	private static int basicKey;
	private static int extraKey;
	private boolean extraFlag = false;
	private static boolean enabled = true;

	/**
	 * Starts global key listener
	 */
	private static void startListener() {
		try {
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Enables or disables listener
	 * @param state new listener state
	 */
	public static void setListenerState(boolean state) {
		enabled = state;
	}

	/**
	 * Sets new keys configuration
	 * @param basic new basic key
	 * @param extra new extra key
	 */
	public static void setConfig(int basic, int extra) {
		basicKey = basic;
		extraKey = extra;
		System.out.println("New config is: " + basicKey + ", " + extraKey);
	}

	/**
	 * Gets current key configuration
	 * @return array with basic and extra keys
	 */
	public static int[] getCurrentConfig() {
		return new int[] {basicKey, extraKey};
	}

	/**
	 * Start listener thread
	 */
	public void run() {
		basicKey = NativeKeyEvent.VC_PRINTSCREEN;
		extraKey = NativeKeyEvent.VC_ALT;
		startListener();
	}

	/**
	 * Listens for keys pressing
	 * @param e mouse click events
	 */
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if(enabled) {
//		ESC pressed - clear window
			if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
				GUIManager.clearSimpleWindow();
			}

//		extra pressed - set flag
			if (e.getKeyCode() == extraKey)
				extraFlag = true;
			else if (e.getKeyCode() == basicKey) {
				try {
					ScreenshotManager manager = new ScreenshotManager(new ScreenshotViaGnome());

//				active window screenshot
					if (extraFlag) {
						manager.captureActiveWindow();
						manager.sendScreenshot();
					}
//				full screenshot
					else {
						manager.captureFullScreen();
						manager.sendScreenshot();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Listens for keys releasing
	 * @param e mouse click events
	 */
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if (e.getKeyCode() == extraKey)
			extraFlag = false;
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
	}
}