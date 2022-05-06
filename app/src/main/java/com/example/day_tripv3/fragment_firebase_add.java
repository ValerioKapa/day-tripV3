package com.example.day_tripv3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class fragment_firebase_add extends Fragment implements AdapterView.OnItemSelectedListener {

    private final Random randomID = new Random();

    private static final String TAG = "Activity Add";

    private static final String KEY_NAME = "name";
    private static final String KEY_SIRNAME = "sirname";
    private static final String KEY_ID = "id";
    private static final String KEY_TRIP = "trip";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_HOTEL = "hotel";

    private EditText editTextName;
    private EditText editTextSirname;
    private Spinner editSpinnerDestination;
    private EditText editTextPhone;
    private EditText editTextHotel;

    private String trip;

    private Button submit_button;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public fragment_firebase_add() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firebase_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Destination Selection Spinner, above button
        editSpinnerDestination = view.findViewById(R.id.spinnerPersonTrip);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.destinations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editSpinnerDestination.setAdapter(adapter);

        editSpinnerDestination.setOnItemSelectedListener(this);

        editTextName = view.findViewById(R.id.editTextPersonName);
        editTextSirname = view.findViewById(R.id.editTextPersonSirname);
        editTextHotel = view.findViewById(R.id.editTextPersonHotel);
        editTextPhone = view.findViewById(R.id.editTextPersonPhone);

        submit_button = view.findViewById(R.id.submit_new_costumer_button);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String sirname = editTextSirname.getText().toString();
                String phone = editTextPhone.getText().toString();
                String hotel = editTextHotel.getText().toString();

                if (editTextName.getText().toString().isEmpty() || editTextPhone.getText().toString().isEmpty() || editTextHotel.getText().toString().isEmpty()){
                    Toast.makeText(fragment_firebase_add.this.getContext(),"Fill out all information!", Toast.LENGTH_SHORT).show();
                }else{
                    String ID = (sirname+name.charAt(0)+String.valueOf(randomID.nextInt(800)+100));

                    Map<String, Object> note = new HashMap<>();
                    note.put(KEY_NAME, name);
                    note.put(KEY_SIRNAME, sirname);
                    note.put(KEY_HOTEL, hotel);
                    note.put(KEY_PHONE, phone);
                    note.put(KEY_ID, ID);
                    note.put(KEY_TRIP, trip);

                    db.collection(trip).document(ID).set(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(fragment_firebase_add.this.getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(fragment_firebase_add.this.getContext(),"Erra!", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        trip = (String) adapterView.getItemAtPosition(i);
        //TODO remove
        Toast.makeText(fragment_firebase_add.this.getContext(), trip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}