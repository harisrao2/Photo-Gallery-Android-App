package com.example.photosapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Photo {
    Bitmap imageBitmap;
    String caption = "";

    String location =  "";
    ArrayList<String> people = new ArrayList<String>();

    public Photo (Bitmap imageBitmap, String caption){
        this.imageBitmap = imageBitmap;
        this.caption = caption;
    }

    public void setBitmap(Bitmap imageBitmap){
        this.imageBitmap = imageBitmap;
    }

    public Bitmap getBitmap() {
        return this.imageBitmap;
    }

    public void setLocation (String location){
        this.location = location;
    }

    public String getLocation (){
        return this.location;
    }

    public void setPeople(ArrayList<String> people){
        this.people = people;
    }

    public ArrayList<String> getPeople (){
        return this.people;
    }


    public void setCaption(String caption){
        this.caption = caption;
    }

    public String getCaption(){
        return this.caption;
    }
}
