package com.example.wrongparking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    private Button ResetButton;
    private EditText EmailInput;
    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        mAuth = FirebaseAuth.getInstance();
        ResetButton = (Button) findViewById(R.id.resetbutton);
        EmailInput = (EditText) findViewById(R.id.resetinput);

        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = EmailInput.getText().toString();

                if (TextUtils.isEmpty(userEmail)){
                    Toast.makeText(ResetPassActivity.this, "Įveskite el. paštą", Toast.LENGTH_LONG).show();
                }else{
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ResetPassActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.alert_passreseted, null);
                                Button ok = (Button) mView.findViewById(R.id.ok);

                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();

                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        startActivity(new Intent(ResetPassActivity.this, RedLogin.class));
                                    }
                                });
/*
                                Toast.makeText(ResetPassActivity.this, "Paziurekite pasta", Toast.LENGTH_LONG).show();
*/
                            }else{
                                String message  = task.getException().getMessage();
                                Toast.makeText(ResetPassActivity.this, "Klaida: " + message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
