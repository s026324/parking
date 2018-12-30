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

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    public static final String MAPS_NAVIGATION_ACTION = "google.navigation:q=";
    public static final String MAPS_INTENT_PATH = "com.google.android.apps.maps";

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Upload> mItemsList;

    String address = "";
    Date date;


    public MainAdapter(Context mContext, ArrayList<Upload> mItemsList) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mItemsList = mItemsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        String imageUrl;
        View view = mInflater.inflate(R.layout.item_maincardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final long timestamp = mItemsList.get(position).getTime();
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

        /// REIKALINGAS I PREVIEWACTIVITY!!!

/*        holder.tvAdresas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressPath = MAPS_NAVIGATION_ACTION + address;
                Uri gmmIntentUri = Uri.parse(addressPath);
                Intent mapIntentPazeidimas = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntentPazeidimas.setPackage(MAPS_INTENT_PATH);
                mContext.startActivity(mapIntentPazeidimas);
            }
        });*/

        //// REIKALINGAS I PREVIEWACTIVITY!!!!

/*        holder.ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mItemsList.get(position).getImageUrl();
                Intent i = new Intent(mContext,FullScreenActivity.class);
                i.putExtra("ItemImage", url);
                mContext.startActivity(i);
            }
        });*/
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PreviewActivity.class);
                String url = mItemsList.get(position).getImageUrl();
                String adress = mItemsList.get(position).getAddress();
                String aprasymas = mItemsList.get(position).getName();
                String valstnum = mItemsList.get(position).getValstnum();
                String answer = mItemsList.get(position).getAnswer();
                /*                   String datetime = mItemsList.get(position).getTime();*/


                i.putExtra("image", url);
                i.putExtra("adress", adress);
                i.putExtra("aprasymas", aprasymas);
                i.putExtra("valstnum", valstnum);
                i.putExtra("time", formattedDate);
                i.putExtra("answer", answer);

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

        public TextView tvValstybinisNr, tvAdresas, tvData, tvAprasymas;
        public ImageView ivFoto;
        public Context context;
        public PhotoView photoView;
        public ImageView ImageBanner;
        public CardView cardView;



        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvValstybinisNr = itemView.findViewById(R.id.tv_valst_nr);
            tvAdresas       = itemView.findViewById(R.id.tv_adressas);
            tvAprasymas     = itemView.findViewById(R.id.tv_aprasymas);
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