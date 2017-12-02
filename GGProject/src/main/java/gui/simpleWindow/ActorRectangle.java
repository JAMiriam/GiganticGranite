package gui.simpleWindow;

import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class ActorRectangle extends Rectangle {
	private final Color normalStrokeColor = Color.web("870F57");
	private final DropShadow shadow = new DropShadow(127, Color.GOLDENROD);
	private Tooltip tooltip;
	private boolean isNameVisible;

	ActorRectangle(int x, int y, int width, int height, String name) {
		super(width, height);
		tooltip = new Tooltip(name);
		isNameVisible = false;
		relocate(x, y);
//		setFill(Color.TRANSPARENT);
		setFill(Color.grayRgb(255, 0.55));
		setStroke(normalStrokeColor);
		setStrokeWidth(4);

		setArcHeight(10);
		setArcWidth(10);

		//TODO set tooltip style
//		tooltip.setStyle("-fx-background: rgba(30,30,30);" +
//				"-fx-text-fill: white;" +
//				"-fx-background-color: rgba(30,30,30,0.8);" +
//				"-fx-padding: 0.667em 0.75em 0.667em 0.75em; /* 10px */" +
//				"-fx-font-size: 0.85em;"
//		);

		setOnMouseEntered(event -> {
			setEffect(shadow);
			showName(event);
		});
		setOnMouseExited(event -> {
			setEffect(null);
			hideName();
		});

		setOnMouseClicked(event -> {
			System.out.println("Rectangle clicked");
		});
	}

	private void showName(MouseEvent event) {
		if(!isNameVisible) {
			tooltip.show(this, event.getSceneX(), event.getSceneY() + 2);
//			Tooltip.install(this, tooltip);
			isNameVisible = true;
		}
	}

	private void hideName() {
		tooltip.hide();
		isNameVisible = false;
	}
}