package gui.smallerWindow;

import java.awt.*;
import java.awt.geom.Rectangle2D;

class FaceRectangle {
	private Rectangle2D faceRect;
	private Rectangle2D labelRect;
	private String label;
	private boolean active;
	boolean activeLabel;
	Point labelPosition;

	FaceRectangle(Rectangle2D faceRect, String name) {
		this.faceRect = faceRect;
		this.label = name;
		active = false;
		activeLabel = false;

		System.out.println("String name length: " + label.length());
		labelPosition = new Point((int)faceRect.getX(), (int)(faceRect.getY()+faceRect.getHeight()+20));
	}

	public Rectangle2D getFaceRect() {
		return faceRect;
	}

	public String getActorName() {
		return label;
	}

	public boolean isActive() {
		return active;
	}

	public void activate() {
		active = true;
		activeLabel = true;
	}

	public void deactivate() {
		active = false;
	}
}
