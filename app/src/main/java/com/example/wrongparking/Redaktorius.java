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
    ArrayList<Upload> list;
    Recycler adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redaktorius);

        recyclerView = (RecyclerView) findViewById(R.id.bambo);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));


        reference = FirebaseDatabase.getInstance().getReference().child("uploads");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Upload>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Upload p = dataSnapshot1.getValue(Upload.class);
                    list.add(p);
                }
                 adapter = new Recycler(Redaktorius.this,list);
                recyclerView.setAdapter(adapter);
            }



        @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Redaktorius.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


/*

    public static final String DATABASE_PATH_UPLOADS = "uploads";





    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;



    private ListView mListView;
    private ArrayList<Upload> mUploads = new ArrayList<>();
    private ArrayList<Upload> list;

    private Recycler adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redaktorius);
       // setContentView(R.layout.recycler_list);

        myRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS);

        mListView = (ListView) findViewById(R.id.redList);

        list = new ArrayList<Upload>();

        final ArrayAdapter<Upload> arrayAdapter = new ArrayAdapter<Upload>(this, android.R.layout.simple_list_item_1, mUploads);

        RecyclerView recyclerList = (RecyclerView) findViewById(R.id.bambo);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
*/
/*        String[] ddd = {"bambo", "eik","tu","nx", "pavyko","dziaugsmas","LAIMES SOKIS"};
        recyclerList.setAdapter(new Recycler(ddd));
        *//*



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Log.d("snapshotValue", postSnapshot.toString());
                    Upload post = postSnapshot.getValue(Upload.class);
                    mUploads.add(post);
                    Log.e("Get Data", post.getName());
                    mListView.setAdapter(arrayAdapter);
                }
                adapter = new Recycler(Redaktorius.this, list)
                        recyclerView.set
*/
/*                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Log.d("snapshotValue", postSnapshot.toString());
                    Upload post = postSnapshot.getValue(Upload.class);
                    mUploads.add(post);
                    Log.e("Get Data", post.getName());
                    mListView.setAdapter(arrayAdapter);
                }*//*

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });




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

*/
    public void onClick(View k) {
        if(k.getId() == R.id.buttonout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Redaktorius.this, MainActivity.class));
            Toast.makeText(Redaktorius.this, "Atsijunget", Toast.LENGTH_LONG).show();
        }

        }


}


