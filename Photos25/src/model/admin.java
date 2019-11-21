package model;

import java.io.Serializable;
import java.util.ArrayList;

public class admin implements Serializable{
	private static final long serialVersionUID = -5292566519690929605L;
	ArrayList <String> usernames = new ArrayList <String>();

	public admin (ArrayList<String> usernames) {
		this.usernames = usernames;
	}
	
	public ArrayList<String> getUsers() {
		return usernames;
	}

	public void setUsers(ArrayList<String> usernames) {
		this.usernames = usernames;
	}
}
