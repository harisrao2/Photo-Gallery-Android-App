package com.example.photosapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DisplayPhoto extends AppCompatActivity {

    FloatingActionButton bd_back;

    ImageView iv_displayPhoto;

    Photo photo;

    int index;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_photo);

       index  = Integer.parseInt(getIntent().getStringExtra("indexp"));

        bd_back = (FloatingActionButton) findViewById(R.id.bd_back);

        photo = PhotosScene.getDataForDisplay(index);

        iv_displayPhoto = findViewById(R.id.iv_displayPhoto);

        bd_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        iv_displayPhoto.setImageBitmap(photo.getBitmap());

    }

}
