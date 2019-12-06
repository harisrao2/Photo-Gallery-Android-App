package com.example.photosapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String albumName = "";

    ArrayList<String> albumNames = new ArrayList<String>();
    static ArrayList<Album> albums = new ArrayList<Album>();

    ListView lv_albums; // = (ListView) findViewById(R.id.lv_albums);

    FloatingActionButton b_newAlbum;// = findViewById(R.id.b_newAlbum);

    FloatingActionButton b_save;


    ArrayAdapter<String> adapter;// = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, albumNames);


    SharedPreferences sharedpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        lv_albums = (ListView) findViewById(R.id.lv_albums);
        b_newAlbum = findViewById(R.id.b_newAlbum);
        b_save = findViewById(R.id.bp_save);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, albumNames);
        lv_albums.setAdapter(adapter);

        load();
        loadListView();
        System.out.println("printing after load : ");
        printAlbums();

        b_newAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAlbum();
            }
        });

        b_save.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                save();
                finish();
            }
        });


        lv_albums.setOnItemClickListener(
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long id){
                    // making alert for Album options
                    showOptions(position);
                }
            });
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

    public void printAlbums(){
        for (int i = 0;i<albums.size();i++){
            System.out.print("i: " + albums.get(i).getAlbumName()+", ");
        }

    }

    public void loadListView(){
        for (int i = 0 ;i<albums.size();i++){
            adapter.add(albums.get(i).getAlbumName());
        }
        adapter.notifyDataSetChanged();
    }

    public void showOptions(final int position){
        AlertDialog.Builder optionsAlert = new AlertDialog.Builder(this);
        optionsAlert.setTitle("Choose an option for album \""+albums.get(position).getAlbumName()+"\"");


        optionsAlert.setNeutralButton("Open", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //save();
                openAlbum(position);
            }
        });

        optionsAlert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.remove(albums.get(position).getAlbumName());
                albums.remove(position);
            }
        });

        optionsAlert.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                promptRename(position);
            }
        });


        optionsAlert.show();
    }

    public void openAlbum(int position){
        Intent intent = new Intent(MainActivity.this, PhotosScene.class);
        intent.putExtra("index", String.valueOf(position));
        startActivity(intent);
    }

    public void promptRename(final int position){
        AlertDialog.Builder rename = new AlertDialog.Builder(this);
        rename.setTitle("Rename album \""+albums.get(position).getAlbumName()+"\" to :");

    final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        rename.setView(input);

        rename.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            adapter.remove(albums.get(position).getAlbumName());
            albums.get(position).setAlbumName(input.getText().toString());
            adapter.insert(albums.get(position).getAlbumName(),position);

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

    public void createNewAlbum(){
        //making a new Alert for Create new Album
        AlertDialog.Builder newAlbumAlert = new AlertDialog.Builder(this);
        newAlbumAlert.setTitle("Create a new Album");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        newAlbumAlert.setView(input);

        //making buttons for alert
        newAlbumAlert.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

                albumName = input.getText().toString();
                System.out.println(albumName);
                Album a = new Album(albumName, new ArrayList<Photo>() );
                albums.add(a);
                adapter.add(a.getAlbumName());
                adapter.notifyDataSetChanged();

            }
        });

        newAlbumAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
            }
        });
        newAlbumAlert.show();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings  ) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static ArrayList<Album> getData(){
        return albums;
    }

}
