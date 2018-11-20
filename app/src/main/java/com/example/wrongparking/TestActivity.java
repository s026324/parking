package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pazeidimai_nav:

                    Intent i = new Intent(TestActivity.this, PazeidimaiActivity.class);
                    startActivity(i);

/*                    mTextMessage.setText("homessss");*/
                    return true;
                    case R.id.pranesti_nav:
                        Intent j = new Intent(TestActivity.this, RedLogin.class);
                        startActivity(j);

/*                    mTextMessage.setText("bam");*/
                    return true;
                case R.id.manopranesimai_nav:
                    Intent k = new Intent(TestActivity.this, pranesti.class);
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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
