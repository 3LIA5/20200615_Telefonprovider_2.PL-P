package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Main extends Application
{
	public void start(Stage primaryStage)
	{
		try
		{
			Scene scene = new Scene(new RootBorderPane(), 600, 250);
			primaryStage.setTitle("Testing TurboTel");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e)
		{
			showAlert(AlertType.ERROR, e.getMessage());
		}
	}

	public static void showAlert(AlertType alertType, String message)
	{
		Alert alert = new Alert(alertType, message, ButtonType.OK);
		alert.setTitle("Achtung!!");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	public static void main(String[] args)
	{
		launch(args);
	}
}
