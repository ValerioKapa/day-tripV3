package com.example.day_tripv3;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.w3c.dom.Text;

import java.util.List;

public class CostumerAdapter extends FirestoreRecyclerAdapter<Costumer, CostumerAdapter.CostumerHolder>{
    public CostumerAdapter(@NonNull FirestoreRecyclerOptions<Costumer> options) {
        super(options);
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private OnItemClickListenerDelete mListener;

    private TextInputEditText updateInfoName;
    private TextInputEditText updateInfoHotel;
    private TextInputEditText updateInfoSurname;
    private String ID;

    private Button updateInfoButton;

    @Override
    //Here we write the data from the database to each card!!
    protected void onBindViewHolder(@NonNull CostumerHolder holder, int position, @NonNull Costumer model) {

        holder.textViewName.setText(model.getName());
        holder.textViewSurname.setText(model.getSirname());
        holder.textViewHotel.setText(model.getHotel());
        holder.textViewID.setText(model.getId());

        holder.update_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(holder.textViewName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_dialog))
                        .setExpanded(true, (int)(getScreenHeight() * 0.6f))
                        .create();

                View view = dialogPlus.getHolderView();

                updateInfoName = view.findViewById(R.id.update_info_name_id);
                updateInfoSurname = view.findViewById(R.id.update_info_surname_id);
                updateInfoHotel = view.findViewById(R.id.update_info_hotel_id);

                updateInfoButton = view.findViewById(R.id.update_info_button);

                updateInfoName.setText(model.getName());
                updateInfoSurname.setText(model.getSirname());
                updateInfoHotel.setText(model.getHotel());
                ID = model.getId();

                dialogPlus.show();

                updateInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Costumer costumer = new Costumer(
                                updateInfoName.getText().toString(),
                                updateInfoSurname.getText().toString(),
                                updateInfoHotel.getText().toString(),
                                ID);

                        db.collection("Trip001").document(ID).set(costumer)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.textViewName.getContext(), "YUHH", Toast.LENGTH_LONG).show();
                                         dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.textViewName.getContext(), "NUUUUHHH", Toast.LENGTH_LONG).show();
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });

    }

    @NonNull
    @Override
    public CostumerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,
                parent,false);
        return new CostumerHolder(v, mListener);
    }

    public void setOnItemClickListenerDelete(OnItemClickListenerDelete listener){
        mListener = listener;
    }

    //Custom Listener for each card
    public interface OnItemClickListenerDelete{
        void onKlickDelete(int position);
    }

    //Here we make some variables that point to the card id's!!
    //View item, is the card's named item.xml
    class CostumerHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        TextView textViewHotel;
        TextView textViewSurname;
        TextView textViewID;

        ImageButton delete_bttn;
        ImageButton update_bttn;

        public CostumerHolder(View item, OnItemClickListenerDelete listener){
            super(item);

            textViewName = item.findViewById(R.id.TextViewName);
            textViewSurname = item.findViewById(R.id.TextViewSurname);
            textViewHotel = item.findViewById(R.id.TextViewHotel);
            textViewID = item.findViewById(R.id.TextViewID);

            update_bttn = item.findViewById(R.id.edit_button);
            delete_bttn = item.findViewById(R.id.delete_button);

            //here we get the position of the card clicked
            delete_bttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onKlickDelete(position);
                        }
                    }
                }
            });
        }
    }

    //When called it will delete the card clicked
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
