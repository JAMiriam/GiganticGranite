package gui.simpleWindow;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;
import models.SimpleActor;
import windowutils.WindowInfo;
import java.util.ArrayList;

public class JavaFXSimpleWindow extends Application {
	private static Stage mainStage;
	private static Scene scene;
	private static Pane canvas;
	private static Group vb;
	private static final int shadowSize = 50;
	private static ArrayList<SimpleActor> actors;
	private static ArrayList<ActorRectangle> rectangles;
	private static WindowInfo info;

	@Override
	public void start(final Stage stage) {
		mainStage = stage;
		StackPane stackPane = new StackPane(createShadowPane());
		scene = new Scene(stackPane, 10, 10);
		canvas = new Pane();
		actors = new ArrayList<>();
		rectangles = new ArrayList<>();

		stage.initStyle(StageStyle.TRANSPARENT);
		stackPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);" +
				"-fx-background-insets: " + shadowSize + ";"
		);
		scene.setFill(Color.TRANSPARENT);
		mainStage.setScene(scene);
		mainStage.setAlwaysOnTop(true);

		vb = new Group();
		vb.getChildren().add(canvas);
		scene.setRoot(vb);
		mainStage.show();

		scene.setOnMouseMoved(event -> {
//			System.out.println("[" + event.getX() + ", " + event.getY() + "]");

			for(ActorRectangle rectangle : rectangles) {
				if(rectangle.getBoundsInParent().contains(event.getX(), event.getY())) {
					System.out.println("Enter: " + rectangle.toString());
					rectangle.changeColor();
				}
			}
		});

		System.out.println("JavaFX simple window created");
	}

	public static void relocateWindow(WindowInfo windowPos) {
		Platform.runLater(() -> {
			info = windowPos;
			mainStage.setMinHeight(info.getHeight());
			mainStage.setMinWidth(info.getWidth());
			mainStage.setX(info.getX());
			mainStage.setY(info.getY());
		});
	}

	public static void loadActors(ArrayList<SimpleActor> recognizedActors) {
		Platform.runLater(() -> {
			actors.addAll(recognizedActors);
			for(SimpleActor a : actors) {
				System.out.println(a.toString());
			}
		});
	}

	//TODO make old rectangles gone!
	public static void drawRectangles() {
		Platform.runLater(() -> {
			canvas.setPrefSize(info.getWidth(), info.getHeight());
			vb.getChildren().removeAll();
			rectangles.clear();

			for(SimpleActor actor : actors) {
				//actor.Pos = {left, top, right, bottom}
				int width = actor.getPos()[3] - actor.getPos()[0];
				int height = actor.getPos()[2] - actor.getPos()[1];
				int x = actor.getPos()[1], y = actor.getPos()[0];
				System.out.println("x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);
				rectangles.add(new ActorRectangle(x, y, width, height, actor.getName()));
			}
			canvas.getChildren().addAll(rectangles);
		});
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
