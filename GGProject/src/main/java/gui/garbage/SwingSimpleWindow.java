package gui.garbage;

import gui.garbage.FaceRectangle;
import windowutils.ActiveWindowInfo;
import windowutils.WindowInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;

public class SwingSimpleWindow extends JFrame {
	public static void main(String args[]) {
		window = new SwingSimpleWindow();
		//test dummy data
		window.drawRectangle(10, 10, 105, 105, "Johnny Depp");
		window.drawRectangle(100, 200, 50, 50, "Alan Rickman");
		window.drawRectangle(200, 250, 105, 105, "Cate Blanchett");
		window.repaint();
	}

	private static SwingSimpleWindow window;
	private ArrayList<FaceRectangle> aRectangles = new ArrayList<>();

	public SwingSimpleWindow() {
		super("GGSmallWindow");
		setLayout(new GridBagLayout());
		setUndecorated(true);
		setLocationRelativeTo(null);
		setOpacity(0.9f);
		setVisible(true);
//		setWindowLocation();
		setBounds(200, 100, 500, 400);
		addMouseListener(new WindowMouseListener());
		addMouseMotionListener(new WindowMouseMotionListener());

		//test dummy data
		drawRectangle(10, 10, 105, 105, "Johnny Depp");
		drawRectangle(100, 200, 50, 50, "Alan Rickman");
		drawRectangle(200, 250, 105, 105, "Cate Blanchett");
		repaint();
	}

	private void setWindowLocation() {
		try {
			WindowInfo info = ActiveWindowInfo.getActiveWindowInfo();
			setBounds(info.getX(), info.getY(), info.getWidth(), info.getHeight());
			System.out.println(info);
		} catch (IOException ignored) {}
	}

	private void drawRectangle(int x, int y, int width, int height, String name) {
		aRectangles.add(new FaceRectangle(new Rectangle2D.Float(x, y, width, height), name));
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Stroke oldStroke = g2.getStroke();

//		int height = getHeight();
//		String text = "This is text";
//		g.drawLine(0, height / 2, getWidth(), height / 2);
//
//		FontMetrics fm = g.getFontMetrics();
//		int totalWidth = (fm.stringWidth(text) * 2) + 4;
//
//		// Baseline
//		int x = (getWidth() - totalWidth) / 2;
//		int y = (getHeight() - fm.getHeight()) / 2;
//		g.setColor(Color.BLACK);
//
//		g.drawString(text, x, y + ((fm.getDescent() + fm.getAscent()) / 2));

		for(FaceRectangle s : aRectangles) {
			g2.setStroke(new BasicStroke(4));
			g2.setColor(new Color(135, 15, 87));
//			g2.setColor(new Color(170, 255, 0));
			if(s.isActive()) {
				g2.setFont(new Font ("Courier New", 1, 20));
				if(s.activeLabel) {
					g2.setColor(Color.BLACK);
					g2.setStroke(new BasicStroke(1));
					Rectangle rectangle = new Rectangle(s.labelPosition.x, s.labelPosition.y, 100, 30);
//					g2.fillRect(s.labelPosition.x, s.labelPosition.y, 100, 30);
//					g2.setColor(Color.WHITE);
					g2.drawString(s.getActorName(), rectangle.x, rectangle.y);

					s.activeLabel = false;
					System.out.println("Drawing name");
				}
				g2.setColor(new Color(171, 255,0));
			}
			g2.draw(s.getFaceRect());
		}
		g2.setStroke(oldStroke);
	}

	class WindowMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			for(FaceRectangle rect: aRectangles) {
				if(rect.getFaceRect().contains(mouseEvent.getX(), mouseEvent.getY())) {
					System.out.println(rect.getActorName() + " clicked.");
				}
				repaint();
			}
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
			for(FaceRectangle rect: aRectangles) {
				if(rect.getFaceRect().contains(mouseEvent.getX(), mouseEvent.getY())) {
					if(!rect.isActive())
						rect.activate();
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
