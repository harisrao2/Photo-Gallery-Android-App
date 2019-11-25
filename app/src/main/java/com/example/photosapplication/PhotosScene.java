package com.example.photosapplication;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PhotosScene extends AppCompatActivity {

    FloatingActionButton b_back;

    FloatingActionButton bp_save;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        b_back = findViewById(R.id.b_back);

        b_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

    }
}
