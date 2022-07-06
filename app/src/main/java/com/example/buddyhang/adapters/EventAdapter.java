package com.example.buddyhang.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buddyhang.R;
import com.example.buddyhang.models.Event;
import com.example.buddyhang.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    public Context context;
    public List<Event> events;
    public int event_position;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Event event = events.get(position);
        // setting description
        holder.eventDesc.setText(event.getEventDescription());
        // setting location
        holder.location.setText(event.getEventLocation());
        // setting event name
        holder.eventname.setText(event.getEventName());
        // setting event date
        holder.eventDate.setText(event.getEventDate());
        // setting the event host's information
        host(holder.eventHostPicture, holder.eventhost, event.getEventHost());
        // when the accept button is clicked, it needs to display in the calendar
        holder.accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Accepts").child(firebaseUser.getUid()).child(event.getEventId()).setValue(true);
                // makes the event disappear in feed after click
                event_position = holder.getAdapterPosition();
                clear(event_position);
            }
        });

        holder.decline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Declines").child(firebaseUser.getUid()).child(event.getEventId()).setValue(true);
                // makes the event disappear in feed after click
                event_position = holder.getAdapterPosition();
                clear(event_position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView eventname;
        public TextView eventhost;
        public ImageView eventHostPicture;
        public TextView eventDesc;
        public TextView location;
        public TextView eventDate;
        public Button accept_button;
        public Button decline_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventname = itemView.findViewById(R.id.eventname);
            eventhost = itemView.findViewById(R.id.eventhost);
            eventHostPicture = itemView.findViewById(R.id.eventHostPicture);
            eventDesc = itemView.findViewById(R.id.eventDesc);
            location = itemView.findViewById(R.id.location);
            eventDate = itemView.findViewById(R.id.eventDate);
            accept_button = itemView.findViewById(R.id.accept_button);
            decline_button = itemView.findViewById(R.id.decline_button);
        }
    }

    private void host (final ImageView eventHostPicture, final TextView eventhost , String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getProfilePicture()).placeholder(R.drawable.ic_baseline_person_24).into(eventHostPicture);
                eventhost.setText(user.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent , false);
        return new EventAdapter.ViewHolder(view);
    }

    public void clear (int event_position) {
        events.remove(event_position);
        notifyDataSetChanged();
    }

}