package gui.simpleWindow;

import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class ActorRectangle extends Rectangle {
	private final Color normalStrokeColor = Color.web("870F57");
	private Tooltip tooltip;
	private boolean isNameVisible;

	ActorRectangle(int x, int y, int width, int height, String name) {
		super(width, height);
		tooltip = new Tooltip(name);
		isNameVisible = false;
		relocate(x, y);
//		setFill(Color.TRANSPARENT);
		setFill(Color.grayRgb(255, 0.6));
		setStroke(normalStrokeColor);
		setStrokeWidth(4);

		setArcHeight(10);
		setArcWidth(10);

		setOnMouseEntered(event -> {
			setEffect(new DropShadow(127, Color.GOLDENROD));
			showName(event);
		});
		setOnMouseExited(event -> {
			setEffect(null);
			hideName();
		});
//
		setOnMouseClicked(event -> {
			System.out.println("Rectangle clicked");
		});
	}

	private void showName(MouseEvent event) {
		if(!isNameVisible) {
			tooltip.show(this, event.getSceneX(), event.getSceneY() + 5);
//			Tooltip.install(this, tooltip);
			isNameVisible = true;
		}
	}

	private void hideName() {
		tooltip.hide();
		isNameVisible = false;
	}
}