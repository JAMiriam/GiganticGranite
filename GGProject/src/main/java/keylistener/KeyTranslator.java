package keylistener;

import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Translates given names to JNativeHook numbers and numbers to JVativeHook names
 */
public class KeyTranslator {

	/**
	 * Translates string with name to key number
	 * @param name key name
	 * @return key number
	 */
	public static int getKeyNumber(String name) {
		switch (name) {
			case "PrtSc": {
				return NativeKeyEvent.VC_PRINTSCREEN;
			}
			case "Insert": {
				return NativeKeyEvent.VC_INSERT;
			}
			case "Delete": {
				return NativeKeyEvent.VC_DELETE;
			}
			case "PgUp": {
				return NativeKeyEvent.VC_PAGE_UP;
			}
			case "PgDown": {
				return NativeKeyEvent.VC_PAGE_DOWN;
			}
			case "ALT": {
				return NativeKeyEvent.VC_ALT;
			}
			case "CTRL": {
				return NativeKeyEvent.VC_CONTROL;
			}
			case "SHIFT": {
				return NativeKeyEvent.VC_SHIFT;
			}
		}
		return 0;
	}

	/**
	 * Translates key number to key name
	 * @param number key number
	 * @return key name
	 */
	public static String getKeyName(int number) {
		switch(number) {
			case 3639: {
				return "PrtSc";
			}
			case 3666: {
				return "Insert";
			}
			case 3667: {
				return "Delete";
			}
			case 3657: {
				return "PgUp";
			}
			case 3665: {
				return "PgDown";
			}
			case 56: {
				return "ALT";
			}
			case 29: {
				return "CTRL";
			}
			case 42: {
				return "SHIFT";
			}
		}
		return "";
	}
}
