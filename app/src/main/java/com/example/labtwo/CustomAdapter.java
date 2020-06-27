package com.example.labtwo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    private Context context;
    private Activity activity;
    private ArrayList id, show_name, ep_watched, ep_total, ratings;


    CustomAdapter(Activity activity, Context context, ArrayList id, ArrayList show_name, ArrayList ep_watched,
                  ArrayList ep_total, ArrayList ratings){
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.show_name = show_name;
        this.ep_watched = ep_watched;
        this.ep_total = ep_total;
        this.ratings = ratings;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.one_row, parent, false);
        return new MyViewHolder(view);



    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        //holder.id_txt.setText(String.valueOf(id.get(position)));
        holder.show_name_txt.setText(String.valueOf(show_name.get(position)));
        holder.ep_watched_txt.setText(String.valueOf(ep_watched.get(position)));
        holder.ep_total_txt.setText(String.valueOf(ep_total.get(position)));
        holder.ratings_txt.setText(String.valueOf(ratings.get(position)));
        //to update the information
        holder.dataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("show", String.valueOf(show_name.get(position)));
                intent.putExtra("watched", String.valueOf(ep_watched.get(position)));
                intent.putExtra("total", String.valueOf(ep_total.get(position)));
                intent.putExtra("ratings", String.valueOf(ratings.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });

    }



    @Override
    public int getItemCount()
    {
        if (id != null)
            return id.size();
        else
            return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id_txt, show_name_txt, ep_watched_txt, ep_total_txt, ratings_txt;
        LinearLayout dataLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //id_txt = itemView.findViewById(R.id.id_txt);
            show_name_txt = itemView.findViewById(R.id.show_name_txt);
            ep_watched_txt = itemView.findViewById(R.id.ep_watched_txt);
            ep_total_txt = itemView.findViewById(R.id.ep_total_txt);
            ratings_txt = itemView.findViewById(R.id.ratings_txt);
            dataLayout = itemView.findViewById(R.id.dataLayout);

        }

    }

}
