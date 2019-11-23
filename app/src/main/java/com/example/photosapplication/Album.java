package com.example.photosapplication;

public class Album {


    private String albumName = "";

    public Album(String name){
        this.albumName = name;

    }

    public void setAlbumName(String name){
        this.albumName = name;
    }

    public String getAlbumName(){
        return this.albumName;
    }

    public String toString(){
        return albumName;
    }

}
