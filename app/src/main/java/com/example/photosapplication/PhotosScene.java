package com.example.photosapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
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
    PhotosAdapter adapter;

    ArrayList<Photo> photoList = new ArrayList<Photo>();

    SharedPreferences sharedpref;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        load();
        b_back = findViewById(R.id.b_back);
        b_addPhoto = findViewById(R.id.b_addphoto);
        gridView = (GridView) findViewById(R.id.photos_grid);
        adapter = new PhotosAdapter(this, photoList);
        gridView.setAdapter(adapter);

        //load();
        adapter.notifyDataSetChanged();
        b_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                save();
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
            System.out.println("checkkkkkkkkkkkkkkkkk :::::::::: " + photoList.get(0));
            adapter.notifyDataSetChanged();


        }

    }

    public void save(){
        //sharedpref = getSharedPreferences("shared preferences",MODE_PRIVATE);
        sharedpref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = sharedpref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(photoList);
        Log.d("TAG","saving albums = " + photoList);
        editor.putString("photoList", json);
        editor.apply();
    }

    public void load(){
        //sharedpref = getSharedPreferences("shared preferences",MODE_PRIVATE);
        sharedpref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = sharedpref.getString("photoList",null);
        Type type = new TypeToken<ArrayList<Photo>>(){}.getType();
        photoList = gson.fromJson(json, type);
        if(photoList == null){
            photoList = new ArrayList<Photo>();
        }
        Log.d("TAG","loading albums = " + photoList);

    }
}
