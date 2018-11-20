package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PazeidimaiActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.pazeidimai_nav:

                    Intent i = new Intent(PazeidimaiActivity.this, PazeidimaiActivity.class);
                    startActivity(i);


                    /*                    mTextMessage.setText("homessss");*/
                    return false;
                case R.id.pranesti_nav:

                    Intent j = new Intent(PazeidimaiActivity.this, pranesti.class);
                    startActivity(j);

                    /*                    mTextMessage.setText("bam");*/

                    return false;
                case R.id.manopranesimai_nav:
                    Intent k = new Intent(PazeidimaiActivity.this, ManoPranesimaiActivity.class);
                    startActivity(k);
                    /*                    mTextMessage.setText("pisk");*/
                    return false;
            }
            return true;
        }
    };

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Upload> itemsList;

    LinearLayoutManager layoutManager;
    public PazeidimasAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pazeidimai);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

/*        View view = navigation.findViewById(R.id.pazeidimai_nav);
        view.performClick();*/
/*
        navigation.setSelectedItemId(R.id.pazeidimai_nav);*/

        layoutManager = new LinearLayoutManager(PazeidimaiActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(true);
        recyclerView = (RecyclerView) findViewById(R.id.rc_pazeidimai);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
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
                adapter = new PazeidimasAdapter(PazeidimaiActivity.this, itemsList);
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PazeidimaiActivity.this, "Kažkas blogai...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        setTitle("Fiksuoti pažeidimai");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item1) {
            Toast.makeText(this, "bambo", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.item2) {
            Intent i = new Intent(PazeidimaiActivity.this, RedLogin.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


}

