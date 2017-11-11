package gui.mainWindow;

import javafx.application.Application;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

public class GUI extends Application {
	private Stage stage;
	private Scene scene;
	private BorderPane mainPane;
	private ScrollPane rightPane;
	private GridPane actorPane;
	private Pane imagePane;
	private MenuBar menuBar;

	public void start(Stage primaryStage) {
		stage = primaryStage;
		primaryStage.setTitle("JavaFX GG");

		mainPane = new BorderPane();
		mainPane.setPrefSize(1000, 470);
		rightPane = new ScrollPane();
		imagePane = new Pane();
		menuBar = new MenuBar();

		setMenuBar();
		setImagePane();
		setActorPane();
		setRightPane();

		mainPane.setTop(menuBar);
		mainPane.setCenter(imagePane);
		mainPane.setRight(rightPane);
		BorderPane.setAlignment(menuBar, Pos.TOP_CENTER);
		BorderPane.setAlignment(imagePane, Pos.CENTER_LEFT);
		BorderPane.setAlignment(rightPane, Pos.CENTER_RIGHT);

		setUserAgentStylesheet(STYLESHEET_MODENA);

		scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX GG");
		primaryStage.show();
	}

	private void setRightPane() {
//		rightPane.setFitToWidth(true);
		rightPane.setMaxWidth(350);
		ImageView photo = new ImageView("File:images/photo.jpg");
		photo.setFitHeight(100);
		photo.setFitWidth(100);
		//leave it commented, oth - image proportions will be respected
		photo.setPreserveRatio(true);
//		photo.fitWidthProperty().bind(imagePane.widthProperty());
//		photo.fitHeightProperty().bind(imagePane.heightProperty());
//		rightPane.setContent(photo);
		rightPane.setContent(actorPane);
	}

	private void setMenuBar() {
		Menu menuFile = new Menu("File");
		Menu menuEdit = new Menu("Edit");
		Menu menuView = new Menu("View");
		menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
	}

	private void setImagePane() {
		ImageView image = new ImageView("File:screens/20171106_02_11_11_PM.png");
		//leave it commented, oth - image proportions will be respected
//		image.setPreserveRatio(true);
		image.fitWidthProperty().bind(imagePane.widthProperty());
		image.fitHeightProperty().bind(imagePane.heightProperty());
		imagePane.getChildren().addAll(image);
	}

	private void setActorPane() {
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
		Label biography = new Label("Biography: ");
		Label knownFrom = new Label("Known from: ");

		Text nameField = new Text("Alan Sidney Patrick Rickman bla bla bla");
		Text birthdateField = new Text("21 February 1946");
		Text diedField = new Text("14 January 2016 (aged 69)");
		Text biographyField = new Text("Alan Rickman was born on a council " +
				"estate in Acton, West London, to Margaret Doreen Rose (Bartlett) " +
				"and Bernard Rickman, who worked at a factory. He had English, Irish, " +
				"and Welsh ancestry. Alan had an older brother David, a younger brother Michael " +
				"and a younger sister Sheila. When Alan was 8 years old, his father died. He attended " +
				"Latymer Upper School on a scholarship. He studied Graphic Design at Chelsea College of Art " +
				"and Design, where he met Rima Horton, who would later become his life partner. After three " +
				"years at Chelsea College, Rickman did graduate studies at the Royal College of Art. He " +
				"opened a successful graphics design business, Graphiti, with friends and ran it for several " +
				"years before his love of theatre led him to seek an audition with the Royal Academy of Dramatic " +
				"Art (RADA). At the relatively late age of 26, Rickman received a scholarship to RADA, which" +
				" started a professional acting career that has lasted nearly 40 years, a career which has spanned " +
				"stage, screen and television and lapped over into directing, as well.");
//		biographyField.maxWidth(100);
		nameField.autosize();
		actorPane.add(name, 0, 3);
		actorPane.add(nameField, 1,3);
		actorPane.add(birthdate, 0,4);
		actorPane.add(birthdateField, 1,4);
		actorPane.add(died, 0,5);
		actorPane.add(diedField, 1,5);
		actorPane.add(biography, 0, 6);
//		actorPane.add(biographyField, 0, 7);
		actorPane.add(knownFrom, 0, 8);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
