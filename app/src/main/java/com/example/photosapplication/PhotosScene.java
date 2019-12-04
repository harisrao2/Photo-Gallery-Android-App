package com.example.photosapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class PhotosScene extends AppCompatActivity {

    FloatingActionButton b_back;

    GridView gridView;

    //ImageView imageView;

    Bitmap imageBitmap;

    String caption;

    FloatingActionButton b_addPhoto;

    FloatingActionButton bp_save;

    private static int PICK_IMAGE_REQUEST = 1;

    Uri imageUri;

    ArrayList<Photo> photoList = new ArrayList<Photo>();




    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        b_back = findViewById(R.id.b_back);
        b_addPhoto = findViewById(R.id.b_addphoto);
        gridView = (GridView) findViewById(R.id.photos_grid);
        PhotosAdapter adapter = new PhotosAdapter(this, photoList);
        gridView.setAdapter(adapter);
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
        System.out.println("DICKAIDcnaDIHBCAIHVIHAD :::::::::: " + photoList.get(0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null){

            try {
                imageUri = data.getData();
                caption = data.getDataString();
                //gridView.getAdapter().getView(viewNum).setImageURI(imageUri);
                //imageView.setImageURI(imageUri);
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(imageStream);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            Photo p = new Photo(imageBitmap,caption);
            photoList.add(p);


        }

    }
}
