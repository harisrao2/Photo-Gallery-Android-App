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

     ArrayList<Album> albums;

    private static int PICK_IMAGE_REQUEST = 1;

    Uri imageUri;
    PhotosAdapter adapter;

    ArrayList<Photo> photoList = new ArrayList<Photo>();

    SharedPreferences sharedpref;

    String albumName;

    String Sindex;
    int index;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        albums = MainActivity.getData();
        printAlbums(albums);
        Sindex = getIntent().getStringExtra("index");
        index = Integer.parseInt(Sindex);
        albums.get(index).setPhotoList(photoList);
        load();
        System.out.println("ALBUMNAMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE :::" + albums.get(index).getAlbumName());
        b_back = findViewById(R.id.b_back);
        b_addPhoto = findViewById(R.id.b_addphoto);
        gridView = (GridView) findViewById(R.id.photos_grid);
        adapter = new PhotosAdapter(this, albums.get(index).getPhotoList());
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


    public void printAlbums(ArrayList<Album> albums){
        for (int i = 0 ;i <albums.size();i++){
            System.out.println("PRINTIGN ALBUMS ::::::::::::::::::: " + albums.get(i).getAlbumName());
        }

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
            albums.get(index).getPhotoList().add(p);
            System.out.println("checkkkkkkkkkkkkkkkkk :::::::::: " + albums.get(index).getPhotoList().get(0));
            adapter.notifyDataSetChanged();


        }

    }

    public void save(){
        //sharedpref = getSharedPreferences("shared preferences",MODE_PRIVATE);
        sharedpref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = sharedpref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(albums);
        Log.d("TAG","saving albums = " + albums);
        editor.putString("albums", json);
        editor.apply();
    }

    public void load(){
        //sharedpref = getSharedPreferences("shared preferences",MODE_PRIVATE);
        sharedpref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = sharedpref.getString("albums",null);
        Type type = new TypeToken<ArrayList<Album>>(){}.getType();
        albums = gson.fromJson(json, type);
        if(albums == null){
            albums = new ArrayList<Album>();
        }
        Log.d("TAG","loading albums = " + albums);

    }


}
