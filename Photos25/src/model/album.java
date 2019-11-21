package model;

import java.io.Serializable;
import java.util.ArrayList;

public class album implements Serializable{

	private static final long serialVersionUID = 1498450865206480494L;
	String albumname;
	ArrayList<photo> photos = new ArrayList<photo>();
	
	public album (String albumname, ArrayList<photo> photos) {
		this.albumname = albumname;
		this.photos = photos;
	}
	
	public String getName() {
		return albumname;
	}
	public void setName(String albumname) {
		this.albumname = albumname;
	}
	public ArrayList<photo> getPhotos() {
		return photos;
	}
	public void setPhotos(ArrayList<photo> photos) {
		this.photos = photos;
	}
	

	
	
}
