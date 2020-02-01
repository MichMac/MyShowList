package com.example.myshowlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ShowCustomFragment extends Fragment {
    private static final String TAG = "ShowCustomFragment";
    EditText textTitle,textEpisodes,textDescription;
    Spinner spinnerType,spinnerStatus,spinnerRating;
    Button btnDodaj;
    DatabaseReference reff;
    ShowAPI show;
    List type,status,rating;
    String itemType,itemStatus,itemRating;
    ArrayAdapter<String> typeAdapter,statusAdapter;
    ArrayAdapter<Integer> ratingAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_custom_fragment,container,false);
        //Toast.makeText(getActivity(),"Custom Fragment",Toast.LENGTH_LONG).show();
        textTitle= (EditText)view.findViewById(R.id.title_edit);
        spinnerType= (Spinner)view.findViewById(R.id.type_spinner);
        spinnerRating= (Spinner) view.findViewById(R.id.rating_spinner);
        textDescription= (EditText)view.findViewById(R.id.description_edit);
        btnDodaj = (Button)view.findViewById(R.id.dodaj_custom_button);

        type = new ArrayList<>();
        type.add(0,"Wprowadź typ produkcji");
        type.add("movie");
        type.add("tv");

        rating = new ArrayList<>();
        rating.add(0,"Wprowadź swoją ocenę");
        rating.add("10");
        rating.add("9");
        rating.add("8");
        rating.add("7");
        rating.add("6");
        rating.add("5");
        rating.add("4");
        rating.add("3");
        rating.add("2");
        rating.add("1");

        //Toast.makeText(getContext(),"Element 0: "+ show.getType(),Toast.LENGTH_LONG).show();

        //Style and populate the spinner
        ArrayAdapter<String> dataAdatper;
        typeAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,type);;
        ratingAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,rating);


        //Dropwdown layout style
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(typeAdapter);
        spinnerRating.setAdapter(ratingAdapter);




        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Wprowadź typ produkcji"))
                {
                    // do nothing
                }
                else
                {
                    // on selecting spinner item
                    itemType = parent.getItemAtPosition(position).toString();
                    //Toast.makeText(parent.getContext(),"Selected: " +itemType, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Wprowadź typ produkcji"))
                {
                    //Toast.makeText(parent.getContext(),"Nie wybrano typu produkcji", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // on selecting spinner item
                    itemRating = parent.getItemAtPosition(position).toString();
                    //Toast.makeText(parent.getContext(),"Selected: " +itemRating, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reff = FirebaseDatabase.getInstance().getReference().child("Show");
                show = new ShowAPI();
                int rating = Integer.parseInt(itemRating.trim());

                String showID = reff.child("Show").push().getKey();
                show.setTitle(textTitle.getText().toString().trim());
                show.setType(itemType);
                show.setRating(String.valueOf(rating));
                show.setImage(" ");
                show.setDescription(textDescription.getText().toString());
                show.setShow_id(showID);
                //reff.push().setValue(show);
                reff.child(show.getShow_id()).setValue(show);
                //Log.d("Database Debug","tytul: "+show.getTitle() + "Typ: " + show.getType());
                Toast.makeText(getContext(),"data inserted sucessfully",Toast.LENGTH_LONG).show();
            }
        });
        //Log.d("Tytul: ",textTitle);
        return view;
    }

}
