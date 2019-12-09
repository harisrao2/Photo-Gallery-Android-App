package com.example.photosapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SearchScene extends AppCompatActivity {

    FloatingActionButton bs_back;
    Button b_location;
    Button b_people;
    Button b_both;
    GridView gridview;
    TextInputEditText tf_tagInput;
    PhotosAdapter adapter;

    PhotosAdapter adapterTemp;

    TextView tv_banner;

    Album album;

    Album tempalbum;
    int index;


    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.search_tag);

        bs_back = findViewById(R.id.bs_back);
        b_both = findViewById(R.id.b_both);
        b_location = findViewById(R.id.b_location);
        b_people = findViewById(R.id.b_people);
        gridview = findViewById(R.id.gv_searchResults);
        tf_tagInput = findViewById(R.id.tf_tagInput);
        //tv_banner = findViewById(R.id.tv_banner);

       // tv_banner.setText("Showing All photos");



        album = PhotosScene.getData();
        tempalbum = album;
        adapter = new PhotosAdapter(this,tempalbum.getPhotoList());
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
                searchWithPeople();
            }
        });

        b_both.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                searchWithBoth();
            }
        });



    }


    public void openPhoto( int position){
        Intent intent = new Intent(SearchScene.this, DisplayPhoto.class);
        intent.putExtra("indexp", String.valueOf(position));
        startActivity(intent);
    }
    public void promptRename(final int position){
        AlertDialog.Builder rename = new AlertDialog.Builder(this);
        rename.setTitle("Rename photo \""+album.getPhotoList().get(position).getCaption()+"\" to :");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        rename.setView(input);

        rename.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                album.getPhotoList().get(position).setCaption(input.getText().toString());
                adapter.notifyDataSetChanged();

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

    public void searchWithPeople(){
        System.out.println("Search with Both");
        String input = tf_tagInput.getText().toString();
        tempalbum = new Album("search results", new ArrayList<Photo>());

        if(input.matches("")){
            AlertDialog.Builder noinput = new AlertDialog.Builder(this);
            noinput.setMessage("Enter a tag int he text field and then hit the button");

            noinput.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            noinput.show();

        }else{
            for (int i = 0; i< album.getPhotoList().size();i++){
                for (int j = 0 ; j<album.getPhotoList().get(i).getPeople().size();j++){
                    if(album.getPhotoList().get(i).getPeople().get(j).contains(input)){
                        tempalbum.getPhotoList().add(album.getPhotoList().get(i));
                        break;
                        //System.out.println("ADDING ::::::::::::::::::::::::::::::" +album.getPhotoList().get(i).getCaption() );
                    }else{
                        //System.out.println("CANT GET IN ::::::::::::::::");
                    }
                }
            }
            adapterTemp = new PhotosAdapter(this, tempalbum.getPhotoList());
            gridview.setAdapter(adapterTemp);
            adapter.notifyDataSetChanged();
            //tv_banner.setText("Showing photos with the location/person tag of \"" + input + "\"");
        }
    }

    public void searchWithBoth(){
        String input = tf_tagInput.getText().toString();
        tempalbum = new Album("search results", new ArrayList<Photo>() );

        if(input.matches("")){
            AlertDialog.Builder noinput = new AlertDialog.Builder(this);
            noinput.setMessage("Enter a tag in the text field and then hit the button");

            noinput.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            noinput.show();
        }else{

            for (int i = 0; i< album.getPhotoList().size();i++){
                for (int j = 0 ; j<album.getPhotoList().get(i).getPeople().size();j++){
                    if(album.getPhotoList().get(i).getPeople().get(j).contains(input)){
                        tempalbum.getPhotoList().add(album.getPhotoList().get(i));
                        break;
                        //System.out.println("ADDING ::::::::::::::::::::::::::::::" +album.getPhotoList().get(i).getCaption() );
                    }else{
                        //System.out.println("CANT GET IN ::::::::::::::::");
                    }
                }
            }

            for (int i = 0; i < album.getPhotoList().size(); i++) {
                if (album.getPhotoList().get(i).getLocation().contains(input)) {
                    tempalbum.getPhotoList().add(album.getPhotoList().get(i));
                }
            }
            adapterTemp = new PhotosAdapter(this, tempalbum.getPhotoList());
            gridview.setAdapter(adapterTemp);
            adapter.notifyDataSetChanged();
            //tv_banner.setText("Showing photos with the person tag of \"" + input + "\"");

        }
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
           // tv_banner.setText("Showing photos with the location tag of \"" + input + "\"");
        }

    }
}
