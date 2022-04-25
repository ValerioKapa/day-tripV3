package com.example.day_tripv3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class fragment_firebase_add extends Fragment {

    private final Random randomID = new Random();

    private static final String TAG = "Activity Add";

    private static final String KEY_NAME = "name";
    private static final String KEY_SIRNAME = "sirname";
    private static final String KEY_ID = "id";
    private static final String KEY_HOTEL = "hotel";

    private EditText editTextName;
    private EditText editTextSirname;
    private EditText editTextTrip;
    private EditText editTextHotel;

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

        editTextName = view.findViewById(R.id.editTextTextPersonName);
        editTextTrip = view.findViewById(R.id.editTextTextPersonTrip);
        editTextSirname = view.findViewById(R.id.editTextTextPersonSirname);
        editTextHotel = view.findViewById(R.id.editTextTextPersonHotel);

        submit_button = view.findViewById(R.id.submit_new_costumer_button);
        //TODO PREVENT EMPTY ADDITIONS TO THE DATABASE, CAUSES CRASH!
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String sirname = editTextSirname.getText().toString();
                String trip = editTextTrip.getText().toString();
                String hotel = editTextHotel.getText().toString();
                String ID = (sirname+name.charAt(0)+String.valueOf(randomID.nextInt(800)+100));

                Map<String, Object> note = new HashMap<>();
                note.put(KEY_NAME, name);
                note.put(KEY_SIRNAME, sirname);
                note.put(KEY_HOTEL, hotel);
                note.put(KEY_ID, ID);

                //TODO FIX HOW COSTUMERS ARE SAVED MANY PEOPLE HAVE THE SAME NAME
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
        });
    }
}