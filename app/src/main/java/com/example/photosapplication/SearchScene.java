package com.example.photosapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SearchScene extends AppCompatActivity {

    FloatingActionButton bs_back;
    Button b_location;
    Button b_people;
    GridView gridview;
    TextInputEditText tf_tagInput;
    PhotosAdapter adapter;

    PhotosAdapter adapterTemp;

    Album album;

    Album tempalbum;
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

        b_location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                searchWithLocation();
            }
        });

        b_people.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });

    }

    public void searchWithLocation(){
        String input = tf_tagInput.getText().toString();
        tempalbum = new Album("search results", new ArrayList<Photo>() );

        if (input.matches("")) {
            AlertDialog.Builder noinput = new AlertDialog.Builder(this);
            noinput.setMessage("Enter a tag in the text field and then hit the button");

            noinput.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            noinput.show();

        }else {
            for (int i = 0; i < album.getPhotoList().size(); i++) {
                if (album.getPhotoList().get(i).getLocation().contains(input)) {
                    tempalbum.getPhotoList().add(album.getPhotoList().get(i));
                }
            }

            adapterTemp = new PhotosAdapter(this, tempalbum.getPhotoList());
            gridview.setAdapter(adapterTemp);
            adapter.notifyDataSetChanged();
        }

    }
}
