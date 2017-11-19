package gui.mainWindow;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Actor;
import main.Main;

/**
 * open in browser (for imdb "More info")
 */
//	try {
//		String s = "http://www.google.com";
//		Desktop desktop = Desktop.getDesktop();
//		desktop.browse(URI.create(s));
//		} catch (IOException e) {
//		e.printStackTrace();
//	}

/**
 * Main GUI class with JavaFX components.
  */
public class GUI extends Application {
	private Stage stage;
	private Scene scene;
	private static BorderPane mainPane;
	private static ScrollPane rightPane;
	private static GridPane actorPane;
	private static GridPane imagePane;
	private static MenuBar menuBar;

	//Actor fields
	private static Text nameField, birthdateField, biographyField, diedField, genderField;

	public void start(Stage primaryStage) {
		Main mainThread = new Main();
		mainThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		stage = primaryStage;
		primaryStage.setTitle("JavaFX GG");

		mainPane = new BorderPane();
		mainPane.setPrefSize(1000, 470);
		rightPane = new ScrollPane();
		imagePane = new GridPane();
		menuBar = new MenuBar();

		setActorPane();
		setMenuBar();
		rightPane.setContent(actorPane);
		mainPane.setTop(menuBar);
		mainPane.setCenter(imagePane);
		mainPane.setRight(rightPane);
		BorderPane.setAlignment(menuBar, Pos.TOP_CENTER);
		BorderPane.setAlignment(imagePane, Pos.CENTER_LEFT);
		BorderPane.setAlignment(rightPane, Pos.CENTER_RIGHT);
		setUserAgentStylesheet(STYLESHEET_MODENA);

		System.out.println("Created");

		scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX GG");
		primaryStage.show();
	}


	private static void setMenuBar() {
		Menu menuFile = new Menu("File");
		Menu menuEdit = new Menu("Edit");
		Menu menuView = new Menu("View");
		menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
	}

	private static void setActorPane() {
		actorPane = new GridPane();
//		actorPane.setAlignment(Pos.CENTER);
		actorPane.setHgap(10);
		actorPane.setVgap(10);
		actorPane.setPadding(new Insets(25, 25, 25, 25));

//		Text scenetitle = new Text("Java GG");
//		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//		actorPane.add(scenetitle, 0, 0, 2, 1);

		Label name = new Label("Name: ");
		Label birthdate = new Label("Birthdate: ");
		Label died = new Label("Died: ");
		Label gender = new Label("Gender");
		Label biography = new Label("Biography: ");

		nameField = new Text();
		birthdateField = new Text();
		diedField = new Text();
		biographyField = new Text();
		genderField = new Text();

		actorPane.add(name, 0, 3);
		actorPane.add(nameField, 1,3);
		actorPane.add(birthdate, 0,4);
		actorPane.add(birthdateField, 1,4);
		actorPane.add(died, 0,5);
		actorPane.add(diedField, 1,5);
		actorPane.add(biography, 0, 6);
		actorPane.add(biographyField, 0, 7);

		actorPane.add(gender, 0, 8);
		actorPane.add(genderField, 1, 8);
	}

	public static void setScreenshot(String screenPath) {
		ImageView image = new ImageView(screenPath);
//		image.setPreserveRatio(true);
		image.fitWidthProperty().bind(imagePane.widthProperty());
		image.fitHeightProperty().bind(imagePane.heightProperty());
		Platform.runLater(() -> imagePane.add(image, 0, 0));
	}

	public static void loadActor(Actor actorData) {
		nameField.setText(actorData.getName());
		birthdateField.setText(actorData.getBirthday());
		diedField.setText(actorData.getDeathday());
		biographyField.setText("Problem with text wrapping. Fix soon");
		genderField.setText(actorData.getGender());
		System.out.println(actorData.getImdb_profile());

		ImageView photo = new ImageView(actorData.getImages().get(0));
//		photo.setFitHeight(100);
		photo.setFitWidth(250);
		photo.setPreserveRatio(true);
		Platform.runLater(() -> actorPane.add(photo, 0, 0));
	}

	public static void main(String[] args) {
		launch(args);
	}
}
