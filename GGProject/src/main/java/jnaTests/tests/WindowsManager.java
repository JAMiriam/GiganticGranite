package jnaTests.tests;

import com.sun.jna.Native;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.Window;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.util.Collections;

public class WindowsManager {
	public static void main(String[] args) {
		X11 x11 = X11.INSTANCE;
		Display display = x11.XOpenDisplay(null);

		Window root = x11.XDefaultRootWindow(display);
		recurse(x11, display, root, 0);
	}

	private static void recurse(X11 x11, Display display, Window root, int depth) {
		X11.WindowByReference windowRef = new X11.WindowByReference();
		X11.WindowByReference parentRef = new X11.WindowByReference();
		PointerByReference childrenRef = new PointerByReference();
		IntByReference childCountRef = new IntByReference();

		x11.XQueryTree(display, root, windowRef, parentRef, childrenRef, childCountRef);
		if (childrenRef.getValue() == null) {
			return;
		}

		long[] ids;

		if (Native.LONG_SIZE == Long.BYTES) {
			ids = childrenRef.getValue().getLongArray(0, childCountRef.getValue());
		} else if (Native.LONG_SIZE == Integer.BYTES) {
			int[] intIds = childrenRef.getValue().getIntArray(0, childCountRef.getValue());
			ids = new long[intIds.length];
			for (int i = 0; i < intIds.length; i++) {
				ids[i] = intIds[i];
			}
		} else {
			throw new IllegalStateException("Unexpected size for Native.LONG_SIZE" + Native.LONG_SIZE);
		}

		for (long id : ids) {
			if (id == 0) {
				continue;
			}
			Window window = new Window(id);
			X11.XTextProperty name = new X11.XTextProperty();
			x11.XGetWMName(display, window, name);

			X11.XWindowAttributes xwa = new X11.XWindowAttributes();
			String windowName = String.join("", Collections.nCopies(depth, "  ")) + name.value;
			if(!windowName.contains("null")) {
				System.out.println(windowName);
				x11.XGetWindowAttributes(display, window, xwa);
				int height = xwa.height, width = xwa.width;
				if(height >= 50 && width >= 50) {
					System.out.println("H: " + height + ", width: " + width);
				}
			}
			x11.XFree(name.getPointer());

			recurse(x11, display, window, depth + 1);
		}
	}
}

