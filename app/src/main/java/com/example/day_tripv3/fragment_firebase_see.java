package com.example.day_tripv3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class fragment_firebase_see extends Fragment{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef;

    private CostumerAdapter adapterCostumer;

    private RecyclerView recyclerView;

    public fragment_firebase_see() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_firebase_see, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_viou);

        //Bundle receives data from fragment_firebase, inside we have which trip the user chose to see
        Bundle bundle = getArguments();
        String destination = bundle.getString("Destination");

        notebookRef = db.collection(destination);

        Query query = notebookRef.orderBy("name",Query.Direction.DESCENDING);

        //System.out.println("should be an ID : "+notebookRef.getId());
        //THIS PRINTS OUT Trip001
        FirestoreRecyclerOptions<Costumer> options = new FirestoreRecyclerOptions.Builder<Costumer>()
                .setQuery(query, Costumer.class)
                .build();

        adapterCostumer = new CostumerAdapter((options));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterCostumer);

        //Here the listener becomes active and calls the actual delete function from CostumerAdapter
        adapterCostumer.setOnItemClickListenerDelete(new CostumerAdapter.OnItemClickListenerDelete() {
            @Override
            public void onKlickDelete(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(fragment_firebase_see.this.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Data cannot be recovered after Deletion!");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapterCostumer.deleteItem(position);
                        Toast.makeText(fragment_firebase_see.this.getContext(), "Deleted Successfully.", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(fragment_firebase_see.this.getContext(), "Cancelled.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        adapterCostumer.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        adapterCostumer.stopListening();
    }
}