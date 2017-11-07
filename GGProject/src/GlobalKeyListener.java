import Screens.*;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class of global keyboard listener (defined actions for PRTSCR and combination ALT+PRTSCR).
 * PRTSCR = taking fullScreen screenshot
 * ALT+PRTSCR = capture only active window
 */
public class GlobalKeyListener implements NativeKeyListener {
	private boolean altPressedFlag = false;

	static void startListener() {
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

//				#2 Screenshot captured via gnome-screenshot program
				manager = new ScreenshotManager(new ScreenshotViaGnome());

				System.out.println("Smile ;]");
//				ALT+PRTSCR pressed - active window screenshot
				if (altPressedFlag) {
					manager.captureActiveWindow();
					System.out.println("Window captured\n");

				}
//				PRTSCR pressed - full screenshot
				else {
					manager.captureFullScreen();
					System.out.println("Fullscreen captured\n");
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
//        System.out.println("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
	}
}