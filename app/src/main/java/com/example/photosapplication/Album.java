package com.example.photosapplication;


import java.util.ArrayList;

public class Album {


    private String albumName = "";

    ArrayList<Photo> photoList = new ArrayList<Photo>();


    public Album(String name , ArrayList<Photo> photoList){
        this.albumName = name;
        this.photoList = photoList;

    }

    public void setAlbumName(String name){
        this.albumName = name;
    }

    public String getAlbumName(){
        return this.albumName;
    }

    public ArrayList<Photo> getPhotoList(){
        return this.photoList;
    }

    public void setPhotoList(ArrayList<Photo> photoList){
        this.photoList = photoList;
    }

    public String toString(){
        return albumName;
    }

}
