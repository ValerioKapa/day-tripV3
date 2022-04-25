package com.example.day_tripv3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class fragment_firebase extends Fragment implements AdapterView.OnItemSelectedListener {

    private Button button_add, button_see;
    private Spinner spinner;

    private String trip;

    public fragment_firebase() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_firebase, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Destination Selection Spinner, above button
        spinner = view.findViewById(R.id.trip_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.destinations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        button_add = view.findViewById(R.id.button_add_firestore);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmetTransaction = getChildFragmentManager().beginTransaction();

                fragmetTransaction.replace(R.id.nav_cloud_db, new fragment_firebase_add());
                fragmetTransaction.addToBackStack(null);
                fragmetTransaction.commit();
            }
        });

        button_see = view.findViewById(R.id.button_see_firestore);

        button_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_firebase_see fragment= new fragment_firebase_see();

                //Here we want to pass which trip the user chose from the spinner, and pass it to fragment_firebase_see
                Bundle bundle = new Bundle();
                bundle.putString("Destination", trip);
                fragment.setArguments(bundle);

                FragmentTransaction fragmetTransaction = getChildFragmentManager().beginTransaction();

                fragmetTransaction.replace(R.id.nav_cloud_db, fragment);
                fragmetTransaction.addToBackStack(null);
                fragmetTransaction.commit();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        trip = (String) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}