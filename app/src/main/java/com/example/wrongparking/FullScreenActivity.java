package com.example.wrongparking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
// Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);


        PhotoView fullScreenPhotoView = (PhotoView) findViewById(R.id.fullScreenPhotoView);


        if(getIntent().getExtras() != null){
            String photoUri = getIntent().getStringExtra("ItemImage");
            if(photoUri != null && !photoUri.equals("")){
                Picasso.get().load(photoUri).into(fullScreenPhotoView);
            }
        }

    }
}
