package model;

import java.io.Serializable;
import java.util.ArrayList;

public class user implements Serializable{
	private static final long serialVersionUID = -8865981517443846810L;
	private String username;
	
	private ArrayList<album> albums = new ArrayList<album>();
	private ArrayList<tag> tags = new ArrayList<tag>();
	
	
	
	public user (String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ArrayList<album> getAlbums() {
		return albums;
	}

	public void setAlbums(ArrayList<album> albums) {
		this.albums = albums;
	}

	public void createTag(boolean multiple, String name) {
		tags.add(new tag(multiple, name));
	}
	
	public ArrayList<tag> getTags() {
		return tags;
	}
	
}
