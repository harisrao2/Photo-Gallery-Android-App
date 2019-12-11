package com.example.photosapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
    Bitmap imageBitmap;
    String caption;
    FloatingActionButton b_addPhoto;
    FloatingActionButton b_search;


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
        b_back = findViewById(R.id.bs_back);
        b_addPhoto = findViewById(R.id.b_addphoto);
        b_search = findViewById(R.id.b_search);

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

        b_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                search();
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
    public void search(){
        Intent intent = new Intent(PhotosScene.this, SearchScene.class);
        startActivity(intent);
    }

    public void showOptions(final int position){


        AlertDialog.Builder photoOptions = new AlertDialog.Builder(this);
        photoOptions.setTitle("Choose an option for photo \""+albums.get(index).getPhotoList().get(position).getCaption());
        photoOptions.setItems(new CharSequence[]
                        {"Open","Add Tag","Remove Tag", "Move", "Rename", "Delete"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if(which==0) { // if Open
                            save();
                            openPhoto(position);
                        }
                        else if(which == 1){ // if Add tag
                            promptAddTag(position);
                        }
                        else if(which == 2){ // if Remove Tag
                            promptRemoveTag(position);
                        }
                        else if( which == 3) {  // if Move
                            promptMove(position);
                        }
                        else if(which == 4){ // if Rename
                            promptRename(position);
                        }
                        else if (which ==5){ // if Delete
                            albums.get(index).getPhotoList().remove(position);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
        photoOptions.create().show();
    }

    public void promptRemoveTag(final int position){
        AlertDialog.Builder chooseTag = new AlertDialog.Builder(this);

        chooseTag.setTitle("Choose a Tag Type to Remove");

        // chooseTag.setMessage("Choose a Tag Type");
        chooseTag.setPositiveButton("Location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                promptRemoveLocationTag(position);
            }
        });
        chooseTag.setNegativeButton("Person", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                promptRemovePersonTag(position);
            }
        });


        chooseTag.show();
    }

    public void promptRemoveLocationTag(final int position){
        if(albums.get(index).getPhotoList().get(position).getLocation()==""){
            AlertDialog.Builder noloc = new AlertDialog.Builder(this);
            noloc.setMessage("There is no location tagged in this photo");
            noloc.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            noloc.show();

        }else {
            AlertDialog.Builder removeloc = new AlertDialog.Builder(this);
            removeloc.setTitle("Do you want to Remove the \"" + albums.get(index).getPhotoList().get(position).getLocation() + "\" location tag");

            removeloc.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    albums.get(index).getPhotoList().get(position).setLocation("");

                }
            });

            removeloc.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            removeloc.show();
        }
    }

    public void promptRemovePersonTag (final int position){
        if(albums.get(index).getPhotoList().get(position).getPeople().size()>0){

            CharSequence [] listOfTags = new CharSequence[albums.get(index).getPhotoList().get(position).getPeople().size()];

            for (int i = 0 ; i<albums.get(index).getPhotoList().get(position).getPeople().size(); i++ ){
                listOfTags[i] = albums.get(index).getPhotoList().get(position).getPeople().get(i);
            }

            AlertDialog.Builder removepeep = new AlertDialog.Builder(this);
            removepeep.setTitle("Select a tag to remove");
            removepeep.setItems(listOfTags, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position
                    // of the selected item
                    albums.get(index).getPhotoList().get(position).getPeople().remove(which);
                    adapter.notifyDataSetChanged();

                }
            });
            removepeep.create().show();


        }else{
            AlertDialog.Builder nopeep = new AlertDialog.Builder(this);
            nopeep.setMessage("There are no people tagged in this photo");
            nopeep.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            nopeep.show();
        }
    }

    public void promptAddTag(final int position){
        AlertDialog.Builder chooseTag = new AlertDialog.Builder(this);

        chooseTag.setTitle("Choose a Tag Type to Add");

       // chooseTag.setMessage("Choose a Tag Type");
        chooseTag.setPositiveButton("Location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                promptLocationTag(position);
            }
        });
        chooseTag.setNegativeButton("Person", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                promptPersonTag(position);
            }
        });


        chooseTag.show();

    }
    public void promptPersonTag(final int position){
        AlertDialog.Builder people = new AlertDialog.Builder(this);
        people.setTitle("Enter the name of the Person");


        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        people.setView(input);

        people.setPositiveButton("Add Tag", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> temp = albums.get(index).getPhotoList().get(position).getPeople();
                temp.add(input.getText().toString());
                albums.get(index).getPhotoList().get(position).setPeople(temp);

            }
        });

       people.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        people.show();

    }

    public void promptLocationTag(final int position){
        if(albums.get(index).getPhotoList().get(position).getLocation()=="") {
            AlertDialog.Builder location = new AlertDialog.Builder(this);
            location.setTitle("Enter a Location");


            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            location.setView(input);

            location.setPositiveButton("Add Tag", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    albums.get(index).getPhotoList().get(position).setLocation(input.getText().toString());
                }
            });

            location.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            location.show();
        }else{
            AlertDialog.Builder locexists = new AlertDialog.Builder(this);
            locexists.setMessage("Remove the Existing location lag from the image to add a new location tag");
            locexists.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            locexists.show();
        }

    }

    public void promptMove(final int position){

        CharSequence [] listOfAlbums = new CharSequence[albums.size()];

        for (int i = 0 ;i<albums.size();i++){
            listOfAlbums[i] = albums.get(i).getAlbumName();
            System.out.println("CHARF SQEUENCEE :::::::::::::::::::::::::::::::"+listOfAlbums[i]);

        }


        AlertDialog.Builder moveOption = new AlertDialog.Builder(this);
        moveOption.setTitle("Select an Album to move this photo to");
        moveOption.setItems(listOfAlbums, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                albums.get(which).getPhotoList().add(albums.get(index).getPhotoList().get(position));
                albums.get(index).getPhotoList().remove(position);
                adapter.notifyDataSetChanged();

            }
        });
        moveOption.create().show();

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



    public void promptRename(final int position){
        AlertDialog.Builder rename = new AlertDialog.Builder(this);
        rename.setTitle("Rename photo \""+albums.get(index).getPhotoList().get(position).getCaption()+"\" to :");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        rename.setView(input);

        rename.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().matches("")) {
                    promptNoInput();

                }else{
                    if(nameExist(albums, input.getText().toString())==true){
                        promptDuplicateName();
                    }else{
                        albums.get(index).getPhotoList().get(position).setCaption(input.getText().toString());
                        adapter.notifyDataSetChanged();
                    }

                }



            }
        });


        rename.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        rename.show();

        //return input.getText().toString();
    }

    public void promptDuplicateName(){
        AlertDialog.Builder noinput = new AlertDialog.Builder(this);
        noinput.setMessage("That image Caption already exists");

        noinput.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noinput.show();
    }
    public boolean nameExist(ArrayList<Album>albums, String name){
        for(int i = 0;i<albums.size();i++){
            if(albums.get(index).getPhotoList().get(i).getCaption().equals(name)){
                return true;
            }
        }
        return false;

    }

    public void promptNoInput(){
        AlertDialog.Builder noinput = new AlertDialog.Builder(this);
        noinput.setMessage("Caption cant be empty");

        noinput.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noinput.show();
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

    public static Album getData(){
        return albums.get(index);
    }

}
