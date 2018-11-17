package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    Intent i = new Intent(MainActivity.this, PazeidimaiActivity.class);
                    startActivity(i);

                    /*                    mTextMessage.setText("homessss");*/
                    return true;
                case R.id.navigation_dashboard:
                    Intent j = new Intent(MainActivity.this, RedLogin.class);
                    startActivity(j);

                    /*                    mTextMessage.setText("bam");*/
                    return true;
                case R.id.navigation_notifications:
                    Intent k = new Intent(MainActivity.this, pranesti.class);
                    startActivity(k);
                    /*                    mTextMessage.setText("pisk");*/
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void onButtonClick(View v)
    {
        if(v.getId() == R.id.pranesti){
            Intent i = new Intent(MainActivity.this, pranesti.class);
        startActivity(i);
        }
}
    public void onButtonClickLogin(View r)
    {
        if(r.getId() == R.id.RedLogin){
            Intent i = new Intent(MainActivity.this, RedLogin.class);
            startActivity(i);
        }
    }

    public void onPazeidimaiClick(View v){
        if(v.getId() == R.id.button2){
            Intent i = new Intent(MainActivity.this, PazeidimaiActivity.class);
            startActivity(i);
        }
    }
}
