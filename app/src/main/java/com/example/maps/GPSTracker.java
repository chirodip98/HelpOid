package com.example.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class GPSTracker implements LocationListener {

    Context context;

    public GPSTracker(Context c) {
        context = c;
    }

    public Location getLocation() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSenabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSenabled) {


            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return null;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
            Location l=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return l;

        }
        else
        {
            Toast.makeText(context,"Please enable your gps!!!",Toast.LENGTH_LONG).show();
        }
        return null;
    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
