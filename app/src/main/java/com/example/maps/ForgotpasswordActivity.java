package com.example.maps;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotpasswordActivity extends AppCompatActivity {

    Button btn_reset;
    EditText txt_email;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        txt_email=findViewById(R.id.txt_email);
        btn_reset=findViewById(R.id.btn_reset);

        firebaseAuth=FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=txt_email.getText().toString().trim();
                if(email.isEmpty())
                {
                    txt_email.setError("Required");
                    txt_email.requestFocus();
                    return;
                }


                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            txt_email.setText("");
                            Toast.makeText(getApplicationContext(), "Check your email!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

}
