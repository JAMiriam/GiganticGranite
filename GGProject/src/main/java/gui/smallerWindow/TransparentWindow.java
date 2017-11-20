package gui.smallerWindow;

import windowutils.ActiveWindowInfo;
import windowutils.WindowInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

public class TransparentWindow extends JFrame {
	private static TransparentWindow window;
	private ArrayList<FaceRectangle> aRectangles = new ArrayList<>();
	private Point cursorPosition;

	public TransparentWindow() {
		super("GGSmallWindow");
		setLayout(new GridBagLayout());
		setUndecorated(true);
		setLocationRelativeTo(null);
		setOpacity(0.3f);
		setVisible(true);
//		setWindowLocation();
		setBounds(200, 100, 500, 400);
		addMouseListener(new WindowMouseListener());
		addMouseMotionListener(new WindowMouseMotionListener());
	}

	private void setWindowLocation() {
		try {
			WindowInfo info = ActiveWindowInfo.getActiveWindowInfo();
			setBounds(info.getX(), info.getY(), info.getWidth(), info.getHeight());
			System.out.println(info);
		} catch (IOException ignored) {}
	}

	private void drawRectangle(int x, int y, int width, int height, String name) {
		aRectangles.add(new FaceRectangle(new Rectangle(x, y, width, height), name));
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Stroke oldStroke = g2.getStroke();
		for(FaceRectangle s : aRectangles){
			g2.setStroke(new BasicStroke(5));
			g2.setColor(new Color(135, 15, 87));
			if(s.isActive()) {
				g2.setFont(new Font ("Courier New", 1, 17));
				if(s.activeLabel) {
					Rectangle rectangle = new Rectangle(cursorPosition.x, cursorPosition.y, 100, 30);
					g2.setColor(Color.BLACK);
					g2.draw(rectangle);
					g2.setColor(Color.CYAN);
					g2.drawString(s.getActorName(), rectangle.x, rectangle.y);
					s.activeLabel = false;
				}
				g2.setColor(Color.yellow);
			}
			g2.draw(s.getRectangle());
		}
		g2.setStroke(oldStroke);
	}

	public static void main(String args[]) {
		window = new TransparentWindow();
		//test dummy data
		window.drawRectangle(10, 10, 105, 105, "Johnny Depp");
		window.drawRectangle(100, 200, 50, 50, "Alan Rickman");
		window.drawRectangle(200, 250, 105, 105, "Cate Blanchett");
		window.repaint();
	}

	class WindowMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {

		}

		@Override
		public void mousePressed(MouseEvent mouseEvent) {

		}

		@Override
		public void mouseReleased(MouseEvent mouseEvent) {

		}

		@Override
		public void mouseEntered(MouseEvent mouseEvent) {

		}

		@Override
		public void mouseExited(MouseEvent mouseEvent) {

		}
	}

	class WindowMouseMotionListener implements MouseMotionListener {
		@Override
		public void mouseMoved(MouseEvent mouseEvent) {
			cursorPosition = mouseEvent.getPoint();
			for(FaceRectangle rect: aRectangles) {
				if(rect.getRectangle().contains(mouseEvent.getX(), mouseEvent.getY())) {
					rect.activate();
					rect.activeLabel = true;
				}
				else
					rect.deactivate();
				repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent mouseEvent) {}
	}
}
