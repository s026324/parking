package com.example.wrongparking;

        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {



    private EditText mSeachField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;


    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("uploads");


        mSeachField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_button);

        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            firebaseUserSearch();
        }
    });

        setTitle("bambo");
    }

    private void firebaseUserSearch() {

        FirebaseRecyclerAdapter<Upload, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Upload, UsersViewHolder>

        /*(

                Upload.class,
                R.layout.item_pazeidimas,
                UsersViewHolder.class,
                mUserDatabase

        )*/

        {
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Upload model) {
                holder.setDetails(model.getValstnum(),model.getAddress(), model.getName(), model.getImageUrl());
            }

        };

        mResultList.setAdapter(firebaseRecyclerAdapter);

    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDetails(String valstNr, String adress, String aprasymas, String image){

            TextView tvValstybinisNr = itemView.findViewById(R.id.tv_valst_nr);
            TextView tvAdresas       = itemView.findViewById(R.id.tv_adressas);
            TextView tvAprasymas     = itemView.findViewById(R.id.tv_aprasymas);
/*            TextView tvData          = itemView.findViewById(R.id.tv_data);*/
            ImageView ivFoto          = itemView.findViewById(R.id.iv_foto);

            tvValstybinisNr.setText(valstNr);
            tvAdresas.setText(adress);
            tvAprasymas.setText(aprasymas);
/*            tvData.setText(data);*/

            Picasso.get()
                    .load(image)
                    .fit()
                    .centerCrop()
                    .into(ivFoto);

/*

            Picasso.with(getApplicationContext()).load(image).into(ivFoto);
*/


        }

    }
}
