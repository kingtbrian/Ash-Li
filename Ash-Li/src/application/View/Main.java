package application.View;

import application.Control.PrimaryController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Primary entry point to application. Program begins by issuing control
 * to the PrimaryController which will show the PrimaryView
 * 
 * @see PrimaryController
 * @see PrimaryView
 * @author brian
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		PrimaryController primaryControl = new PrimaryController(primaryStage);
		primaryControl.showPrimaryView();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
