package com.example.photosapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PhotosAdapter extends ArrayAdapter<Photo> {

    private Context context;

    public ArrayList <Photo> photoList = new ArrayList<Photo>();

    public PhotosAdapter (Activity context, ArrayList<Photo> photoList){
        super(context, R.layout.photo, photoList);
        this.context = context;
        this.photoList = photoList;
    }

    @Override
    public int getCount(){
        return photoList.size();
    }

    @Override
    public Photo getItem (int position){
        return photoList.get(position);
    }
    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView (int position, View convertView , ViewGroup parent){
        if(convertView == null){
            LayoutInflater LI = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) LI.inflate(R.layout.photo, parent, false);

        }
        TextView tv = (TextView) convertView.findViewById(R.id.caption);
        if(photoList.get(position).getCaption().length()<=15){
            tv.setText(photoList.get(position).getCaption());
        }else{
            tv.setText(photoList.get(position).getCaption().substring(0,15));
        }
        ImageView iv =  (ImageView) convertView.findViewById(R.id.image);
        iv.setImageBitmap(photoList.get(position).getBitmap());

        return convertView;

    }

}
