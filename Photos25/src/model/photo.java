package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.scene.image.Image;


/** 
 * 
 * @author Haris
 * 
 *
 */
public class photo implements Serializable{

	private static final long serialVersionUID = 3851132190498798642L;
	byte [] image;
	String name;
	
	public void addTag(boolean multiple, String name) {
		tags.add(new tag(multiple, name));
	}
	
	public void removeTag(String name) {
		for(int i = 0; i < tags.size(); i++) {
			if (tags.get(i).name == name) {
				tags.get(i).elements.clear();
				tags.remove(i);
			}
		}
	}
	
	ArrayList<tag> tags = new ArrayList<tag>();
	
	public ArrayList<tag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<tag> tags) {
		this.tags = tags;
	}

	//tags
	Date date;
	String fileType;
	
	
	
	public photo (byte [] image, String type, String name, Date date) {
		this.image = image;
		this.fileType = type;
		this.name = name;
		this.date = date;
	}
	
	public String getType() {
		return fileType;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public byte [] getImage() {
		return image;
	}
	public void setImage(byte [] image) {
		this.image = image;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
