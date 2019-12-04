package com.example.photosapplication;

import android.graphics.Bitmap;

public class Photo {
    Bitmap imageBitmap;
    String caption = "";

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

    public void setCaption(String caption){
        this.caption = caption;
    }

    public String getCaption(){
        return this.caption;
    }
}
