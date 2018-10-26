package com.example.wrongparking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {
    DatabaseReference reference;
    public Date mTime;

    public static final String DATABASE_PATH_UPLOADS = "uploads";



    //  public Date dateTime;
    Context context;
    ArrayList<Upload> informacija;

    //   private String[] data;
    public Recycler(Context c, ArrayList<Upload> p) {

        context = c;
        informacija = p;
        reference = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mTime = new Date();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_list, parent, false));
/*        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_list, parent, false);
        return new ViewHolder(view);*/
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tv_placeAdress.setText(informacija.get(position).getAddress());
        holder.name.setText(informacija.get(position).getName());
        holder.valstnum.setText(informacija.get(position).getValstnum());
        Picasso.get().load(informacija.get(position).getImageUrl()).into(holder.imageUrl);
        final long timeStamp = mTime.getTime();
        Date data = new Date();
        final String dateString = new SimpleDateFormat("MM/dd/yyyy").format(timeStamp);
        holder.dateTime.setText(String.valueOf(dateString));

        holder.btnPatvirtinti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference reference = firebaseDatabase.getReference();
                Query query = reference.child("uploads").orderByChild("time").equalTo(informacija.get(position).getTime());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "/" + dataSnapshot.getKey() + "/" + key;
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("patvirtintas", true);
                        result.put("perziuretas",true);
                        reference.child(path).updateChildren(result);

                        informacija.remove(position);
                        Redaktorius.adapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
                        Log.e(">>> ErrorDatabse:","find onCancelled:" + databaseError);
                    }
                });
            }
        });


        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference reference = firebaseDatabase.getReference();
                Query query = reference.child("uploads").orderByChild("valstnum").equalTo(informacija.get(position).getValstnum());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "/" + dataSnapshot.getKey() + "/" + key;
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("patvirtintas", false);
                        result.put("perziuretas",true);
                        reference.child(path).updateChildren(result);

                        informacija.remove(position);
                        Redaktorius.adapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
                        Log.e(">>> ErrorDatabse:","find onCancelled:" + databaseError);
                    }
                });
            }
        });



    }




    @Override
    public int getItemCount() {
        return informacija.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, valstnum, dateTime, patvirtintas, perziuretas, tv_placeAdress;

        ImageView imageUrl;
        Button btn, btnPatvirtinti;

        /*        TextView recyclerID;*/
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recyclerID);
            valstnum = (TextView) itemView.findViewById(R.id.recyclerID2);
            imageUrl = (ImageView) itemView.findViewById(R.id.nuotrauka);
            // imageUrl = (TextView) itemView.findViewById(R.id.recyclerID3);
            dateTime = (TextView) itemView.findViewById(R.id.recyclerID4);
/*            patvirtintas = (TextView) itemView.findViewById(R.id.recyclerID5);
            perziuretas = (TextView) itemView.findViewById(R.id.recyclerID6);*/

            tv_placeAdress = itemView.findViewById(R.id.tv_place_adress);

            //  email = (TextView) itemView.findViewById(R.id.email);
            // profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            btn = (Button) itemView.findViewById(R.id.delete);
            btnPatvirtinti = (Button) itemView.findViewById(R.id.patvirtinti);
/*            super(itemView);
            recyclerID = itemView.findViewById(R.id.recyclerID);*/
        }

    }
}