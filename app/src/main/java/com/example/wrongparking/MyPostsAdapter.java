package com.example.wrongparking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder> {

    public static final String MAPS_NAVIGATION_ACTION = "google.navigation:q=";
    public static final String MAPS_INTENT_PATH = "com.google.android.apps.maps";

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Upload> mItemsList;

    String address = "";
    Date date;


    public MyPostsAdapter(Context mContext, ArrayList<Upload> mItemsList) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mItemsList = mItemsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        String imageUrl;
        View view = mInflater.inflate(R.layout.item_mypostscardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long timestamp = mItemsList.get(position).getTime();
        address = mItemsList.get(position).getAddress();
        date = new Date(timestamp);

        final String formattedDate = simpleDateFormat.format(date);

        Picasso.get()
                .load(mItemsList.get(position)
                        .getImageUrl())
                .placeholder( R.drawable.progress_animation )
                .fit()
                .centerCrop()
                .into(holder.ivFoto);

        holder.tvValstybinisNr.setText(mItemsList.get(position).getValstnum());
        holder.tvAprasymas.setText(mItemsList.get(position).getName());
        holder.tvAdresas.setText(address);
        holder.tvData.setText(formattedDate);

        if(mItemsList.get(position).isPatvirtintas()){
            holder.tvPatvirtintas.setText("Patvirtintas");
            holder.tvPatvirtintas.setTextColor(mContext.getResources().getColor(R.color.colorPatvirtintas));
        } else {
            holder.tvPatvirtintas.setText("Nepatvirtintas");
            holder.tvPatvirtintas.setTextColor(mContext.getResources().getColor(R.color.colorNepatvirtintas));
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PreviewActivity.class);
                String url = mItemsList.get(position).getImageUrl();
                String adress = mItemsList.get(position).getAddress();
                String aprasymas = mItemsList.get(position).getName();
                String valstnum = mItemsList.get(position).getValstnum();
                String answer = mItemsList.get(position).getAnswer();
                boolean activatedornot = mItemsList.get(position).isPatvirtintas();
                /*                   String datetime = mItemsList.get(position).getTime();*/


                i.putExtra("image", url);
                i.putExtra("adress", adress);
                i.putExtra("aprasymas", aprasymas);
                i.putExtra("valstnum", valstnum);
                i.putExtra("time", formattedDate);
                i.putExtra("answer", answer);
                i.putExtra("activatedornot", activatedornot);

                mContext.startActivity(i);
            }
        });

    }
    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public void filterList(ArrayList<Upload> filteredList) {

        mItemsList = filteredList   ;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvValstybinisNr, tvAdresas, tvData, tvAprasymas,tvPatvirtintas;
        public ImageView ivFoto;
        public Context context;
        public PhotoView photoView;
        public CardView cardView;


        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvValstybinisNr = itemView.findViewById(R.id.tv_valst_nr);
            tvAdresas       = itemView.findViewById(R.id.tv_adressas);
            tvAprasymas     = itemView.findViewById(R.id.tv_aprasymas);
            tvPatvirtintas  = itemView.findViewById(R.id.tvPatvirtintas);
            tvData          = itemView.findViewById(R.id.tv_data);
            ivFoto          = itemView.findViewById(R.id.iv_foto);
            context         = itemView.getContext();
            photoView = itemView.findViewById(R.id.your_photo_view);
            cardView = itemView.findViewById(R.id.cardView);
        }
        @Override
        public void onClick(View view) {

        }
    }


}
