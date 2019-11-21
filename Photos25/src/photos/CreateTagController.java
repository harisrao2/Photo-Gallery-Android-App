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
import javafx.scene.control.CheckBox;
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

public class CreateTagController {
	
	@FXML Button b_addTag;
	@FXML TextField tf_tag;
	@FXML CheckBox cb_multiple;
	
	user user;
	public void transferUser(user currentUser) {
		user = currentUser;
	}
	
	@FXML
	public void b_newTagHandler(ActionEvent event) throws IOException{
		cb_multiple.setIndeterminate(false);
		ArrayList<String> tagNames = getTagNames(user.getTags());
		TextInputDialog tagDi = new TextInputDialog();
		tagDi.setHeaderText("Create new tag");
		tagDi.setContentText("Enter tag name: ");
		String tagName = tf_tag.getText();
		if(tagNames.contains(tagName)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("This tag already exists");
			alert.showAndWait();
		}
		else {
			user.getTags().add(new tag(cb_multiple.isSelected(), tagName));
			user.getTags().sort(Comparator.comparing(tag::getName, String.CASE_INSENSITIVE_ORDER));
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.close();
		}
	}
	
	public ArrayList<String> getTagNames(ArrayList<tag> tags){
		ArrayList<String> tagNameList = new ArrayList<String>();
		for (int i = 0; i < tags.size(); i++) {
			tagNameList.add(user.getTags().get(i).getName());
		}
		return tagNameList;
	}
}
