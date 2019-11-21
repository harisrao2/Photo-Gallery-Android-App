package photos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
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
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
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

/**
 * Handles all the buttons and tasks done on the User Scene that is displayed after opening an album in the User scene.
 * @author Haris Rao and Justin Fuentes
 *
 */
public class AlbumController {
	@FXML TilePane tp_photos;
	@FXML Button b_removePhoto;
	@FXML Button b_captionPhoto;
	@FXML Button b_openPhoto;
	@FXML Button b_addPhoto;
	@FXML Button b_back;
	@FXML Button b_logout;
	@FXML Button b_quit;
	@FXML Button b_slideshow;
	@FXML Button b_copyPhoto;
	@FXML Button b_movePhoto;
	@FXML Button b_addTag;
	@FXML Button b_deleteTag;
	
	static album album;
	user user;
	//photo photo;
	
	int selectedIndex;
	
	public void initialize() {
		selectedIndex = -1;
	}
	
	
	Stage window;
	
	class pImageView
	{
		public ImageView imageView;
		public photo photo;
		public pImageView(ImageView imageView, photo photo) {
			this.imageView = imageView;
			this.photo = photo;
		}
	}
	
	ArrayList<pImageView> albumWithImageViews;
	
	
	
	private static ObservableList <ImageView> obsPhotoList;
	
	@FXML
	public void b_slideshowHandler(ActionEvent event) throws IOException {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader();
			//root = FXMLLoader.load(getClass().getClassLoader().getResource("/photos/copyScene.fxml"));
			loader.setLocation(getClass().getResource("/photos/slideshow.fxml"));
			Scene scene = new Scene(loader.load(),600,400);
			
			SlideshowController slideshowController = loader.getController();
			
		
			slideshowController.transferPhoto(user, album, window);

			
			Stage stage = new Stage();
			stage.setTitle("SlideShow");
			stage.setScene(scene);
			stage.show();
			
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void b_copyPhotoHandler(ActionEvent event) throws IOException {
		if (album.getPhotos().size() == 0 || selectedIndex == -1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No photo selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a photo to copy");
			alert.showAndWait();
		}
		else {
			Parent root;
			try {
				FXMLLoader loader = new FXMLLoader();
				//root = FXMLLoader.load(getClass().getClassLoader().getResource("/photos/copyScene.fxml"));
				loader.setLocation(getClass().getResource("/photos/copyScene.fxml"));
				Scene scene = new Scene(loader.load(),600,400);
				
				CopyController copyController = loader.getController();
				TilePane tilePane = (TilePane)tp_photos.getChildren().get(selectedIndex);
				
				System.out.println("Copy C : " + copyController);
				System.out.println("Tp : " + tilePane.getChildren());
				copyController.transferPhoto(GetPhoto((ImageView)tilePane.getChildren().get(0)), user, album, window);

				
				Stage stage = new Stage();
				stage.setTitle("Copy Photo");
				stage.setScene(scene);
				stage.show();
				
		        
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		LoadList(album);
	}
	
	
	
	/**
	 * Serializes the user object for a specific user.
	 * @param user the user object.
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
	
	// Was trying to store to byte array and then serialize, but idk H
	/**public void serializePhoto(user user) throws IOException {
		
		/*ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(buffer);
		out.writeObject(user);
		out.close();
		byte [] data = buffer.toByteArray();
	}**/
	
	/**
	 * Button handler for the back button on the User Album Display Scene, loads the previous scene which is the Main user scene displaying all the albums
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_backHandler(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/userScene.fxml"));
		Parent root;
		root = loader.load();
		UserController userController = loader.getController();
		userController.transferUser(user, window);
		Scene userScene = new Scene(root);
		window.setScene(userScene);
		window.show();
		
	}
	
	/**
	 * Button handler for the logout button on the User Album Display Scene, serializes the user data and safely logs the user out prompting them back to the Login Scene.
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_logoutHandler (ActionEvent event) throws IOException {	
		serialize(user);	
		Parent loginroot = FXMLLoader.load(getClass().getClassLoader().getResource("./photos/loginScene.fxml"));
		Scene loginScene = new Scene(loginroot);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.show();
	}
	
	/**
	 * Button handler for the quit button on the User Album Display Scene, serializes the user data and safely closes the application.
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
	 * Transforms the appropriate variables from the Initial User Scene to the Album Display Scene.
	 * @param currentAlbum album object for the specific album that is opened
	 * @param currentWindow current stage information
	 * @param currentUser user object for the specific user that is logged in
	 */
	public void transferAlbum(album currentAlbum, Stage currentWindow, user currentUser) {
		albumWithImageViews = new ArrayList<pImageView>();
		album = currentAlbum;
		window = currentWindow;
		user = currentUser;
		if(album.getPhotos().size() == 0)
		{
			ArrayList<photo> photos = new ArrayList<photo>();
			//File file = new File("./stockPhotos/stock1.jpg");
			//Image abc = new Image(file.toURI().toString());
			//Calendar date = Calendar.getInstance();
			//photos.add(new photo(abc, date));
			//user.getAlbums().add(new album("album1", photos));
		}
		
		LoadList(album);
		
	}
	
	/**
	 * Loads the Images in the album on a Tile pane, each image is loaded with its corresponding caption
	 * @param album album object for the specific album that is opened
	 */
	public void LoadList(album album) {
		tp_photos.getChildren().clear();
		
		for (int i = 0; i<album.getPhotos().size();i++) {
			TilePane imageCap = new TilePane();
			tp_photos.setHgap(7);
			tp_photos.setVgap(3);
			tp_photos.maxWidth(100);
			tp_photos.maxWidth(100);
			imageCap.setPrefColumns(1);
			imageCap.setPrefRows(2);
			ImageView imageView;
			Text caption = new Text();
			if(album.getPhotos().get(i).getName().length()>14) {
				caption.setText(album.getPhotos().get(i).getName().substring(0,15));
			}else {
				caption.setText(album.getPhotos().get(i).getName());
			}
			System.out.println(album.getPhotos().get(i).getType());
			imageView = makeImageView(ByteArrayToImage(album.getPhotos().get(i).getImage(), album.getPhotos().get(i).getType()));
			System.out.println(imageView);
			albumWithImageViews.add(new pImageView(imageView, album.getPhotos().get(i)));
			imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {
					t.getSource();
					selectedIndex = GetImageViewIndex(tp_photos, imageView);
					/*photo photo = GetPhoto(imageView);
					OpenPhoto(photo);*/
				}
			});
			
			imageCap.getChildren().add(imageView);
			imageCap.getChildren().add(caption);
			
			//obsPhotoList.add(imageView);
			tp_photos.getChildren().add(imageCap);
			
		}
		
		
	}
	
	int GetImageViewIndex(TilePane tp_photos, ImageView imageView) {
		for (int i = 0; i < tp_photos.getChildren().size(); i++){
			TilePane temp = (TilePane)tp_photos.getChildren().get(i);
			if ((ImageView)temp.getChildren().get(0) == imageView) {
				return i;
			}
		}
		return 0;
	}
	
	byte [] ImageToByteArray(Image image, String type) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		byte[] byteArray = null;
		try {
			oos = new ObjectOutputStream(bos);
			ImageIO.write(SwingFXUtils.fromFXImage(image,  null), type, oos);
			byteArray = bos.toByteArray();
			oos.flush();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byteArray;
	}
	
	Image ByteArrayToImage(byte [] byteImage, String type) {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteImage);
		ObjectInputStream ios = null;
		Image image = null;
		try {
			System.out.println("1");
			ios = new ObjectInputStream(bis);
			//ios.defaultReadObjecFmaket();
			//System.out.println(ios.read);
			BufferedImage buffImage = ImageIO.read(ios);
			System.out.println(buffImage);
			image = SwingFXUtils.toFXImage(buffImage, null);
			System.out.println("4");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
	/**
	 * Creates an image view from the image provided.
	 * @param image uploaded image by the user
	 * @return Imageview holding the upload image by the user.
	 */
	private ImageView makeImageView (Image image) {
		ImageView imageView = null;
		imageView = new ImageView(image);
		imageView.setFitWidth(80);
		imageView.setFitHeight(75);
		return imageView;
	}
	
	/**
	 * Function creates an Arraylist of String with all the Album names for that specific user.
	 * @param albums Arraylist of the album object holding albums for that user
	 * @return returns an Arraylist of String with all the Album names.
	 */
	public ArrayList<String> getPhotoNames(ArrayList<album> albums){
		ArrayList<String> albumNameList = new ArrayList<String>();
		for (int i = 0; i < albums.size(); i++) {
			albumNameList.add(album.getPhotos().get(i).getName());
		}
		return albumNameList;
	}
	
	private void showItem(){};
	
	/**
	 * Button Handler for the Add button in the User Album Display Scene.
	 * @param event captured On Action event of the Button
	 * @throws IOException
	 */
	@FXML
	public void b_addPhotoHandler(ActionEvent event) throws IOException {

		FileChooser chooser = new FileChooser();
		//FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		File imageFile = chooser.showOpenDialog(null);
		

		
		try {
			String picName =  imageFile.getName();
			String type = picName.substring(picName.length() - 3);
			BufferedImage bufferedImage = ImageIO.read(imageFile);
			Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			Calendar cal = Calendar.getInstance();
			//System.out.println("ddd" + picName);
			photo p = new photo(ImageToByteArray(image, type), type, picName,cal.getTime());
			album.getPhotos().add(p);
			System.out.println("error?");
			LoadList(album);
		} catch (NullPointerException ex) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Invalid Format");
			alert.setHeaderText(null);
			alert.setContentText("Select a Valid Image Format, JPG or PNG");
			alert.showAndWait();
		}
		
	}
	
	/**
	 * Check if the Photo is in the Album
	 * @param image Image to display
	 * @return Returns the image if its found, otherwise it returns null
	 */
	public photo GetPhoto(ImageView imageView) {
		for (int i = 0; i < albumWithImageViews.size(); i++) {
			if (albumWithImageViews.get(i).imageView == imageView) {
				return albumWithImageViews.get(i).photo;
			}
		}
		return null;
	}
	
	/**
	 * Opens a bigger photo in its new Scene showing its details.
	 * @param photo Photo to be opened
	 */
	@FXML
	public void b_openPhotoHandler(ActionEvent event) throws IOException {
		if (album.getPhotos().size() == 0 || selectedIndex == -1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No photo selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a photo to open");
			alert.showAndWait();
		}
		else {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/photoScene.fxml"));
		        Parent root;
				root = loader.load();
				PhotoController photoController = loader.getController();
				TilePane tilePane = (TilePane)tp_photos.getChildren().get(selectedIndex);
		        photoController.transferPhoto(GetPhoto((ImageView)tilePane.getChildren().get(0)), user, album, window);
		        Scene photoScene = new Scene(root);
		        window.setScene(photoScene);
				window.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@FXML void b_removePhotoHandler(ActionEvent event) throws IOException {
		if (album.getPhotos().size() == 0 || selectedIndex == -1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No photo selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a photo to remove");
			alert.showAndWait();
		}
		else {
			TilePane tilePane = (TilePane)tp_photos.getChildren().get(selectedIndex);
			photo photo = GetPhoto((ImageView)tilePane.getChildren().get(0));
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("Are you sure you want to delete this photo?");
			alert.setContentText(photo.getName());
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {
				tp_photos.getChildren().remove(selectedIndex);
				album.getPhotos().remove(selectedIndex);
			}
		}
	}
	
	@FXML
	public void b_captionPhotoHandler(ActionEvent event) throws IOException {
		if (album.getPhotos().size() == 0 || selectedIndex == -1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No photo selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a photo to edit caption of");
			alert.showAndWait();
		}
		else {
			TextInputDialog photoDi = new TextInputDialog();
			photoDi.setHeaderText("Caption/Recaption photo");
			photoDi.setContentText("Enter new caption: ");
			Optional<String> caption = photoDi.showAndWait();
			if(!caption.isEmpty()) {
				TilePane tilePane = (TilePane)tp_photos.getChildren().get(selectedIndex);
				photo photo = GetPhoto((ImageView)tilePane.getChildren().get(0));
				photo.setName(caption.get());
				Text tpCaption = (Text)tilePane.getChildren().get(1);
				tpCaption.setText(caption.get());
			}
		}
	}
	
	@FXML
	public void b_addTagHandler(ActionEvent event) throws IOException {
		try {
			TilePane tilePane = (TilePane)tp_photos.getChildren().get(selectedIndex);
			photo photo = GetPhoto((ImageView)tilePane.getChildren().get(0));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/addTagScene.fxml"));
			Scene scene = new Scene(loader.load());
	        AddTagController addTagController = loader.getController();
	        addTagController.transferPhoto(user, album, selectedIndex, photo);
	        Stage window = new Stage();
	        window.setTitle("Add tag to selected photo");
	        window.setScene(scene);
	        window.show();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
}
