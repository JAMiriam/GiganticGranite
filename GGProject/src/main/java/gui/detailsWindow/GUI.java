package gui.detailsWindow;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Actor;
import main.Main;

import java.util.Optional;

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
	private static BorderPane windowPane;
	private static BorderPane mainPane;
	private static ScrollPane scrollablePane;
	private static GridPane actorPane;
	private static StackPane photoPane;
	private static MenuBar menuBar;

	//Actor fields
	private static Text nameField, birthdateField, biographyField, diedField, genderField;
	private static Button reportButton;
	private static TextInputDialog dialog;

	public void start(Stage primaryStage) {
		Main mainThread = new Main();
//		mainThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		stage = primaryStage;
		primaryStage.setTitle("JavaFX GG");



		windowPane = new BorderPane();
		windowPane.setPrefSize(400, 500);
		mainPane = new BorderPane();
		scrollablePane = new ScrollPane();
		photoPane = new StackPane();
		menuBar = new MenuBar();

		windowPane.setStyle("-fx-background-color: cadetblue; "
				+ "-fx-font-style: italic;");

		setOtherComponents();
		setActorPane();
		setMainPane();
		setMenuBar();

		scrollablePane.setContent(mainPane);
		windowPane.setTop(menuBar);
		windowPane.setCenter(scrollablePane);
		BorderPane.setAlignment(menuBar, Pos.TOP_CENTER);
		BorderPane.setAlignment(scrollablePane, Pos.CENTER_RIGHT);
		setUserAgentStylesheet(STYLESHEET_MODENA);

		System.out.println("Created");

		scene = new Scene(windowPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX GG");
		primaryStage.show();
	}

	private void setMainPane() {
		mainPane.setTop(photoPane);
		mainPane.setCenter(actorPane);
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

		Label name = new Label("Name: ");
		Label birthdate = new Label("Birthdate: ");
		Label died = new Label("Deathdate: ");
		Label gender = new Label("Gender:");
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

		actorPane.add(reportButton, 0, 9);
		reportButton.setOnAction(e -> {
			Optional<String> result = dialog.showAndWait();
			if(result.isPresent()) {
				System.out.println("New identity sent.");
				//TODO send json to server with new data
			}

//		result.ifPresent(name -> System.out.println("New identity: " + name));
		});
	}

	public static void loadActor(Actor actorData) {
		nameField.setText(actorData.getName());
		birthdateField.setText(actorData.getBirthday());
		diedField.setText(actorData.getDeathday());
		biographyField.setText("Problem with text wrapping. Fix soon");
//		biographyField.setText(actorData.getBiography());
		genderField.setText(actorData.getGender());
		System.out.println(actorData.getImdb_profile());

		ImageView photo = new ImageView(actorData.getImages().get(0));
		StackPane.setAlignment(photo, Pos.CENTER);
//		photo.setFitHeight(100);
		photo.setFitWidth(120);
		photo.setPreserveRatio(true);
		Platform.runLater(() -> photoPane.getChildren().add(photo));
	}

	// init button, dialog window
	private static void setOtherComponents() {
		reportButton = new Button("Report");
		reportButton.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00);" +
				"-fx-background-radius: 30;" +
				"-fx-background-insets: 0;" +
				"-fx-text-fill: white;" +
				"-fx-pref-height: 28px;" +
				"-fx-pref-width: 200px;");

		dialog = new TextInputDialog("");
		dialog.setTitle("Report actor's identity");
		dialog.setHeaderText("Insert new identity");
		dialog.setContentText("Please enter new name:");
	}

	public static void main(String[] args) {
		launch(args);
	}
}
