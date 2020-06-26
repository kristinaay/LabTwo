package com.example.labtwo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText show_input, ep_watched_input, ep_total_input;
    Spinner spinner;
    Button update_button, delete_button;

    String id, name, watched, total, rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        show_input = findViewById(R.id.show_name_2);
        ep_watched_input = findViewById(R.id.ep_watched_2);
        ep_total_input = findViewById(R.id.ep_total_2);
        spinner = findViewById(R.id.ratings_2);
        update_button = findViewById(R.id.update_item);
        delete_button = findViewById(R.id.delete_item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rating, android.R.layout.simple_spinner_item);
        spinner = findViewById(R.id.ratings_2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                rating = String.valueOf(spinner.getSelectedItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(name);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                DatabaseHelper db = new DatabaseHelper(UpdateActivity.this);
                name = show_input.getText().toString().trim();
                watched = ep_watched_input.getText().toString().trim();
                total = ep_total_input.getText().toString().trim();
                rating = rating.trim();

                db.updateData(id, name, watched, total, rating);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("show") &&
                getIntent().hasExtra("watched") && getIntent().hasExtra("total")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("show");
            watched = getIntent().getStringExtra("watched");
            total = getIntent().getStringExtra("total");
            //rating = getIntent().getStringExtra("rating");


            //Setting Intent Data
            show_input.setText(name);
            ep_watched_input.setText(watched);
            ep_total_input.setText(total);
            Log.d("stev", name+" "+watched+" "+total+" ");
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = new DatabaseHelper(UpdateActivity.this);
                db.deleteOneRow(id);
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

