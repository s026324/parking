package com.example.wrongparking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class TestActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Button mButton;
    private boolean firstTimeUsed = false;
    private String firstTimeUsedKey="FIRST_TIME";

    private String sharedPreferencesKey = "MY_PREF";
    private String buttonClickedKey = "BUTTON_CLICKED";
    private SharedPreferences mPrefs;
    private long savedDate=0;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pazeidimai_nav:

                    Intent i = new Intent(TestActivity.this, TestActivity.class);
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

                    return true;
            }
            return false;
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mButton = (Button) findViewById(R.id.buttonTest);
        mPrefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        savedDate = mPrefs.getLong(buttonClickedKey,0);
        firstTimeUsed = mPrefs.getBoolean(firstTimeUsedKey,true);//default is true if no value is saved
        checkPrefs();

        mButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                saveClickedTime();
            }
        });




        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void checkPrefs(){


        if(firstTimeUsed==false){
            if(savedDate>0){

                //create two instances of Calendar and set minute,hour,second and millis
                //to 1, to be sure that the date differs only from day,month and year

                Calendar currentCal = Calendar.getInstance();
                currentCal.set(Calendar.MINUTE,1);
                currentCal.set(Calendar.HOUR,1);
                currentCal.set(Calendar.SECOND,1);
                currentCal.set(Calendar.MILLISECOND,1);

                Calendar savedCal = Calendar.getInstance();
                savedCal.setTimeInMillis(savedDate); //set the time in millis from saved in sharedPrefs
                savedCal.set(Calendar.MINUTE,1);
                savedCal.set(Calendar.HOUR,1);
                savedCal.set(Calendar.SECOND,1);
                savedCal.set(Calendar.MILLISECOND,1);

                if(currentCal.getTime().after(savedCal.getTime()))
                {
                    mButton.setVisibility(View.VISIBLE);
                }
                else if(currentCal.getTime().equals(savedCal.getTime())){

                    mButton.setVisibility(View.GONE);
                }

            }
        }else{

            //just set the button visible if app is used the first time

            mButton.setVisibility(View.VISIBLE);

        }

    }

    private void saveClickedTime(){

        SharedPreferences.Editor mEditor = mPrefs.edit();
        Calendar cal = Calendar.getInstance();
        long millis = cal.getTimeInMillis();
        mEditor.putLong(buttonClickedKey,millis);
        mEditor.putBoolean(firstTimeUsedKey,false); //the button is clicked first time, so set the boolean to false.
        mEditor.commit();

        //hide the button after clicked
        mButton.setVisibility(View.GONE);

    }

}
