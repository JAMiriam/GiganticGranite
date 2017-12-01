package keylistener;

import org.jnativehook.*;
import org.jnativehook.keyboard.*;
import screenshots.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class of global keyboard listener (defined actions for PRTSCR and combination ALT+PRTSCR).
 * PRTSCR = taking fullScreen screenshot
 * ALT+PRTSCR = capture only active window
 */
public class GlobalKeyListener extends Thread implements NativeKeyListener  {
	private boolean altPressedFlag = false;

	public static void startListener() {
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

	public void run() {
		startListener();
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
//		ESC pressed - exit program
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
		}

//		ALT pressed - set flag
		if (e.getKeyCode() == NativeKeyEvent.VC_ALT)
			altPressedFlag = true;
		else if (e.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN) {
			try {
				ScreenshotManager manager;
//				#1 Screenshot captured via Robot.createScreenCapture
//				manager = new ScreenshotManager(new ScreenshotViaRobot());

				manager = new ScreenshotManager(new ScreenshotViaGnome());

				//TODO capture active window blocked - need to figure out full screen window info!
//				ALT+PRTSCR pressed - active window screenshot
				if (altPressedFlag) {
//					manager.captureActiveWindow();
//					manager.sendScreenshot();
				}
//				PRTSCR pressed - full screenshot
				else {
					manager.captureFullScreen();
					manager.sendScreenshot();
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_ALT)
			altPressedFlag = false;
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
	}
}