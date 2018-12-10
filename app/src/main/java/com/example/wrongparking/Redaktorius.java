package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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


        /*reference = FirebaseDatabase.getInstance().getReference().child("uploads");

        reference.child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> adresas = new ArrayList<String>();
                for (DataSnapshot adresSnapshot: dataSnapshot.getChildren()){
                    String adresName = adresSnapshot.child("address").getValue(String.class);
                    adresas.add(adresName);
                }
                Spinner adresasSpinner = (Spinner) findViewById(R.id.spinnerAdresas);
                ArrayAdapter<String> adresasAdapter = new ArrayAdapter<String>(Redaktorius.this, R.layout.support_simple_spinner_dropdown_item, adresas);
                adresasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                adresasSpinner.setAdapter(adresasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        Spinner mySpinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Redaktorius.this,
        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.redaktorius_aktyvuoti));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);



        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //Nepatvirtinti
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
                                adapter = new Recycler(Redaktorius.this,itemsList, Constants.TYPE_NEPATVIRTINIT);
                                recyclerView.setAdapter(adapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(Redaktorius.this, "Klaida", Toast.LENGTH_SHORT).show();
                            }
                        });


                    break;

                    case 1:
                        //PATVIRTINTI
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
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Upload singleItem = dataSnapshot1.getValue(Upload.class);
                                    assert singleItem != null;
                                    if (singleItem.isPatvirtintas()) {
                                        itemsList.add(singleItem);
                                    }
                                }
                                adapter = new Recycler(Redaktorius.this,itemsList, Constants.TYPE_PATVIRTINTI);
                                recyclerView.setAdapter(adapter);
                            }



                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(Redaktorius.this, "Klaida", Toast.LENGTH_SHORT).show();
                            }
                        });


                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_redaktoriui, menu);
        setTitle("Redaktoriaus pultas");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(getApplicationContext(), PazeidimaiActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Redaktorius.this, "Atsijungėte", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), PazeidimaiActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }


/*    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }*/
/*
    public void onClick(View k) {
        if(k.getId() == R.id.buttonout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Redaktorius.this, "Atsijunget", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), PazeidimaiActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        }*/
}