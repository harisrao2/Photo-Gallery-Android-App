package com.example.photosapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class PhotosAdapter extends BaseAdapter {

    private Context mContext;

    public ArrayList <Integer> photosArray = new ArrayList<Integer>();

    public PhotosAdapter (Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount(){
        return photosArray.size();
    }

    @Override
    public Object getItem (int position){
        return photosArray;
    }
    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView (int position, View convertView , ViewGroup parent){
        ImageView imageView = new ImageView (mContext);
        //imageView.setImageResource(photosArray[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(340,350));

        return imageView;
    }

}
