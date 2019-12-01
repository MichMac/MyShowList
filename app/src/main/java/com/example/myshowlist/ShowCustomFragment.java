package com.example.myshowlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ShowCustomFragment extends Fragment {
    private static final String TAG = "ShowCustomFragment";
    EditText textTitle,textType,textEpisodes
            ,textStatus,textRating,textDescription;
    Button btnDodaj;
    DatabaseReference reff;
    Show show;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_custom_fragment,container,false);
        //Toast.makeText(getActivity(),"Custom Fragment",Toast.LENGTH_LONG).show();
        textTitle= (EditText)view.findViewById(R.id.title_edit);
        textType= (EditText)view.findViewById(R.id.type_edit);
        textEpisodes= (EditText)view.findViewById(R.id.episodes_edit);
        textStatus= (EditText)view.findViewById(R.id.status_edit);
        textRating= (EditText)view.findViewById(R.id.rating_edit);
        textDescription= (EditText)view.findViewById(R.id.description_edit);
        btnDodaj = (Button)view.findViewById(R.id.dodaj_custom_button);

        show = new Show();
        reff = FirebaseDatabase.getInstance().getReference().child("Show");
        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show.setTitle(textTitle.getText().toString().trim());
                show.setType(textTitle.getText().toString().trim());
                show.setEpisodes(textEpisodes.getText().toString().trim());
                show.setStatus(textStatus.getText().toString().trim());
                show.setRating(textRating.getText().toString().trim());
                show.setDescription(textDescription.getText().toString());
                reff.push().setValue(show);
                Toast.makeText(getContext(),"data inserted sucessfully",Toast.LENGTH_LONG).show();
            }
        });
        //Log.d("Tytul: ",textTitle);
        return view;
    }

}
