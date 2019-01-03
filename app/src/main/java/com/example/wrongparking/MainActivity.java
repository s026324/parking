package com.example.wrongparking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.pazeidimai_nav:

/*
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
*/
                    return false;

                case R.id.pranesti_nav:

                    Intent j = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(j);
                    finish();


                    return false;


                case R.id.manopranesimai_nav:
                    Intent k = new Intent(MainActivity.this, MyPostsActivity.class);
                    startActivity(k);
                    finish();

                    return false;
            }
            return true;
        }
    };

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Upload> itemsList;


    LinearLayoutManager layoutManager;
    public MainAdapter adapter;
    ProgressBar progress;
    public TextView noitems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Boolean isFirstRun = getSharedPreferences("FIRSTRUN", MODE_PRIVATE).getBoolean("isfirstrun", true);
        if(isFirstRun) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_firstrun, null);
            Button ok = (Button) mView.findViewById(R.id.ok);

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            getSharedPreferences("FIRSTRUN", MODE_PRIVATE).edit().putBoolean("isfirstrun",false).apply();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        final TextView noitems = (TextView) findViewById(R.id.noitems);



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(true);
        recyclerView = (RecyclerView) findViewById(R.id.rc_pazeidimai);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setVisibility(View.GONE);

        progress = (ProgressBar)findViewById(R.id.spin);
        final Wave wave = new Wave();
        progress.setIndeterminateDrawable(wave);
/*                progress = ProgressDialog.show(this, null,
                "Kraunama", true);*/


        databaseReference = FirebaseDatabase.getInstance().getReference().child("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progress.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                itemsList = new ArrayList<Upload>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Upload singleItem = dataSnapshot1.getValue(Upload.class);
                    assert singleItem != null;
                    if (singleItem.isPatvirtintas()) {
                        itemsList.add(singleItem);
                    }
                }
                adapter = new MainAdapter(MainActivity.this, itemsList);
                recyclerView.setAdapter(adapter);

                if (itemsList != null && itemsList.isEmpty()){
                    noitems.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Kažkas blogai...", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(this);
        setTitle("Fiksuoti pažeidimai");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.apie) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_pazeidimai, null);
            Button ok = (Button) mView.findViewById(R.id.ok);

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

/*                Toast.makeText(this, "bambo", Toast.LENGTH_LONG).show();*/

/*            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    //set icon
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    //set title
                    .setTitle("Fiksuoti pažeidimai")
                    //set message
                    .setMessage("Šiame lange yra matomi visi patvirtinti netaisiklingai priparkuotu transporto priemonių pranešimai")
                    //set positive button
                    .setPositiveButton("Supratau", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what would happen when positive button is clicked

                        }
                    })
                    //set negative button
                    .show();*/
        }
/*        if (id == R.id.item1){
            Toast.makeText(this, "apie aplikacija", Toast.LENGTH_LONG).show();
        }*/
        if (id == R.id.item2) {
            Intent i = new Intent(MainActivity.this, RedLogin.class);
            startActivity(i);
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
        return true;
    }
}

