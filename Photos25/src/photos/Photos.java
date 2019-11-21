package photos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.album;
import model.photo;
import model.user;

/**
 * Holds the Main method of the Application that Launches the GUI
 * @author Haris Rao and Justin Fuentes
 *
 */
public class Photos extends Application{
	
	/**
	 * Sets the Stage for the initial Login Scene and Initializes it.
	 */
	@Override
	public void start (Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/photos/loginScene.fxml"));
		Parent root = (Parent) loader.load();
		LoginController controller = loader.getController();
		controller.start(primaryStage);
		primaryStage.setTitle("Photos");
		primaryStage.setScene(new Scene(root, 600,400));
		primaryStage.show();
	}
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main (String [] args){
		launch(args);
		
		
		
	}
	
	
}
