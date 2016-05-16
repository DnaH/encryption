package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainWindow extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainWindow.class.getResource("Appearance.fxml"));
			AnchorPane root = loader.load();
			Scene mainScene = new Scene(root);
			primaryStage.setScene(mainScene);
			primaryStage.show();
		}
		catch(Exception e)
		{
			
		}
		
		
	}

	public static void main(String[] args)
	{
		Application.launch(args);
	}

}
