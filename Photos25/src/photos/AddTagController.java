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

public class AddTagController {
	
	@FXML ListView<String> lv_tags;
    @FXML Button b_enter;
    @FXML TextField tf_element;
	
	photo photo;
	user user;
	album album;
	int selectedIndex;
	public void transferPhoto(user currentUser, album currentAlbum, int currentSelectedIndex, photo currentPhoto) {
		photo = currentPhoto;
		user = currentUser;
		album = currentAlbum;
		selectedIndex = currentSelectedIndex;
		LoadTags(user);
	}
	
	private static ObservableList <String> obsTagsList;
	public void LoadTags(user user) {
		obsTagsList = FXCollections.observableArrayList(getTagNames(user.getTags()));
		lv_tags.setItems(null);
		lv_tags.setItems(obsTagsList);
		lv_tags.getSelectionModel().select(0);
    	showItem();
    	lv_tags.getSelectionModel().select(lv_tags.getSelectionModel().getSelectedIndex());
    	lv_tags.getSelectionModel().selectedIndexProperty().addListener((obsTagsList,oldVal,newVal)-> showItem());
	}
	
	public ArrayList<String> getTagNames(ArrayList<tag> tags){
		ArrayList<String> tagNameList = new ArrayList<String>();
		for (int i = 0; i < tags.size(); i++) {
			tagNameList.add(user.getTags().get(i).getName());
		}
		return tagNameList;
	}
	
	@FXML
	public void b_enterHandler(ActionEvent event) throws IOException {
		if(user.getTags().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("This user has no tags. Go back and create a tag first (in user scene).");
			alert.showAndWait();
		}else {
			if(lv_tags.getSelectionModel().getSelectedIndex()==-1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Please select a tag type first");
				alert.showAndWait();
			}else {
				System.out.println("In add tag handle");
				photo photo = album.getPhotos().get(selectedIndex);
				boolean multiple = user.getTags().get(lv_tags.getSelectionModel().getSelectedIndex()).isMultiple();
				String name = user.getTags().get(lv_tags.getSelectionModel().getSelectedIndex()).getName();
				photo.addTag(multiple, name);
				photo.getTags().get(photo.getTags().size() - 1).getElements().add(tf_element.getText());
				Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
				window.close();
			}
		}
	}
	
	private void showItem(){};
}
