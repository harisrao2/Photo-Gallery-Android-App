package photos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Collections;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.admin;
import model.user;
import model.album;
import model.photo;
import javafx.scene.Node;
import java.util.Calendar;
import javafx.scene.image.Image;

/**
 * Handles all the buttons and tasks done in the Login Screen
 * @author Haris Rao and Justin Fuentes
 *
 */
public class LoginController{
	
	// loginScreen fxml Stuff
	@FXML Button b_login;
	@FXML TextField tf_login;
	@FXML Button b_logout;
	
	Stage window;
	
	/**
	 * Start function of the initial Login screen of this application
	 * @param mainStage current stage 
	 * @throws FileNotFoundException
	 */
	
	public void start(Stage mainStage)throws FileNotFoundException{
		window = mainStage;
		// login button handle
		b_login.setOnAction(arg0 -> {
			try {
				b_loginHandle(arg0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});	
		
	}
	
	/**
	 * Button Handler for the login button on the Login Page, checks if an admin if logging in or an existing user is logging in
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_loginHandle(ActionEvent event) throws IOException {
		boolean userexists = false;
		admin admin = deserializeAdmin();
		System.out.println("in Login handle");
		/*if(admin.getUsers().isEmpty()) {
			admin.getUsers().add(new user ("stock")); // Setting stock albums to numm for now
		}*/
		String input = tf_login.getText();
		if(input.equals("admin")){ // if admin is logging in
			loginToAdmin(event);
			userexists = true;
		}else {
			if (admin.getUsers().contains(input))
			{
				loginToUser(event, input);
				userexists = true;
			}
		}
		if(userexists ==false) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Enter a valid username");
			alert.showAndWait();
		}
	}
	
	/**
	 * Deserializes the admin.ser file to retain all the information needed for the Admin scene 
	 * @return the admin object
	 */
	private admin deserializeAdmin() {
		/// Deserializing admin serialized data
		File file = null;
		String filePath = "./Admindata/admin.ser";
		FileInputStream fis = null;
		ObjectInputStream inputstream = null; 
		try {
			System.out.println("Deserializing Admin");
			file = new File(filePath);
			if (!file.exists()) {
				return new admin(new ArrayList<String>());
			}
			else{
				fis = new FileInputStream(filePath);
				inputstream = new ObjectInputStream(fis);
				
				admin admin = (admin) inputstream.readObject();
				inputstream.close();
				return admin;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Deserializes the user.ser file for the corresponding user to load the Application data for that user.
	 * @param username username of the user
	 * @return the user object
	 */
	private user deserializeUser(String username) {	
		/// Deserializing user serialized data
		File file = null;
		String filePath = "./Userdata/"+ username +".ser";
		FileInputStream fis = null;
		ObjectInputStream inputstream = null; 
		try {
			//System.out.println("Deserializing User :" + username);
			file = new File(filePath);
			if (!file.exists()) {
				return new user(username);
			}
			else {
				fis = new FileInputStream(filePath);
				inputstream = new ObjectInputStream(fis);
				user user = (user) inputstream.readObject();
				inputstream.close();
				return user;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Function that switches scenes to the Admin scene when an admin is logged on.
	 * @param event captured On Action event of the Login Button
	 * @throws IOException
	 */
	public void loginToAdmin (ActionEvent event) throws IOException {
		Parent adminroot = FXMLLoader.load(getClass().getClassLoader().getResource("./photos/adminScene.fxml"));
		Scene adminScene = new Scene(adminroot);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(adminScene);
		window.show();
	}
	
	/**
	 * Function that switches scenes to the User scene when a user is logged on.
	 * @param event captured On Action event of the Login Button
	 * @param username username of the user
	 * @throws IOException
	 */
	public void loginToUser (ActionEvent event, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/userScene.fxml"));
        Parent root = loader.load();
        UserController userController = loader.getController();
        Scene userScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        user user = deserializeUser(username);
        userController.transferUser(user, window);
        
        window.setScene(userScene);
		window.show();
	}
}
