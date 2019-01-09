package com.example.wrongparking;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.prefs.Prefs;

public class MyPostsActivity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {


    public static final String PREFS_KEY_PAZEIDIMAI = "pazeidimai_list";

    private String pazeidimaiJson = "";
    Gson gson ;
    RecyclerView recyclerView;
    ArrayList<Upload> itemsList;

    LinearLayoutManager layoutManager;
    public MyPostsAdapter adapter;
    private Button button;
    public SharedPreferences sharedPref;
    public TextView noitems;
    public TextView noResults;

    ProgressDialog dialog;
    final ArrayList<Upload> mUploadArrayList = new ArrayList<>();
    ArrayList<Upload> mFinalDataList;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pazeidimai_nav:

                    Intent i = new Intent(MyPostsActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                    return true;
                case R.id.pranesti_nav:
                    Intent j = new Intent(MyPostsActivity.this, AddActivity.class);
                    startActivity(j);
                    finish();

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




        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        gson = new Gson();
        itemsList = new ArrayList<>();

        prepareAllDataList();

    }

    private void prepareData(ArrayList<Upload> allDataList){

        ArrayList<Upload> userItems = new ArrayList<>();
        pazeidimaiJson = Prefs.with(this).read(PREFS_KEY_PAZEIDIMAI,"");
        if (pazeidimaiJson.equals("")) {

/*
            Toast.makeText(this,"Jus neturite pranesimu",Toast.LENGTH_LONG).show();
*/

        } else {
            Type founderListType = new TypeToken<ArrayList<Upload>>(){}.getType();
            userItems = gson.fromJson(pazeidimaiJson, founderListType);
            Log.e("pranesimaiList", "json: ->>>>> " + pazeidimaiJson);
        }

        prepareAllUserData(userItems, allDataList);


    }

    private void prepareAllUserData(ArrayList<Upload> userItems,ArrayList<Upload> allItems){
        mFinalDataList = new ArrayList<>();

        for (int i = 0; i < allItems.size(); i++) {
            for (Upload upl: userItems) {
                if(upl.getTime() == allItems.get(i).getTime()){
                    mFinalDataList.add(allItems.get(i));
                }
            }
        }



        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(true);
        recyclerView = (RecyclerView) findViewById(R.id.rc_pazeidimai);
        recyclerView.setLayoutManager(layoutManager);

        Collections.reverse(mFinalDataList);
        adapter = new MyPostsAdapter(this, mFinalDataList);
        recyclerView.setAdapter(adapter);
        dialog.dismiss();
    }

    private void prepareAllDataList(){

        dialog.show();

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        Query fetchAllData = mRootRef.child("uploads");
        fetchAllData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot lDataSnapshot : dataSnapshot.getChildren()){
                    Upload singleItem = lDataSnapshot.getValue(Upload.class);
                    assert singleItem != null;
                    mUploadArrayList.add(singleItem);
                    Log.e("data",singleItem.getName());
                }

                prepareData(mUploadArrayList);

                final TextView noitems = (TextView) findViewById(R.id.noAdds);

                if (pazeidimaiJson.equals("")){
                    noitems.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("cantGet",databaseError.getMessage());
                Toast.makeText(MyPostsActivity.this,"ErrorHappened",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void filter(String text){

        ArrayList<Upload> filteredList = new ArrayList<>();

        for (Upload item : mFinalDataList) {

            text = text.toLowerCase();

            prepareData(mFinalDataList);


            if(item.getName().toLowerCase().contains(text) ||
                    item.getValstnum().toLowerCase().contains(text) ||
                    item.getAddress().toLowerCase().contains(text) ){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);

        final TextView noResults = (TextView) findViewById(R.id.noResults);

        if(filteredList.isEmpty()){
            noResults.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            noResults.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(this);

        setTitle("Mano pranešimai");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.apie) {


            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyPostsActivity.this);
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


        }

/*        if (id == R.id.item1) {
            Toast.makeText(this, "bambo", Toast.LENGTH_LONG).show();
        }*/
        if (id == R.id.item2) {
            Intent i = new Intent(MyPostsActivity.this, RedLogin.class);
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
