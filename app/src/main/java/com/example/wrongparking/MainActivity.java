package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
