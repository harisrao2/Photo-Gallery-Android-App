package com.example.photosapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
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


    static ArrayList<Album> albums;

    private static int PICK_IMAGE_REQUEST = 1;

    Uri imageUri;
    PhotosAdapter adapter;

    //static ArrayList<Photo> photoList;

    SharedPreferences sharedpref;

    String albumName;

    String Sindex;
    static int index;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        albums = MainActivity.getData();
        printAlbums(albums);
        Sindex = getIntent().getStringExtra("index");
        index = Integer.parseInt(Sindex);
        //albums.get(index).setPhotoList(photoList);
        //load();
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




        gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id){
                        // making alert for Album options
                        showOptions(position);
                    }
                });



    }

    public void showOptions(final int position){
        AlertDialog.Builder optionsAlert = new AlertDialog.Builder(this);
        optionsAlert.setTitle("Choose an option for photo \""+albums.get(index).getPhotoList().get(position).getCaption()+"\"");


        optionsAlert.setNeutralButton("Open", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //save();
                openPhoto(position);
            }
        });

        optionsAlert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                albums.get(index).getPhotoList().remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        optionsAlert.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //promptRename(position);
            }
        });


        optionsAlert.show();
    }

    public void openPhoto( int position){
        Intent intent = new Intent(PhotosScene.this, DisplayPhoto.class);
        intent.putExtra("indexp", String.valueOf(position));
        startActivity(intent);
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

                File file = new File(imageUri.getPath());

                caption = file.getName();


                //gridView.getAdapter().getView(viewNum).setImageURI(imageUri);
                //imageView.setImageURI(imageUri);
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(imageStream);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            Photo p = new Photo(imageBitmap,caption);
            System.out.println("CAPTION :::::::::::::::: " + caption);
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

    public static Photo getDataForDisplay(int position) {
        Photo p = albums.get(index).getPhotoList().get(position);
        return p;
    }

}
