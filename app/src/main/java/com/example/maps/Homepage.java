package com.example.maps;


import android.app.ActionBar;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.SEND_SMS;

public class Homepage extends AppCompatActivity {

    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;

    private long backPressedTime = 0;
    TextView username;
    Button btn_help,btn_logout,btn_chk;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    FirebaseAuth mauth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_homepage);

        username=(TextView)findViewById(R.id.username);
        btn_help=(Button)findViewById(R.id.btn_help);
        btn_logout=(Button)findViewById(R.id.btn_logout);
        mauth=FirebaseAuth.getInstance();
        btn_chk=(Button)findViewById(R.id.btn_chk);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(Homepage.this);



        btn_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker object= new GPSTracker(getApplicationContext());
                Location l=object.getLocation();
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.signOut();
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }
        });

        username.setVisibility(View.GONE);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());


        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ActivityCompat.checkSelfPermission(Homepage.this,SEND_SMS)!=PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Homepage.this,new String[]{
                            SEND_SMS},REQUEST_CODE);


                    return;
                }

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Users users=dataSnapshot.getValue(Users.class);
                        String name=users.getName();
                        String number=users.getNumber();
                        String email=users.getEmail();
                        String fathers_name=users.getFname();
                        String fathers_number=users.getFnumber();
                        String contact_name=users.getCname();
                        String contact_number=users.getCnumber();
                        String address=users.getAddress();

                        System.out.println(name+"\n"+number+"\n"+email+"\n"+fathers_name+"\n"+fathers_number+"\n"+contact_name+"\n"+contact_number+"\n"+address);

                        sendmessage(name,number,email,fathers_name,fathers_number,contact_name,contact_number,address);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users users=dataSnapshot.getValue(Users.class);
                username.setVisibility(View.VISIBLE);
                username.setText(users.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    public void sendmessage(String name,String number,String email,String fathers_name,String fathers_number,String contact_name,String contact_number,String address){

        System.out.println(name+"\n"+number+"\n"+email+"\n"+fathers_name+"\n"+fathers_number+"\n"+contact_name+"\n"+contact_number+"\n"+address);

        String police_text="HELP!!!\n"+"i am "+name+",i am in Distress\nPlease send help ASAP!!\nmy number is "+number+".";



//        Intent intent=new Intent(getApplicationContext(),Homepage.class);
//        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage("7002764503", null,police_text ,null,null);
        sms.sendTextMessage(contact_number, null,police_text ,null,null);
        sms.sendTextMessage(fathers_number, null,police_text ,null,null);

        Toast.makeText(getApplicationContext(), "Message Sent successfully! don't worry ! don't turn of your mobile! we are tracking you!",
                Toast.LENGTH_SHORT).show();

        fetchLastLocation(fathers_number,contact_number);

    }

    public void fetchLastLocation(final String fathers_number, final String contact_number) {

        if(ActivityCompat.checkSelfPermission(Homepage.this, ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Homepage.this, new String[]{
                    ACCESS_FINE_LOCATION},REQUEST_CODE);

            return;
        }
        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {

                    currentlocation=location;
                    double lat=currentlocation.getLatitude();
                    String latt=""+lat;
                    double lon=currentlocation.getLongitude();
                    String lonn=""+lon;
                    System.out.println(latt+" / "+lonn);
                    SmsManager sms=SmsManager.getDefault();
                    sms.sendTextMessage("7002764503", null,"locate me at \n"+"https://maps.google.com/?q="+latt+","+lonn,null,null);
                    sms.sendTextMessage(fathers_number, null,"PAPA, Locate me i am in danger : \n"+"https://maps.google.com/?q="+latt+","+lonn,null,null);
                    sms.sendTextMessage(contact_number, null,"Locate me i am in danger : \n"+"https://maps.google.com/?q="+latt+","+lonn,null,null);
     //               sms.sendTextMessage("7002764503", null,"locate me i am in danger :\n"+"https://maps.google.com/?q="+latt+","+lonn,null,null);
//                    Toast.makeText(getApplicationContext(),"father no:"+fathers_number+"\n contact number:"+contact_number,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Location is shared !",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}

