package com.example.maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(MainActivity.this);
        fetchLastLocation();

    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
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
                    Toast.makeText(getApplicationContext(),currentlocation.getLatitude()+" "+currentlocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    double lat=currentlocation.getLatitude();
                    String latt=""+lat;
                    double lon=currentlocation.getLongitude();
                    String lonn=""+lon;
                    SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
                    supportMapFragment.getMapAsync(MainActivity.this);

                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng=new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am Here send Help ASAP");

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
        googleMap.addMarker(markerOptions);




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }

    }
}
