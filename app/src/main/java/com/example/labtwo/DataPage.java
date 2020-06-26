package com.example.labtwo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class DataPage extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView nodata;
    FloatingActionButton add;

    DatabaseHelper db;
    ArrayList<String> id, show_name, ep_watched, ep_total, ratings;
    CustomAdapter customAdapter;




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

        storeDataInArrays();

        customAdapter = new CustomAdapter(DataPage.this, this, id, show_name, ep_watched, ep_total, ratings);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DataPage.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
        if(requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            CustomAdapter.getIB().setImageURI(image);
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

