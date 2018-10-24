package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Redaktorius extends AppCompatActivity {

    public static final String DATABASE_PATH_UPLOADS = "uploads";





    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;



    private ListView mListView;
    private ArrayList<Upload> mUploads = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // setContentView(R.layout.recycler_list);

        myRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS);

        mListView = (ListView) findViewById(R.id.redList);

        final ArrayAdapter<Upload> arrayAdapter = new ArrayAdapter<Upload>(this, android.R.layout.simple_list_item_1, mUploads);

/*        RecyclerView recyclerList = (RecyclerView) findViewById(R.id.recycler);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        String[] ddd = {"bambo"};
        recyclerList.setAdapter(new Recycler(ddd));*/
/*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Log.d("snapshaotValue", postSnapshot.toString());
                    Upload post = postSnapshot.getValue(Upload.class);
                    mUploads.add(post);
                    Log.e("Get Data", post.getName());
                    mListView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });*/




//        myRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String value = dataSnapshot.getValue(String.class);
//                mName.add(value);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        Query myTopPostsQuery = myRef.child("uploads").child("wot")
//                .orderByChild("valstnum");
//
//        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    Log.d("elementai",postSnapshot.toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }


    public void onClick(View k) {
        if(k.getId() == R.id.buttonout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Redaktorius.this, MainActivity.class));
            Toast.makeText(Redaktorius.this, "Atsijunget", Toast.LENGTH_LONG).show();
        }

        }



    }

