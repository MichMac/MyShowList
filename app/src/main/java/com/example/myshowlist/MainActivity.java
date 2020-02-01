package com.example.myshowlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    DatabaseReference reff;
    MainActivityRecyclerViewAdapter adapter;
    String showID;
    ShowAPI show;
    FirebaseDatabase mFirebaseDatabase;
    RecyclerView recyclerView;

    //vars for adapter
    private ArrayList<ShowAPI> shows = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings_addShow:
                startActivity(new Intent(MainActivity.this, AddShow.class));
                return true;

            case R.id.settings_Titles:
                Toast.makeText(this,"Sortowanie po tytułach!",Toast.LENGTH_LONG).show();
                if (shows.size() > 0) {
                    Collections.sort(shows, new Comparator<ShowAPI>() {
                        @Override
                        public int compare(ShowAPI o1, ShowAPI o2) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                    });
                }
                initRecycleView();
                return true;

            case R.id.settings_Ratings:
                if (shows.size() > 0) {
                    Collections.sort(shows, new Comparator<ShowAPI>() {
                        @Override
                        public int compare(ShowAPI o1, ShowAPI o2) {
                            return o1.getRating().compareTo(o2.getRating());
                        }
                    });
                }
                initRecycleView();
                return true;

            case R.id.setting_Type:
                if (shows.size() > 0) {
                    Collections.sort(shows, new Comparator<ShowAPI>() {
                        @Override
                        public int compare(ShowAPI o1, ShowAPI o2) {
                            return o1.getType().compareTo(o2.getType());
                        }
                    });
                }
                initRecycleView();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"firebase connection success",Toast.LENGTH_LONG).show();


        recyclerView = findViewById(R.id.recycleView_mainWindow);
        reff = FirebaseDatabase.getInstance().getReference().child("Show");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.i(TAG,"onDataChange weszlo");
                //Log.i(TAG, "onDataChange: " + dataSnapshot.child("1").child("title").getValue().toString());
                //Log.i(TAG,"onDataChange childrenCounter: " + dataSnapshot.getChildrenCount());
                getData(dataSnapshot);

                initRecycleView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getData(DataSnapshot dataSnapshot) {
        clearData();

        for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            Log.v(TAG,""+ postSnapshot.child("show_id").getValue());
            show = new ShowAPI();
            show.setTitle(postSnapshot.child("title").getValue().toString());
            show.setImage(postSnapshot.child("image").getValue().toString());
            show.setShow_id(postSnapshot.child("show_id").getValue().toString());
            show.setType(postSnapshot.child("type").getValue().toString());
            show.setRating(postSnapshot.child("rating").getValue().toString());
            show.setDescription(postSnapshot.child("description").getValue().toString());

            shows.add(show);
        }

    }
    private void initRecycleView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        adapter = new MainActivityRecyclerViewAdapter(this, shows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void clearData(){

        shows.clear();
    }

}



