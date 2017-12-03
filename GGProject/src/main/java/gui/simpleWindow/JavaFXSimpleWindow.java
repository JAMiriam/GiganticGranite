package gui.simpleWindow;

import javafx.application.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import models.SimpleActor;
import windowutils.WindowInfo;
import java.util.ArrayList;

import static javafx.stage.StageStyle.TRANSPARENT;

//TODO make proper css for all nodes!
public class JavaFXSimpleWindow extends Application {
	private static Stage mainStage;
	private static Scene scene;
	private static Pane canvas;
	private static Group groupRoot;
	private static final int shadowSize = 50;
	private static ArrayList<SimpleActor> actors;
	private static ArrayList<ActorRectangle> rectangles;
	private static WindowInfo info;

	@Override
	public void start(final Stage stage) {
		mainStage = stage;
		canvas = new Pane();
		groupRoot = new Group();
		actors = new ArrayList<>();
		rectangles = new ArrayList<>();

		stage.initStyle(TRANSPARENT);
		groupRoot.setStyle("-fx-background-color: transparent");
		groupRoot.getChildren().add(canvas);
		scene = new Scene(groupRoot, 1, 1);
		scene.setFill(null);
		mainStage.setScene(scene);

//		scene.setOnMouseMoved(event -> {
////			System.out.println("[" + event.getX() + ", " + event.getY() + "]");
//		});

		System.out.println("JavaFX simple window created");
	}

	public static void relocateWindow(WindowInfo windowPos) {
		Platform.runLater(() -> {
			info = windowPos;
			mainStage.setX(info.getX());
			mainStage.setY(info.getY());
			mainStage.setMinHeight(info.getHeight());
			mainStage.setMinWidth(info.getWidth());
		});
	}

	public static void loadActors(ArrayList<SimpleActor> recognizedActors) {
		Platform.runLater(() -> {
			actors.addAll(recognizedActors);
			System.out.println("\nACTORS RECOGNIZED:");
			for(SimpleActor a : actors) {
				System.out.println(a.toString());
			}
		});
	}

	//TODO make old rectangles gone!
	public static void drawRectangles() {
		Platform.runLater(() -> {
			canvas.setPrefSize(info.getWidth(), info.getHeight());

			for(SimpleActor actor : actors) {
				//actor.Pos = {left, top, right, bottom}
				int width = actor.getPos()[3] - actor.getPos()[0];
				int height = actor.getPos()[2] - actor.getPos()[1];
				int x = actor.getPos()[1], y = actor.getPos()[0];
				rectangles.add(new ActorRectangle(x, y, width, height, actor.getName()));
			}
			canvas.getChildren().addAll(rectangles);
			mainStage.setAlwaysOnTop(true);
			mainStage.show();
		});
	}

	public static void clearData() {
		Platform.runLater(() -> {
			rectangles.clear();
			actors.clear();
			canvas.getChildren().clear();
			scene.setFill(null);
			mainStage.setScene(scene);
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
