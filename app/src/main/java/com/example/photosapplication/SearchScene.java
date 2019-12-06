package com.example.photosapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class SearchScene extends AppCompatActivity {

    FloatingActionButton bs_back;
    Button b_location;
    Button b_people;
    GridView gridview;
    TextInputEditText tf_tagInput;
    PhotosAdapter adapter;

    Album album;
    int index;


    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.search_tag);

        bs_back = findViewById(R.id.bs_back);
        b_location = findViewById(R.id.b_location);
        b_people = findViewById(R.id.b_people);
        gridview = findViewById(R.id.gv_searchResults);
        tf_tagInput = findViewById(R.id.tf_tagInput);

        album = PhotosScene.getData();
        adapter = new PhotosAdapter(this,album.getPhotoList());
        gridview.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        bs_back.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });




    }
}
