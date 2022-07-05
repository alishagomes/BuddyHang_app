package com.example.buddyhang.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddyhang.R;
import com.example.buddyhang.fragments.ProfileFragment;
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
    public TextView eventhost;
    public TextView eventDesc;
    public TextView location;

    private FirebaseUser firebaseUser;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Event post = events.get(position);

        // setting description
        if (post.getEventDescription().equals("")){
            holder.eventDesc.setVisibility(View.GONE);
        } else {
            holder.eventDesc.setVisibility(View.VISIBLE);
            holder.eventDesc.setText(post.getEventDescription());
        }

        // setting location
        if (post.getEventLocation().equals("")){
            holder.location.setVisibility(View.GONE);
        } else {
            holder.location.setVisibility(View.VISIBLE);
            holder.location.setText(post.getEventLocation());
        }

        if (post.getEventName().equals("")){
            holder.eventname.setVisibility(View.GONE);
        } else {
            holder.eventname.setVisibility(View.VISIBLE);
            holder.eventname.setText(post.getEventName());
        }


        host(holder.eventHostPicture , holder.eventhost , holder.eventhost , post.getEventHost());

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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventname = itemView.findViewById(R.id.eventname);
            eventhost = itemView.findViewById(R.id.eventhost);
            eventHostPicture = itemView.findViewById(R.id.eventHostPicture);
            eventDesc = itemView.findViewById(R.id.eventDesc);
            location = itemView.findViewById(R.id.location);
        }
    }


    private void host (final ImageView image_profile , final TextView username , final TextView eventhost , String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getProfilePicture()).placeholder(R.drawable.ic_baseline_person_24).into(image_profile);
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

}