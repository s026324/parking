package com.example.wrongparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RedLogin extends Activity {

    private EditText mEmail_Edittext;
    private EditText mPass_Edittext;

    private Button mLoginBtn;
    private FirebaseAuth mAuth;

    private  FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redlogin);

        mAuth = FirebaseAuth.getInstance();

        mEmail_Edittext = (EditText) findViewById(R.id.mEmail);
        mPass_Edittext = (EditText) findViewById(R.id.mPass);

        mLoginBtn = (Button) findViewById(R.id.mbutton);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null){

                    //For faster login
                    startActivity(new Intent(RedLogin.this, Redaktorius.class));

                }

            }
        };

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //change when release
               // startActivity(new Intent(RedLogin.this, Redaktorius.class));
                startSignIn();

            }
        });
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

            Toast.makeText(RedLogin.this, "Uzpildykite visus laukus", Toast.LENGTH_LONG).show();

        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(RedLogin.this, "ok", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(RedLogin.this, "neok", Toast.LENGTH_LONG).show();
                }

                }
            });
        }
    }
}
