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


public class SlideshowController {
	
	@FXML ImageView Iv_show;
	@FXML Button b_next;
	@FXML Button b_prev;
	
	album album;
	user user;
	Stage window; 
	photo photo;
	
	int photoindex=0;
	
	public void LoadImage (album album) {
		for (int i = 0; i<album.getPhotos().size();i++) {
			if(photoindex == -1) {
				break;
			}
			if(i == photoindex) {
				Iv_show.setImage(ByteArrayToImage(album.getPhotos().get(i).getImage(), album.getPhotos().get(i).getType()));
			}
			
		}
	}
	
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
	@FXML public void b_nextHandler(ActionEvent event) throws IOException{
		photoindex = photoindex + 1;
		LoadImage(album);
	}
	
	@FXML public void b_prevHandler(ActionEvent event) throws IOException{
		photoindex = photoindex -1;
		LoadImage(album);
	}
	public void transferPhoto( user user, album album, Stage window) {
		
		this.user = user;
		this.album = album;
		this.window = window;
		
		LoadImage(album);
	}
	
}
