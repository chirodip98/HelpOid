package com.example.maps;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;


public class SignUp extends AppCompatActivity {

    EditText txt_email,txt_your_name,txt_your_num,txt_your_addr,txt_father_name,txt_father_num,txt_contact1_name;
    EditText txt_contact1_num,txt_your_pass;
    Button btn_save;
    FirebaseAuth mauth;
    DatabaseReference reference;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        txt_email=(EditText)findViewById(R.id.txt_email);
        txt_your_pass=(EditText)findViewById(R.id.txt_your_pass);
        txt_your_name=(EditText)findViewById(R.id.txt_your_name);
        txt_your_num=(EditText)findViewById(R.id.txt_your_num);
        txt_your_addr=(EditText)findViewById(R.id.txt_your_addr);
        txt_father_name=(EditText)findViewById(R.id.txt_father_name);
        txt_father_num=(EditText)findViewById(R.id.txt_father_num);
        txt_contact1_name=(EditText)findViewById(R.id.txt_contact1_name);
        txt_contact1_num=(EditText)findViewById(R.id.txt_contact1_num);
        btn_save=(Button)findViewById(R.id.btn_save);
        progressBar=findViewById(R.id.progressbar);

        progressBar.setVisibility(View.GONE);
        mauth=FirebaseAuth.getInstance();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }

    public void registerUser() {

        final String name=txt_your_name.getText().toString().trim();
        final String email=txt_email.getText().toString().trim();
        final String password=txt_your_pass.getText().toString().trim();
        final String num=txt_your_num.getText().toString().trim();
        final String address=txt_your_addr.getText().toString().trim();
        final String fname=txt_father_name.getText().toString().trim();
        final String fnum=txt_father_num.getText().toString().trim();
        final String cname=txt_contact1_name.getText().toString().trim();
        final String cnum=txt_contact1_num.getText().toString().trim();

        if(name.isEmpty()){
            txt_your_name.setError("Required");
            txt_your_name.requestFocus();
            return;
        }
        if(email.isEmpty()){
            txt_email.setError("Required");
            txt_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            txt_your_pass.setError("Required");
            txt_your_pass.requestFocus();
            return;
        }
        if(password.length()<6){
            txt_your_pass.setError("More than 6 characters !");
            txt_your_pass.requestFocus();
        }
        if(num.isEmpty()){
            txt_your_num.setError("Required");
            txt_your_num.requestFocus();
            return;
        }
        if(address.isEmpty()){
            txt_your_addr.setError("Required");
            txt_your_addr.requestFocus();
            return;
        }
        if(fname.isEmpty()){
            txt_father_name.setError("Required");
            txt_father_name.requestFocus();
            return;
        }
        if(fnum.isEmpty()){
            txt_father_num.setError("Required");
            txt_father_num.requestFocus();
            return;
        }
        if(cname.isEmpty()){
            txt_contact1_name.setError("Required");
            txt_contact1_name.requestFocus();
            return;
        }
        if(cnum.isEmpty()){
            txt_contact1_num.setError("Required");
            txt_contact1_num.requestFocus();
            return;
        }
        if(fnum.length()<10)
        {
            txt_father_num.setError("Enter a valid phone no");
            txt_father_num.requestFocus();
            return;

        }
        if(cnum.length()<10) {

            txt_contact1_num.setError("Enter a valid phone no");
            txt_contact1_num.requestFocus();
            return;
        }
        if(num.length()<10)
        {
            txt_your_num.setError("Enter a valid phone number");
            txt_your_num.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        btn_save.setVisibility(View.GONE);
        ////adding the user to firebase auth
        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    btn_save.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    txt_your_name.setText("");
                    txt_email.setText("");
                    txt_your_pass.setText("");
                    txt_your_num.setText("");
                    txt_your_addr.setText("");
                    txt_father_name.setText("");
                    txt_father_num.setText("");
                    txt_contact1_name.setText("");
                    txt_contact1_num.setText("");

                    mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                FirebaseUser  firebaseUser=mauth.getCurrentUser();
                                String userid=firebaseUser.getUid();

                                reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                HashMap<String,String>  hashMap=new HashMap<>();
                                hashMap.put("id",userid);
                                hashMap.put("name",name);
                                hashMap.put("email",email);
                                hashMap.put("number",num);
                                hashMap.put("address",address);
                                hashMap.put("fname",fname);
                                hashMap.put("fnumber",fnum);
                                hashMap.put("cname",cname);
                                hashMap.put("cnumber",cnum);

                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            btn_save.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),"Please verify your email!",Toast.LENGTH_SHORT).show();
                                            mauth.signOut();
                                        }
                                        else{
                                            btn_save.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
                            else{
                                btn_save.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    btn_save.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
