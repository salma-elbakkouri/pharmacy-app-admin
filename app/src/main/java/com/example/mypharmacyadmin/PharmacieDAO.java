package com.example.mypharmacyadmin;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PharmacieDAO {

    private DatabaseReference databaseReference;
    public PharmacieDAO()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference =db.getReference(Pharmacies_pending.class.getSimpleName());
    }

    public Task<Void> delete(String key)
    {
        return databaseReference.child(key).removeValue();
    }

}
