package com.example.fuse2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {


    //instance variables
    Context context;
    ArrayList list;

    //constructor for the adapter
    public ChatsAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatsAdapter.ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new ChatsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ChatsViewHolder holder, int position) {

        HashMap<String, String> map = (HashMap<String, String>) list.get(position);

        holder.chat_name.setText(map.get("name")+" "+map.get("surname"));

        holder.messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getIntochat(map.get("name")+""+map.get("surname"));

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatsViewHolder extends RecyclerView.ViewHolder {

        //item_chats UI variables
        TextView chat_name;
        Button messageBtn;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            //instantiating the UI variables
            chat_name = itemView.findViewById(R.id.chat_name);
            messageBtn = itemView.findViewById(R.id.chat_message_btn);

        }
    }


    public void getIntochat(String friend_name){

        Intent intent = new Intent(context, Messages.class);
        intent.putExtra("friend_name",friend_name );
        context.startActivity(intent);

    }}
