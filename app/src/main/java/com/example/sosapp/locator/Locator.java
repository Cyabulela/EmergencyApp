package com.example.sosapp.locator;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Locator implements LocationListener {
    private final int ALL_PERMISSION_RESULT = 101;
    private final long MIN_DISTANCE_FOR_UPDATES = 10, MIN_TIME_BW_UPDATE = 1000 * 60;
    private final LocationManager locationManager;
    private Location location;
    private final ArrayList<String> permissionsToRequest;
    private final ArrayList<String> permissionRejected = new ArrayList<>();
    private final boolean isGPS;
    private final boolean isNetwork;
    private boolean canGetLocation = true;
    private final Context context;

    public Locator(Context context) {
        this.context = context;
        locationManager = (LocationManager) this.context.getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermission(permissions);

        if (!isGPS && !isNetwork){
            getLastLocation();
        }
        else{
            checkPermission();
            getLocation();
        }
    }

    private ArrayList<String> findUnAskedPermission(ArrayList<String> permissions) {
        ArrayList<String> result = new ArrayList<>();
        for(String permission : permissions){
            if (!hasPermission(permission)){
                result.add(permission);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(context , permission) == PackageManager.PERMISSION_GRANTED;
    }


    public void getLastLocation(){
        try{
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria , false);
            location = locationManager.getLastKnownLocation(provider);
        }  
        catch (SecurityException e){
            Log.e("Fault" , e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean locationNotNull(){ return location != null;}

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //TO DO
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        if (locationManager != null){
            locationManager.removeUpdates(this);
        }
    }

    public void checkPermission() {
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions((Activity) context, permissionsToRequest.toArray(new String[0]), ALL_PERMISSION_RESULT);
            canGetLocation = false;
        } else {
            canGetLocation = true;
        }
    }

    private void getLocation() {
        if (canGetLocation) {
            try {
                if (isGPS) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATE,
                            MIN_DISTANCE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                  else {
                      getLastLocation();
                  }
                }

                else if (isNetwork) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATE,
                            MIN_DISTANCE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                   else {
                       getLastLocation();
                    }
                }
               else {
                   getLastLocation();
                }
            }
            catch (SecurityException e) {
                getLastLocation();
                Log.e("Fault" , e.getMessage());
                e.printStackTrace();
            }
        }
        else{
            getLastLocation();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "\n\nLast known location: \nhttps://maps.google.com/?q=" + location.getLatitude()+ "," + location.getLongitude();
    }
}
