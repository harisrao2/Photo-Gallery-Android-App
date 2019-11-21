package photos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.admin;
import model.photo;
import model.user;
import model.album;

public class PhotoController {
	@FXML ImageView iV_image;
	@FXML Button b_logout;
	@FXML Button b_quit;
	@FXML Button b_back;
	@FXML TextArea tA_details;
	
	photo photo;
	Stage window;
	user user;
	album album;
	
	/**
	 * Displays the details of the photo Opened
	 * @param photo photos object that is opened
	 */
	public void LoadDetails(photo photo) {
		tA_details.clear();
		tA_details.appendText("Caption: \n" + photo.getName() + "\n\nDate: \n" + photo.getDate());	
		
		tA_details.appendText("\n\nTag: \n\t");
		
		System.out.println("photo.getag :" + photo.getTags());
		System.out.println("photo.getag000000 :" + photo.getTags().get(0).getElements());
		for (int i = 0 ;i<photo.getTags().size();i++) {
			/*tA_details.appendText(photo.getTags().get(i).getName() +" :\n\t");
			System.out.println("Element Size :" + photo.getTags().get(i).getElements().size());
			System.out.println("photogetTag.geti :" + photo.getTags().get(i));
			System.out.println("photogetTag.getielement :" + photo.getTags().get(i).getElements());
			System.out.println("photo.getag :" + photo.getTags());*/
			
			for (int j = 0;j<photo.getTags().get(i).getElements().size();j++) {
				tA_details.appendText(photo.getTags().get(i).getElements().get(j) + "\n");
			}
		}
	}
	
	/** 
	 * Serializes the user object for a specific user.
	 * @param user the user object for the specific user.
	 */
	private void serialize(user user) {	
		String filename = "./Userdata/"+ user.getUsername() +".ser";
		FileOutputStream fos = null;
		ObjectOutputStream outputstream = null;
		try {
			fos = new FileOutputStream (filename);
			outputstream = new ObjectOutputStream(fos);
			outputstream.writeObject(user);
			outputstream.close();
		}catch (Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	/**
	 * Button handler for the back button on the Photos Scene, loads the previous scene which is the Album Display scene
	 * @param event captures on Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_backHandler(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/albumScene.fxml"));
		Parent root;
		root = loader.load();
		AlbumController albumController = loader.getController();
		albumController.transferAlbum(album, window, user);
		Scene userScene = new Scene(root);
		window.setScene(userScene);
		window.show();
	}
	
	/**
	 * Button handler for the quit button on the Photo Display Scene, safely exits the Application and serializes the user data.
	 * @param event captures on Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_quitHandler (ActionEvent event) throws IOException {
		serialize(user);
		Platform.exit();
		System.exit(0);
	}
	
	/**
	 * Button handler for the logout button on the Photo Display Scene, safely logs the user out and serialized the user data.
	 * @param event captures on Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_logoutHandler(ActionEvent event) throws IOException {
		serialize(user);	
		
		Parent loginroot = FXMLLoader.load(getClass().getClassLoader().getResource("./photos/loginScene.fxml"));
		Scene loginScene = new Scene(loginroot);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(loginScene);
		window.show();
	}
	
	/**
	 * Transfers the appropriate variables from the Album Scene to the Display Photo Scene.
	 * @param currentPhoto photo object for the specific photo that was selected and opened.
	 * @param user user object for the specific user that is logged in
	 * @param album album object for the specific albbum that is opened
	 * @param window current stage information
	 */
	public void transferPhoto(photo currentPhoto, user user, album album, Stage window) {
		photo = currentPhoto;
		this.user = user;
		this.album = album;
		this.window = window;
		iV_image.setImage(ByteArrayToImage(photo.getImage(), photo.getType()));
		LoadDetails(photo);
	}
	
	/**
	 * turns Non serializable Image object to a serializable byte array and returns the byte array
	 * @param image the image object to be converted to byte array
	 * @param type type of the image, either jpg or png
	 * @return serializable byte array
	 */
	byte [] ImageToByteArray(Image image, String type) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			ImageIO.write(SwingFXUtils.fromFXImage(image,  null), type, oos);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
	/**
	 * Turns the byte array to the Image Object and returns that Image
	 * @param byteImage byte array of the Image
	 * @param type type of the image either png or jpg
	 * @return image 
	 */
	Image ByteArrayToImage(byte [] byteImage, String type) {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteImage);
		ObjectInputStream ios;
		Image image = null;
		try {
			ios = new ObjectInputStream(bis);
			//ios.defaultReadObject();
			BufferedImage buffImage = ImageIO.read(ios);
			image = SwingFXUtils.toFXImage(buffImage, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
}
