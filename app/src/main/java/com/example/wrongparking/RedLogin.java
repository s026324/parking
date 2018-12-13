package com.example.wrongparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RedLogin extends AppCompatActivity {

    private EditText mEmail_Edittext;
    private EditText mPass_Edittext;

    private Button mLoginBtn;
    private FirebaseAuth mAuth;

    private  FirebaseAuth.AuthStateListener mAuthListener;

/*    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.pazeidimai_nav:

                    Intent i = new Intent(RedLogin.this, PazeidimaiActivity.class);
                    startActivity(i);


                    return true;
                case R.id.pranesti_nav:
                    Intent j = new Intent(RedLogin.this, pranesti.class);
                    startActivity(j);



                    return true;
                case R.id.manopranesimai_nav:
                    Intent k = new Intent(RedLogin.this, ManoPranesimaiActivity.class);
                    startActivity(k);

                    return true;
            }
            return false;
        }
    };*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redlogin);


/*        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/

        mAuth = FirebaseAuth.getInstance();

        mEmail_Edittext = (EditText) findViewById(R.id.mEmail);
        mPass_Edittext = (EditText) findViewById(R.id.mPass);


        mLoginBtn = (Button) findViewById(R.id.mbutton);

        setOnTextChangeListeners();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null){

                    startActivity(new Intent(RedLogin.this, Redaktorius.class));
                    finish();

                }
            }
        };


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSignIn();

            }
        });
    }

    private void setOnTextChangeListeners() {

        mEmail_Edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                TextInputLayout tilEmail = (TextInputLayout) findViewById(R.id.til_email);

                if(s.length() != 0){
                    tilEmail.setError(null);
                } else {
                    tilEmail.setError(getString(R.string.til_empty_text_edit));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mPass_Edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextInputLayout tilPass = (TextInputLayout) findViewById(R.id.til_pass);

                if(s.length() != 0){
                    tilPass.setError(null);
                } else {
                    tilPass.setError(getString(R.string.til_empty_text_edit));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        setTitle("Redaktoriaus prisijungimas");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item1) {
            Toast.makeText(this, "bambo", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn(){
        String email = mEmail_Edittext.getText().toString();
        String password = mPass_Edittext.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

            Toast.makeText(RedLogin.this, "Užpildykite visus laukus", Toast.LENGTH_LONG).show();

        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(RedLogin.this, "Prisijungėte", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(RedLogin.this, "Neteisingai suvesti duomenys", Toast.LENGTH_LONG).show();
                }

                }
            });
        }
    }
}
