package photos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.admin;
import model.user;


/**
 * Handles all the buttons and tasks done in the Admin Scene
 * @author Haris Rao & Justin Fuentes
 *
 *This is the Admin Controller that controls the Admin scene for this Photos Assignment. It contains all FXML objects used in the admin scene along with all the button handlers 
 */

public class AdminController {
	//adminScene fxml Stuff
	@FXML Button b_listuser;
	@FXML Button b_newuser;
	@FXML Button b_deleteuser;
	@FXML ListView<String> lv_users;
	
	private static ObservableList <String> obsUserList;
	ArrayList<String> userForObs = new ArrayList<String>();
	
	static admin admin;
	
	/**
	 * Deserializes the admin object
	 */
	public void initialize() {
		admin = deserialize();
		if (admin == null)
		{
			admin = deserialize();
		}
	}
	
	/**
	 * Button Handler for the List User Button in the Admin Scene, This loads all the names of the users in the List view, when pressed.
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_listuserHandle(ActionEvent event) throws IOException {
		obsUserList = FXCollections.observableArrayList(admin.getUsers());
		lv_users.setItems(obsUserList); 
		lv_users.getSelectionModel().select(0);
    	showItem();
    	lv_users.getSelectionModel().select(lv_users.getSelectionModel().getSelectedIndex());
    	lv_users.getSelectionModel().selectedIndexProperty().addListener((obsUserList,oldVal,newVal)-> showItem());
	}
	
	private void showItem(){};
	
	/**
	 * Button Handler for the New User Button in the Admin Scene, This creates a New user with the same provided, Checks for duplicate users and if the user and admin creation.
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML 
	public void b_newuserHandle(ActionEvent event) throws IOException {
		TextInputDialog usernameDi = new TextInputDialog();
		usernameDi.setHeaderText("Create new User");
		usernameDi.setContentText("Enter username: ");
		Optional<String> username = usernameDi.showAndWait();
		if(username.isEmpty()) {
			return;
		}
		else if(admin.getUsers().contains(username.get())) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("This user already exists");
			alert.showAndWait();
		}
		else if(username.get().contentEquals("admin")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Cannot create user named: " + username.get());
			alert.showAndWait();
		}
		else {
			admin.getUsers().add(username.get());
			Collections.sort(admin.getUsers());
		}
		lv_users.setItems(FXCollections.observableArrayList(admin.getUsers())); 
	}
	
	/**
	 * Button Handler for the Delete User Button in the Admin Scene, This deletes a user that is selected from the List view. If No user is selected while the button is pressed, it prompts the User with an alert message.
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_deleteuserHandle (ActionEvent event) throws IOException{
		if(admin.getUsers().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("There are no more users");
			alert.showAndWait();
			
		}else {
			if(lv_users.getSelectionModel().getSelectedIndex()==-1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Please select a user to delete");
				alert.showAndWait();
			}else {
				System.out.println("In delete user handle");
				int index = lv_users.getSelectionModel().getSelectedIndex();
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to delete the following User: ");
				alert.setContentText(admin.getUsers().get(index));
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK) {
					String filePath = "./Userdata/" + admin.getUsers().get(index) + ".ser";
					File file = new File(filePath);
					file.delete();
					admin.getUsers().remove(index);
					Collections.sort(admin.getUsers());
					lv_users.setItems(FXCollections.observableArrayList(admin.getUsers())); 
				}
			}
		}
	}
	
	/**
	 * Button Handler for the Logout Button in the Admin Scene, This logs the admin out safely, meaning it will serialize the data and return to the Login Screen.
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_logoutHandler (ActionEvent event) throws IOException {		
		serialize();	
		Parent loginroot = FXMLLoader.load(getClass().getClassLoader().getResource("./photos/loginScene.fxml"));
		Scene loginScene = new Scene(loginroot);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.show();
	}

	/**
	 * Button Handler for the Quit Button in the Admin Scene, This closes the application safely, meaning it will serialize the data and close the application window.
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_quitHandler (ActionEvent event) throws IOException {
		
		serialize();
		Platform.exit();
		System.exit(0);
	}
	
	/**
	 * This function serializes the data resulting in a file called admin.ser in the Admindata folder.
	 */
	private void serialize() {
		//When the logout button is Pressed, this will Serialize the admin object
		String filename = "./Admindata/admin.ser";
		FileOutputStream fos = null;
		ObjectOutputStream outputstream = null;
		try {
			System.out.println("Serializing Admin");
			fos = new FileOutputStream (filename);
			outputstream = new ObjectOutputStream(fos);
			System.out.println("admin in serializez: " + admin);
			outputstream.writeObject(admin);
			outputstream.close();
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This deserializes the admin.ser file in the Admindata folder and returns it as an admin object.
	 * @return the admin object
	 */
	private admin deserialize() {
		/// Deserializing admin serialized data
		File file = null;
		String filePath = "./Admindata/admin.ser";
		FileInputStream fis = null;
		ObjectInputStream inputstream = null; 
		try {
			System.out.println("Deserializing Admin");
			file = new File(filePath);
			if (!file.exists()) {
				admin = new admin(new ArrayList<String>());
				serialize();
			}
			else{
				fis = new FileInputStream(filePath);
				inputstream = new ObjectInputStream(fis);
				
				admin = (admin) inputstream.readObject();
				inputstream.close();
				return admin;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
