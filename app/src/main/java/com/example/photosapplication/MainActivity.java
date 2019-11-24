package com.example.photosapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String albumName = "";

    ArrayList<String> albumNames = new ArrayList<String>();
    ArrayList<Album> albums = new ArrayList<Album>();

    ListView lv_albums; // = (ListView) findViewById(R.id.lv_albums);

    FloatingActionButton b_newAlbum;// = findViewById(R.id.b_newAlbum);

    ArrayAdapter<String> adapter;// = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, albumNames);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv_albums = (ListView) findViewById(R.id.lv_albums);
        b_newAlbum = findViewById(R.id.b_newAlbum);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, albumNames);

        lv_albums.setAdapter(adapter);

        b_newAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAlbum();


            }
        });
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
                Album a = new Album(albumName);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
