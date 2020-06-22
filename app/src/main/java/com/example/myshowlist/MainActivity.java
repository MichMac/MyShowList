package com.example.myshowlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    FirebaseUser firebaseUser;
    DatabaseReference reff;
    DatabaseReference user_reff;
    MainActivityRecyclerViewAdapter adapter;
    ShowAPI show;
    RecyclerView recyclerView;
    boolean sortTitle, sortRating, sortType;
    //vars for adapter
    private ArrayList<ShowAPI> shows = new ArrayList<>();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    User current_User = new User();
    private FirebaseAuth mAuth;
    TextView view_email, view_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(this,"firebase connection success",Toast.LENGTH_LONG).show();
        Log.i(TAG, "Firebase connection is success");

        //inicjowanie drawera
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting user from database
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //database references for show and user
        reff = FirebaseDatabase.getInstance().getReference().child("Show");
        user_reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        //Log.i(TAG,firebaseUser.getUid());

        //changing header values
        View headerView = navigationView.getHeaderView(0);
        view_email = headerView.findViewById(R.id.email_header);
        view_name = headerView.findViewById(R.id.name_header);


        //user information
        getUserDataFromFirebase();


        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recycleView_mainWindow);
        sortTitle = true;
        sortRating = true;
        sortType = true;


        navigationView.setNavigationItemSelectedListener(this);

        //if (drawerLayout == null) throw new AssertionError("drawer is null");
        //if (toolbar == null) throw new AssertionError("toolbar is null");
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();


        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
                initRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUserDataFromFirebase() {
        user_reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current_User.setEmail(dataSnapshot.child("email").getValue().toString());
                current_User.setName(dataSnapshot.child("name").getValue().toString());

                //setting view for user information
                view_name.setText(current_User.name);
                view_email.setText(current_User.email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getData(DataSnapshot dataSnapshot) {
        clearData();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Log.v(TAG, "" + postSnapshot.child("show_id").getValue());
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

    private void initRecycleView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        adapter = new MainActivityRecyclerViewAdapter(this, shows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void clearData() {
        shows.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_Titles:
                if (shows.size() >= 1 && sortTitle == true) {
                    //Toast.makeText(this,"Ascending",Toast.LENGTH_LONG).show();
                    Collections.sort(shows, (o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
                    sortTitle = false;
                } else if (shows.size() >= 1 && !sortTitle) {
                    //Toast.makeText(this,"Descending",Toast.LENGTH_LONG).show();
                    Collections.sort(shows, Collections.reverseOrder((o1, o2) -> o1.getTitle().compareTo(o2.getTitle())));
                    sortTitle = true;
                } else
                    Toast.makeText(this, "Brak produkcji do sortowania", Toast.LENGTH_LONG).show();

                initRecycleView();
                return true;

            case R.id.settings_Ratings:
                if (shows.size() >= 1 && sortRating == true) {
                    //Toast.makeText(this,"Ascending",Toast.LENGTH_LONG).show();
                    Collections.sort(shows, (o2, o1) -> o1.getRating().compareTo(o2.getRating()));
                    sortRating = false;
                } else if (shows.size() >= 1 && !sortRating) {
                    //Toast.makeText(this,"Descending",Toast.LENGTH_LONG).show();
                    Collections.sort(shows, Collections.reverseOrder((o2, o1) -> o1.getRating().compareTo(o2.getRating())));
                    sortRating = true;
                } else
                    Toast.makeText(this, "Brak produkcji do sortowania", Toast.LENGTH_LONG).show();
                initRecycleView();
                return true;

            case R.id.setting_Type:
                if (shows.size() >= 1 && sortType == true) {
                    //Toast.makeText(this,"Ascending",Toast.LENGTH_LONG).show();
                    Collections.sort(shows, (o1, o2) -> o1.getType().compareTo(o2.getType()));
                    sortType = false;
                } else if (shows.size() >= 1 && !sortType) {
                    //Toast.makeText(this,"Descending",Toast.LENGTH_LONG).show();
                    Collections.sort(shows, Collections.reverseOrder((o1, o2) -> o1.getType().compareTo(o2.getType())));
                    sortType = true;
                } else
                    Toast.makeText(this, "Brak produkcji do sortowania", Toast.LENGTH_LONG).show();
                initRecycleView();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_add_show) {
            startActivity(new Intent(getApplicationContext(), AddShow.class));
        }
        if (item.getItemId() == R.id.nav_logout) {
            Logout();
        }
        return true;
    }
}



