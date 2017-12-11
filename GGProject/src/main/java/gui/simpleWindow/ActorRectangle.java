package gui.simpleWindow;

import gui.WindowManager;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class ActorRectangle extends Rectangle {
	private final Color normalStrokeColor = Color.web("870F57");
	private final Color rightStrokeColor = Color.GREEN;
	private final Color wrongStrokeColor = Color.RED;
	private final DropShadow shadow = new DropShadow(127, Color.GOLDENROD);
	private Tooltip tooltip;
	private String imdb;
	private boolean isNameVisible;

	ActorRectangle(int x, int y, int width, int height, String name, String imdb, String reliability) {
		super(width, height);
		tooltip = new Tooltip(name);
		this.imdb = imdb;
		isNameVisible = false;
		relocate(x, y);
//		setFill(Color.TRANSPARENT);
		setFill(Color.grayRgb(255, 0.6));
		if(reliability.equals("wrong"))
			setStroke(wrongStrokeColor);
		else
			setStroke(rightStrokeColor);
		setStrokeWidth(4);
		setArcHeight(10);
		setArcWidth(10);

		tooltip.setStyle("-fx-font: normal bold 14 Langdon; "
				+ "-fx-base: #AE3522; "
				+ "-fx-text-fill: orange;");

		setOnMouseEntered(event -> {
			setEffect(shadow);
			showName(event);
		});
		setOnMouseExited(event -> {
			setEffect(null);
			hideName();
		});

		setOnMouseClicked(event -> {
			WindowManager.showDetailsPane();
			JavaFXSimpleWindow.loadActorDetails(imdb);
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