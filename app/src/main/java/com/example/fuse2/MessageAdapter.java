package com.example.fuse2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    //instance variables
    Context context;
    ArrayList list;

    //constructor


    public MessageAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {

        Message message = (Message) list.get(position);

        holder.message_content.setText(message.getContent());
        holder.date.setText(message.getDate());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        //item_messages view variables
        TextView message_content, time, date;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            //instantiating the variables
            message_content = itemView.findViewById(R.id.message_content);
            time = itemView.findViewById(R.id.message_time);
            date = itemView.findViewById(R.id.message_date);
        }
    }
}
