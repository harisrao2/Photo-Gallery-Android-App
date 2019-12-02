package com.example.photosapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PhotosScene extends AppCompatActivity {

    FloatingActionButton b_back;

    GridView gridView;

    ImageView imageView;

    FloatingActionButton b_addPhoto;

    FloatingActionButton bp_save;

    private static int PICK_IMAGE_REQUEST = 1;

    Uri imageUri;

    ArrayList<Uri> imageUris = new ArrayList<Uri>();




    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        b_back = findViewById(R.id.b_back);
        b_addPhoto = findViewById(R.id.b_addphoto);
        gridView = (GridView) findViewById(R.id.photos_grid);
        gridView.setAdapter( new PhotosAdapter(this));
        b_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        b_addPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                openFileChooser();
            }
        });




    }



    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null){
            imageUri = data.getData();
            //gridView.getAdapter().getView(viewNum).setImageURI(imageUri);
            imageUris.add(imageUri);
            imageView.setImageURI(imageUri);
        }

    }
}
