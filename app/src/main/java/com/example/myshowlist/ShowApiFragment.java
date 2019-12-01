package com.example.myshowlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ShowApiFragment extends Fragment {
    private static final String TAG = "ShowApiFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_api_fragment,container,false);
        //Toast.makeText(getActivity(),"Api Fragment",Toast.LENGTH_LONG).show();

        return view;

    }
}
