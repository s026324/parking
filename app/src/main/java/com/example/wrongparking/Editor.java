package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Editor extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Upload> itemsList;
    public static Recycler adapter;
    LinearLayoutManager layoutManager;
    TextView noitems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        final TextView noitems = (TextView) findViewById(R.id.noitems);



        Spinner mySpinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Editor.this,
        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.redaktorius_aktyvuoti));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //LAUKIANTYS PATVIRTINIMO.
                        layoutManager = new LinearLayoutManager(Editor.this, LinearLayoutManager.VERTICAL, false);
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
                                adapter = new Recycler(Editor.this,itemsList, Constants.TYPE_NEPATVIRTINIT);
                                recyclerView.setAdapter(adapter);

                                if (itemsList != null && itemsList.isEmpty()){
                                    noitems.setVisibility(View.VISIBLE);
                                    noitems.setText("Nepatvirtintų pažeidimų nėra");
                                    recyclerView.setVisibility(View.GONE);
                                }else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    noitems.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(Editor.this, "Klaida", Toast.LENGTH_SHORT).show();
                            }
                        });


                    break;

                    case 1:
                        //PATVIRTINTI
                        layoutManager = new LinearLayoutManager(Editor.this, LinearLayoutManager.VERTICAL, false);
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
                                adapter = new Recycler(Editor.this,itemsList, Constants.TYPE_PATVIRTINTI);
                                recyclerView.setAdapter(adapter);

                                if (itemsList != null && itemsList.isEmpty()){
                                    noitems.setVisibility(View.VISIBLE);
                                    noitems.setText("Patvirtintų pažeidimų nėra");
                                    recyclerView.setVisibility(View.GONE);
                                }else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    noitems.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(Editor.this, "Klaida", Toast.LENGTH_SHORT).show();
                            }
                        });


                        break;


                    case 2:
                        //ATMESTI
                        layoutManager = new LinearLayoutManager(Editor.this, LinearLayoutManager.VERTICAL, false);
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
                                    if (!singleItem.isPatvirtintas() && singleItem.isPerziuretas()) {
                                        itemsList.add(singleItem);
                                    }
                                }
                                adapter = new Recycler(Editor.this,itemsList, Constants.TYPE_ATMESTI);
                                recyclerView.setAdapter(adapter);

                                if (itemsList != null && itemsList.isEmpty()){
                                    noitems.setVisibility(View.VISIBLE);
                                    noitems.setText("Atmestų pažeidimų nėra");
                                    recyclerView.setVisibility(View.GONE);
                                }else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    noitems.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(Editor.this, "Klaida", Toast.LENGTH_SHORT).show();
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
    private void filter(String text){


        ArrayList<Upload> filteredList = new ArrayList<>();

        for (Upload item : itemsList) {

            text = text.toLowerCase();

            if(item.getName().toLowerCase().contains(text) ||
                    item.getValstnum().toLowerCase().contains(text) ||
                    item.getAddress().toLowerCase().contains(text) ){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_redaktoriui, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Paieška...");
        setTitle("Redaktoriaus pultas");
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
            Toast.makeText(Editor.this, "Atsijungėte", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText.toString());
        return false;
    }
}