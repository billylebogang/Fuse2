package com.example.fuse2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ReserveViewHolder> {

    Context context;
    ArrayList<HashMap> list;



    //constructor

    public NotificationsAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationsAdapter.ReserveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemreserves, parent, false);
        return new ReserveViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ReserveViewHolder holder, int position) {


        Map usr = list.get(position);


        //method to remove the match with the remove button
        Object[] keys = usr.keySet().toArray();

        holder.reservenames.setText(usr.get("name")+" "+usr.get("surname"));
        holder.reserveAge.setText(usr.get("email").toString());
        holder.reserveLocation.setText(usr.get("location").toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReserveViewHolder extends RecyclerView.ViewHolder{

        TextView reserveLocation, reserveAge, reservenames;
        ImageView userdp;
        Button removeMatchBtn;

        public ReserveViewHolder(@NonNull View itemView) {
            super(itemView);

            //matchView
            reserveAge = itemView.findViewById(R.id.reserveAge);
            reserveLocation = itemView.findViewById(R.id.reserveLocation);
            reservenames = itemView.findViewById(R.id.reservenames);

            removeMatchBtn = itemView.findViewById(R.id.removeMatchBtn1);
        }


    }
}
