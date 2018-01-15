package gui.simpleWindow;

import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import models.*;
import transmission.Client;
import transmission.SessionData;
import windowutils.WindowInfo;
import java.io.IOException;
import java.util.*;

import static javafx.stage.StageStyle.TRANSPARENT;

public class JavaFXSimpleWindow extends Application {
	private static Stage mainStage;
	private static Scene scene;
	private static Group groupRoot;
	private static Pane canvas;
	private static GridPane actorPane;

	private static ArrayList<SimpleActor> actors;
	private static ArrayList<ActorRectangle> rectangles;
	private static Dialog<String> dialog;
	private static Text nameField, birthdateField, diedField, biographyField, genderField;
	private static String profileUrl;
	private static Pane detailsPhotoPane;
	private static WindowInfo info;

	@Override
	public void start(final Stage stage) {
		mainStage = stage;
		canvas = new Pane();
		groupRoot = new Group();
		Pane detailsPane = new Pane();
		actors = new ArrayList<>();
		rectangles = new ArrayList<>();
		dialog = new Dialog<>();

		stage.initStyle(TRANSPARENT);
		groupRoot.setStyle("-fx-background-color: transparent");
		groupRoot.getChildren().addAll(canvas, detailsPane);
		scene = new Scene(groupRoot, 1, 1);
		scene.setFill(null);
		mainStage.setScene(scene);
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

	public static void drawRectangles() {
		Platform.runLater(() -> {
			canvas.setPrefSize(info.getWidth(), info.getHeight());
			for(SimpleActor actor : actors) {
				int width = actor.getPos()[3] - actor.getPos()[0];
				int height = actor.getPos()[2] - actor.getPos()[1];
				int x = actor.getPos()[1], y = actor.getPos()[0];
				rectangles.add(new ActorRectangle(x, y, width, height, actor.getName(), actor.getImdb_id(), actor.getReliability()));
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
			dialog.close();
		});
	}

	public static void showDetailsPanel() {
		//TODO singleton pattern: once created = next time just load new data
		Platform.runLater(() -> {
			dialog = new Dialog<>();
			dialog.setTitle("Details pane");
//			dialog.setHeaderText("This is a custom dialog. Enter info and \n" +
//					"press Okay (or click title bar 'X' for cancel).");
			dialog.setResizable(true);
			dialog.setX(0);
			dialog.setY(0);

			BorderPane windowPane;
			BorderPane mainPane;
			ScrollPane scrollablePane;
			MenuBar menuBar;
			Button reportButton, imdbprofileButton;
			TextInputDialog reportDialog;

			windowPane = new BorderPane();
			windowPane.setPrefSize(420, 500);
			mainPane = new BorderPane();
			scrollablePane = new ScrollPane();
			detailsPhotoPane = new StackPane();
			menuBar = new MenuBar();
			reportButton = new Button("Report");
			imdbprofileButton = new Button("IMDB profile");

			windowPane.setStyle("-fx-background-color: cadetblue; "
					+ "-fx-font-style: italic;");


			reportButton.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00);" +
					"-fx-background-radius: 30;" +
					"-fx-background-insets: 0;" +
					"-fx-text-fill: white;" +
					"-fx-pref-height: 28px;" +
					"-fx-pref-width: 200px;");

			reportDialog = new TextInputDialog("");
			reportDialog.setTitle("Report actor's identity");
			reportDialog.setHeaderText("Insert new identity");
			reportDialog.setContentText("Please enter new name:");

			actorPane = new GridPane();
//		actorPane.setAlignment(Pos.CENTER);
			actorPane.setHgap(10);
			actorPane.setVgap(10);
			actorPane.setPadding(new Insets(25, 25, 25, 25));

			Label name = new Label("Name: ");
			Label birthdate = new Label("Birthdate: ");
			Label died = new Label("Deathdate: ");
			Label gender = new Label("Gender:");
			Label biography = new Label("Biography: ");
			Label imdbProfile = new Label("Imdb profile:");
			Label knownFrom = new Label("Known from:");
			setLabelCss(name);
			setLabelCss(birthdate);
			setLabelCss(died);
			setLabelCss(gender);
			setLabelCss(biography);
			setLabelCss(imdbProfile);
			setLabelCss(knownFrom);

			nameField = new Text();
			birthdateField = new Text();
			diedField = new Text();
			biographyField = new Text();
			genderField = new Text();

			actorPane.add(name, 0, 3);
			actorPane.add(nameField, 1,3);
			actorPane.add(gender, 0, 4);
			actorPane.add(genderField, 1, 4);
			actorPane.add(birthdate, 0,5);
			actorPane.add(birthdateField, 1,5);
			actorPane.add(died, 0,6);
			actorPane.add(diedField, 1,6);
			actorPane.add(biography, 0, 7);
			actorPane.add(biographyField, 0, 9, 2, 1);
			actorPane.add(knownFrom, 0, 10);
			actorPane.add(imdbProfile, 0, 14);
			actorPane.add(imdbprofileButton, 1, 14);

			actorPane.add(reportButton, 0, 16);

			imdbprofileButton.setOnAction(e -> {
				try {
					if (Runtime.getRuntime().exec(new String[] {"which", "xdg-open"}).getInputStream().read() != -1) {
						Runtime.getRuntime().exec(new String[] {"xdg-open", profileUrl});
					}
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			});

			reportButton.setOnAction(e -> {
				Optional<String> resultName = reportDialog.showAndWait();
				if (resultName.isPresent()) {
					SimpleActor actor = getActiveActor();
					if(actor != null) {
						Complaint complaint = new Complaint(resultName.get(), actor.getPos(), SessionData.getScreenshotPath());
						System.out.println("Suggestion: " + complaint.toString());
						Client.sendSuggestionToServer(complaint);
					}
					else {
						System.out.println("There's some kind of problem. We will fix it someday...");
					}
				}
			});
			mainPane.setTop(detailsPhotoPane);
			mainPane.setCenter(actorPane);
			Menu menuFile = new Menu("");
			menuBar.getMenus().addAll(menuFile);

			scrollablePane.setContent(mainPane);
			windowPane.setTop(menuBar);
			windowPane.setCenter(scrollablePane);
			BorderPane.setAlignment(menuBar, Pos.TOP_CENTER);
			BorderPane.setAlignment(scrollablePane, Pos.CENTER_RIGHT);
			setUserAgentStylesheet(STYLESHEET_MODENA);

			dialog.getDialogPane().setContent(windowPane);

			ButtonType button = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(button);
			dialog.showAndWait();
		});
	}

	private static void setLabelCss(Label label) {
		label.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
	}

	public static void loadActorDetails(String imdb_id) {
		Platform.runLater(() -> {
			try {
				Actor actorData = SessionData.getActorDetails(imdb_id);
				nameField.setText(actorData.getName());
				birthdateField.setText(actorData.getBirthday());
				diedField.setText(actorData.getDeathday());
				biographyField.setWrappingWidth(300);
				biographyField.setText(actorData.getBiography());
				genderField.setText(actorData.getGender());
				profileUrl = actorData.getImdb_profile();

				for(int i = 0, j = 11; i < 3 && i < actorData.getMovie_credits().size(); i++, j++) {
					ImageView poster = new ImageView(actorData.getMovie_credits().get(i).getPoster());
					poster.setFitWidth(120);
					poster.setPreserveRatio(true);
					actorPane.add(poster, 0, j);
				}

				ImageView photo = new ImageView(actorData.getImages().get(0));
				StackPane.setAlignment(photo, Pos.CENTER);
		//		photo.setFitHeight(100);
				photo.setFitWidth(120);
				photo.setPreserveRatio(true);
				detailsPhotoPane.getChildren().add(photo);
			} catch (Exception e) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				loadActorDetails(imdb_id);
			}
		});
	}

	private static SimpleActor getActiveActor() {
		String imdb = "";
		for(ActorRectangle actor : rectangles)
			if(actor.isActive())
				imdb = actor.getImdb();
		for(SimpleActor a : actors)
			if(a.getImdb_id().equals(imdb))
				return a;
		return null;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
