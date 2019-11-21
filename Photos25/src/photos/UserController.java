package photos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

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
import model.tag;
import javafx.scene.Node;
import java.util.Calendar;
import java.util.Collections;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

/**
 * Handles all the buttons and tasks done in the User Scene
 * @author Haris Rao and Justin Fuentes
 *
 */
public class UserController{
	//userScene fxml Stuff
	@FXML Button b_deleteAlbum;
	@FXML Button b_renameAlbum;
	@FXML Button b_openAlbum;
	@FXML Button b_createAlbum;
	@FXML Button b_createTag;
	@FXML Button b_logout;
	@FXML Button b_quit;
	@FXML Button b_searchTag;
	@FXML Button b_createAlbumByTag;
	@FXML TextField tf_searchAlbum;
	@FXML ListView<String> lv_albums;
	@FXML ListView<String> lv_tags;
	
	private static ObservableList <String> obsAlbumList;
	private static ObservableList <String> obsTagsList;
	//ArrayList<String> albumsForObs = new ArrayList<String>();
	album albumToDelete;
	
	user user;
	
	Stage window;
	/**
	 * Transfers the necessary data from the previous Login Scene to the User scene
	 * @param currentUser User that is logged in
	 * @param currentWindow Has Current Window
	 */
	public void transferUser(user currentUser, Stage currentWindow) {
		user = currentUser;
		window = currentWindow;
		if(user.getAlbums().size() == 0)
		{
			ArrayList<photo> photos = new ArrayList<photo>();
			//File file = new File("./stockPhotos/stock1.jpg");
			//Image abc = new Image(file.toURI().toString());
			//Calendar date = Calendar.getInstance();
			//photos.add(new photo(abc, date));
			//user.getAlbums().add(new album("album1", photos));
		}
		LoadList(user);
		LoadTags(user);
	}
	
	public ArrayList<String> getAlbumNames(ArrayList<album> albums){
		ArrayList<String> albumNameList = new ArrayList<String>();
		for (int i = 0; i < albums.size(); i++) {
			albumNameList.add(user.getAlbums().get(i).getName());
		}
		return albumNameList;
	}
	
	public ArrayList<String> getTagNames(ArrayList<tag> tags){
		ArrayList<String> tagNameList = new ArrayList<String>();
		for (int i = 0; i < tags.size(); i++) {
			tagNameList.add(user.getTags().get(i).getName());
		}
		return tagNameList;
	}
	
	/**
	 * Loads the list of Albums for the User
	 * @param user Current user logged in
	 */
	public void LoadList(user user) {
		obsAlbumList = FXCollections.observableArrayList(getAlbumNames(user.getAlbums()));
		lv_albums.setItems(null);
		lv_albums.setItems(obsAlbumList);
		lv_albums.getSelectionModel().select(0);
    	showItem();
    	lv_albums.getSelectionModel().select(lv_albums.getSelectionModel().getSelectedIndex());
    	lv_albums.getSelectionModel().selectedIndexProperty().addListener((obsAlbumList,oldVal,newVal)-> showItem());
	}
	
	public void LoadTags(user user) {
		obsTagsList = FXCollections.observableArrayList(getTagNames(user.getTags()));
		lv_tags.setItems(null);
		lv_tags.setItems(obsTagsList);
		lv_tags.getSelectionModel().select(0);
    	showItem();
    	lv_tags.getSelectionModel().select(lv_tags.getSelectionModel().getSelectedIndex());
    	lv_tags.getSelectionModel().selectedIndexProperty().addListener((obsTagsList,oldVal,newVal)-> showItem());
	}
	
	private void showItem(){};
	
	/**
	 * Serializes the user object
	 * @param user Current user logged in
	 */
	private void serialize(user user) {	
		String filename = "./Userdata/"+ user.getUsername() +".ser";
		FileOutputStream fos = null;
		ObjectOutputStream outputstream = null;
		try {
			//System.out.println("Serializing User :" + userN);
			fos = new FileOutputStream (filename);
			outputstream = new ObjectOutputStream(fos);
			outputstream.writeObject(user);
			outputstream.close();
		}catch (Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	/**
	 * Deserializes the User data for the current user
	 * @param username Username of the current user logged in
	 * @return Return the user object after reading all the data from the serialized file.
	 */
	private user deserialize(String username) {	
		/// Deserializing user serialized data
		File file = null;
		String filePath = "./Userdata/"+ username +".ser";
		FileInputStream fis = null;
		ObjectInputStream inputstream = null; 
		try {
			//System.out.println("Deserializing User :" + username);
			file = new File(filePath);
			if (!file.exists()) {
				serialize(new user(username));
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
	 * Handles the logout button on the User Scene and safely logs the user out after serializing their data
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_logoutHandler (ActionEvent event) throws IOException {	
		System.out.println(user.getUsername());
		serialize(user);	
		Parent loginroot = FXMLLoader.load(getClass().getClassLoader().getResource("./photos/loginScene.fxml"));
		Scene loginScene = new Scene(loginroot);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.show();
	}

	/**
	 * Handles the quit button on the User Scene and Safely closes the application after serializing their data.
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_quitHandler (ActionEvent event) throws IOException {
		serialize(user);
		Platform.exit();
		System.exit(0);
	}
	
	/**
	 * Handles the delete album button on the User Scene and deletes the album that is selected in the List View. If no album is selected it prompts an alert asking to select an album
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_deleteAlbumHandler (ActionEvent event) throws IOException {
		if(user.getAlbums().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("There are no more albums");
			alert.showAndWait();
			
		}else {
			if(lv_albums.getSelectionModel().getSelectedIndex()==-1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Please select an album to delete");
				alert.showAndWait();
			}else {
				System.out.println("In delete album handle");
				int index = lv_albums.getSelectionModel().getSelectedIndex();
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to delete the following Album: ");
				alert.setContentText(user.getAlbums().get(index).getName());
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK) {
					user.getAlbums().remove(index);
					user.getAlbums().sort(Comparator.comparing(album::getName, String.CASE_INSENSITIVE_ORDER));
					obsAlbumList = FXCollections.observableArrayList(getAlbumNames(user.getAlbums()));
					lv_albums.setItems(FXCollections.observableArrayList(obsAlbumList)); 
				}
			}
		}
	}
	
	/**
	 * Handles the create album button on the User Scene and creates a new Album for the User
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_createAlbumHandler (ActionEvent event) throws IOException {
		ArrayList<String> albumNames = getAlbumNames(user.getAlbums());
		TextInputDialog albumDi = new TextInputDialog();
		albumDi.setHeaderText("Create new Album");
		albumDi.setContentText("Enter Album name: ");
		Optional<String> albumName = albumDi.showAndWait();
		if(albumName.isEmpty()) {
			return;
		}
		else if(albumNames.contains(albumName.get())) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("This album already exists");
			alert.showAndWait();
		}
		else {
			user.getAlbums().add(new album(albumName.get(), new ArrayList<photo>()));
			user.getAlbums().sort(Comparator.comparing(album::getName, String.CASE_INSENSITIVE_ORDER));
			obsAlbumList = FXCollections.observableArrayList(getAlbumNames(user.getAlbums()));
			lv_albums.setItems(FXCollections.observableArrayList(obsAlbumList));
		}
	}
	
	/**
	 * Handles the rename album button on the User scene and lets the user rename the selected album from the list view. If no album is selected the user is prompted with an alert asking to select an album
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_renameAlbumHandler (ActionEvent event) throws IOException {
		if(user.getAlbums().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("There are no more albums");
			alert.showAndWait();
			
		}else {
			if(lv_albums.getSelectionModel().getSelectedIndex()==-1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Please select an album to rename");
				alert.showAndWait();
			}else {
				System.out.println("In rename album handle");
				int index = lv_albums.getSelectionModel().getSelectedIndex();
				TextInputDialog albumDi = new TextInputDialog();
				albumDi.setHeaderText("Rename album");
				albumDi.setContentText("Enter album name: ");
				Optional<String> albumName = albumDi.showAndWait();
				if(!albumName.isEmpty()) {
					user.getAlbums().get(index).setName(albumName.get());
					user.getAlbums().sort(Comparator.comparing(album::getName, String.CASE_INSENSITIVE_ORDER));
					obsAlbumList = FXCollections.observableArrayList(getAlbumNames(user.getAlbums()));
					lv_albums.setItems(FXCollections.observableArrayList(obsAlbumList)); 
				}
			}
		}
	}
	
	/**
	 * Handles the Open Album button and opens the Album that is selected from the list view. If no album is selected the user is prompted with an alert asking to select an album. If an Album is selected it switches the Scenes to the Album display Scene
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_openAlbumHandler (ActionEvent event) throws IOException {
		if(user.getAlbums().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("There are no more albums");
			alert.showAndWait();
		}else {
			if(lv_albums.getSelectionModel().getSelectedIndex()==-1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Please select an album to open");
				alert.showAndWait();
			}else {
				System.out.println("In open album handle");
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/albumScene.fxml"));
				//System.out.println("debug");
		        Parent root = loader.load();
		        AlbumController albumController = loader.getController();
		        albumController.transferAlbum(user.getAlbums().get(lv_albums.getSelectionModel().getSelectedIndex()), window,user);
		        Scene albumScene = new Scene(root);
				Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		        window.setScene(albumScene);
				window.show();
			}
		}
	}
	
	@FXML
	public void b_createTagHandler (ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/createTagScene.fxml"));
			Scene scene = new Scene(loader.load());
	        CreateTagController createTagController = loader.getController();
	        createTagController.transferUser(user);
	        Stage window = new Stage();
	        window.setTitle("Create new tag");
	        window.setScene(scene);
	        window.showAndWait();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
}
