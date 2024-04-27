package com.example.mypharmacyadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Pharmacies_pending> list;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;


    public MyAdapter(Context context, ArrayList<Pharmacies_pending> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        Pharmacies_pending pharmacy = list.get(position);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Pharmacies_pending");


        holder.txt_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.txt_option);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_confirm:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, "confirm was clicked", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                                builder.setMessage("etes vous sur de vouloir confirmer ce dossier ?");
                                builder.setTitle("Alerte");

                                AlertDialog d = builder.create();
                                d.show();
                                break;

                            case R.id.menu_remove:

                                databaseReference.orderByChild("nom")
                                        .equalTo(pharmacy.getNom()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                    String pharmaKey = childSnapshot.getKey();
                                                    PharmacieDAO pharmacieDAO = new PharmacieDAO();

                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                                    builder1.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            PharmacieDAO pharmacieDAO = new PharmacieDAO();
                                                            pharmacieDAO.delete(pharmaKey).addOnSuccessListener(suc ->
                                                            {
                                                                Intent i = new Intent(context, MainActivity.class);
                                                                context.startActivity(i);

                                                                Toast.makeText(context, "dossier supprime avec succees", Toast.LENGTH_SHORT).show();
                                                            }).addOnFailureListener(err ->
                                                            {
                                                                Toast.makeText(context, "" + err.getMessage(), Toast.LENGTH_SHORT).show();
                                                            });
                                                        }
                                                    });
                                                    builder1.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    });

                                                    builder1.setMessage("etes vous sur de vouloir supprimer ce dossier ?");
                                                    builder1.setTitle("Alerte");

                                                    AlertDialog d1 = builder1.create();
                                                    d1.show();
                                                    break;
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

//                                PharmacieDAO pharmacieDAO = new PharmacieDAO();
//
//                                pharmacieDAO.delete(pharmacy.getKey()).addOnSuccessListener(suc ->
//                                {
//                                    Intent i = new Intent(context, MainActivity.class);
//                                    context.startActivity(i);
//
//                                    Toast.makeText(context, "dossier supprime avec succees", Toast.LENGTH_SHORT).show();
//                                }).addOnFailureListener(err ->
//                                {
//                                    Toast.makeText(context, "" + err.getMessage(), Toast.LENGTH_SHORT).show();
//                                });


//

                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });


        holder.nom.setText(pharmacy.getNom());
        holder.NumPortable.setText(pharmacy.getNumPortable());
        holder.numFix.setText(pharmacy.getNumFix());
        holder.adressePharmacie.setText(pharmacy.getAdressePharmacie());
        holder.adresseEmail.setText(pharmacy.getAdresseEmail());
        if ((pharmacy.getDebutGarde()).isEmpty() && (pharmacy.getFinGarde()).isEmpty()) {
            holder.gardeText.setText("pas de garde");
            holder.gardeText.setPadding(0, 0, 0, 15);
            holder.debutGarde.setVisibility(View.GONE);
            holder.finGarde.setVisibility(View.GONE);
        } else {
            holder.gardeText.setText("garde");
            holder.debutGarde.setText(" du " + pharmacy.getDebutGarde());
            holder.finGarde.setText(" au " + pharmacy.getFinGarde());
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nom, NumPortable, numFix, adressePharmacie, adresseEmail, debutGarde, finGarde, gardeText, uid, txt_option;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nom = itemView.findViewById(R.id.nomPharmacieText);
            NumPortable = itemView.findViewById(R.id.numPortableText);
            numFix = itemView.findViewById(R.id.numFixText);
            adressePharmacie = itemView.findViewById(R.id.adressePharmacieText);
            adresseEmail = itemView.findViewById(R.id.adresseEmailText);
            debutGarde = itemView.findViewById(R.id.debutGardeText);
            finGarde = itemView.findViewById(R.id.finGardeText);
            gardeText = itemView.findViewById(R.id.gardeText);

            txt_option = itemView.findViewById(R.id.txt_option);

        }
    }

}
