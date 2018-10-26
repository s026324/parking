package com.example.wrongparking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PazeidimasAdapter extends RecyclerView.Adapter<PazeidimasAdapter.ViewHolder> {

    public static final String MAPS_NAVIGATION_ACTION = "google.navigation:q=";
    public static final String MAPS_INTENT_PATH = "com.google.android.apps.maps";

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Upload> mItemsList;

    String address = "";
    Date date;

    public PazeidimasAdapter(Context mContext, ArrayList<Upload> mItemsList) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mItemsList = mItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_pazeidimas,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long timestamp = mItemsList.get(position).getTime();
        address = mItemsList.get(position).getAddress();
        date = new Date(timestamp);

        String formattedDate = simpleDateFormat.format(date);

        holder.tvValstybinisNr.setText(mItemsList.get(position).getValstnum());
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

        Picasso.get().load(mItemsList.get(position).getImageUrl()).into(holder.ivFoto);
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvValstybinisNr, tvAdresas, tvData;
        public ImageView ivFoto;
        public Context context;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvValstybinisNr = itemView.findViewById(R.id.tv_valst_nr);
            tvAdresas = itemView.findViewById(R.id.tv_adressas);
            tvData = itemView.findViewById(R.id.tv_data);
            ivFoto = itemView.findViewById(R.id.iv_foto);

            context = itemView.getContext();
        }

        @Override
        public void onClick(View view) {

        }
    }
}
