package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Redaktorius extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Upload> itemsList;
    public static Recycler adapter;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redaktorius);


        layoutManager = new LinearLayoutManager(Redaktorius.this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView = (RecyclerView) findViewById(R.id.reda);
        recyclerView.setLayoutManager(layoutManager);


        reference = FirebaseDatabase.getInstance().getReference().child("uploads");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsList = new ArrayList<Upload>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Upload p = dataSnapshot1.getValue(Upload.class);
                    assert p != null;
                    if(!p.isPerziuretas()){
                        itemsList.add(p);
                    }

                }
                 adapter = new Recycler(Redaktorius.this,itemsList);
                recyclerView.setAdapter(adapter);
            }



        @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Redaktorius.this, "Klaida", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    public void onClick(View k) {
        if(k.getId() == R.id.buttonout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Redaktorius.this, "Atsijunget", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        }
}