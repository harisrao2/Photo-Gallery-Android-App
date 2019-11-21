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

public class CopyController {

	@FXML ListView<String> lvC_albums;
	@FXML Button b_back;
	@FXML Button b_copy;
	@FXML Button b_move;
	
	album album;
	user user;
	Stage window;
	photo photo;
	
	ArrayList<album> albums;
	int indexF = 0;
	private static ObservableList <String> CobsAlbumList;
	
	
	@FXML
	public void b_moveHandler (ActionEvent event) throws IOException{
		boolean boolAlert = false;
		
		//System.out.println("IndexF: " + indexF);
		//System.out.println("album name : "+ album.getName());
		//System.out.println("album name2 : "+ albums.get(indexF).getName());
		if(lvC_albums.getSelectionModel().getSelectedIndex()==indexF) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Cannot Move to the Same Album");
			alert.showAndWait();
			boolAlert = true;
		}else {
			int index = lvC_albums.getSelectionModel().getSelectedIndex();
			user.getAlbums().get(index).getPhotos().add(photo);
			for (int i = 0; i<album.getPhotos().size(); i++) {
				if(album.getPhotos().get(i).getName().contentEquals(photo.getName())) {
					album.getPhotos().remove(i);
				}
			}
		}
		//user.getAlbums().get(index).getPhotos().add(photo);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		if(boolAlert == true) {
			
		}else {
			window.close();
		}
	
	}
	
	@FXML
	public void b_copyHandler (ActionEvent event) throws IOException{
		boolean boolAlert = false;
		
		//System.out.println("IndexF: " + indexF);
		//System.out.println("album name : "+ album.getName());
		//System.out.println("album name2 : "+ albums.get(indexF).getName());
		if(lvC_albums.getSelectionModel().getSelectedIndex()==indexF) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Cannot Copy to the Same Album");
			alert.showAndWait();
			boolAlert = true;
		}else {
			int index = lvC_albums.getSelectionModel().getSelectedIndex();
			user.getAlbums().get(index).getPhotos().add(photo);
		}
		//user.getAlbums().get(index).getPhotos().add(photo);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		if(boolAlert == true) {
			
		}else {
			window.close();
		}
	
	}
	
	public void LoadList(user user) {
		CobsAlbumList = FXCollections.observableArrayList(getAlbumNames(user.getAlbums()));
		lvC_albums.setItems(null);
		lvC_albums.setItems(CobsAlbumList);
		System.out.println(CobsAlbumList);
		lvC_albums.getSelectionModel().select(0);
    	showItem();
    	lvC_albums.getSelectionModel().select(lvC_albums.getSelectionModel().getSelectedIndex());
    	lvC_albums.getSelectionModel().selectedIndexProperty().addListener((obsAlbumList,oldVal,newVal)-> showItem());
	}
	
	public ArrayList<String> getAlbumNames(ArrayList<album> albums){
		this.albums = albums;
		ArrayList<String> albumNameList = new ArrayList<String>();
		for (int i = 0; i < albums.size(); i++) {
			if(album.getName().contentEquals(albums.get(i).getName())){
				indexF  = i;
				albumNameList.add(user.getAlbums().get(i).getName());
			}
			else {
				albumNameList.add(user.getAlbums().get(i).getName());
			}
			//System.out.println("heeeeeeeeeeeeeey" + user.getAlbums().get(i).getName());
		}
		return albumNameList;
	}
	
	private void showItem(){};
	
	public void transferPhoto(photo currentPhoto, user user, album album, Stage window) {
		photo = currentPhoto;
		this.user = user;
		this.album = album;
		this.window = window;
		
		LoadList(user);
	}
	
	
	
}
