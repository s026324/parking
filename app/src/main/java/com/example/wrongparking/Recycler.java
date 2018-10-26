package com.example.wrongparking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
             holder.btnPatvirtinti.setText("bambo");
            }
        });


        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 //     .deleteProduct(shoppingCart.getId());
/*                informacija.remove(position);
                notifyItemRemoved(position);*/


                FirebaseDatabase.getInstance().getReference()
                        .child("uploads").removeValue();


             //  reference.child(DATABASE_PATH_UPLOADS).removeValue();



/*                if(reference!=null) {
                    reference.child("uploads").removeValue();
                }*/
               // informacija.remove(position);

/*                informacija.remove(position);
//                reference.child("uploads").removeValue();
                .notifyItemRemoved(position);*/

/*                FirebaseDatabase.getInstance().getReference()
                        .child("uploads").child(Upload).removeValue();*/
               // reference.child("uploads").child(informacija.toString()).removeValue();
            }
        });



    }




    @Override
    public int getItemCount() {
        return informacija.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, valstnum, dateTime, patvirtintas, perziuretas;

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

            //  email = (TextView) itemView.findViewById(R.id.email);
            // profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            btn = (Button) itemView.findViewById(R.id.delete);
            btnPatvirtinti = (Button) itemView.findViewById(R.id.patvirtinti);
/*            super(itemView);
            recyclerID = itemView.findViewById(R.id.recyclerID);*/
        }

    }
}