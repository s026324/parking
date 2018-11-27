package com.example.wrongparking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    public static final String MAPS_NAVIGATION_ACTION = "google.navigation:q=";
    public static final String MAPS_INTENT_PATH = "com.google.android.apps.maps";
    private int type;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Upload> mItemsList;

    String address = "";
    Date date;

    public Recycler(Context mContext, ArrayList<Upload> mItemsList, int type) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mItemsList = mItemsList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_redaktorius,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long timestamp = mItemsList.get(position).getTime();
        address = mItemsList.get(position).getAddress();
        date = new Date(timestamp);

        switch (type){
            case Constants.TYPE_PATVIRTINTI:
                holder.btnPatvirtinti.setVisibility(View.GONE);

                break;
            case Constants.TYPE_NEPATVIRTINIT:

                break;
            case Constants.TYPE_ATMESTI:
                break;
            case Constants.TYPE_VISI:
                break;

                default:

                    break;

        }

        String formattedDate = simpleDateFormat.format(date);

        Picasso.get()
                .load(mItemsList.get(position)
                        .getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.ivFoto);

        holder.tvValstybinisNr.setText(mItemsList.get(position).getValstnum());
        holder.tvAprasymas.setText(mItemsList.get(position).getName());
        holder.tvAdresas.setText(address);
        holder.tvData.setText(formattedDate);
        holder.tvAdresas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressPath = MAPS_NAVIGATION_ACTION + address;
                Uri gmmIntentUri = Uri.parse(addressPath);
                Intent mapIntentPazeidimas = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntentPazeidimas.setPackage(MAPS_INTENT_PATH);
                mContext.startActivity(mapIntentPazeidimas);
            }
        });

        holder.ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mItemsList.get(position).getImageUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                mContext.startActivity(i);
            }
        });

        holder.btnPatvirtinti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("patvirtintiClick","patvirtinti");
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference reference = firebaseDatabase.getReference();
                Query query = reference.child("uploads").orderByChild("time").equalTo(mItemsList.get(position).getTime());
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

                        mItemsList.remove(position);
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

        holder.atmesti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("atmestiClick","atmesti");
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference reference = firebaseDatabase.getReference();
                Query query = reference.child("uploads").orderByChild("time").equalTo(mItemsList.get(position).getTime());
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

                        mItemsList.remove(position);
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
        return mItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvValstybinisNr, tvAdresas, tvData, tvAprasymas;
        public ImageView ivFoto;
        public Context context;
        public Button btnPatvirtinti, atmesti;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvValstybinisNr = itemView.findViewById(R.id.tv_valst_nr);
            tvAdresas       = itemView.findViewById(R.id.tv_adressas);
            tvAprasymas     = itemView.findViewById(R.id.tv_aprasymas);
            tvData          = itemView.findViewById(R.id.tv_data);
            ivFoto          = itemView.findViewById(R.id.iv_foto);
            btnPatvirtinti = (Button) itemView.findViewById(R.id.patvirtinti);
            atmesti = (Button) itemView.findViewById(R.id.atmesti);
            context         = itemView.getContext();
        }

        @Override
        public void onClick(View view) {

        }
    }
}
