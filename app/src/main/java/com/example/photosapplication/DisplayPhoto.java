package com.example.photosapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DisplayPhoto extends AppCompatActivity {

    FloatingActionButton bd_back;
    FloatingActionButton b_next;
    FloatingActionButton b_prev;

    EditText peopleHolder;
    EditText locationHolder;

    EditText peopleText;
    EditText locationText;

    ImageView iv_displayPhoto;
    Album album ;
    Photo photo;
    int index;
    int slideShowIndex;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_photo);

        index  = Integer.parseInt(getIntent().getStringExtra("indexp"));
        slideShowIndex = index;

        bd_back = (FloatingActionButton) findViewById(R.id.bd_back);
        b_next = findViewById(R.id.b_next);
        b_prev = findViewById(R.id.b_prev);
        peopleHolder = findViewById(R.id.peopleHolder);
        locationHolder = findViewById(R.id.locationHolder);
        peopleHolder.setKeyListener(null);
        locationHolder.setKeyListener(null);
        peopleText = findViewById(R.id.peopleText);
        peopleText.setKeyListener(null);
        locationText = findViewById(R.id.locationText);
        locationText.setKeyListener(null);

        //photo = PhotosScene.getDataForDisplay(index);
        album = PhotosScene.getData();

        iv_displayPhoto = findViewById(R.id.iv_displayPhoto);

        bd_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        b_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(slideShowIndex == album.getPhotoList().size()-1){

                }else{
                    slideShowIndex = slideShowIndex + 1;
                }
                iv_displayPhoto.setImageBitmap(album.getPhotoList().get(slideShowIndex).getBitmap());
            }
        });

        b_prev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(slideShowIndex == 0){

                }else{
                    slideShowIndex = slideShowIndex - 1;
                }
                iv_displayPhoto.setImageBitmap(album.getPhotoList().get(slideShowIndex).getBitmap());
            }
        });

        iv_displayPhoto.setImageBitmap(album.getPhotoList().get(index).getBitmap());

        locationHolder.setText(album.getPhotoList().get(index).getLocation());
        displayPeopleTag();
    }

    public void displayPeopleTag(){
        String people = "";
        for (int i = 0;i< album.getPhotoList().get(index).getPeople().size();i++){
            people = people + album.getPhotoList().get(index).getPeople().get(i) + ", ";
        }
        System.out.print("ALL PEOPLEEEEEEEEEEEEEEEEEEEEE ::::::::::::::::::::::::::::::  " + people);
        peopleHolder.setText(people);
    }

}
