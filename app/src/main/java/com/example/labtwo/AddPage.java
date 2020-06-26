package com.example.labtwo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPage extends AppCompatActivity {
    EditText show_name_input, ep_watched_input, ep_total_input;
    Spinner spinner;
    Button add_button;
    String rating;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);



        show_name_input = findViewById(R.id.show_name);
        ep_watched_input = findViewById(R.id.ep_watched);
        ep_total_input = findViewById(R.id.ep_total);
        add_button = findViewById(R.id.add_item);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rating, android.R.layout.simple_spinner_item);
        spinner = findViewById(R.id.ratings);
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



        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(AddPage.this);
                db.addShow(show_name_input.getText().toString().trim(),
                        Integer.parseInt(ep_watched_input.getText().toString().trim()),
                        Integer.parseInt(ep_total_input.getText().toString().trim()),
                        rating.trim());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sh1 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh1.getString("show_name","");
        String s2 = sh1.getString("ep_watched","");
        int p1 = sh1.getInt("ep_total",0);


        show_name_input.setText(s1);
        ep_watched_input.setText(s2);
        ep_total_input.setText(String.valueOf(p1));


    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();

        myEdit.putString("title_input", show_name_input.getText().toString().trim());
        myEdit.putString("author_input", ep_watched_input.getText().toString().trim());
        myEdit.putInt("pages_input", Integer.parseInt(ep_total_input.getText().toString().trim()));
        myEdit.commit();
    }

}
