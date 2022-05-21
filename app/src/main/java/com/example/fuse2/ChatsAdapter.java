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

        //at first i used the hash map here, and was crappy.. note! intention was to get proper names


        //this is the data that will be passed from the activity that fetches data
        String map = (String) list.get(position);

        //setting the name of the user name
        holder.chat_name.setText(map);

        //this will take u to the messages of the target user
        holder.messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //function called here and passing the string of the user_name of target
                getIntochat(map);

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
        //function that takes you to the messages from the chat section
        Intent intent = new Intent(context, Messages.class);

        //this intent will be used to get messages of the user
        intent.putExtra("friend_name",friend_name );
        context.startActivity(intent);

    }

}
