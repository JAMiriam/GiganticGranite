package gui.simpleWindow;

import gui.GUIManager;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class with graphical representation of actor's rectangle
 */
class ActorRectangle extends Rectangle {
	private final DropShadow shadow = new DropShadow(127, Color.GOLDENROD);
	private Tooltip tooltip;
	private boolean isNameVisible;
	private String imdb;

	/**
	 * Class constructor
	 * @param x coordinate
	 * @param y	coordinate
	 * @param width	width of rectangle
	 * @param height height of rectangle
	 * @param name full name for tooltip
	 * @param imdb imdb id
	 * @param reliability recognition is right/wrong
	 */
	ActorRectangle(int x, int y, int width, int height, String name, String imdb, String reliability) {
		super(width, height);
		tooltip = new Tooltip(name);
		isNameVisible = false;
		this.imdb = imdb;

		Color rightStrokeColor = Color.GREEN;
		Color wrongStrokeColor = Color.RED;
		relocate(x, y);
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
			hideName();
			GUIManager.showDetailsPane();
			JavaFXSimpleWindow.setActorDetails(imdb);
		});
	}

	/**
	 * Shows tooltip with actor's name near to the rectangle
	 * @param event mouse event for coordinates
	 */
	private void showName(MouseEvent event) {
		if(!isNameVisible) {
			tooltip.show(this, event.getSceneX(), event.getSceneY() + 2);
			isNameVisible = true;
		}
	}

	/**
	 * Hides tooltip with name
	 */
	private void hideName() {
		tooltip.hide();
		isNameVisible = false;
	}

	/**
	 * @return if user is pointing this actor
	 */
	public boolean isActive() {
		return isNameVisible;
	}

	/**
	 * @return actor's imdb identifier
	 */
	public String getImdb() {
		return imdb;
	}
}