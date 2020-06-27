package com.example.labtwo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


public class DataPage extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView nodata;
    FloatingActionButton add;

    DatabaseHelper db;
    ArrayList<String> id, show_name, ep_watched, ep_total, ratings;
    CustomAdapter customAdapter;

    ImageButton header;
    private static final int RESULT_LOAD_IMAGE = 2;
    Uri image = null;

    SharedPreferences myPrefs;
    SharedPreferences.Editor editor;

    Boolean check;
    String string;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_data);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        nodata = (TextView) findViewById(R.id.no_data);

        add = (FloatingActionButton) findViewById(R.id.addbtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DataPage.this, AddPage.class);
                startActivity(myIntent);
            }
        });

        db = new DatabaseHelper(DataPage.this);
        id = new ArrayList<>();
        show_name = new ArrayList<>();
        ep_watched = new ArrayList<>();
        ep_total = new ArrayList<>();
        ratings = new ArrayList<>();
        header = findViewById(R.id.imageButton2);





        storeDataInArrays();

        myPrefs = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = myPrefs.edit();



        if (ContextCompat.checkSelfPermission(
                DataPage.this,  Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {



            String string = myPrefs.getString("image", null);
            check = myPrefs.contains("image");
            if (check) {
                //imageView.setImageURI(uri);


                header.setImageURI(Uri.parse(string));
            }
        }

        customAdapter = new CustomAdapter(DataPage.this, this, id, show_name, ep_watched, ep_total, ratings);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DataPage.this));

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageButton.setImageResource(R.drawable.bg1);
                if (ContextCompat.checkSelfPermission(
                        DataPage.this,  Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {


                    Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, RESULT_LOAD_IMAGE);
                } else {
                    // You can directly ask for the permission.
                    ActivityCompat.requestPermissions(DataPage.this,
                            new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }



            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
        if(requestCode == 2 && resultCode == RESULT_OK && data != null) {
            image = data.getData();
            header.setImageURI(image);
            editor.putString("image", image.toString());
            editor.commit();


        }

    }


    void storeDataInArrays(){
        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            nodata.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                show_name.add(cursor.getString(1));
                ep_watched.add(cursor.getString(2));
                ep_total.add(cursor.getString(3));
                ratings.add(cursor.getString(4));
            }
            nodata.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDialog();
        }
        else if (item.getItemId() == R.id.refresh) {
            Intent refresh = new Intent(this, DataPage.class);
            startActivity(refresh);//Start the same Activity
            finish(); //finish Activity.
        }
        return super.onOptionsItemSelected(item);

    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = new DatabaseHelper(DataPage.this);
                db.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(DataPage.this, DataPage.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

}

