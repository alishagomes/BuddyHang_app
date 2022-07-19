package com.example.buddyhang.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddyhang.models.PublicEvent;
import com.example.buddyhang.R;

import java.util.List;

public class PublicEventRecyclerViewAdapter extends RecyclerView.Adapter<PublicEventRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<PublicEvent> events;

    public PublicEventRecyclerViewAdapter(Context context, List<PublicEvent> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.ticketmaster_event_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.text_title.setText(events.get(position).getName());
        holder.text_source.setText(events.get(position).getUrl());

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_source;
        TextView text_title;



        public MyViewHolder(View itemView) {
            super(itemView);

            text_source = itemView.findViewById(R.id.text_source);
            text_title = itemView.findViewById(R.id.text_title);

        }
    }

}