package com.example.maps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText txt_email,txt_pass;
    TextView txt_acc;
    Button btn_login;
    TextView txt_forgot;
    private long backPressedTime = 0;
    FirebaseAuth mauth;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_email=findViewById(R.id.txt_email);
        txt_pass=findViewById(R.id.txt_pass);
        btn_login=findViewById(R.id.btn_login);
        txt_acc=findViewById(R.id.txt_acc);
        txt_forgot=findViewById(R.id.txt_forgot);
        progressbar=findViewById(R.id.progressbar);

        progressbar.setVisibility(View.GONE);

        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ForgotpasswordActivity.class);
                startActivity(i);
            }
        });

        mauth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();
            }
        });

        txt_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),SignUp.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mauth.getCurrentUser() != null) {
            if(mauth.getCurrentUser().isEmailVerified()){
            finish();
            startActivity(new Intent(this, Homepage.class));}
            else{
                Toast.makeText(getApplicationContext(),"Verify your mail!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void userLogin() {

        String email = txt_email.getText().toString().trim();
        String password = txt_pass.getText().toString().trim();

        if (email.isEmpty()) {
            txt_email.setError("Required");
            txt_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            txt_pass.setError("Required");
            txt_pass.requestFocus();
            return;
        }
        if (password.length()<6) {
            txt_pass.setError("Incorrect password!");
            txt_pass.requestFocus();
            return;
        }

        btn_login.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);

        mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    if(mauth.getCurrentUser().isEmailVerified()){
                        progressbar.setVisibility(View.GONE);
                        Intent i = new Intent(getApplicationContext(), Homepage.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        btn_login.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(getApplicationContext(),"Verify Your email !",Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "You are not a valid user!", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Press back again to Exit",
                    Toast.LENGTH_SHORT).show();
        } else {
            // clean up
            super.onBackPressed();       // bye
        }

    }

}
