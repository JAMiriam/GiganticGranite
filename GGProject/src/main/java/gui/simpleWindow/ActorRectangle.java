package gui.simpleWindow;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class ActorRectangle extends Rectangle {
	private final Color normalStrokeColor = Color.web("870F57");
	private Rectangle filling;
	private String name;
	private boolean isActive;

	ActorRectangle(int x, int y, int width, int height, String name) {
		super(width, height);
		this.name = name;
		isActive = false;
		relocate(x, y);
		setFill(Color.TRANSPARENT);
		setStroke(normalStrokeColor);
		setStrokeWidth(5);

		setArcHeight(10);
		setArcWidth(10);


//		setOnMouseEntered(event -> {
//			System.out.println("Rectangle entered");
//			changeColor();
//		});
//		setOnMouseExited(event -> {
//			System.out.println("Rectangle exited");
//			changeColor();
//		});
//
//		setOnMouseClicked(event -> {
//			System.out.println("Rectangle clicked");
//		});

	}

	void changeColor() {
		if(!isActive)
			setEffect(new DropShadow(20, Color.GOLDENROD));
		else
			setEffect(null);
		isActive = !isActive;
	}

	public String toString() {
		return name;
	}
}
