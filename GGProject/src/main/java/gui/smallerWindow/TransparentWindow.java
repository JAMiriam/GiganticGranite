package gui.smallerWindow;

import windowutils.ActiveWindowInfo;
import windowutils.WindowInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TransparentWindow extends JFrame {
	private static TransparentWindow window;

	public TransparentWindow() {
		super("GGSmallWindow");
		setLayout(new GridBagLayout());
		setUndecorated(true);
		setLocationRelativeTo(null);
		setOpacity(0.3f);
		setVisible(true);
//		setWindowLocation();
		setBounds(200, 100, 500, 400);
	}

	private void setWindowLocation() {
		try {
			WindowInfo info = ActiveWindowInfo.getActiveWindowInfo();
			setBounds(info.getX(), info.getY(), info.getWidth(), info.getHeight());
			System.out.println(info);
		} catch (IOException ignored) {}
	}

	private void drawRectangle(int x, int y, int height, int width) {

	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(5));
		g.setColor(new Color(135, 15, 87));
		g.drawRect(5, 5, 105, 105);
		g2.setStroke(oldStroke);

	}

	public static void main(String args[]) {
		window = new TransparentWindow();
		window.repaint();
	}
}
