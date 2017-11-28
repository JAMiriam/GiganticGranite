package gui.smallerWindow;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;

public class TestJavafxTransparent extends Application {
	private static final int shadowSize = 50;

	@Override public void start(final Stage stage) {
		stage.initStyle(StageStyle.TRANSPARENT);

		StackPane stackPane = new StackPane(createShadowPane());
		stackPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);" +
						"-fx-background-insets: " + shadowSize + ";"
		);

		Scene scene = new Scene(stackPane, 450, 450);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);

		drawShapes(scene);
		stage.show();
	}

	private void drawShapes(Scene scene) {
		VBox vb = new VBox();

//		Pane canvas = new Pane();
//		canvas.setPrefSize(200,200);
//		Circle circle = new Circle(50,Color.BLUE);
//		circle.relocate(20, 20);
//		Rectangle rectangle = new Rectangle(100,100,Color.RED);
//		rectangle.relocate(70,70);
//		canvas.getChildren().addAll(circle,rectangle);

//		vb.getChildren().add(canvas);

		scene.setRoot(vb);
	}

	private Pane createShadowPane() {
		Pane shadowPane = new Pane();
		shadowPane.setStyle(
				"-fx-background-color: white;" + "-fx-effect: " +
						"dropshadow(gaussian, #870f57, " + shadowSize + ", 0, 0, 0);" +
						"-fx-background-insets: " + shadowSize + ";"
		);

		Rectangle innerRect = new Rectangle();
		Rectangle outerRect = new Rectangle();
		shadowPane.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
					innerRect.relocate(
							newBounds.getMinX() + shadowSize,
							newBounds.getMinY() + shadowSize
					);
					innerRect.setWidth(newBounds.getWidth() - shadowSize * 2);
					innerRect.setHeight(newBounds.getHeight() - shadowSize * 2);
					outerRect.setWidth(newBounds.getWidth());
					outerRect.setHeight(newBounds.getHeight());
					Shape clip = Shape.subtract(outerRect, innerRect);
					shadowPane.setClip(clip);
				}
		);
		return shadowPane;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
