package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Redaktorius extends AppCompatActivity {

    private static final String TAG = "ViewData";

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Redatvaizdavimas redList;

    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redaktorius);
    }


    public void onClick(View k) {
        if(k.getId() == R.id.buttonout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Redaktorius.this, MainActivity.class));
            Toast.makeText(Redaktorius.this, "Atsijunget", Toast.LENGTH_LONG).show();
        }
        redList = new Redatvaizdavimas();
        mListView = (ListView) findViewById(R.id.redList);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("uploads");
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.info_upload, R.id.uploadInfo, list);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    redList = ds.getValue(Redatvaizdavimas.class);
                    list.add(redList.getName().toString());

                }
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }



    }

