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
    Show show;
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
        textEpisodes= (EditText)view.findViewById(R.id.episodes_edit);
        spinnerStatus= (Spinner) view.findViewById(R.id.status_spinner);
        spinnerRating= (Spinner) view.findViewById(R.id.rating_spinner);
        textDescription= (EditText)view.findViewById(R.id.description_edit);
        btnDodaj = (Button)view.findViewById(R.id.dodaj_custom_button);
        show = new Show();
        reff = FirebaseDatabase.getInstance().getReference().child("Show"); //tworzenie instancji firebase i pobrania informacji z obiektu show

        type = new ArrayList<>();
        type.add(0,"Wprowadź typ produkcji");
        type.add("Film");
        type.add("Serial");
        type.add("Bajka");

        status = new ArrayList<>();
        status.add(0,"Wprowadź status produkcji");
        status.add("Skończone");
        status.add("Planowane");
        status.add("Wstrzymane");
        status.add("Oczekujące");

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
        typeAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,type);
        statusAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,status);
        ratingAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,rating);


        //Dropwdown layout style
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(typeAdapter);
        spinnerRating.setAdapter(ratingAdapter);
        spinnerStatus.setAdapter(statusAdapter);



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
                    Toast.makeText(parent.getContext(),"Selected: " +itemType, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Wprowadź typ produkcji"))
                {
                    // do nothing
                }
                else
                {
                    // on selecting spinner item
                    itemStatus = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Selected: " +itemStatus, Toast.LENGTH_LONG).show();

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
                    // do nothing
                }
                else
                {
                    // on selecting spinner item
                    itemRating = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Selected: " +itemRating, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int episodes = Integer.parseInt(textEpisodes.getText().toString().trim());
                int rating = Integer.parseInt(itemRating.trim());

                show.setTitle(textTitle.getText().toString().trim());
                show.setType(itemType);
                show.setEpisodes(episodes);
                show.setStatus(itemStatus);
                show.setRating(rating);
                show.setDescription(textDescription.getText().toString());
                reff.push().setValue(show);
                //Log.d("Database Debug","tytul: "+show.getTitle() + "Typ: " + show.getType());
                Toast.makeText(getContext(),"data inserted sucessfully",Toast.LENGTH_LONG).show();
            }
        });
        //Log.d("Tytul: ",textTitle);
        return view;
    }

}
