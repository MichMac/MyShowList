package com.example.myshowlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    //TYMCZASOWE ROZWIAZANIE
    private static long maxid;
    //////////////////////////

    private Context mContext;
    private List<ShowAPI> shows = new ArrayList<>();
    private DatabaseReference reff;
    private ShowAPI show;
    //private long maxid;


    public RecyclerViewAdapter(Context mContext,ArrayList<ShowAPI> shows_list) {
        this.mContext = mContext;
        this.shows = shows_list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView type;
        TextView rating;
        TextView description;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.textView_title);
            type = itemView.findViewById(R.id.textView_type);
            rating = itemView.findViewById(R.id.textView_rating);
            description = itemView.findViewById(R.id.textView_description);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + shows.get(position).getImage());

        Glide.with(mContext)
                .asBitmap()
                .load(shows.get(position).getImage())
                .into(holder.image);
        holder.title.setText(shows.get(position).getTitle());
        holder.type.setText(shows.get(position).getType());
        holder.rating.setText(shows.get(position).getRating());
        holder.description.setText(shows.get(position).getDescription());
        Log.d(TAG,"onClick: clicked on: " + shows.get(position).getTitle());

        holder.parentLayout.setOnClickListener(v -> {
            //Log.d(TAG,"onClick: clicked on: " + shows.get(position).getTitle());
            //Toast.makeText(mContext, mTitles.get(position), Toast.LENGTH_LONG).show();
            reff = FirebaseDatabase.getInstance().getReference().child("Show");

            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Log.d(TAG,"onDataChange: entry");
                        maxid=(dataSnapshot.getChildrenCount());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            String showID = reff.child("Show").push().getKey();
            shows.get(position).getImage();
            shows.get(position).getType();
            shows.get(position).getRating();
            shows.get(position).getDescription();
            shows.get(position).setShow_id(showID);

            Log.d(TAG,"key value: " + shows.get(position).getShow_id());
            //mShow_id.add(position,showID);
            //Log.d(TAG,"Increment value: " + maxid);
            reff.child(shows.get(position).getShow_id()).setValue(shows.get(position));
            //Log.d("Database Debug","tytul: "+show.getTitle() + "Typ: " + show.getType());
            Toast.makeText(mContext,"Dodałeś do ulubionych!",Toast.LENGTH_LONG).show();
        });


    }

    @Override
    public int getItemCount() {
        return shows.size();
    }


}
