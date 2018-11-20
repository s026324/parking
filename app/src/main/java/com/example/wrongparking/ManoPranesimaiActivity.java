package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.prefs.Prefs;

public class ManoPranesimaiActivity extends AppCompatActivity {


    public static final String PREFS_KEY_PAZEIDIMAI = "pazeidimai_list";

    private String pazeidimaiJson = "";
    Gson gson ;
    RecyclerView recyclerView;
    ArrayList<Upload> itemsList;

    LinearLayoutManager layoutManager;
    public PazeidimasAdapter adapter;

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

            Type founderListType = new TypeToken<ArrayList<Upload>>(){}.getType();

            itemsList = gson.fromJson(pazeidimaiJson, founderListType);
            Log.e("pranesimaiList", "json: ->>>>> " + pazeidimaiJson);
            Collections.reverse(itemsList);
            adapter = new PazeidimasAdapter(this, itemsList);
            recyclerView.setAdapter(adapter);
        }







    }
}
