package gui.smallerWindow;

import java.awt.*;

class FaceRectangle {
	private Shape rectangle;
	private String label;
	private boolean active;
	boolean activeLabel;

	FaceRectangle(Shape rectangle, String name) {
		this.rectangle = rectangle;
		this.label = name;
		active = false;
		activeLabel = false;
	}

	public Shape getRectangle() {
		return rectangle;
	}

	public String getActorName() {
		return label;
	}

	public boolean isActive() {
		return active;
	}

	public void activate() {
		active = true;
	}

	public void deactivate() {
		active = false;
	}
}
