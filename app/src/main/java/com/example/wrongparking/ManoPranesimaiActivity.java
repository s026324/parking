package com.example.wrongparking;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.prefs.Prefs;

public class ManoPranesimaiActivity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {


    public static final String PREFS_KEY_PAZEIDIMAI = "pazeidimai_list";

    private String pazeidimaiJson = "";
    Gson gson ;
    RecyclerView recyclerView;
    ArrayList<Upload> itemsList;

    LinearLayoutManager layoutManager;
    public PazeidimasAdapter adapter;
    private Button button;
    public SharedPreferences sharedPref;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pazeidimai_nav:

                    Intent i = new Intent(ManoPranesimaiActivity.this, PazeidimaiActivity.class);
                    startActivity(i);

                    return true;
                case R.id.pranesti_nav:
                    Intent j = new Intent(ManoPranesimaiActivity.this, pranesti.class);
                    startActivity(j);

                    return true;
                case R.id.manopranesimai_nav:


                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mano_pranesimai);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        View view = navigation.findViewById(R.id.manopranesimai_nav);
        view.performClick();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(true);
        recyclerView = (RecyclerView) findViewById(R.id.rc_pazeidimai);
        recyclerView.setLayoutManager(layoutManager);

        gson = new Gson();
        itemsList = new ArrayList<>();

        pazeidimaiJson = Prefs.with(this).read(PREFS_KEY_PAZEIDIMAI,"");
        if (pazeidimaiJson.equals("")) {
             Toast.makeText(this,"Jus neturite pranesimu",Toast.LENGTH_LONG).show();
        } else {

            sharedPref = getPreferences(MODE_PRIVATE);
            String UploadId = sharedPref.getString("firebasekey", "");

            Type founderListType = new TypeToken<ArrayList<Upload>>(){}.getType();

            itemsList = gson.fromJson(pazeidimaiJson, founderListType);
            Log.e("pranesimaiList", "json: ->>>>> " + pazeidimaiJson);
            Collections.reverse(itemsList);
            adapter = new PazeidimasAdapter(this, itemsList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(this);

        setTitle("Mano pranešimai");
/*        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);*/
        return true;
    }
/*

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.apie) {


            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ManoPranesimaiActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_manopranesimai, null);
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





/*
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    //set icon
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    //set title
                    .setTitle("Mano pranešimai")
                    //set message
                    .setMessage("Šiame lange yra matomi visi Jūsų pranešimai.")
                    //set positive button
                    .setPositiveButton("Supratau", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what would happen when positive button is clicked

                        }
                    })

                    .show();*/
        }

        if (id == R.id.item1) {
            Toast.makeText(this, "bambo", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.item2) {
            Intent i = new Intent(ManoPranesimaiActivity.this, RedLogin.class);
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
        return false;
    }

    private void filter(String text) {

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
}
