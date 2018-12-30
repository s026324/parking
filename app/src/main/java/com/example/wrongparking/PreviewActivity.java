package com.example.wrongparking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.wrongparking.MainAdapter.MAPS_INTENT_PATH;
import static com.example.wrongparking.MainAdapter.MAPS_NAVIGATION_ACTION;

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = "PreviewActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Log.d(TAG, "onCreate: started");
        getIncomingIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
        finish();
        return true;
    }
    private void getIncomingIntent(){
        if(getIntent().hasExtra("image") && getIntent().hasExtra("adress") &&
                getIntent().hasExtra("aprasymas") && getIntent().hasExtra("valstnum") &&
                getIntent().hasExtra("time") && getIntent().hasExtra("answer")
                ) {
            String imageUrl = getIntent().getStringExtra("image");
            String adressLine = getIntent().getStringExtra("adress");
            String aprasymas = getIntent().getStringExtra("aprasymas");
            String valstnum = getIntent().getStringExtra("valstnum");
            String datetime = getIntent().getStringExtra("time");
            String answer = getIntent().getStringExtra("answer");


            setImage(imageUrl, adressLine, aprasymas, valstnum, datetime, answer);
        }
    }

    private void setImage (final String imageUrl, final String adressLine, String aprasymas, String valstnum,
                           String datetime, String answer){
        final ImageView image = findViewById(R.id.imagepreview);

        Picasso.get()
                .load(imageUrl)
                .placeholder( R.drawable.progress_animation )
                .fit()
                .centerCrop()
                .into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = imageUrl;
                Intent i = new Intent(PreviewActivity.this,FullScreenActivity.class);

                i.putExtra("ItemImage", url);
                startActivity(i);
            }
        });

        TextView adressText = findViewById(R.id.adressid);
        adressText.setText(adressLine);

        adressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressPath = MAPS_NAVIGATION_ACTION + adressLine;
                Uri gmmIntentUri = Uri.parse(addressPath);
                Intent mapIntentPazeidimas = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntentPazeidimas.setPackage(MAPS_INTENT_PATH);
                startActivity(mapIntentPazeidimas);
            }
        });

        TextView aprasymasText = findViewById(R.id.aprasymas);
        aprasymasText.setText(aprasymas);

        TextView valstnumText = findViewById(R.id.valstnumpreview);
        valstnumText.setText(valstnum);

        TextView datetimeText = findViewById(R.id.timepreview);
        datetimeText.setText(datetime);

        TextView answerText = findViewById(R.id.answerpreview);
        answerText.setText(answer);

        final TextView answerTextas = findViewById(R.id.textView9);

        if (answer.equals("")){
            answerTextas.setVisibility(View.GONE);
        }
    }
}
