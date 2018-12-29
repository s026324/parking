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
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Button mButton;
    private TextView Score;
    private boolean firstTimeUsed = false;
    private String firstTimeUsedKey="FIRST_TIME";

    private String sharedPreferencesKey = "MY_PREF";
    private String buttonClickedKey = "BUTTON_CLICKED";
    private SharedPreferences mPrefs;
    private SharedPreferences mClickPrefs; // paspaudimai
    private long savedDate=0;
    final Integer maxclicks = 5;
    Integer currentnumber = 0;
    int clicks = 0;



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
                    Intent k = new Intent(TestActivity.this, AddActivity.class);
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

/*        mButton = (Button) findViewById(R.id.buttonTest);
        Score = (TextView) findViewById(R.id.Score);*/

        mPrefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        savedDate = mPrefs.getLong(buttonClickedKey,0);
        //if (getDate == current day) firstTimeUser false ? true
        firstTimeUsed = mPrefs.getBoolean(firstTimeUsedKey,true);//default is true if no value is saved
        checkPrefs();
        checkTime();


/*
        mClickPrefs = getSharedPreferences("clicks", Context.MODE_PRIVATE); // paspaudimai
*/
        final SharedPreferences myBambo = this.getSharedPreferences("clicks", Context.MODE_PRIVATE);

        clicks = myBambo.getInt("score", 0);

        Score.setText("bambo:" +clicks);
/*        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", this.MODE_PRIVATE);*/


        mButton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                if(clicks >= 5) {
                    mButton.setEnabled(false);

                    Toast.makeText(TestActivity.this, "nesvaik", Toast.LENGTH_SHORT).show();

                }else{

                    clicks += 1;
                    SharedPreferences myBambo = getSharedPreferences("clicks", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = myBambo.edit();
                    editor.putInt("score", clicks);
                    editor.putLong("ExpiredDate", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10));
                    editor.apply();

                    Score.setText("Score: " + clicks);

                }
/*                if (myBambo.getLong("ExpiredDate", -1) < System.currentTimeMillis()) {
                    SharedPreferences.Editor editor = myBambo.edit();
                    editor.clear();
                    editor.apply();
                }*/

/*
                putValueInSharedPrefs(++clicks);

                clicks++;
                if(clicks==0) {
                    mButton.setEnabled(true);
                }else if(clicks >=2){
                    mButton.setEnabled(false);
                }
                SharedPreferences.Editor editor = mClickPrefs.edit();
                editor.putInt("clicks", clicks);
*/
/*                                editor.putString("date",date.toString());*//*

                editor.apply();
*/

/*
                    saveClickedTime();*/
            }

            private void putValueInSharedPrefs(int clickTimes) {
                SharedPreferences.Editor editor = mClickPrefs.edit();
/*                editor = sharedPreferences.edit();*/
                editor.putInt("clicks", clickTimes);
                editor.apply();
                editor.commit();

                Toast.makeText(TestActivity.this, "Example Button is clicked " +clickTimes+ "time(s)", Toast.LENGTH_SHORT).show();

            }

        });

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void checkTime() {

        SharedPreferences myBambo = getSharedPreferences("clicks", Context.MODE_PRIVATE);
                        if (myBambo.getLong("ExpiredDate", -1) < System.currentTimeMillis()) {
                    SharedPreferences.Editor editor = myBambo.edit();
                    editor.clear();
                    editor.apply();
                }
    }

    private void checkPrefs(){


        if(firstTimeUsed==false){
            if(savedDate>0){

                //create two instances of Calendar and set minute,hour,second and millis
                //to 1, to be sure that the date differs only from day,month and year

                Calendar currentCal = Calendar.getInstance();

                Date dateCurrent = currentCal.getTime();


/*
                currentCal.get(Calendar.MINUTE);
                currentCal.get(Calendar.HOUR);
                currentCal.get(Calendar.SECOND);
                currentCal.get(Calendar.MILLISECOND);
*/

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
/*                savedCal.get(Calendar.MINUTE);
                savedCal.get(Calendar.HOUR);
                savedCal.get(Calendar.SECOND);
                savedCal.get(Calendar.MILLISECOND);*/

                if(currentCal.getTime().after(savedCal.getTime()))
                {
                /*    mButton.setEnabled(true);*/
/*                    mButton.setVisibility(View.VISIBLE);*/
                }
                else if(currentCal.getTime().equals(savedCal.getTime())){
/*                    mButton.setEnabled(false);*/

/*
                    mButton.setVisibility(View.GONE);*/
                }

            }
        }else{

            //just set the button visible if app is used the first time
           /* mButton.setEnabled(true);*/
/*            mButton.setVisibility(View.VISIBLE);*/

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
/*        if(currentnumber == maxclicks){
            mButton.setEnabled(false);
        }else {
            currentnumber = currentnumber + 1;
        }*/

/*        mButton.setVisibility(View.GONE);*/
    }

}
