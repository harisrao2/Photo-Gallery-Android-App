package model;

import java.util.ArrayList;
import java.io.Serializable;

public class tag implements Serializable{
	private static final long serialVersionUID = 2643211120419605778L;
	boolean multiple;
	String name;
	ArrayList<String> elements = new ArrayList<String>();
	public ArrayList<String> getElements() {
		return elements;
	}

	public void setElements(ArrayList<String> elements) {
		this.elements = elements;
	}

	public tag(boolean multiple, String name){
		this.multiple = multiple;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isMultiple() {
		return multiple;
	}
}
